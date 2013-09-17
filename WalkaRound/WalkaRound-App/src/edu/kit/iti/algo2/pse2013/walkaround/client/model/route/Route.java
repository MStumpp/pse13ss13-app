package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateNormalizer;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateNormalizerException;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.Geocoder;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

/**
 *
 * @author Lukas M�ller
 * @version 2.0.42
 *
 */
public class Route implements RouteInfo {

	private static String TAG_ROUTE = Route.class.getSimpleName();

	@SuppressWarnings("unused")
	private String name;
	private Waypoint activeWaypoint;
	private LinkedList<Coordinate> routeCoordinates;
	private RouteProcessing routeProcessor;

	/**
	 * @param coordsOfNewRoute a list of the coordinates contained in the newly constructed route
	 */
	public Route(LinkedList<Coordinate> coordsOfNewRoute) {
		Log.d(TAG_ROUTE, "Route Constructor");
		this.routeCoordinates = coordsOfNewRoute;
		this.activeWaypoint = null;
		this.name = "";
		this.routeProcessor = RouteProcessing.getInstance();
	}

	/**
	 *
	 * @param id
	 * @return if a waypoint with the given id is contained in this route
	 */
	public boolean setActiveWaypoint(int id) {
		Log.d(TAG_ROUTE, "setActiveWaypoint(id)");

		for (Waypoint wp : this.getWaypoints()) {
			if (wp.getId() == id) {
				this.setActiveWaypoint(wp);
				return true;
			}
		}
		return false;
	}

	/**
	 * @param newActiveWP the WP which should be set active
	 * @return if this route contains the specified waypoint
	 */
	public boolean setActiveWaypoint(Waypoint newActiveWP) {
		Log.d(TAG_ROUTE, "setActiveWaypoint(Waypoint)");
		if (this.containsWaypoint(newActiveWP)) {
			this.activeWaypoint = newActiveWP;
			return true;
		}
		return false;
	}

	/**
	 *
	 */
	public void resetActiveWaypoint() {
		Log.d(TAG_ROUTE, "resetActiveWaypoint()");
		this.activeWaypoint = null;
	}

	/**
	 * Moves the coordinate represented by the active waypoint to the given
	 * waypoint-position within the route.
	 * @param newPos
	 */
	public void moveActiveWaypointInOrder(int newPos) {
		Log.d(TAG_ROUTE, "moveActiveWaypointInOrder(" + newPos + ")");
		LinkedList<Waypoint> waypoints = this.getWaypoints();
		Waypoint activeWaypoint = this.activeWaypoint;

		// TODO: bestimme vorherigen und nächsten WP an neuer Position
		// TODO: L�sche diese Methode :-P Weil ersetzt durch changeOrder...

		//

		assert (newPos >= 0 && newPos < waypoints.size());

		Waypoint previousWaypoint = this.getPreviousWaypoint(newPos);
		waypoints.add(newPos, activeWaypoint);
		Waypoint nextWaypoint = this.getNextWaypoint(newPos);

		this.deletePathBetweenTwoWaypoints(previousWaypoint, nextWaypoint);
		this.routeCoordinates.add(this.routeCoordinates.indexOf(previousWaypoint) + 1, activeWaypoint);

		this.deleteActiveWaypoint();

		// this.computeShortestPath(coordinate1, coordinate2)

		// this.addRouteBetweenTwoCoords(route, one, two)

		// F�ge den aktiven WP an der �bergebenen Position in die Route ein.

		this.setActiveWaypoint(activeWaypoint);
		this.cleanRouteOfDuplicateCoordinatePairs();
	}


	/**
	 * Changes the order of the waypoints on the route to the new, given order.
	 * @param newOrder
	 */
	public void changeOrderOfWaypoints(LinkedList<Waypoint> newWPOrder) {
		Log.d(TAG_ROUTE, String.format("changeOrderOfWaypoints(LinkedList<Waypoint>) METHOD START"));
		if (newWPOrder != null && newWPOrder.size() > 0) {
			final LinkedList<? extends Coordinate> oldWPOrder = this.getWaypoints();
			final LinkedList<Coordinate> oldCoordOrder = this.routeCoordinates;
			// TODO Sollte newWPOrder sein!
			this.routeCoordinates = (LinkedList<Coordinate>) oldWPOrder;
			List<Coordinate> newCoordOrderSYNCHRON = Collections.synchronizedList(this.routeCoordinates);
			
			// TODO:
			// Spezialfall newOrder mit L�nge 1 
			// Spezialfall Bumerang (waypoint.isAnchorForRoundtrip()...)
			// Kombi der beiden Spezialf�lle...

			// Run through all pairs of Waypoints in new Order:
			Iterator<Waypoint> newOrderIter = newWPOrder.iterator();
			Waypoint prevWP = null;
			Waypoint currentWP = null;
			if (newOrderIter.hasNext()) {
				currentWP = newOrderIter.next();
			}
			while (newOrderIter.hasNext()) {
				prevWP = currentWP;
				currentWP = newOrderIter.next();
				// If the pair already exists in the old order...
				if (this.pairOfCoordsInCoordList((Coordinate) prevWP, (Coordinate) currentWP, oldWPOrder)) {
					// ... then recycle the existing route between the waypoints...
					List<? extends Coordinate> existingPath = extractCoordsBetweenPairFromCoordList(prevWP, currentWP, oldCoordOrder);
					// and add it to the current route:
					this.addRouteBetweenTwoCoords(new Route((LinkedList<Coordinate>) existingPath), prevWP, currentWP);
					
					// don't forget to check for roundtrips (this only concerns the second WP):
					if (currentWP.isAnchorToRoundtrip()) {
						Route roundtrip = new Route (this.getRoundtripCoords(currentWP));
						
						// TODO: this.addRouteBetweenTwoCoords(roundtrip, , );
					}
					
					// TODO Synchronize all methods changing list!
				} else {
					// The pair does not yet exist. Thus compute a new route...
					
					// But mind that one of the waypoints can be an anchor for a roundtrip:
					// Multithreading!
					// TODO
					
					Thread pathProcessor = new Thread(new Runnable() {	
						@Override
						public void run() {
							LinkedList<Coordinate> computedShortestPath = new LinkedList<Coordinate>();
							Log.d(TAG_ROUTE, "Thread.run() in changeOrderOfWaypoints()");
							// TODO: 
						}
					});
					pathProcessor.start();
					
					
					
				}
				
				// Nutze previousWP / Next.... Methoden ?
				// Kann man hier die Funktion sublist von LinkedList sinnvoll nutzen?
				
				
			}
			
		}
	}
	
	
	

	/**
	 * Adds a new waypoint at the given coordinate to the end of the route.
	 * @param w the waypoint which should be added
	 */
	public void addWaypoint(Waypoint w) {
		Log.d(TAG_ROUTE, String.format("addWaypoint(%s) METHOD START", w));
		Log.d(TAG_ROUTE, String.format("addWaypoint(%s) to route with Coordinates", w, this.routeCoordinates.size()));
		// TODO: Add alert when coord not normalized
		
		Coordinate normalizedCoordinate = null;
		if (w != null) {
			try {
				normalizedCoordinate = CoordinateNormalizer.normalizeCoordinate(w, (int) BoundingBox.getInstance().getLevelOfDetail());
				Log.d(TAG_ROUTE, "addWaypoint() - Waypoint normalized");
				w.setPosition(normalizedCoordinate);
			} catch (IllegalArgumentException e) {
				Log.e(TAG_ROUTE, "addWaypoint() - Waypoint NOT normalized", e);
			} catch (CoordinateNormalizerException e) {
				Log.e(TAG_ROUTE, "addWaypoint() - Waypoint NOT normalized", e);
			} catch (InterruptedException e) {
				Log.e(TAG_ROUTE, "addWaypoint() - Waypoint NOT normalized", e);
			}
			
			Geocoder geo = new Geocoder();
			geo.reverseGeocode(w);
			
			if (this.routeCoordinates.size() != 0) {
				Log.d(TAG_ROUTE, String.format("addWaypoint(%s) -> computing shortest path", w));
				RouteInfo routeExtension;
				routeExtension = this.computeShortestPath(this.getEnd(), w);

				Log.d(TAG_ROUTE, String.format("addWaypoint(%s) -> addingRoute with %d Coordinates", w, routeExtension.getCoordinates().size()));
				this.addRoute(routeExtension);
			} else {
				Log.d(TAG_ROUTE, "addWaypoint() adding Waypoint to empty Route ");
				this.routeCoordinates.add(w);
			}

			this.setActiveWaypoint(w);
			this.cleanRouteOfDuplicateCoordinatePairs();
			
		}
		Log.d(TAG_ROUTE, String.format("addWaypoint(%s) - new size of route: %d", w, this.routeCoordinates.size()));
		Log.d(TAG_ROUTE, String.format("addWaypoint(%s) METHOD END", w));
	}

	/**
	 * @param profile	the roundtrip-profile
	 * @param length	the length of the roundtrip
	 */
	public void addRoundtripAtActiveWaypoint(int profile, int length) {
		Log.d(TAG_ROUTE, String.format("addRoundtripAtActiveWaypoint(profile %d, length %d)", profile, length));
		RouteInfo newRoundtrip;
		try {
			newRoundtrip = this.routeProcessor.computeRoundtrip(this.getActiveWaypoint(), profile, length);
			if (newRoundtrip != null) {
				this.addRoute(newRoundtrip);
			}
		} catch (RouteProcessingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException ille) {
			Log.d(TAG_ROUTE, "coordinate 1 and coordinate 2 must be provided");
		}
	}

	/**
	 * Adds the given RouteInfo to the end of the route.
	 * @param newRoute the route to be appended to the end of route
	 */
	public void addRoute(RouteInfo newRoute) {
		Log.d(TAG_ROUTE, "addRoute(RouteInfo) METHOD START");
		Log.d(TAG_ROUTE,
				"addRoute(RouteInfo) adding route with the following coordinates: "
						+ newRoute);

		// Determine if an intermediate route is necessary:
		if (this.getEnd() != null && !this.getEnd().equals(newRoute.getStart())) {
			Log.d(TAG_ROUTE,
					"intermediate path has to be computed. Current Route end: "
							+ this.getEnd() + ", new Route start"
							+ newRoute.getStart());
			Log.d(TAG_ROUTE, "addRoute(RouteInfo) -> computing intermediate route");
			// Calculate and add the intermediate route:
			RouteInfo intermediateRoute = this.computeShortestPath(this.getEnd(), newRoute.getStart());
			this.addRoute(intermediateRoute);
		}


		assert(newRoute.getCoordinates().size() > 0);
		if (getEnd() != null && getEnd().equals(newRoute.getStart())) {
			newRoute.getCoordinates().removeFirst();
		}

		Iterator<Coordinate> newCoordsIter = newRoute.getCoordinates().iterator();
		Coordinate tempCoord = null;

		while (newCoordsIter.hasNext()) {
			tempCoord = newCoordsIter.next();
			this.routeCoordinates.addLast(tempCoord);
		}

		this.cleanRouteOfDuplicateCoordinatePairs();
	}


	/**
	 * Moves the active waypoint to the position of the given coordinate.
	 * Does not compute new routes afterwards.
	 * @param coord
	 */
	public void moveActiveWaypointMoveOnly(final Coordinate coord) {
		Log.d(TAG_ROUTE, "moveActiveWaypointMoveOnly(Coordinate " + coord + ") METHOD START ");
		// TODO: Spezialfall Bumerrangpunkt!
		if (this.activeWaypoint != null && coord != null) {
			Log.d(TAG_ROUTE,
					"moveActiveWaypointMoveOnly(Coordinate) Active Waypoint is "
							+ this.activeWaypoint.toString());
			LinkedList<Waypoint> waypoints = this.getWaypoints();
			int indexOfActiveWaypoint = waypoints.indexOf(this.getActiveWaypoint());
			Log.d(TAG_ROUTE, "moveActiveWaypointMoveOnly(coord) Active Waypoint is Nr. "
							+ (indexOfActiveWaypoint + 1) + " of "
							+ waypoints.size() + " Waypoints in route.");

			final Waypoint beforeActive = this
					.getPreviousWaypoint(indexOfActiveWaypoint);
			final Waypoint afterActive = this
					.getNextWaypoint(indexOfActiveWaypoint);

			if (beforeActive != null) {
				Log.d(TAG_ROUTE,
						"moveActiveWaypointMoveOnly(coord) case beforeActive != null, beforeActive is nr. "
								+ (waypoints.indexOf(beforeActive) + 1) + " / "
								+ waypoints.size() + " in route");

				this.deletePathBetweenTwoWaypoints(beforeActive, this.activeWaypoint);

			}

			if (afterActive != null) {
				Log.d(TAG_ROUTE,
						"moveActiveWaypoint(coord) case afterActive != null, afterActive is nr. "
								+ (waypoints.indexOf(afterActive) + 1) + " / "
								+ waypoints.size() + " in route");

				this.deletePathBetweenTwoWaypoints(this.activeWaypoint,	afterActive);
			}

			this.activeWaypoint.setLongitude(coord.getLongitude());
			this.activeWaypoint.setLatitude(coord.getLatitude());

		}
		this.cleanRouteOfDuplicateCoordinatePairs();

		new Geocoder().reverseGeocode(activeWaypoint);
	}


	/**
	 * Moves the active waypoint to the position of the given coordinate. OLD VERSION!
	 * @param coord
	 */
	public void moveActiveWaypointComputeOnly(final Coordinate coord) {
		Log.d(TAG_ROUTE, "moveActiveWaypointComputeOnly(Coordinate " + coord
				+ ") METHOD START ");

		try {
		if (activeWaypoint != null && coord != null) {
			Log.d(TAG_ROUTE, "moveActiveWaypointComputeOnly(Coordinate) Active Waypoint is " + this.activeWaypoint.toString());
			LinkedList<Waypoint> waypoints = this.getWaypoints();
			int indexOfActiveWaypoint = waypoints.indexOf(this.getActiveWaypoint());
			Log.d(TAG_ROUTE, "moveActiveWaypointComputeOnly(coord) Active Waypoint is Nr. " + (indexOfActiveWaypoint + 1) + " of " + waypoints.size() + " Waypoints in route.");

			final Waypoint beforeActive = this.getPreviousWaypoint(indexOfActiveWaypoint);
			final Waypoint afterActive = this.getNextWaypoint(indexOfActiveWaypoint);
			Waypoint activeWP = this.activeWaypoint;
			RouteInfo newRouteBeforeActiveWaypoint = null;
			RouteInfo newRoutePastActiveWaypoint = null;

			if (beforeActive != null) {
				Log.d(TAG_ROUTE, "moveActiveWaypointComputeOnly(coord) case beforeActive != null, beforeActive is nr. "	+ (waypoints.indexOf(beforeActive) + 1) + " / "	+ waypoints.size() + " in route");
				this.deletePathBetweenTwoWaypoints(beforeActive, this.activeWaypoint);

				newRouteBeforeActiveWaypoint = this.computeShortestPath(beforeActive, coord);
				this.addRouteBetweenTwoCoords(newRouteBeforeActiveWaypoint, beforeActive, activeWP);
			}

			if (afterActive != null) {
				Log.d(TAG_ROUTE, "moveActiveWaypointComputeOnly(coord) case afterActive != null, afterActive is nr. " + (waypoints.indexOf(afterActive) + 1) + " / " + waypoints.size() + " in route");
				this.deletePathBetweenTwoWaypoints(this.activeWaypoint, afterActive);

				newRoutePastActiveWaypoint = this.computeShortestPath(coord, afterActive);
				this.addRouteBetweenTwoCoords(newRoutePastActiveWaypoint, activeWP, afterActive);
			}

			if (newRouteBeforeActiveWaypoint != null && newRouteBeforeActiveWaypoint.getCoordinates().size() > 0) {
				Coordinate normalizedActWP = newRouteBeforeActiveWaypoint.getCoordinates().getLast();
				// der aktive waypoint wird an anderer stelle null gesetzt daher kann es zu ausfällen kommen
				// Log.d("daddel", (this.activeWaypoint == null) + " oder norm " + (normalizedActWP == null));
				this.activeWaypoint.setLongitude(normalizedActWP.getLongitude());
				this.activeWaypoint.setLatitude(normalizedActWP.getLatitude());
				Log.d(TAG_ROUTE, "moveActiveWaypointComputeOnly(coord) setting active Waypoint to: " + normalizedActWP);
			} else if (newRoutePastActiveWaypoint != null && newRoutePastActiveWaypoint.getCoordinates().size() > 0) {
				Coordinate normalizedActWP = newRoutePastActiveWaypoint.getCoordinates().getFirst();
				this.activeWaypoint.setLongitude(normalizedActWP.getLongitude());
				this.activeWaypoint.setLatitude(normalizedActWP.getLatitude());
				Log.d(TAG_ROUTE, "moveActiveWaypointComputeOnly(coord) setting active Waypoint to: " + normalizedActWP);
			}
			new Geocoder().reverseGeocode(activeWaypoint);

		}
		} catch (NullPointerException e) {
			
		}
		

		this.cleanRouteOfDuplicateCoordinatePairs();
	}



	public void deleteActiveWaypoint() {
		Log.d(TAG_ROUTE, "deleteActiveWaypoint() METHOD START");
		int indexOfActiveWaypoint = this.getWaypoints().indexOf(
				this.getActiveWaypoint());
		Waypoint beforeActive = this.getPreviousWaypoint(indexOfActiveWaypoint);
		Waypoint afterActive = this.getNextWaypoint(indexOfActiveWaypoint);
		Log.d(TAG_ROUTE, "deleteActiveWaypoint(coord) Active Waypoint is Nr. "
				+ (indexOfActiveWaypoint + 1) + " of "
				+ this.getWaypoints().size() + " Waypoints in route.");
		Log.d(TAG_ROUTE, "deleteActiveWaypoint() beforeActive is "
				+ beforeActive + ", afterActive is " + afterActive);

		if (beforeActive == null && afterActive == null) {
			Log.d(TAG_ROUTE,
					"deleteActiveWaypoint() case beforeActive == null && afterActive == null");
			this.resetRoute();
		} else if (beforeActive == null && afterActive != null) {
			Log.d(TAG_ROUTE,
					"deleteActiveWaypoint() case beforeActive == null && afterActive != null");
			this.deletePathBetweenTwoWaypoints(this.activeWaypoint, afterActive);
			this.routeCoordinates.remove(this.activeWaypoint);
		} else if (beforeActive != null && afterActive == null) {
			Log.d(TAG_ROUTE,
					"deleteActiveWaypoint() case beforeActive != null && afterActive == null");
			this.deletePathBetweenTwoWaypoints(beforeActive,
					this.activeWaypoint);
			this.routeCoordinates.remove(this.activeWaypoint);
		} else if (beforeActive != null && afterActive != null) {
			Log.d(TAG_ROUTE,
					"deleteActiveWaypoint() case beforeActive != null && afterActive != null");
			this.deletePathBetweenTwoWaypoints(beforeActive,
					this.activeWaypoint);
			this.deletePathBetweenTwoWaypoints(this.activeWaypoint, afterActive);
			this.routeCoordinates.remove(this.activeWaypoint);

			RouteInfo route;

			route = this.computeShortestPath(beforeActive, afterActive);
			this.addRouteBetweenTwoCoords(route, beforeActive, afterActive);
		}
		this.resetActiveWaypoint();
		this.cleanRouteOfDuplicateCoordinatePairs();
	}



	public void invertRoute() {
		Log.d(TAG_ROUTE, "invertRoute()");
		LinkedList<Coordinate> revertedRoute = new LinkedList<Coordinate>();

		Iterator<Coordinate> routeCoordsDecIter = this.routeCoordinates
				.descendingIterator();

		while (routeCoordsDecIter.hasNext()) {
			revertedRoute.add(routeCoordsDecIter.next());
		}

		this.routeCoordinates = revertedRoute;
	}

	/*
	 *
	 */
	public void resetRoute() {
		Log.d(TAG_ROUTE, "resetRoute()");
		this.routeCoordinates = new LinkedList<Coordinate>();
	}

	/*
	 *
	 */
	/*
	 * public void optimizeRoute() { Log.d(TAG_ROUTE, "optimizeRoute()");
	 * RouteInfo optimizedRoute; try { optimizedRoute = this.routeProcessor
	 * .computeOptimizedRoute((RouteInfo) this); this.routeCoordinates =
	 * optimizedRoute.getCoordinates(); } catch (RouteProcessingException e) {
	 * e.printStackTrace(); } }
	 */

	/*
	 * public String getName() { Log.d(TAG_ROUTE, "getName()"); return
	 * this.name; }
	 */

	@Override
	public Waypoint getStart() {
		Log.d(TAG_ROUTE, "getStart()");
		if (this.routeCoordinates.size() > 0) {
			return this.getWaypoints().getFirst();
		}
		return null;
	}

	@Override
	public Waypoint getEnd() {
		LinkedList<Waypoint> waypoints = this.getWaypoints();
		Log.d(TAG_ROUTE,
				"getEnd() on Waypoint list with size " + waypoints.size());
		if (waypoints.size() > 0) {
			return waypoints.getLast();
		}
		return null;
	}

	@Override
	public Waypoint getActiveWaypoint() {
		Log.d(TAG_ROUTE, "getActiveWaypoint()");
		return this.activeWaypoint;
	}

	@Override
	public LinkedList<Waypoint> getWaypoints() {
		Log.d(TAG_ROUTE, "getWaypoints()");
		LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();
		for (Coordinate coord : this.routeCoordinates) {
			if (coord instanceof Waypoint) {
				waypoints.add((Waypoint) coord);
			}
		}
		return waypoints;
	}

	@Override
	public boolean containsWaypoint(Waypoint wp) {
		Log.d(TAG_ROUTE, "containsWaypoint(Waypoint)");
		if (this.getWaypoints().contains(wp)) {
			return true;
		}
		return false;
	}

	@Override
	public LinkedList<Coordinate> getCoordinates() {
		// Log.d(TAG_ROUTE, "getCoordinates()");
		return this.routeCoordinates;
	}

	/**
	 *
	 * @param waypointNr
	 * @return
	 */
	private Waypoint getPreviousWaypoint(int waypointNr) {
		LinkedList<Waypoint> waypoints = this.getWaypoints();
		Log.d(TAG_ROUTE, "getPreviousWaypoint(int " + waypointNr + ")" + " amaount: " + waypoints.size());
		if (waypointNr == 0 || waypoints.size() < waypointNr || waypointNr < 0) {
			return null;
		}
		return (waypoints.get(waypointNr - 1));
	}

	/**
	 *
	 * @param waypointNr
	 * @return
	 */
	private Waypoint getNextWaypoint(int waypointNr) {
		Log.d(TAG_ROUTE, "getNextNextWaypoint(int " + waypointNr + ")");
		LinkedList<Waypoint> waypoints = this.getWaypoints();
		if (waypointNr == (waypoints.size() - 1) || waypointNr < 0 || waypointNr > waypoints.size()) {
			return null;
		}
		return (waypoints.get(waypointNr + 1));
	}

	/**
	 *
	 * @param one
	 * @param two
	 * @return
	 */
	private boolean deletePathBetweenTwoWaypoints(Waypoint one, Waypoint two) {
		Log.d(TAG_ROUTE, "deletePathBetweenTwoWaypoints(Waypoint " + one
				+ ", Waypoint " + two + ") METHOD START");
		Log.d(TAG_ROUTE,
				"deletePathBetweenTwoWaypoints(Waypoint, Waypoint) Working on route with size: "
						+ this.routeCoordinates.size());
		Iterator<Coordinate> routeCoordsIter = this.routeCoordinates.iterator();

		Coordinate tempCoord = null;
		while (routeCoordsIter.hasNext() && !one.equals(tempCoord)) {
			tempCoord = routeCoordsIter.next();
		}
		if (routeCoordsIter.hasNext()) {
			tempCoord = routeCoordsIter.next();
		}

		while (routeCoordsIter.hasNext() && !two.equals(tempCoord)) {
			routeCoordsIter.remove();
			tempCoord = routeCoordsIter.next();
		}
		Log.d(TAG_ROUTE,
				"deletePathBetweenTwoWaypoints(Waypoint, Waypoint) METHOD END, length of resulting route: "
						+ this.routeCoordinates.size());
		return true;
	}

	/**
	 *
	 * @param route
	 * @param one
	 * @param two
	 * @return
	 */
	private boolean addRouteBetweenTwoCoords(RouteInfo route, Coordinate one,
			Coordinate two) {
		Log.d(TAG_ROUTE, "addRouteBetweenTwoCoords(RouteInfo, Coordinate : "
				+ one + ", Coordinate: " + two + ")");
		Log.d(TAG_ROUTE,
				"addRouteBetweenTwoCoords(RouteInfo, Coord, Coord) working with route size "
						+ this.routeCoordinates.size()
						+ ", adding route size: "
						+ route.getCoordinates().size());
		assert (this.routeCoordinates.indexOf(one) + 1 == this.routeCoordinates
				.indexOf(two));
		assert (route.getStart().equals(one) && route.getEnd().equals(two));
		List<Coordinate> routeCoordsSYNCHRONIZED = Collections.synchronizedList(this.routeCoordinates);
		
		synchronized (routeCoordsSYNCHRONIZED) {
			Iterator<Coordinate> routeCoordsIter = routeCoordsSYNCHRONIZED.iterator();
			// Fast forward iterator to coordinate "one":
			Coordinate tempCoord = null;
			while (routeCoordsIter.hasNext() && !one.equals(tempCoord)) {
				tempCoord = routeCoordsIter.next();
			}

			LinkedList<Coordinate> bridgingCoords = route.clone().getCoordinates();
			// Remove (waypoint-) coordinates from inserted route:
			bridgingCoords.removeFirst();
			bridgingCoords.removeLast();

			int indexOfInsertion = this.routeCoordinates.indexOf(one) + 1;
			Log.d(TAG_ROUTE, "size of Route: " + this.routeCoordinates.size()
					+ ", indexOfInsertion: " + indexOfInsertion
					+ ", route contains one " + this.routeCoordinates.contains(one));

			for (Coordinate coord : bridgingCoords) {
				this.routeCoordinates.add(indexOfInsertion, coord);
				indexOfInsertion++;
			}
		}
		Log.d(TAG_ROUTE,
				"addRouteBetweenTwoCoords(RouteInfo, Coord, Coord) METHOD END, length of resulting route: "
						+ this.routeCoordinates.size());

		return true;
	}

	/**
	 *
	 */
	private void cleanRouteOfDuplicateCoordinatePairs() {
		Log.d(TAG_ROUTE, "cleanRouteOfDuplicateCoordinatePairs()");
		Iterator<Coordinate> routeCoordsIter = this.routeCoordinates.iterator();
		Coordinate previousCoord = null;
		Coordinate tempCoord;

		if (routeCoordsIter.hasNext()) {
			previousCoord = routeCoordsIter.next();
		}

		while (routeCoordsIter.hasNext()) {
			tempCoord = routeCoordsIter.next();
			if (tempCoord.equals(previousCoord)) {
				routeCoordsIter.remove();
			}
			previousCoord = tempCoord;
		}
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		Log.d(TAG_ROUTE, "toString()");
		String output = "";
		for (int i = 0; i < this.routeCoordinates.size(); i++) {
			output += i + ". " + this.routeCoordinates.get(i) + ", ";
		}
		return output;
	}

	@Override
	public RouteInfo clone() {
		Log.d(TAG_ROUTE, "clone()");
		LinkedList<Coordinate> clonedCoords = new LinkedList<Coordinate>();
		for (Coordinate coord : this.routeCoordinates) {
			clonedCoords.add(coord.clone());
		}
		return new Route(clonedCoords);
	}

	private RouteInfo computeShortestPath(Coordinate start, Coordinate end) {
		RouteInfo output = null;
		Log.d(TAG_ROUTE, "computeShortestPath(Start Coordinate: " + start
				+ ", End Coordinate: " + end + ")");
		try {
			output = this.routeProcessor.computeShortestPath(start, end);
			Log.d(TAG_ROUTE, "computeShortestPath() returning Route: " + output);
		} catch (Exception e) {
			Log.d(TAG_ROUTE,
					"computeShortestPath() - RETURN TO SENDER, ADDRESS UNKNOWN.");
			if (output == null) {
				LinkedList<Coordinate> coordinatesOfOutputRoute = new LinkedList<Coordinate>();
				coordinatesOfOutputRoute.add(start);
				coordinatesOfOutputRoute.add(end);
				output = new Route(coordinatesOfOutputRoute);
			}
			e.printStackTrace();
		}
		return output;
	}

	@Override
	public Waypoint getWaypoint(int id) {

		for(Waypoint w : this.getWaypoints()){
			if(w.getId() == id){
				return w;
			}
		}
		return null;
	}
	
	/**
	 * Returns the index of the given waypoint within the list of ALL route coordinates.
	 * @param w
	 * @return
	 */
	public int getIndexOfWaypointInCoordinates(Waypoint w) {
		int index = -1;
		if (w != null && this.routeCoordinates.contains(w)) {
			index = this.routeCoordinates.indexOf(w);
		}
		return index;
	}
	
	
	/**
	 * Checks a given coordinate list for a pair of coordinates. (Mind order!)
	 * @param first
	 * @param second
	 * @param coordList
	 * @return
	 */
	public boolean pairOfCoordsInCoordList(Coordinate first, Coordinate second, LinkedList<? extends Coordinate> coordList) {
		boolean pairExists = false;
		if (first != null && second != null && coordList != null) {
			Iterator<? extends Coordinate> coordIter = coordList.iterator();
			Coordinate prevCoord = null;
			Coordinate currentCoord = null;
			while (coordIter.hasNext()) {
				prevCoord = currentCoord;
				currentCoord = coordIter.next();
				if (first.equals(prevCoord) && second.equals(currentCoord)) {
					pairExists = true;
				}
			}
		}
		return pairExists;
	}


	public List<? extends Coordinate> extractCoordsBetweenPairFromCoordList(Coordinate first, Coordinate second, LinkedList<? extends Coordinate> coordList) {
		List<? extends Coordinate> output = new LinkedList<Coordinate>();
		if (first != null && second != null && coordList != null && coordList.contains(first) && coordList.contains(second)) {
			int indexOfFirst = coordList.indexOf(first);
			int indexOfSecond = coordList.indexOf(second);
			output = coordList.subList(indexOfFirst + 1, indexOfSecond);
		}
		return output;
	}
	
	
	public LinkedList<Coordinate> getRoundtripCoords(Waypoint anchor) {
		LinkedList<Coordinate> roundtripCoords = new LinkedList<Coordinate>();
		if (anchor != null && anchor.isAnchorToRoundtrip() && this.routeCoordinates.contains(anchor)) {
			List<Coordinate> routeCoordsSYNCHRON = Collections.synchronizedList(this.routeCoordinates);
			synchronized (routeCoordsSYNCHRON) {
				Iterator<Coordinate> coordsIter = routeCoordsSYNCHRON.iterator();
				Coordinate tempCoord = null;
				while (coordsIter.hasNext() && !((Coordinate)anchor).equals(tempCoord)) {
					tempCoord = coordsIter.next();
					roundtripCoords.add(tempCoord);
				}
			}
		}
		return roundtripCoords;
	}
	
	
	
	
}


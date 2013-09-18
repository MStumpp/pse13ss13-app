package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateNormalizer;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateNormalizerException;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.Geocoder;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

/**
 *
 * @author Lukas Mï¿½ller
 * @version 2.0.42
 *
 */
public class Route implements RouteInfo {

	private static String TAG = Route.class.getSimpleName();

	@SuppressWarnings("unused")
	private String name;
	private Waypoint activeWaypoint;
	private LinkedList<Coordinate> routeCoordinates;
	private RouteProcessing routeProcessor;

	/**
	 * @param coordsOfNewRoute a list of the coordinates contained in the newly constructed route
	 */
	public Route(LinkedList<Coordinate> coordsOfNewRoute) {
		this(coordsOfNewRoute, null);
	}
	
	/**
	 * @param coordsOfNewRoute a list of the coordinates contained in the newly constructed route
	 */
	public Route(LinkedList<Coordinate> coordsOfNewRoute, Waypoint activeWaypoint) {
		Log.d(TAG, "Route Constructor");
		this.routeCoordinates = coordsOfNewRoute;
		this.activeWaypoint = activeWaypoint;
		this.name = "";
		this.routeProcessor = RouteProcessing.getInstance();
	}

	/**
	 *
	 * @param id
	 * @return if a waypoint with the given id is contained in this route
	 */
	public boolean setActiveWaypoint(int id) {
		Log.d(TAG, "setActiveWaypoint(id)");

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
		Log.d(TAG, "setActiveWaypoint(Waypoint) METHOD START");
		Log.d(TAG, "setActiveWaypoint(Waypoint) is anchor to roundtrip: " + newActiveWP.isAnchorToRoundtrip());
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
		Log.d(TAG, "resetActiveWaypoint()");
		this.activeWaypoint = null;
	}


	/**
	 * Changes the order of the waypoints on the route to the new, given order.
	 * @param newOrder
	 */
	public void changeOrderOfWaypoints(LinkedList<Waypoint> newWPOrder) {
		Log.d(TAG, String.format("changeOrderOfWaypoints(LinkedList<Waypoint>) METHOD START"));
		if (newWPOrder != null && newWPOrder.size() > 0) {
			final LinkedList<? extends Coordinate> oldWPOrder = this.getWaypoints();
			final LinkedList<Coordinate> oldCoordOrder = this.routeCoordinates;
			// TODO Sollte newWPOrder sein!
			this.routeCoordinates = (LinkedList<Coordinate>) oldWPOrder;
			List<Coordinate> newCoordOrderSYNCHRON = Collections.synchronizedList(this.routeCoordinates);
			
			// TODO:
			// Spezialfall newOrder mit Länge 1 
			// Spezialfall Bumerang (waypoint.isAnchorForRoundtrip()...)
			// Kombi der beiden Spezialfälle...

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
				int indexOfPrevWP = this.routeCoordinates.indexOf(prevWP);
				int indexOfCurrentWP = this.routeCoordinates.indexOf(currentWP);
				
				// TODO: Überarbeiten:
				
				// If the pair already exists in the old order...
				if (this.isPairOfCoordsInCoordList((Coordinate) prevWP, (Coordinate) currentWP, oldWPOrder)) {
					// ... then recycle the existing route between the waypoints...
					List<? extends Coordinate> existingPath = extractCoordsBetweenPairFromCoordList(prevWP, currentWP, oldCoordOrder);
					// add to route:
					this.addCoordinatesSYNCHRONIZED(indexOfPrevWP + 1, (Collection) existingPath);
					
					// extract possible roundtrip from second WP and add to route:
					if (currentWP.isAnchorToRoundtrip()) {
						LinkedList<Coordinate> roundTripCoords = this.extractRoundtripCoordsFromCoordList(currentWP, oldCoordOrder);
						this.addCoordinatesSYNCHRONIZED(indexOfCurrentWP + 1, roundTripCoords);
					}
				} else {
					// The pair does not yet exist. Trigger a new route computation:
					
					// But mind that one of the waypoints can be an anchor for a roundtrip:
					
					
					Thread pathProcessor = new Thread(new Runnable() {	
						@Override
						public void run() {
							LinkedList<Coordinate> computedShortestPath = new LinkedList<Coordinate>();
							Log.d(TAG, "Thread.run() in changeOrderOfWaypoints()");
							// TODO: 
							
						}
					});
					pathProcessor.start();
					
				}
				
			}
			
		}
	}
	
	
	

	/**
	 * Adds a new waypoint at the given coordinate to the end of the route.
	 * @param w the waypoint which should be added
	 */
	public void addWaypoint(Waypoint w) {
		Log.d(TAG, String.format("addWaypoint(%s) METHOD START", w));
		Log.d(TAG, String.format("addWaypoint(%s) to route with Coordinates", w, this.routeCoordinates.size()));
		// TODO: Add alert when coord not normalized
		
		Coordinate normalizedCoordinate = null;
		if (w != null) {
			try {
				normalizedCoordinate = CoordinateNormalizer.normalizeCoordinate(w, (int) BoundingBox.getInstance().getLevelOfDetail());
				Log.d(TAG, "addWaypoint() - Waypoint normalized");
				w.setPosition(normalizedCoordinate);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "addWaypoint() - Waypoint NOT normalized", e);
			} catch (CoordinateNormalizerException e) {
				Log.e(TAG, "addWaypoint() - Waypoint NOT normalized", e);
			} catch (InterruptedException e) {
				Log.e(TAG, "addWaypoint() - Waypoint NOT normalized", e);
			}
			
			Geocoder geo = new Geocoder();
			geo.reverseGeocode(w);
			
			if (this.routeCoordinates.size() != 0) {
				Log.d(TAG, String.format("addWaypoint(%s) -> computing shortest path", w));
				RouteInfo routeExtension;
				routeExtension = this.computeShortestPath(this.getEnd(), w);

				Log.d(TAG, String.format("addWaypoint(%s) -> addingRoute with %d Coordinates", w, routeExtension.getCoordinates().size()));
				this.addRoute(routeExtension);
			} else {
				Log.d(TAG, "addWaypoint() adding Waypoint to empty Route ");
				this.routeCoordinates.add(w);
			}

			this.setActiveWaypoint(w);
			this.cleanRouteOfDuplicateCoordinatePairs();
			
		}
		Log.d(TAG, String.format("addWaypoint(%s) - new size of route: %d", w, this.routeCoordinates.size()));
		Log.d(TAG, String.format("addWaypoint(%s) METHOD END", w));
	}

	/**
	 * @param profile	the roundtrip-profile
	 * @param length	the length of the roundtrip
	 */
	public void addRoundtripAtActiveWaypoint(int profile, int length) {
		Log.d(TAG, String.format("addRoundtripAtActiveWaypoint(profile %d, length %d)", profile, length));
		RouteInfo newRoundtrip;
		try {
			newRoundtrip = this.routeProcessor.computeRoundtrip(this.getActiveWaypoint(), profile, length);
			if (newRoundtrip != null) {
				this.addRoundtripAtWaypoint(newRoundtrip, this.getActiveWaypoint(), profile);
			}
		} catch (RouteProcessingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException ille) {
			Log.d(TAG, "coordinate 1 and coordinate 2 must be provided");
		}
	}

	/**
	 * Adds the given RouteInfo to the end of the route.
	 * @param newRoute the route to be appended to the end of route
	 */
	public void addRoute(RouteInfo newRoute) {
		Log.d(TAG, "addRoute(RouteInfo) METHOD START");
		Log.d(TAG,
				"addRoute(RouteInfo) adding route with the following coordinates: "
						+ newRoute);

		// Determine if an intermediate route is necessary:
		if (this.getEnd() != null && !this.getEnd().equals(newRoute.getStart())) {
			Log.d(TAG,
					"intermediate path has to be computed. Current Route end: "
							+ this.getEnd() + ", new Route start"
							+ newRoute.getStart());
			Log.d(TAG, "addRoute(RouteInfo) -> computing intermediate route");
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
		Log.d(TAG, "moveActiveWaypointMoveOnly(Coordinate " + coord + ") METHOD START ");
		if (this.activeWaypoint != null && coord != null) {
			Log.d(TAG,
					"moveActiveWaypointMoveOnly(Coordinate) Active Waypoint is "
							+ this.activeWaypoint.toString());
			LinkedList<Waypoint> waypoints = this.getWaypoints();
			int indexOfActiveWaypoint = waypoints.indexOf(this.getActiveWaypoint());
			Log.d(TAG, "moveActiveWaypointMoveOnly(coord) Active Waypoint is Nr. "
							+ (indexOfActiveWaypoint + 1) + " of "
							+ waypoints.size() + " Waypoints in route.");

			final Waypoint beforeActive = this
					.getPreviousWaypoint(indexOfActiveWaypoint);
			final Waypoint afterActive = this
					.getNextWaypoint(indexOfActiveWaypoint);
			
			// To delete a roundtrip at the moved waypoint:
			this.clearRoundtripAtWaypoint(this.getActiveWaypoint());
			
			// To change the route before the moved waypoint:
			if (beforeActive != null) {
				Log.d(TAG,
						"moveActiveWaypointMoveOnly(coord) case beforeActive != null, beforeActive is nr. "
								+ (waypoints.indexOf(beforeActive) + 1) + " / "
								+ waypoints.size() + " in route");

				this.deletePathBetweenTwoCoordinates(beforeActive, this.activeWaypoint);

			}

			// To change the route after the moved waypoint:
			if (afterActive != null) {
				Log.d(TAG,
						"moveActiveWaypoint(coord) case afterActive != null, afterActive is nr. "
								+ (waypoints.indexOf(afterActive) + 1) + " / "
								+ waypoints.size() + " in route");

				this.deletePathBetweenTwoCoordinates(this.activeWaypoint,	afterActive);
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
		Log.d(TAG, "moveActiveWaypointComputeOnly(Coordinate " + coord
				+ ") METHOD START ");

		try {
		if (activeWaypoint != null && coord != null) {
			Log.d(TAG, "moveActiveWaypointComputeOnly(Coordinate) Active Waypoint is " + this.activeWaypoint.toString());
			LinkedList<Waypoint> waypoints = this.getWaypoints();
			int indexOfActiveWaypoint = waypoints.indexOf(this.getActiveWaypoint());
			Log.d(TAG, "moveActiveWaypointComputeOnly(coord) Active Waypoint is Nr. " + (indexOfActiveWaypoint + 1) + " of " + waypoints.size() + " Waypoints in route.");

			// To delete a roundtrip at the moved waypoint:
			this.clearRoundtripAtWaypoint(this.getActiveWaypoint());
			
			final Waypoint beforeActive = this.getPreviousWaypoint(indexOfActiveWaypoint);
			final Waypoint afterActive = this.getNextWaypoint(indexOfActiveWaypoint);
			Waypoint activeWP = this.activeWaypoint;
			RouteInfo newRouteBeforeActiveWaypoint = null;
			RouteInfo newRoutePastActiveWaypoint = null;
			
			if (beforeActive != null) {
				Log.d(TAG, "moveActiveWaypointComputeOnly(coord) case beforeActive != null, beforeActive is nr. "	+ (waypoints.indexOf(beforeActive) + 1) + " / "	+ waypoints.size() + " in route");
				this.deletePathBetweenTwoCoordinates(beforeActive, this.activeWaypoint);

				newRouteBeforeActiveWaypoint = this.computeShortestPath(beforeActive, coord);
				this.addRouteBetweenTwoWaypoints(newRouteBeforeActiveWaypoint, beforeActive, activeWP);
			}

			if (afterActive != null) {
				Log.d(TAG, "moveActiveWaypointComputeOnly(coord) case afterActive != null, afterActive is nr. " + (waypoints.indexOf(afterActive) + 1) + " / " + waypoints.size() + " in route");
				this.deletePathBetweenTwoCoordinates(this.activeWaypoint, afterActive);

				newRoutePastActiveWaypoint = this.computeShortestPath(coord, afterActive);
				this.addRouteBetweenTwoWaypoints(newRoutePastActiveWaypoint, activeWP, afterActive);
			}

			if (newRouteBeforeActiveWaypoint != null && newRouteBeforeActiveWaypoint.getCoordinates().size() > 0) {
				Coordinate normalizedActWP = newRouteBeforeActiveWaypoint.getCoordinates().getLast();
				// der aktive waypoint wird an anderer stelle null gesetzt daher kann es zu ausfÃ¤llen kommen
				// Log.d("daddel", (this.activeWaypoint == null) + " oder norm " + (normalizedActWP == null));
				this.activeWaypoint.setLongitude(normalizedActWP.getLongitude());
				this.activeWaypoint.setLatitude(normalizedActWP.getLatitude());
				Log.d(TAG, "moveActiveWaypointComputeOnly(coord) setting active Waypoint to: " + normalizedActWP);
			} else if (newRoutePastActiveWaypoint != null && newRoutePastActiveWaypoint.getCoordinates().size() > 0) {
				Coordinate normalizedActWP = newRoutePastActiveWaypoint.getCoordinates().getFirst();
				this.activeWaypoint.setLongitude(normalizedActWP.getLongitude());
				this.activeWaypoint.setLatitude(normalizedActWP.getLatitude());
				Log.d(TAG, "moveActiveWaypointComputeOnly(coord) setting active Waypoint to: " + normalizedActWP);
			}
			new Geocoder().reverseGeocode(activeWaypoint);

		}
		} catch (NullPointerException e) {
			
		}
		

		this.cleanRouteOfDuplicateCoordinatePairs();
	}



	public void deleteActiveWaypoint() {
		Log.d(TAG, "deleteActiveWaypoint() METHOD START");
		
		// To delete a roundtrip at the moved waypoint:
		this.clearRoundtripAtWaypoint(this.getActiveWaypoint());
		
		int indexOfActiveWaypoint = this.getWaypoints().indexOf(
				this.getActiveWaypoint());
		Waypoint beforeActive = this.getPreviousWaypoint(indexOfActiveWaypoint);
		Waypoint afterActive = this.getNextWaypoint(indexOfActiveWaypoint);
		Log.d(TAG, "deleteActiveWaypoint(coord) Active Waypoint is Nr. "
				+ (indexOfActiveWaypoint + 1) + " of "
				+ this.getWaypoints().size() + " Waypoints in route.");
		Log.d(TAG, "deleteActiveWaypoint() beforeActive is "
				+ beforeActive + ", afterActive is " + afterActive);

		if (beforeActive == null && afterActive == null) {
			Log.d(TAG,
					"deleteActiveWaypoint() case beforeActive == null && afterActive == null");
			this.resetRoute();
		} else if (beforeActive == null && afterActive != null) {
			Log.d(TAG,
					"deleteActiveWaypoint() case beforeActive == null && afterActive != null");
			this.deletePathBetweenTwoCoordinates(this.activeWaypoint, afterActive);
			this.routeCoordinates.remove(this.activeWaypoint);
		} else if (beforeActive != null && afterActive == null) {
			Log.d(TAG,
					"deleteActiveWaypoint() case beforeActive != null && afterActive == null");
			this.deletePathBetweenTwoCoordinates(beforeActive,
					this.activeWaypoint);
			this.routeCoordinates.remove(this.activeWaypoint);
		} else if (beforeActive != null && afterActive != null) {
			Log.d(TAG,
					"deleteActiveWaypoint() case beforeActive != null && afterActive != null");
			this.deletePathBetweenTwoCoordinates(beforeActive,
					this.activeWaypoint);
			this.deletePathBetweenTwoCoordinates(this.activeWaypoint, afterActive);
			this.routeCoordinates.remove(this.activeWaypoint);

			RouteInfo route;

			route = this.computeShortestPath(beforeActive, afterActive);
			this.addRouteBetweenTwoWaypoints(route, beforeActive, afterActive);
		}
		
		this.resetActiveWaypoint();
		this.cleanRouteOfDuplicateCoordinatePairs();
	}



	public void invertRoute() {
		Log.d(TAG, "invertRoute()");
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
		Log.d(TAG, "resetRoute()");
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
		Log.d(TAG, "getStart()");
		if (this.routeCoordinates.size() > 0) {
			return this.getWaypoints().getFirst();
		}
		return null;
	}

	@Override
	public Waypoint getEnd() {
		LinkedList<Waypoint> waypoints = this.getWaypoints();
		Log.d(TAG,
				"getEnd() on Waypoint list with size " + waypoints.size());
		if (waypoints.size() > 0) {
			return waypoints.getLast();
		}
		return null;
	}

	@Override
	public Waypoint getActiveWaypoint() {
		Log.d(TAG, "getActiveWaypoint()");
		return this.activeWaypoint;
	}

	@Override
	public LinkedList<Waypoint> getWaypoints() {
		Log.d(TAG, "getWaypoints()");
		List<Coordinate> routeCoords = this.getCoordinatesSYNC();
		LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();
		for (Coordinate coord : routeCoords) {
			if (coord instanceof Waypoint) {
				waypoints.add((Waypoint) coord);
			}
		}
		return waypoints;
	}

	private List<Coordinate> getCoordinatesSYNC() {
		return Collections.synchronizedList(this.routeCoordinates);
	}

	@Override
	public boolean containsWaypoint(Waypoint wp) {
		Log.d(TAG, "containsWaypoint(Waypoint)");
		if (this.getWaypoints().contains(wp)) {
			return true;
		}
		return false;
	}

	@Override
	public LinkedList<Coordinate> getCoordinates() {
		// Log.d(TAG_ROUTE, "getCoordinates()");
		return new LinkedList<Coordinate>(this.routeCoordinates);
	}

	/**
	 *
	 * @param waypointNr
	 * @return
	 */
	private Waypoint getPreviousWaypoint(int waypointNr) {
		LinkedList<Waypoint> waypoints = this.getWaypoints();
		Log.d(TAG, "getPreviousWaypoint(int " + waypointNr + ")" + " amaount: " + waypoints.size());
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
		Log.d(TAG, "getNextNextWaypoint(int " + waypointNr + ")");
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
	private boolean deletePathBetweenTwoCoordinates(Coordinate one, Coordinate two) {
		Log.d(TAG, "deletePathBetweenTwoCoordinates(Coordinate " + one
				+ ", Coordinate " + two + ") METHOD START");
		Log.d(TAG,
				"deletePathBetweenTwoCoordinate(Coordinate, Coordinate) Working on route with size: "
						+ this.routeCoordinates.size());
		if (one != null && two != null) {
			int indexOfOne = this.routeCoordinates.indexOf(one);
			int indexOfTwo = this.routeCoordinates.indexOf(two);
			List<Coordinate> path = this.getCoordinatesSYNC().subList(indexOfOne + 1, indexOfTwo);
			if (one instanceof Waypoint && ((Waypoint) one).isAnchorToRoundtrip()) {
				int indexOfRoundtripEnd = path.indexOf(new Coordinate(one));
				path = path.subList(indexOfRoundtripEnd + 1, path.size());
			}
			path.clear();
			
		}
		return false;
	}

	/**
	 *
	 * @param route
	 * @param one
	 * @param two
	 * @return
	 */
	private boolean addRouteBetweenTwoWaypoints(RouteInfo route, Waypoint one, Waypoint two) {
		Log.d(TAG, "addRouteBetweenTwoWaypoints(RouteInfo, Waypoint: "
				+ one + ", Waypoint: " + two + ")");
		Log.d(TAG,
				"addRouteBetweenTwoWaypoints(RouteInfo, Waypoint, Waypoint) working with route size "
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
			Log.d(TAG, "size of Route: " + this.routeCoordinates.size()
					+ ", indexOfInsertion: " + indexOfInsertion
					+ ", route contains one " + this.routeCoordinates.contains(one));

			for (Coordinate coord : bridgingCoords) {
				this.routeCoordinates.add(indexOfInsertion, coord);
				indexOfInsertion++;
			}
		}
		Log.d(TAG,
				"addRouteBetweenTwoCoords(RouteInfo, Waypoint, Waypoint) METHOD END, length of resulting route: "
						+ this.routeCoordinates.size());

		return true;
	}

	
	
	
	
	/**
	 *
	 */
	private void cleanRouteOfDuplicateCoordinatePairs() {
		Log.d(TAG, "cleanRouteOfDuplicateCoordinatePairs()");
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
		Log.d(TAG, "toString()");
		String output = "";
		for (int i = 0; i < this.routeCoordinates.size(); i++) {
			output += i + ". " + this.routeCoordinates.get(i) + ", ";
		}
		return output;
	}

	@Override
	public RouteInfo clone() {
		Log.d(TAG, "clone()");
		Route clone = null;
		LinkedList<Coordinate> clonedCoords = new LinkedList<Coordinate>();
		for (Coordinate coord : this.routeCoordinates) {
			clonedCoords.add(coord.clone());
		}
		clone = new Route(clonedCoords, this.activeWaypoint);
		Log.d(TAG, "clone() #Waypoints: " + clone.getWaypoints().size());
		return clone;
	}

	private RouteInfo computeShortestPath(Coordinate start, Coordinate end) {
		RouteInfo output = null;
		Log.d(TAG, "computeShortestPath(Start Coordinate: " + start
				+ ", End Coordinate: " + end + ")");
		try {
			output = this.routeProcessor.computeShortestPath(start, end);
			Log.d(TAG, "computeShortestPath() returning Route: " + output);
		} catch (Exception e) {
			Log.d(TAG,
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
	public boolean isPairOfCoordsInCoordList(Coordinate first, Coordinate second, LinkedList<? extends Coordinate> coordList) {
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
	
	
	public LinkedList<Coordinate> extractRoundtripCoordsFromCoordList(Waypoint anchor, LinkedList<Coordinate> coordList) {
		LinkedList<Coordinate> roundtripCoords = new LinkedList<Coordinate>();
		if (anchor != null && anchor.isAnchorToRoundtrip() && coordList.contains(anchor)) {
			List<Coordinate> routeCoordsSYNCHRON = Collections.synchronizedList(coordList);
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
	
	public void clearRoundtripAtWaypoint(Waypoint anchor) {
		Log.d(TAG, "clearRoundtripAtWaypoint(" + anchor + ") METHOD START");
		if (anchor != null && anchor.isAnchorToRoundtrip() && this.routeCoordinates.contains(anchor)) {
			Log.d(TAG, "clearRoundtripAtWaypoint(" + anchor + ") find and destroy roundtrip");
			int indexOfAnchor = this.routeCoordinates.indexOf(anchor);
			Log.d(TAG, "clearRoundtripAtWaypoint(" + anchor + ") indexOfAnchor = " + indexOfAnchor);
			Log.d(TAG, "clearRoundtripAtWaypoint(" + anchor + ") Route Size: " + this.routeCoordinates.size());
			List<Coordinate> tempList = this.routeCoordinates.subList(indexOfAnchor + 1, this.routeCoordinates.size());
			int indexOfRoundtripEnd = tempList.indexOf(new Coordinate(anchor));
			tempList = tempList.subList(0, indexOfRoundtripEnd + 1);
			tempList.clear();
			Log.d(TAG, "clearRoundtripAtWaypoint(" + anchor + ") Route Size after clear op: " + this.routeCoordinates.size());
			// setting roundtrip profile back to 0:
			anchor.setProfile(-1);
		}
	}
	
	private boolean addCoordinatesSYNCHRONIZED(int pos, Collection<Coordinate> newCoords) {
		Log.d(TAG, "addCoordinatesSYNCHRONIZED(...)");
		if (newCoords != null && pos >= 0 && pos <= this.routeCoordinates.size()) {
			Log.d(TAG, "addCoordinatesSYNCHRONIZED(...) pre adding #Total Coords: " + this.routeCoordinates.size());
			List<Coordinate> routeCoords = Collections.synchronizedList(this.routeCoordinates);
			routeCoords.addAll(pos, newCoords);
			Log.d(TAG, "addCoordinatesSYNCHRONIZED(...) post adding #Total Coords: " + this.routeCoordinates.size());
			return true;
		}
		return false;
	}
	
	private void addRoundtripAtWaypoint(RouteInfo roundtrip, Waypoint newAnchor, int profile) {
		Log.d(TAG, "addRoundtripAtWaypoint(RouteInfo, Waypoint, profile) METHOD START");
		if (roundtrip != null && roundtrip.getCoordinates().size() > 0 && this.routeCoordinates.contains(newAnchor)) {
			Log.d(TAG, "addRoundtripAtWaypoint() length: " + ((Route) roundtrip).getLengthInMeters() + "m");
			Log.d(TAG, "addRoundtripAtWaypoint() #Coords: " + roundtrip.getCoordinates().size());
			LinkedList<Coordinate> roundtripCoords = roundtrip.getCoordinates();
			// get roundtrip anchor sent from server:
			Coordinate serverAnchor = roundtripCoords.removeFirst();
			// add the roundtrip coords...
			int indexOfNewAnchor = this.routeCoordinates.indexOf(newAnchor);
			this.addCoordinatesSYNCHRONIZED(indexOfNewAnchor + 1, roundtripCoords);
			// newAnchor.setPosition(roundtripCoords.getFirst());
			newAnchor.setProfile(profile);
			newAnchor.setPosition(serverAnchor);
		}
	}
	
	public double getLengthInMeters() {
		double totalLengthInMeters = 0.00;
		Iterator<Coordinate> coordsIter = Collections.synchronizedList(this.routeCoordinates).iterator();
		Coordinate prevCoord = null;
		Coordinate currentCoord = null;
		if (coordsIter.hasNext()) {
			currentCoord = coordsIter.next();
		}
		while (coordsIter.hasNext()) {
			prevCoord = currentCoord;
			currentCoord = coordsIter.next();
			totalLengthInMeters += CoordinateUtility.calculateDifferenceInMeters(prevCoord, currentCoord);
		}
		return totalLengthInMeters;
	}
	
	
}


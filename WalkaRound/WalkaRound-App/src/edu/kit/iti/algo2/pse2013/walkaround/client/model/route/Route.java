package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.Iterator;
import java.util.LinkedList;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class Route implements RouteInfo {

	private static String TAG_ROUTE = Route.class.getSimpleName();

	private String name;
	private Waypoint activeWaypoint;
	private LinkedList<Coordinate> routeCoordinates;
	private RouteProcessing routeProcessor;

	/**
	 *
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
	 * @return
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
	 *
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
	 */
	public void moveActiveWaypointInOrder(int newPos) {
		Log.d(TAG_ROUTE, "moveActiveWaypointInOrder(" + newPos + ")");

		LinkedList<Waypoint> waypoints = this.getWaypoints();
		Waypoint activeWaypoint = this.activeWaypoint;

		// TODO: bestimme vorherigen und nächsten WP an neuer Position

		assert (newPos >= 0 && newPos <= waypoints.size());

		Waypoint previousWaypoint = this.getPreviousWaypoint(newPos);
		waypoints.add(newPos, activeWaypoint);
		Waypoint nextWaypoint = this.getNextWaypoint(newPos);

		this.deletePathBetweenTwoWaypoints(previousWaypoint, nextWaypoint);
		this.routeCoordinates.add(
				this.routeCoordinates.indexOf(previousWaypoint) + 1,
				activeWaypoint);

		this.deleteActiveWaypoint();

		// this.computeShortestPath(coordinate1, coordinate2)

		// this.addRouteBetweenTwoCoords(route, one, two)

		// Füge den aktiven WP an der übergebenen Position in die Route ein.

		this.setActiveWaypoint(activeWaypoint);
		this.cleanRouteOfDuplicateCoordinatePairs();
	}

	/**
	 * Adds a new waypoint at the given coordinate to the end of the route.
	 */
	public void addWaypoint(Waypoint w) {
		Log.d(TAG_ROUTE, String.format("addWaypoint(%s) METHOD START", w.toString()));
		Log.d(TAG_ROUTE, String.format("addWaypoint(%s) to route with Coordinates", w, this.routeCoordinates.size()));
		if (this.routeCoordinates.size() != 0) {
			Log.d(TAG_ROUTE, String.format("addWaypoint(%s) -> computing shortest path", w.toString()));
			RouteInfo routeExtension;
			routeExtension = this.computeShortestPath(this.getEnd(), w);

			Log.d(TAG_ROUTE, String.format("addWaypoint(%s) -> addingRoute with %d Coordinates",
					w.toString(), routeExtension.getCoordinates().size()));
			this.addRoute(routeExtension);
		} else {
			this.routeCoordinates.add(w);
		}

		this.setActiveWaypoint(w);
		this.cleanRouteOfDuplicateCoordinatePairs();
		Log.d(TAG_ROUTE, String.format("addWaypoint(%s) - new size of route: %d", w.toString(), this.routeCoordinates.size()));
		Log.d(TAG_ROUTE, String.format("addWaypoint(%s) METHOD END", w.toString()));
	}

	/**
	 *
	 */
	public void addRoundtripAtActiveWaypoint(int profile, int length) {
		Log.d(TAG_ROUTE, "addRoundtripAtActiveWaypoint(profile, length()");
		RouteInfo newRoundtrip;
		try {
			newRoundtrip = this.routeProcessor.computeRoundtrip(
					this.getActiveWaypoint(), profile, length);
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
	 */
	public void addRoute(RouteInfo newRoute) {
		Log.d(TAG_ROUTE, "addRoute(RouteInfo) METHOD START");
		Log.d(TAG_ROUTE, "addRoute(RouteInfo) adding route with the following coordinates: " + newRoute);

		Iterator<Coordinate> newRouteCoordsIter = newRoute.getCoordinates().iterator();

		if (this.getEnd() != null && !this.getEnd().equals(newRoute.getStart())) {
			Log.d(TAG_ROUTE,
					"intermediate path has to be computed. Current Route end: "
							+ this.getEnd() + ", new Route start"
							+ newRoute.getStart());
			Log.d(TAG_ROUTE, "addRoute(RouteInfo) -> computing shortest path");
			this.computeShortestPath(this.getEnd(), newRoute.getStart());
		}

		newRouteCoordsIter.next();
		while (newRouteCoordsIter.hasNext()) {
			this.routeCoordinates.addLast(newRouteCoordsIter.next());
		}
		this.cleanRouteOfDuplicateCoordinatePairs();
	}

	/**
	 * Moves the active waypoint to the position of the given coordinate.
	 */
	public void moveActiveWaypoint(final Coordinate coord) {
		Log.d(TAG_ROUTE, "moveActiveWaypoint(Coordinate " + coord
				+ ") METHOD START ");

		//TODO:
		if (this.activeWaypoint != null) {
			Log.d(TAG_ROUTE, "moveActiveWaypoint(Coordinate) Active Waypoint is "
					+ this.activeWaypoint.toString());
			LinkedList<Waypoint> waypoints = this.getWaypoints();
			int indexOfActiveWaypoint = waypoints.indexOf(this.getActiveWaypoint());
			Log.d(TAG_ROUTE,
					"moveActiveWaypoint(coord) Active Waypoint is Nr. "
							+ (indexOfActiveWaypoint + 1) + " of "
							+ waypoints.size() + " Waypoints in route.");

			final Waypoint beforeActive = this.getPreviousWaypoint(indexOfActiveWaypoint);
			final Waypoint afterActive = this.getNextWaypoint(indexOfActiveWaypoint);

			final Waypoint activeWP = this.activeWaypoint;
			final Route tempCurrentRoute = this;

			if (beforeActive != null) {
				Log.d(TAG_ROUTE,
						"moveActiveWaypoint(coord) case beforeActive != null, beforeActive is nr. "
								+ (waypoints.indexOf(beforeActive) + 1) + " / "
								+ waypoints.size() + " in route");

				this.deletePathBetweenTwoWaypoints(beforeActive, this.activeWaypoint);

				Thread pathCalculator = new Thread (new Runnable() {
					@Override
					public void run() {
						Log.d(TAG_ROUTE, "Thread.run() in moveActiveWaypoint(Coordinate)");
						RouteInfo newRouteBeforeActiveWaypoint;

						newRouteBeforeActiveWaypoint = tempCurrentRoute.computeShortestPath(beforeActive, coord);
						tempCurrentRoute.addRouteBetweenTwoCoords(newRouteBeforeActiveWaypoint, beforeActive, activeWP);

					}
				});
				pathCalculator.start();
			}

			if (afterActive != null) {
				Log.d(TAG_ROUTE,
						"moveActiveWaypoint(coord) case afterActive != null, afterActive is nr. "
								+ (waypoints.indexOf(afterActive) + 1) + " / "
								+ waypoints.size() + " in route");

				this.deletePathBetweenTwoWaypoints(this.activeWaypoint, afterActive);

				Thread pathCalculator = new Thread (new Runnable() {
					@Override
					public void run() {
						Log.d(TAG_ROUTE, "Thread.run() in moveActiveWaypoint(Coordinate)");
						RouteInfo newRoutePastActiveWaypoint;

						newRoutePastActiveWaypoint = tempCurrentRoute.computeShortestPath(coord, afterActive);
						tempCurrentRoute.addRouteBetweenTwoCoords(newRoutePastActiveWaypoint, activeWP, afterActive);

					}
				});
				pathCalculator.start();
			}

			this.activeWaypoint.setLongitude(coord.getLongitude());
			this.activeWaypoint.setLatitude(coord.getLatitude());

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

	/*
	 * Inverts all Coordinates in the route.
	 */
	public void invertRoute() {
		Log.d(TAG_ROUTE, "invertRoute()");
		LinkedList<Coordinate> revertedRoute = new LinkedList<Coordinate>();

		Iterator<Coordinate> routeCoordsDecIter = this.routeCoordinates.descendingIterator();

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
	public void optimizeRoute() {
		Log.d(TAG_ROUTE, "optimizeRoute()");
		RouteInfo optimizedRoute;
		try {
			optimizedRoute = this.routeProcessor
					.computeOptimizedRoute((RouteInfo) this);
			this.routeCoordinates = optimizedRoute.getCoordinates();
		} catch (RouteProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		Log.d(TAG_ROUTE, "getName()");
		return this.name;
	}

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
		Log.d(TAG_ROUTE, "getEnd() on Waypoint list with size " + waypoints.size());
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
	public boolean isFavorite() {
		Log.d(TAG_ROUTE, "isFavorite()");
		return FavoriteManager.getInstance().containsRoute(this);
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
		Log.d(TAG_ROUTE, "getCoordinates()");
		return this.routeCoordinates;
	}

	/**
	 *
	 * @param waypointNr
	 * @return
	 */
	private Waypoint getPreviousWaypoint(int waypointNr) {
		Log.d(TAG_ROUTE, "getPreviousWaypoint(int " + waypointNr + ")");
		LinkedList<Waypoint> waypoints = this.getWaypoints();
		if (waypointNr == 0) {
			return null;
		} else {
			return (waypoints.get(waypointNr - 1));
		}
	}

	/**
	 *
	 * @param waypointNr
	 * @return
	 */
	private Waypoint getNextWaypoint(int waypointNr) {
		Log.d(TAG_ROUTE, "getNextNextWaypoint(int " + waypointNr + ")");
		LinkedList<Waypoint> waypoints = this.getWaypoints();
		if (waypointNr == (waypoints.size() - 1)) {
			return null;
		} else {
			return (waypoints.get(waypointNr + 1));
		}
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
		tempCoord = routeCoordsIter.next();

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
		Iterator<Coordinate> routeCoordsIter = this.routeCoordinates.iterator();

		Coordinate tempCoord = null;
		while (routeCoordsIter.hasNext() && !one.equals(tempCoord)) {
			tempCoord = routeCoordsIter.next();
		}

		LinkedList<Coordinate> bridgingCoords = route.getCoordinates();
		bridgingCoords.removeFirst();
		bridgingCoords.removeLast();
		int indexOfInsertion = this.routeCoordinates.indexOf(one);
		Log.d(TAG_ROUTE, "size of Route: " + this.routeCoordinates.size()
				+ ", indexOfInsertion: " + indexOfInsertion
				+ ", route contains one " + this.routeCoordinates.contains(one));

		for (Coordinate coord : bridgingCoords) {
			this.routeCoordinates.add(indexOfInsertion, coord);
			indexOfInsertion++;
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
		Log.d(TAG_ROUTE, "computeShortestPath(Start Coordinate: " + start + ", End Coordinate: " + end + ")");
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
				output = (RouteInfo) new Route(coordinatesOfOutputRoute);
			}
			e.printStackTrace();
		}
		return output;
	}

}
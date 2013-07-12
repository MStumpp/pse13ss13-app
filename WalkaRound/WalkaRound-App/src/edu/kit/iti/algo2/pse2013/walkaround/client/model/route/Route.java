package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.Iterator;
import java.util.LinkedList;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoritesManager;
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

		// F�ge den aktiven WP an der �bergebenen Position in die Route ein.

		this.setActiveWaypoint(activeWaypoint);
		this.cleanRouteOfDuplicateCoordinatePairs();
	}

	/**
	 * Adds a new waypoint at the given coordinate to the end of the route.
	 */
	public void addWaypoint(Coordinate c) {
		Log.d(TAG_ROUTE, "addWaypoint(Coordinate c) METHOD START");
		Log.d(TAG_ROUTE, "addWaypoint(Coordinate c) to route with "
				+ this.routeCoordinates.size() + " Coordinates");
		if (this.routeCoordinates.size() != 0) {
			Log.d(TAG_ROUTE,
					"addWaypoint(Coordinate c) -> computing shortest path");
			RouteInfo routeExtension;
			routeExtension = this.computeShortestPath(this.getEnd(), c);

			Log.d(TAG_ROUTE, "addWaypoint(Coordinate c) -> addingRoute with " + routeExtension.getCoordinates().size() + " Coordinates");
			this.addRoute(routeExtension);
		} else {
			this.routeCoordinates.add(c);
		}
		this.setActiveWaypoint(this.getEnd());
		this.cleanRouteOfDuplicateCoordinatePairs();
		Log.d(TAG_ROUTE, "addWaypoint(Coordinate c) - new size of route: "
				+ this.routeCoordinates.size());
		Log.d(TAG_ROUTE, "addWaypoint(Coordinate c) METHOD END");
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
			this.addRoute(newRoundtrip);
		} catch (RouteProcessingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Adds the given RouteInfo to the end of the route.
	 */
	public void addRoute(RouteInfo newRoute) {
		Log.d(TAG_ROUTE, "addRoute(RouteInfo) METHOD START");
		Log.d(TAG_ROUTE,
				"addRoute(RouteInfo) adding route with the following coordinates: "
						+ newRoute);

		Iterator<Coordinate> newRouteCoordsIter = newRoute.getCoordinates()
				.iterator();

		if (!this.getEnd().equals(newRoute.getStart())) {
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
	public void moveActiveWaypoint(Coordinate coord) {
		Log.d(TAG_ROUTE, "moveActiveWaypoint(Coordinate " + coord + ") METHOD START ");
		Log.d(TAG_ROUTE, "moveActiveWaypoint(Coordinate) Active Waypoint is " + this.activeWaypoint.toString());

		if (this.activeWaypoint != null) {
			LinkedList<Waypoint> waypoints = this.getWaypoints();
			int indexOfActiveWaypoint = waypoints.indexOf(this.getActiveWaypoint());
			Log.d(TAG_ROUTE, "moveActiveWaypoint(coord) Active Waypoint is Nr. " + (indexOfActiveWaypoint + 1) + " of " + waypoints.size() + " Waypoints in route.");

			Waypoint beforeActive = this.getPreviousWaypoint(indexOfActiveWaypoint);
			Waypoint afterActive = this.getNextWaypoint(indexOfActiveWaypoint);

			if (beforeActive != null) {
				Log.d(TAG_ROUTE, "moveActiveWaypoint(coord) case beforeActive != null, beforeActive is nr. " + (waypoints.indexOf(beforeActive) + 1) + " / " + waypoints.size() + " in route");
				RouteInfo newRouteBeforeActiveWaypoint;
				newRouteBeforeActiveWaypoint = this.computeShortestPath(beforeActive, coord);
				this.deletePathBetweenTwoWaypoints(beforeActive, this.activeWaypoint);
				this.addRouteBetweenTwoCoords(newRouteBeforeActiveWaypoint, beforeActive, this.activeWaypoint);
			}

			if (afterActive != null) {
				Log.d(TAG_ROUTE, "moveActiveWaypoint(coord) case afterActive != null, afterActive is nr. " + (waypoints.indexOf(afterActive) + 1) + " / " + waypoints.size() + " in route");
				RouteInfo newRoutePastActiveWaypoint;
				newRoutePastActiveWaypoint = this.computeShortestPath(coord, afterActive);
				this.deletePathBetweenTwoWaypoints(this.activeWaypoint, afterActive);
				this.addRouteBetweenTwoCoords(newRoutePastActiveWaypoint, this.activeWaypoint, afterActive);
			}

			this.activeWaypoint.setLongitude(coord.getLongitude());
			this.activeWaypoint.setLatitude(coord.getLatitude());

		}

		this.cleanRouteOfDuplicateCoordinatePairs();
	}

	public void deleteActiveWaypoint() {
		Log.d(TAG_ROUTE, "deleteActiveWaypoint() METHOD START");
		int indexOfActiveWaypoint = this.getWaypoints().indexOf(this.getActiveWaypoint());
		Waypoint beforeActive = this.getPreviousWaypoint(indexOfActiveWaypoint);
		Waypoint afterActive = this.getNextWaypoint(indexOfActiveWaypoint);
		Log.d(TAG_ROUTE, "deleteActiveWaypoint(coord) Active Waypoint is Nr. " + (indexOfActiveWaypoint + 1) + " of " + this.getWaypoints().size() + " Waypoints in route.");
		Log.d(TAG_ROUTE, "deleteActiveWaypoint() beforeActive is " + beforeActive + ", afterActive is " + afterActive);
		
		if (beforeActive == null && afterActive == null) {
		Log.d(TAG_ROUTE, "deleteActiveWaypoint() case beforeActive == null && afterActive == null");
			this.resetRoute();
		} else if (beforeActive == null && afterActive != null) {
			Log.d(TAG_ROUTE, "deleteActiveWaypoint() case beforeActive == null && afterActive != null");
			this.deletePathBetweenTwoWaypoints(this.activeWaypoint, afterActive);
			this.routeCoordinates.remove(this.activeWaypoint);
		} else if (beforeActive != null && afterActive == null) {
			Log.d(TAG_ROUTE, "deleteActiveWaypoint() case beforeActive != null && afterActive == null");
			this.deletePathBetweenTwoWaypoints(beforeActive, this.activeWaypoint);
			this.routeCoordinates.remove(this.activeWaypoint);
		} else if (beforeActive != null && afterActive != null) {
			Log.d(TAG_ROUTE, "deleteActiveWaypoint() case beforeActive != null && afterActive != null");
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
	 * Reverts all Coordinates in the route.
	 */
	public void revertRoute() {
		Log.d(TAG_ROUTE, "revertRoute()");
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
	public void optimizeRoute() {
		Log.d(TAG_ROUTE, "optimizeRoute()");
		RouteInfo optimizedRoute;
		try {
			optimizedRoute = this.routeProcessor
					.computeOptimizedRoute((RouteInfo) this);
			this.routeCoordinates = optimizedRoute.getCoordinates();
		} catch (RouteProcessingException e) {
			// TODO Auto-generated catch block
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
		Log.d(TAG_ROUTE, "getEnd()");
		if (this.routeCoordinates.size() > 0) {
			return this.getWaypoints().getLast();
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
		return FavoritesManager.getInstance().containsRoute(this);
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
		Log.d(TAG_ROUTE, "deletePathBetweenTwoWaypoints(Waypoint " + one + ", Waypoint " + two + ") METHOD START");
		Log.d(TAG_ROUTE, "deletePathBetweenTwoWaypoints(Waypoint, Waypoint) Working on route with size: " + this.routeCoordinates.size());
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
		Log.d(TAG_ROUTE, "deletePathBetweenTwoWaypoints(Waypoint, Waypoint) METHOD END, length of resulting route: " + this.routeCoordinates.size());
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
		Log.d(TAG_ROUTE, "addRouteBetweenTwoCoords(RouteInfo, Coordinate : " + one + ", Coordinate: " + two + ")");
		Log.d(TAG_ROUTE, "addRouteBetweenTwoCoords(RouteInfo, Coord, Coord) working with route size " + this.routeCoordinates.size() + ", adding route size: " + route.getCoordinates().size());
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
		Log.d(TAG_ROUTE, "addRouteBetweenTwoCoords(RouteInfo, Coord, Coord) METHOD END, length of resulting route: " + this.routeCoordinates.size());

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
		
		try {
			output = this.routeProcessor.computeShortestPath(start, end);
		} catch (Exception e) {
			Log.d(TAG_ROUTE, "computeShortestPath() - RETURN TO SENDER, ADDRESS UNKNOWN.");
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
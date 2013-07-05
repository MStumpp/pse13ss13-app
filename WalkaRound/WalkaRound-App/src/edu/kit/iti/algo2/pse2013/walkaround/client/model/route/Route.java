package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.Iterator;
import java.util.LinkedList;

import android.util.Log;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class Route implements RouteInfo {

	private static String TAG_ROUTE = Route.class.getSimpleName();

	private String name;
	private Waypoint activeWaypoint;
	private LinkedList<Coordinate> routeCoordinates;
	private RouteProcessing routeProcessor;
	
	private int idCounter;


	/*
	 *
	 */
	public Route(LinkedList<Coordinate> coordsOfNewRoute) {
		Log.d(TAG_ROUTE, "Route Constructor");
		this.routeCoordinates = coordsOfNewRoute;
		this.activeWaypoint = null;
		this.name = "";
		this.routeProcessor = RouteProcessing.getInstance();
		this.idCounter = 0;
	}
	


	/*
	 *
	 */
	public boolean setActiveWaypoint(Waypoint newActiveWP) {
		Log.d(TAG_ROUTE, "setActiveWaypoint(Waypoint)");
		if(this.containsWaypoint(newActiveWP)) {
			this.activeWaypoint = newActiveWP;
			return true;
		}
		return false;
	}


	/*
	 *
	 */
	public void resetActiveWaypoint() {
		Log.d(TAG_ROUTE, "resetActiveWaypoint()");
		this.activeWaypoint = null;
	}


	/*
	 * Moves the coordinate represented by the active waypoint to the given waypoint-position within the route.
	 */
	public void moveActiveWaypointInOrder(int newPos) {
		Log.d(TAG_ROUTE, "moveActiveWaypointInOrder(int)");
		// TODO:
		Waypoint activeWaypoint = this.activeWaypoint;
		this.deleteActiveWaypoint();
		Waypoint previousWaypoint = this.getPreviousWaypoint(newPos);
		Waypoint nextWaypoint = this.getNextWaypoint(newPos);




		// F�ge den aktiven WP an der �bergebenen Position in die Route ein.

		this.setActiveWaypoint(activeWaypoint);
	}


	/*
	 * Adds a new waypoint at the given coordinate to the end of the route.
	 */
	public void addWaypoint(Coordinate c) {
		Log.d(TAG_ROUTE, "addWaypoint(Coordinate c)");
		if (this.routeCoordinates.size() != 0) {
			Log.d(TAG_ROUTE, "addWaypoint(Coordinate c) -> sending Route to Server");
			RouteInfo routeExtension = this.routeProcessor.computeShortestPath(c, this.getEnd());

			Log.d(TAG_ROUTE, "addWaypoint(Coordinate c) -> addingRoute with " + routeExtension.getCoordinates().size() + " Coordinates");
			this.addRoute(routeExtension);
		} else {
			this.routeCoordinates.add(new Waypoint(c.getLongtitude(), c.getLatitude(), 1, "Wegpunkt"));
		}
		Log.d(TAG_ROUTE, "" + this.routeCoordinates.size());
		this.setActiveWaypoint(this.getEnd());
	}





	/*
	 *
	 */
	public void addRoundtripAtActiveWaypoint(int profile, int length) {
		Log.d(TAG_ROUTE, "addRoundtripAtActiveWaypoint(profile, length()");
		RouteInfo newRoundtrip = this.routeProcessor.computeRoundtrip(this.getActiveWaypoint(), profile, length);
		this.addRoute(newRoundtrip);
	}


	/*
	 * Adds the given RouteInfo to the end of the route.
	 */
	public void addRoute(RouteInfo newRoute) {
		Log.d(TAG_ROUTE, "addRoute(RouteInfo)");
		Iterator<Coordinate> newRouteCoordsIter = newRoute.getCoordinates().iterator();
		
		if (!(this.getEnd().getLatitude() == newRoute.getStart().getLatitude())
				|| !(this.getEnd().getLongtitude() == newRoute.getStart().getLongtitude())) {
			Log.d(TAG_ROUTE, "addRoute(RouteInfo) -> computing intermediate path");
			this.routeProcessor.computeShortestPath(this.getEnd(), newRoute.getStart());
			
		}
		
		newRouteCoordsIter.next();
		while (newRouteCoordsIter.hasNext()) {
			this.routeCoordinates.addLast(newRouteCoordsIter.next());
		}
	}


	/*
	 * Moves the active waypoint to the position of the given coordinate.
	 */
	public void moveActiveWaypoint(Coordinate coord) {
		Log.d(TAG_ROUTE, "moveActiveWaypoint(coord)");
		// TODO:
		if (this.activeWaypoint != null) {
			//Waypoint beforeActive = (Waypoint) this.getWaypointBeforeActiveWaypoint();
			//Waypoint afterActive = (Waypoint) this.getWaypointPastActiveWaypoint();

			//RouteInfo newRouteBeforeActiveWaypoint = this.routeProcessor.computeShortestPath(beforeActive, coord);
			//RouteInfo newRoutePastActiveWaypoint = this.routeProcessor.computeShortestPath(coord, afterActive);


			// Prüfe bei beiden ob null, wenn nicht, schicke Weg an Server zur Neu-Berechnung.
			// Entferne außerdem die alten Routen.
		}
	}


	public void deleteActiveWaypoint() {
		Log.d(TAG_ROUTE, "deleteActiveWaypoint()");

		// TODO:

		Iterator<Coordinate> routeCoordsIter = this.routeCoordinates.iterator();
		Iterator<Coordinate> routeCoordsIterDesc = this.routeCoordinates.descendingIterator();

		while (!routeCoordsIter.equals(this.activeWaypoint)) {
			routeCoordsIter.next();
		}
		while (!routeCoordsIterDesc.equals(this.activeWaypoint)) {
			routeCoordsIterDesc.next();
		}

		LinkedList<Waypoint> waypoints = this.getWaypoints();
		int indexOfActiveWaypoint = waypoints.indexOf(this.getActiveWaypoint());
		Waypoint previousWaypoint = this.getPreviousWaypoint(indexOfActiveWaypoint);
		Waypoint nextWaypoint = this.getNextWaypoint(indexOfActiveWaypoint);


		if (previousWaypoint == null && nextWaypoint == null) {
			this.resetRoute();
		} else if (previousWaypoint == null && nextWaypoint != null) {
			do {
				routeCoordsIter.remove();
				routeCoordsIter.next();
			} while (!routeCoordsIter.equals(nextWaypoint));
		} else if (previousWaypoint != null && nextWaypoint == null) {
			do {
				routeCoordsIterDesc.remove();
				routeCoordsIterDesc.next();
			} while (!routeCoordsIterDesc.equals(previousWaypoint));
		} else {
			RouteInfo routePieceAtPositionOfDeletedActiveWaypoint = this.routeProcessor.computeShortestPath(previousWaypoint, nextWaypoint);

		}
		this.resetActiveWaypoint();
	}





	/*
	 * Reverts all Coordinates in the route.
	 */
	public void revertRoute() {
		Log.d(TAG_ROUTE, "revertRoute()");
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
		RouteInfo optimizedRoute = this.routeProcessor.computeOptimizedRoute(this.clone());
		this.routeCoordinates = optimizedRoute.getCoordinates();
	}


	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Route clone() {
		Log.d(TAG_ROUTE, "clone()");
		LinkedList<Coordinate> clonedCoords = new LinkedList<Coordinate>();
		for (Coordinate coord : this.routeCoordinates) {
			//clonedCoords.add(coord.clone()); // TODO: Entweder clone() implementieren, oder diese Zeile entfernen
		}
		return new Route(clonedCoords);
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
		for (Coordinate cor : this.routeCoordinates) {
			if (cor instanceof Waypoint) {
				waypoints.add((Waypoint)cor);
			}
		}
		return waypoints;
	}


	@Override
	public boolean isFavorite() {
		Log.d(TAG_ROUTE, "isFavorite()");
		return false;
		// TODO: Zugriff auf Favs über getInstance();
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


	/*
	 *
	 */
	private Waypoint getPreviousWaypoint(int posOfWaypoint) {
		Log.d(TAG_ROUTE, "getPreviousWaypoint(int)");
		LinkedList<Waypoint> waypoints = this.getWaypoints();
		if (posOfWaypoint == 0) {
			return null;
		} else {
			return (waypoints.get(posOfWaypoint - 1));
		}
	}


	/*
	 *
	 */
	private Waypoint getNextWaypoint(int posOfWaypoint) {
		Log.d(TAG_ROUTE, "getNextNextWaypoint(int)");
		LinkedList<Waypoint> waypoints = this.getWaypoints();
		if (posOfWaypoint == (waypoints.size() - 1)) {
			return null;
		} else {
			return (waypoints.get(posOfWaypoint + 1));
		}
	}














	private void testMethod() {
		LinkedList<Integer> ints = new LinkedList<Integer>();

	}




	
	private int getNextID() {
		this.idCounter++;
		return this.idCounter;
	}






}

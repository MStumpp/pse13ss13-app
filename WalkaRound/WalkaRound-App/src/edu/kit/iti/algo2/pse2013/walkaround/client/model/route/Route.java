package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.Iterator;
import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class Route implements RouteInfo {

	private String name;
	private Waypoint activeWaypoint;
	private LinkedList<Coordinate> routeCoordinates;
	private RouteProcessing routeProcessor;


	/*
	 *
	 */
	public Route(LinkedList<Coordinate> coordsOfNewRoute) {
		this.routeCoordinates = coordsOfNewRoute;
		this.activeWaypoint = null;
		this.name = "";
		this.routeProcessor = RouteProcessing.getInstance();
	}


	/*
	 *
	 */
	public boolean setActiveWaypoint(Waypoint newActiveWP) {
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
		this.activeWaypoint = null;
	}


	/*
	 * Moves the coordinate represented by the active waypoint to the given waypoint-position within the route.
	 */
	public void moveActiveWaypointInOrder(int newPos) {
		// TODO:
		Coordinate tempCoord = new Coordinate (this.activeWaypoint.getLongtitude(), this.activeWaypoint.getLatitude());
		// TODO: lösche die 0-2 Teilstücke, erstelle einen neuen WP an der entsprechenden WP Coord
		

	}


	/*
	 * Adds a new waypoint at the given position.
	 */
	public void addWaypoint(Coordinate c) {
		RouteInfo routeExtension = this.routeProcessor.computeShortestPath(c, this.getEnd());
		this.addRoute(routeExtension);
		this.setActiveWaypoint(this.getEnd());
	}


	/*
	 *
	 */
	public void addRoundtripAtActiveWaypoint(int profile, int length) {
		RouteInfo newRoundtrip = this.routeProcessor.computeRoundtrip(this.getActiveWaypoint(), profile, length);
		this.addRoute(newRoundtrip);
	}


	/*
	 * Adds the given RouteInfo to the end of the route.
	 */
	public void addRoute(RouteInfo newRoute) {
		Iterator<Coordinate> newRouteCoordsIter = newRoute.getCoordinates().iterator();

		if (!this.getEnd().equals(newRouteCoordsIter)) {
			this.addRoute(this.routeProcessor.computeShortestPath(this.getEnd(), newRoute.getStart()));
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
		// TODO:
		if (this.activeWaypoint != null) {
			Waypoint beforeActive = (Waypoint) this.getWaypointBeforeActiveWaypoint();
			Waypoint afterActive = (Waypoint) this.getWaypointPastActiveWaypoint();
			
			
			
			// Prüfe bei beiden ob null, wenn nicht, schicke Weg an Server zur Neu-Berechnung.
			// Entferne außerdem die alten Routen.
		}
	}


	public void deleteActiveWaypoint() {
		// Entferne alle am aktiven WP anh�ngenden Strecken
		// Wenn

		// TODO:
		Iterator<Coordinate> routeCoordsIter = this.routeCoordinates.iterator();

		boolean startIsActiveWaypoint = this.getActiveWaypoint().equals(this.getActiveWaypoint());
		boolean endIsActiveWaypoint = this.getEnd().equals(this.getActiveWaypoint());

		if (startIsActiveWaypoint && endIsActiveWaypoint) {
			this.resetRoute();
		}

		if (startIsActiveWaypoint && !endIsActiveWaypoint) {

		}

		if (!startIsActiveWaypoint && endIsActiveWaypoint) {

		}

		if (!startIsActiveWaypoint && !endIsActiveWaypoint) {

		}


		// Prüft before / after ActiveWP, entfernt Coords zwischen actWP und bef/aft,
		// wenn bef und aft ungleich null, schicke ShortestPath(bef,aft) an Server
		this.resetActiveWaypoint();
	}


	/*
	 * Reverts all Coordinates in the route.
	 */
	public void revertRoute() {
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
		this.routeCoordinates = new LinkedList<Coordinate>();
	}


	/*
	 *
	 */
	public void optimizeRoute() {
		RouteInfo optimizedRoute = this.routeProcessor.computeOptimizedRoute(this.clone());
		this.routeCoordinates = optimizedRoute.getCoordinates();
	}


	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Route clone() {
		LinkedList<Coordinate> clonedCoords = new LinkedList<Coordinate>();
		for (Coordinate coord : this.routeCoordinates) {
			//clonedCoords.add(coord.clone()); // TODO: Entweder clone() implementieren, oder diese Zeile entfernen
		}
		return new Route(clonedCoords);
	}



	@Override
	public String getname() {
		return this.name;
	}


	@Override
	public Waypoint getStart() {
		return (Waypoint) this.routeCoordinates.getFirst();
	}


	@Override
	public Waypoint getEnd() {
		return (Waypoint) this.routeCoordinates.getLast();
	}


	@Override
	public Waypoint getActiveWaypoint() {
		return this.activeWaypoint;
	}


	@Override
	public LinkedList<Waypoint> getWaypoints() {
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
		return false;
		// TODO: Zugriff auf Favs über getInstance();
	}


	@Override
	public boolean containsWaypoint(Waypoint wp) {
		if (this.getWaypoints().contains(wp)) {
			return true;
		}
		return false;
	}


	@Override
	public LinkedList<Coordinate> getCoordinates() {
		return this.routeCoordinates;
	}

	
	/*
	 * 
	 */
	private Waypoint getWaypointPastActiveWaypoint() {
		if (this.activeWaypoint.equals(this.getEnd())) {
			return null;
		} else {
			LinkedList<Waypoint> waypoints = this.getWaypoints();
			return (waypoints.get(waypoints.indexOf(this.activeWaypoint) + 1));
		}
	}

	
	/*
	 * 
	 */
	private Waypoint getWaypointBeforeActiveWaypoint() {
		if (this.activeWaypoint.equals(this.getStart())) {
			return null;
		} else {
			LinkedList<Waypoint> waypoints = this.getWaypoints();
			return (waypoints.get(waypoints.indexOf(this.activeWaypoint) - 1));
		}
	}



	private void testMethod() {
		LinkedList<Integer> ints = new LinkedList<Integer>();

	}










}

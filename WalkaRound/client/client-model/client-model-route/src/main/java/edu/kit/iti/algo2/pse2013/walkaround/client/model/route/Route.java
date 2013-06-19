package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.Iterator;
import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

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
		//TODO:
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
		Coordinate tempCoord = new Coordinate (this.activeWaypoint.getLongitude, this.activeWaypoint.getLatitude);
// TODO: lösche die 0-2 Teilstücke, erstelle einen neuen WP an der entsprechenden WP Coord

	}

	/*
	 * Adds a new waypoint at the given position.
	 */
	public void addWaypoint(Coordinate) {
		// schicke Berechnung über Shortest Path (this.getEnd() und Coordinate) an Server
		// Füge Ergebnis der Route hinzu.

		// Setze neue WP auf aktiv.
	}

	public void addRoundtripAtActiveWaypoint(int profile, int length) {
		// Starte Berechnung mit Processor
		// Füge Ergebnisroute hinzu.

	}

	/*
	 * Adds the given RouteInfo to the end of the route.
	 */
	public void addRoute(RouteInfo ri) {
		// prüfe ob übergebene Route an aktueller Route endet.
		// wenn ja, füge Route direkt an.
		// wenn nein, berechne Zwischenstück über Server, füge dann Route an. (oder umgekehrt)
	}

	/*
	 * Moves the active waypoint to the position of the given coordinate.
	 */
	public void moveActiveWaypoint(Coordinate coord) {
		if (this.activeWaypoint != null) {
			// Suche WPs vor und nach aktivem WP heraus.
			Waypoint beforeActive;
			Waypoint afterActive;
			// Prüfe bei beiden ob null, wenn nicht, schicke Weg an Server zur Neu-Berechnung.
			// Entferne außerdem die alten Routen.
		}
	}

	public void deleteActiveWaypoint() {
		// Prüft before / after ActiveWP, entfernt Coords zwischen actWP und bef/aft,
		// wenn bef und aft ungleich null, schicke ShortestPath(bef,aft) an Server
		this.resetActiveWaypoint();
	}

	/*
	 * Reverts all Coordinates in the route.
	 */
	public void revertRoute() {
		LinkedList<Coordinate> revertedRoute = new LinkedList<Coordinate>();
		// durchlaufe Liste der Coords, kehre sie 1:1 um
		Iterator<Coordinate> routeCoordsDecIter = this.routeCoordinates.decendingIterator();

		while (routeCoordsDecIter.hasnext()) {
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
		// Sende ganze Route an Server über this.routeProcessor.
		// Setze ganze Route auf Server Ergebnis.

	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Route clone() {
		LinkedList<Coordinate> cloneCoords = new LinkedList<Coordinate>();
		for (Coordinate coord : this.routeCoordinates) {
			cloneCoords.add(coord.clone());
		}
		return new Route(cloneCoords);
	}



	@Override
	public String getname() {
		return this.name;
	}

	@Override
	public Waypoint getStart() {
		return this.routeCoordinates.getFirst();
	}

	@Override
	public Waypoint getEnd() {
		return this.routeCoordinates.getLast();
	}

	@Override
	public Waypoint getActiveWaypoint() {
		return this.activeWaypoint;
	}


	@Override
	public LinkedList<Route> getRoutes() {
		LinkedList<Route> routes = new LinkedList<Route>;
		Iterator<Coordinate> coordIter = this.routeCoordinates.Iterator();

		int waypointsCounted = 0;

		LinkedList<Coordinate> routePiece = new LinkedList<Coordinate>();

		while(coordIter.hasnext()) {
			Coordinate coordTemp = coordIter.next();
			routePiece.add(coordTemp);

			//TODO: Automat überlegen:
			if (coordTemp.isInstanceOf(Waypoint)) {
				waypointsCounted++;
			}




		}

		// durchlaufe routeCoordinates
		// stückle an WPs in einzelne Routen auf

	}

	@Override
	public LinkedList<Waypoint> getWaypoints() {
		LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();
		for (Coordinate cor : this.routeCoordinates) {
			if (cor.isInstanceOf(Waypoint)) {
				waypoints.add(Waypoint(cor));
			}
		}
	}

	public boolean containsWaypoint(Coordinate coord) {
		if (this.routeCoordinates.contains((Waypoint) coord)) {
			return true;
		}
		return false;
	}



	@Override
	public boolean isFavorite() {
		// Zugriff auf Favs über getInstance();
	}

	@Override
	public boolean containsWaypoint(Waypoint wp) {
		// TODO Auto-generated method stub
		return false;
	}



	/*
	 * RouteProcessing Methoden sind:
	 * computeOptimizedRoute(RouteInfo):RouteInfo
computeShortestPath(Coordinate, Coordinate):RouteInfo
computeRoundtrip(Coordinate, int profile, int length):RouteInfo
	 *
	 *
	 *
	 */

}

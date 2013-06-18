package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.LinkedList;

public class Route implements RouteInfo {
	
	private Waypoint activeWaypoint;
	private LinkedList<Coordinate> routeCoordinates;
	
	
	public Route(LinkedList<Coordinate> coordsOfNewRoute) {
		this.routeCoordinates = coordsOfNewRoute;
		this.activeWaypoint = null;
	}
	
	
	public boolean setActiveWaypoint(Waypoint newActiveWP) {
// TODO: Prüfe ob Wegpunkt in Route:		
		this.activeWaypoint = newActiveWP;
	}
	
	public void resetActiveWaypoint() {
		this.activeWaypoint = null;
	}
	
	public void moveActiveWaypointInOrder(int newPos) {
		Coordinate tempCoord = new Coordinate (this.activeWaypoint.getLongitude, this.activeWaypoint.getLatitude);
// TODO: lösche die 0-2 Teilstücke, erstelle einen neuen WP an der entsprechenden WP Coord 
		
	}
	
	public void addWaypoint(Coordinate) {
		// schicke Berechnung über Shortest Path (this.getEnd() und Coordinate) an Server
		// Füge Ergebnis der Route hinzu.
		
		// Setze neue WP auf aktiv.
	}
	
	public void addRoundtripAtActiveWaypoint(int profile, int length) {
		// Starte Berechnung mit Processor
		// Füge Ergebnisroute hinzu.
		
	}
	
	
	public void addRoute(RouteInfo) {
		// prüfe ob übergebene Route an aktueller Route endet.
		// wenn ja, füge Route direkt an.
		// wenn nein, berechne Zwischenstück über Server, füge dann Route an. (oder umgekehrt)
	}
	
	public void moveActiveWaypoint(Coordinate) {
		// Suche WPs vor und nach aktivem WP heraus.
		Waypoint beforeActive;
		Waypoint afterActive;
		// Prüfe bei beiden ob null, wenn nicht, schicke Weg an Server zur Neu-Berechnung.
		// Entferne außerdem die alten Routen.
		
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
		// durchlaufe Liste der Coords, kehre sie 1:1 um
		
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
		// Sende ganze Route an Server.
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Waypoint getStart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Waypoint getEnd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Waypoint getActiveWaypoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<Route> getRoutes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<Waypoint> getWaypoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsWaypoint() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFavorite() {
		// TODO Auto-generated method stub
		return false;
	}}

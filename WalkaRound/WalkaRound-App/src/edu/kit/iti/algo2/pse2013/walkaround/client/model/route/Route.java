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
		Waypoint activeWaypoint = this.activeWaypoint;
		this.deleteActiveWaypoint();
		Waypoint previousWaypoint = this.getPreviousWaypoint(newPos);
		Waypoint nextWaypoint = this.getNextWaypoint(newPos);
		
		
		
		
		// Füge den aktiven WP an der übergebenen Position in die Route ein.
		
		this.setActiveWaypoint(activeWaypoint);
	}


	/*
	 * Adds a new waypoint at the given coordinate to the end of the route.
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
			
			RouteInfo newRouteBeforeActiveWaypoint = this.routeProcessor.computeShortestPath(beforeActive, coord);
			RouteInfo newRoutePastActiveWaypoint = this.routeProcessor.computeShortestPath(coord, afterActive);
			
			
			// PrÃ¼fe bei beiden ob null, wenn nicht, schicke Weg an Server zur Neu-Berechnung.
			// Entferne auÃŸerdem die alten Routen.
		}
	}


	public void deleteActiveWaypoint() {
		Log.d();
		// Entferne alle am aktiven WP anhï¿½ngenden Strecken
		// Wenn
		
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
		// TODO: Zugriff auf Favs Ã¼ber getInstance();
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
	private Waypoint getPreviousWaypoint(int posOfWaypoint) {
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










}

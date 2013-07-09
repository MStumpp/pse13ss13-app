package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoritesManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Profile;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class RouteMenuController {

	private LinkedList<RouteListener> routeListeners;
	private Route currentRoute;

	private static boolean intanceExists;
	private static RouteMenuController routeMC;

	private RouteMenuController() {
		this.routeListeners = new LinkedList<RouteListener>();
		// Beispiele aus MapController :
		// public static Coordinate defaultCoordinate = new Coordinate(49.00471,
		// 8.3858300); // Brauerstraße
		// public static Coordinate defaultCoordinate = new Coordinate(49.0145,
		// 8.419); // 211
		// public static Coordinate defaultCoordinate = new Coordinate(49.01,
		// 8.40333); // Marktplatz
		LinkedList<Coordinate> ll = new LinkedList<Coordinate>();
		//ll.add(new Coordinate(49.00471, 8.3858300));
		//ll.add(new Coordinate(49.0145, 8.419));
		//ll.add(new Coordinate(49.01, 8.40333));
		this.currentRoute = new Route(ll);

		
	}

	public static RouteMenuController getInstance() {
		if (!intanceExists) {
			routeMC = new RouteMenuController();
		}
		return routeMC;
	}

	public void registerRouteListener(RouteListener newRL) {
		if (!this.routeListeners.contains(newRL)) {
			this.routeListeners.add(newRL);
		}
		this.notifyAllRouteListeners();
	}

	private void notifyAllRouteListeners() {
		Waypoint activeWaypoint = this.currentRoute.getActiveWaypoint();
		if(this.currentRoute != null && activeWaypoint != null) {
			for (RouteListener rl : this.routeListeners) {
				rl.onRouteChange((RouteInfo)this.currentRoute, activeWaypoint);
			}
		}
	}

	public void setActiveWaypoint(Waypoint wp) {
		this.currentRoute.setActiveWaypoint(wp);
		this.notifyAllRouteListeners();
	}

	public void resetAvticeWaypoint() {
		this.currentRoute.resetActiveWaypoint();
		this.notifyAllRouteListeners();
	}

	public void moveActiveWaypointInOrder(int i) {
		this.currentRoute.moveActiveWaypointInOrder(i);
		this.notifyAllRouteListeners();
	}

	public void addWaypoint(Coordinate c) {
		this.currentRoute.addWaypoint(c);
		this.notifyAllRouteListeners();
	}

	public void addRoundtrip(Profile p, int i) {
		this.currentRoute.addRoundtripAtActiveWaypoint(p.getID(), i);
		this.notifyAllRouteListeners();
	}

	public void addRoute(RouteInfo ri) {
		this.currentRoute.addRoute(ri);
		this.notifyAllRouteListeners();
	}

	public void moveActiveWaypoint(Coordinate c) {
		this.currentRoute.moveActiveWaypoint(c);
		this.notifyAllRouteListeners();
	}

	public void deleteActiveWaypoint() {
		this.currentRoute.deleteActiveWaypoint();
		this.notifyAllRouteListeners();
	}

	public void revertRoute() {
		this.currentRoute.revertRoute();
		this.notifyAllRouteListeners();
	}

	public void resetRoute() {
		this.currentRoute.resetRoute();
		this.notifyAllRouteListeners();
	}

	public void optimizeRoute() {
		this.currentRoute.optimizeRoute();
		this.notifyAllRouteListeners();
	}

	public void replaceFullRoute(Route r) {
		this.currentRoute = r;
		this.notifyAllRouteListeners();
	}

	public boolean containsWaypoint(Waypoint w) {
		return this.currentRoute.containsWaypoint(w);
	}

	public void addRouteToFavorites(RouteInfo ri, String name) {
		FavoritesManager.getInstance().addRouteToFavorites(ri, name);
	}

	public void addLocationToFavorites(Location ri, String name) {
		FavoritesManager.getInstance().addLocationToFavorites(ri, name);
	}

}

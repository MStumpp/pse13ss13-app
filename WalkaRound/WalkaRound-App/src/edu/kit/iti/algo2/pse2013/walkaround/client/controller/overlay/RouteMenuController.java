package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Profile;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

public class RouteMenuController {

	private LinkedList<RouteListener> routeListeners;
	private Route currentRoute;

	private static boolean intanceExists;
	private static RouteMenuController routeMC;

	private RouteMenuController() {
		this.routeListeners = new LinkedList<RouteListener>();
		this.currentRoute = new Route(new LinkedList<Coordinate>());
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
		for (RouteListener rl : this.routeListeners) {
			rl.onRouteChange((RouteInfo)this.currentRoute, activeWaypoint);
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
		//TODO
	}

	public void addLocationToFavorites(Location ri, String name) {
		//TODO
	}


}

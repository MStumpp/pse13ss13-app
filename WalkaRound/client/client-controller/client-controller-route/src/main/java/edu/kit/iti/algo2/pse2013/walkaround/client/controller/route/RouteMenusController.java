package edu.kit.iti.algo2.pse2013.walkaround.client.controller.route;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Profile;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

public class RouteMenusController {

	private LinkedList<RouteListener> routeListeners;
	private Route currentRoute;

	private static boolean intanceExists;
	private static RouteMenusController routeMC;

	private RouteMenusController() {
		this.routeListeners = new LinkedList<>();
		this.currentRoute = new Route(new LinkedList<Coordinate>());
	}

	public static RouteMenusController getInstance() {
		if (!intanceExists) {
			routeMC = new RouteMenusController();
		}
		return routeMC;
	}

	public void registerRouteListener(RouteListener newRL) {
		if (!this.routeListeners.contains(newRL)) {
			this.routeListeners.add(newRL);
		}
	}

	private void notifyAllRouteListeners() {
		for (RouteListener rl : this.routeListeners) {
			rl.onRouteChange((RouteInfo)this.currentRoute, this.currentRoute.getActiveWaypoint());
		}
	}


	public void setActiveWaypoint(Waypoint wp) {
		this.currentRoute.setActiveWaypoint(wp);
	}

	public void resetAvticeWaypoint() {
		this.currentRoute.resetActiveWaypoint();
	}

	public void moveActiveWaypointInOrder(int i) {
		this.currentRoute.moveActiveWaypointInOrder(i);
	}

	public void addWaypoint(Coordinate c) {
		this.currentRoute.addWaypoint(c);
	}

	public void addRoundtrip(Profile p, int i) {
		this.currentRoute.addRoundtripAtActiveWaypoint(p.getID(), i);
	}

	public void addRoute(RouteInfo ri) {
		this.currentRoute.addRoute(ri);
	}

	public void moveActiveWaypoint(Coordinate c) {
		this.currentRoute.moveActiveWaypoint(c);
	}

	public void deleteActiveWaypoint() {
		this.currentRoute.deleteActiveWaypoint();
	}

	public void revertRoute() {
		this.currentRoute.revertRoute();
	}

	public void resetRoute() {
		this.currentRoute.resetRoute();
	}

	public void optimizeRoute() {
		this.currentRoute.optimizeRoute();
	}

	public void replaceFullRoute(Route r) {
		this.currentRoute = r;
	}

	public boolean containsWaypoint(Coordinate c) {
		return this.currentRoute.containsWaypoint(c);
	}

	public void addRouteToFavorites(RouteInfo ri, String name) {
		//TODO
	}

	public void addLocationToFavorites(Location ri, String name) {
		//TODO
	}


}
package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import java.util.LinkedList;

import android.util.Log;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoritesManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Profile;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class RouteController {
	
	private static String TAG_ROUTE_CONTROLLER = "RouteController";

	private static final String TAG = RouteController.class.getSimpleName();
	private LinkedList<RouteListener> routeListeners;
	private Route currentRoute;

	private static RouteController routeMC;

	private RouteController() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController Constructor");
		this.routeListeners = new LinkedList<RouteListener>();
		LinkedList<Coordinate> coordinateList = new LinkedList<Coordinate>();
		this.currentRoute = new Route(coordinateList);
	}

	public static RouteController getInstance() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.getInstance()");
		if (routeMC == null) {
			routeMC = new RouteController();
		}
		return routeMC;
	}

	public void registerRouteListener(RouteListener newRL) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.registerRouteListener(RouteListener " + newRL.getClass().getSimpleName() + ")");
		if (!this.routeListeners.contains(newRL)) {
			this.routeListeners.add(newRL);
		}
		this.notifyAllRouteListeners();
	}

	private void notifyAllRouteListeners() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.notifyAllRouteListeners() - sending Route with " + this.currentRoute.getCoordinates().size() + " Coordinates and " + this.currentRoute.getWaypoints().size() + " Waypoints.");
		Log.d((TAG_ROUTE_CONTROLLER), "Coordinates of Route: " + this.currentRoute);
		Waypoint activeWaypoint = this.currentRoute.getActiveWaypoint();
		if (this.currentRoute != null && activeWaypoint != null) {
			for (RouteListener rl : this.routeListeners) {
				rl.onRouteChange((RouteInfo)this.currentRoute, activeWaypoint);
			}
		}
	}

	public void setActiveWaypoint(Waypoint wp) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.setActiveWaypoint(Waypoint)");
		this.currentRoute.setActiveWaypoint(wp);
		this.notifyAllRouteListeners();
	}

	public void resetAvticeWaypoint() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.resetActiveWaypoint()");
		this.currentRoute.resetActiveWaypoint();
		this.notifyAllRouteListeners();
	}

	public void moveActiveWaypointInOrder(int i) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.moveActiveWaypointInOrder(int)");
		this.currentRoute.moveActiveWaypointInOrder(i);
		this.notifyAllRouteListeners();
	}

	public void addWaypoint(Coordinate c) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.addWaypoint(Coordinate)");
		Log.d(TAG, "addWaypoint(" + c + ")");
		this.currentRoute.addWaypoint(c);
		this.notifyAllRouteListeners();
	}

	public void addRoundtrip(int profileID, int length) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.addRoundtrip(int-profile " + profileID + ", int-length " + length + ")");
		this.currentRoute.addRoundtripAtActiveWaypoint(profileID, length);
		this.notifyAllRouteListeners();
	}

	public void addRoute(RouteInfo ri) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.addRoute(RouteInfo)");
		this.currentRoute.addRoute(ri);
		this.notifyAllRouteListeners();
	}

	public void moveActiveWaypoint(Coordinate c) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.moveActiveWaypoint(Coordinate)");
		this.currentRoute.moveActiveWaypoint(c);
		this.notifyAllRouteListeners();
	}

	public void deleteActiveWaypoint() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.deleteActiveWaypoint()");
		this.currentRoute.deleteActiveWaypoint();
		this.notifyAllRouteListeners();
	}

	public void revertRoute() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.revertRoute()");
		this.currentRoute.revertRoute();
		this.notifyAllRouteListeners();
	}

	public void resetRoute() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.resetRoute()");
		this.currentRoute.resetRoute();
		this.notifyAllRouteListeners();
	}

	public void optimizeRoute() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.optimizeRoute()");
		this.currentRoute.optimizeRoute();
		this.notifyAllRouteListeners();
	}

	public void replaceFullRoute(Route r) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.replaceFullRoute(Route)");
		this.currentRoute = r;
		this.notifyAllRouteListeners();
	}

	public boolean containsWaypoint(Waypoint w) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.containsWaypoint(Waypoint)");
		return this.currentRoute.containsWaypoint(w);
	}

	public void addRouteToFavorites(RouteInfo ri, String name) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.addRouteToFavorites(RouteInfo, String)");
		FavoritesManager.getInstance().addRouteToFavorites(ri, name);
	}

	public void addLocationToFavorites(Location ri, String name) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.addLocationToFavorites(Location, String)");
		FavoritesManager.getInstance().addLocationToFavorites(ri, name);
	}

}

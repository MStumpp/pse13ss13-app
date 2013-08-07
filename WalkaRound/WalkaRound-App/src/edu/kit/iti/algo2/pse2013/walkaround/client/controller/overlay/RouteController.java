package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import java.util.LinkedList;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoritesManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class RouteController {

	public class Routelistener {

	}

	private static String TAG_ROUTE_CONTROLLER = "RouteController";
	
	private static final String TAG = RouteController.class.getSimpleName();
	private LinkedList<RouteListener> routeListeners;
	private Route currentRoute;
	private static Thread routeChanger;
	
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
		Log.d(TAG_ROUTE_CONTROLLER,
				"RouteController.registerRouteListener(RouteListener "
						+ newRL.getClass().getSimpleName() + ")");
		if (!this.routeListeners.contains(newRL)) {
			this.routeListeners.add(newRL);
		}
		this.notifyAllRouteListeners();
	}

	public void unregisterRouteListener(RouteListener formerRL) {
		Log.d(TAG_ROUTE_CONTROLLER,
				"RouteController.unregisterRouteListener(RouteListener "
						+ formerRL.getClass().getSimpleName() + ")");
		if (!this.routeListeners.contains(formerRL)) {
			this.routeListeners.remove(formerRL);
		}
	}

	private void notifyAllRouteListeners() {
		Log.d(TAG_ROUTE_CONTROLLER,
				"RouteController.notifyAllRouteListeners() - sending Route with "
						+ this.currentRoute.getCoordinates().size()
						+ " Coordinates and "
						+ this.currentRoute.getWaypoints().size()
						+ " Waypoints to " + this.routeListeners.size()
						+ " listeners.");
		Log.d(TAG_ROUTE_CONTROLLER, "Coordinates of Route: "
				+ this.currentRoute);
		Waypoint activeWaypoint = this.currentRoute.getActiveWaypoint();
		if (this.currentRoute != null && activeWaypoint != null) {
			for (RouteListener rl : this.routeListeners) {
				rl.onRouteChange((RouteInfo) this.currentRoute);
			}
		}
	}

	/**
	 * Sets a Waypoint active by his id
	 * 
	 * @param id
	 *            of the Waypoint
	 */
	public boolean setActiveWaypoint(int id) {
		// TODO: in boolean ändern
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.setActiveWaypoint(id)");
		
		for (Waypoint value : this.currentRoute.getWaypoints()) {
			if (value.getId() == id) {
				this.setActiveWaypoint(value);
				return true;
			}
		}
		return false;
	}

	public boolean setActiveWaypoint(final Waypoint wp) {
		Log.d(TAG_ROUTE_CONTROLLER,
				"RouteController.setActiveWaypoint(Waypoint)");
		
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {
				@Override
				public void run() {
					newCurrentRoute.setActiveWaypoint(wp);
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					RouteController.getInstance().notifyAllRouteListeners();
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean resetActiveWaypoint() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.resetActiveWaypoint()");

		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {
				@Override
				public void run() {
					newCurrentRoute.resetActiveWaypoint();
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					RouteController.getInstance().notifyAllRouteListeners();
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
		
	}

	public boolean moveActiveWaypointInOrder(final int i) {
		// TODO: in boolean ändern
		Log.d(TAG_ROUTE_CONTROLLER,
				"RouteController.moveActiveWaypointInOrder(int)");
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {
				@Override
				public void run() {
					newCurrentRoute.moveActiveWaypointInOrder(i);
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					RouteController.getInstance().notifyAllRouteListeners();
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean addWaypoint(final Coordinate c) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.addWaypoint(Coordinate)");
		Log.d(TAG, "addWaypoint(" + c + ")");
		
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {
				@Override
				public void run() {
					newCurrentRoute.addWaypoint(c);
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					RouteController.getInstance().notifyAllRouteListeners();
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean addRoundtrip(final int profileID, final int length) {
		// TODO: in boolean ändern
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.addRoundtrip(int-profile "
				+ profileID + ", int-length " + length + ")");
		
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {
				@Override
				public void run() {
					newCurrentRoute.addRoundtripAtActiveWaypoint(profileID, length);
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					RouteController.getInstance().notifyAllRouteListeners();
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean addRoute(final RouteInfo ri) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.addRoute(RouteInfo)");
		
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {
				@Override
				public void run() {
					newCurrentRoute.addRoute(ri);
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					RouteController.getInstance().notifyAllRouteListeners();
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean moveActiveWaypoint(final Coordinate c) {
		// TODO: in boolean ändern
		Log.d(TAG_ROUTE_CONTROLLER,
				"RouteController.moveActiveWaypoint(Coordinate)");
		
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {
				@Override
				public void run() {
					newCurrentRoute.moveActiveWaypoint(c);
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					RouteController.getInstance().notifyAllRouteListeners();
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean deleteActiveWaypoint() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.deleteActiveWaypoint()");
		
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {
				@Override
				public void run() {
					newCurrentRoute.deleteActiveWaypoint();
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					RouteController.getInstance().notifyAllRouteListeners();
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
		
	}

	public boolean revertRoute() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.revertRoute()");
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {
				@Override
				public void run() {
					newCurrentRoute.revertRoute();
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					RouteController.getInstance().notifyAllRouteListeners();
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean resetRoute() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.resetRoute()");
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {
				@Override
				public void run() {
					newCurrentRoute.resetRoute();
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					RouteController.getInstance().notifyAllRouteListeners();
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean optimizeRoute() {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.optimizeRoute()");

		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {
				@Override
				public void run() {
					newCurrentRoute.optimizeRoute();
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					RouteController.getInstance().notifyAllRouteListeners();
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public void replaceFullRoute(RouteInfo r) {
		Log.d(TAG_ROUTE_CONTROLLER, "RouteController.replaceFullRoute(Route)");
		this.currentRoute = (Route) r;
		this.notifyAllRouteListeners();
	}

	public boolean containsWaypoint(Waypoint w) {
		Log.d(TAG_ROUTE_CONTROLLER,
				"RouteController.containsWaypoint(Waypoint)");
		return this.currentRoute.containsWaypoint(w);
	}

	public void addRouteToFavorites(String name) {
		// TODO: in boolean ändern (namen dürfen nicht doppelt vorkommen,
		// favoriten manager containsName())
		Log.d(TAG_ROUTE_CONTROLLER,
				"RouteController.addRouteToFavorites(RouteInfo, String)");
		FavoritesManager.getInstance().addRouteToFavorites(currentRoute, name);
	}

	public void addLocationToFavorites(Location ri, String name) {
		// TODO: in boolean ändern (namen dürfen nicht doppelt vorkommen,
		// favoriten manager containsName())
		Log.d(TAG_ROUTE_CONTROLLER,
				"RouteController.addLocationToFavorites(Location, String)");
		FavoritesManager.getInstance().addLocationToFavorites(ri, name);
	}
	
	
	
	
	
	
	

}
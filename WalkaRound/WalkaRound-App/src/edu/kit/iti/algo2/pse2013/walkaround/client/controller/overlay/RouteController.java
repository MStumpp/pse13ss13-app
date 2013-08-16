package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import java.util.LinkedList;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class RouteController {

	public class Routelistener {

	}

	private static final String TAG = RouteController.class.getSimpleName();
	private LinkedList<RouteListener> routeListeners;
	private Route currentRoute;
	private static Thread routeChanger;

	private static RouteController routeMC;

	private RouteController() {
		Log.d(TAG, "RouteController Constructor");
		this.routeListeners = new LinkedList<RouteListener>();
		LinkedList<Coordinate> coordinateList = new LinkedList<Coordinate>();
		this.currentRoute = new Route(coordinateList);
	}

	public static RouteController getInstance() {
		Log.d(TAG, "RouteController.getInstance()");
		if (routeMC == null) {
			routeMC = new RouteController();
		}
		return routeMC;
	}

	public void registerRouteListener(RouteListener newRL) {
		Log.d(TAG,
				"RouteController.registerRouteListener(RouteListener "
						+ newRL.getClass().getSimpleName() + ")");
		if (!this.routeListeners.contains(newRL)) {
			this.routeListeners.add(newRL);
		}
		this.notifyAllRouteListeners();
	}

	public void unregisterRouteListener(RouteListener formerRL) {
		Log.d(TAG, "RouteController.unregisterRouteListener(RouteListener "
						+ formerRL.getClass().getSimpleName() + ")");
		if (!this.routeListeners.contains(formerRL)) {
			this.routeListeners.remove(formerRL);
		}
	}

	private void notifyAllRouteListeners() {
		Log.d(TAG,
			String.format(
				"RouteController.notifyAllRouteListeners() - sending Route with %d Coordinates and %d Waypoints to %d listeners.",
				currentRoute.getCoordinates().size(),
				currentRoute.getWaypoints().size(),
				routeListeners.size()
			)
		);
		Log.d(TAG, "Coordinates of Route: "
				+ this.currentRoute);
		Waypoint activeWaypoint = this.currentRoute.getActiveWaypoint();
		if (this.currentRoute != null) {
			for (RouteListener rl : routeListeners) {
				Log.d(TAG, "Notify " + rl.getClass().getSimpleName());
				rl.onRouteChange((RouteInfo) currentRoute);
			}
		} else {
			Log.d(
				TAG,
				String.format(
					"No RouteListener was notified, because %s!",
					currentRoute == null ? "currentRoute is null" : activeWaypoint == null ? "activeWaypoint is null" : "no RouteListener is registered"
				)
			);
		}
	}

	/**
	 * Sets a Waypoint active by his id
	 *
	 * @param id	ID of the Waypoint
	 */
	public boolean setActiveWaypoint(int id) {
		Log.d(TAG, "Routecontroller.setActiveWaypoint(id)");

		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			this.currentRoute.setActiveWaypoint(id);
			this.notifyAllRouteListeners();
			return true;
		}

		/* OLD VERSION
		if (this.currentRoute.getActiveWaypoint().getId() != id) {
			if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
				final Route newCurrentRoute = this.currentRoute;
				RouteController.routeChanger = new Thread (new Runnable() {

					public void run() {
						Log.d(TAG_ROUTE_CONTROLLER, "Thread.run() in setActiveWaypoint(id)");
						newCurrentRoute.setActiveWaypoint(id);
						RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					}
				});
				RouteController.routeChanger.start();
				return true;
			}
		}*/

		return false;
	}


	public boolean setActiveWaypoint(final Waypoint wp) {
		Log.d(TAG, "RouteController.setActiveWaypoint(Waypoint)");

		if (!this.currentRoute.getActiveWaypoint().equals(wp)) {
			if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
				final Route newCurrentRoute = this.currentRoute;
				RouteController.routeChanger = new Thread (new Runnable() {
					public void run() {
						Log.d(TAG, "Thread.run() in setActiveWaypoint(wp)");
						newCurrentRoute.setActiveWaypoint(wp);
						RouteController.getInstance().replaceFullRoute(newCurrentRoute);
					}
				});
				RouteController.routeChanger.start();
				return true;
			}
		}
		return false;
	}

	public boolean resetActiveWaypoint() {
		Log.d(TAG, "RouteController.resetActiveWaypoint()");

		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {

				public void run() {
					Log.d(TAG, "Thread.run() in resetActiveWaypoint()");
					newCurrentRoute.resetActiveWaypoint();
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;

	}

	/*public boolean moveActiveWaypointInOrder(final int i) {
		Log.d(TAG,
				"RouteController.moveActiveWaypointInOrder(int)");
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {

				public void run() {
					Log.d(TAG, "Thread.run() in moveActiveWaypointInOrder(int)");
					newCurrentRoute.moveActiveWaypointInOrder(i);
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}*/

	public boolean addWaypoint(final Waypoint w) {
		Log.d(TAG, String.format("RouteController.addWaypoint(%s)", w.toString()));

		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {

				public void run() {
					Log.d(TAG, String.format("Thread.run() in RouteController.addWaypoint(%s)", w.toString()));
					newCurrentRoute.addWaypoint(w);
					replaceFullRoute(newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		Log.e(TAG, String.format("%s wurde NICHT der Route hinzugefügt! Der routeChanger ist gerade beschäftigt.", w.toString()));
		return false;
	}

	public boolean addRoundtrip(final int profileID, final int length) {
		Log.d(TAG, "RouteController.addRoundtrip(int-profile "
				+ profileID + ", int-length " + length + ")");

		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {

				public void run() {
					Log.d(TAG, "Thread.run() in addRoundtrip(int profile, int length)");
					newCurrentRoute.addRoundtripAtActiveWaypoint(profileID, length);
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean addRoute(final RouteInfo ri) {
		Log.d(TAG, "RouteController.addRoute(RouteInfo)");

		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {

				public void run() {
					Log.d(TAG, "Thread.run() in addRoute(RouteInfo)");
					newCurrentRoute.addRoute(ri);
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean moveActiveWaypoint(Coordinate c) {
		Log.d(TAG,	"RouteController.moveActiveWaypoint(Coordinate) METHOD START");

		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			this.currentRoute.moveActiveWaypoint(c);
			this.notifyAllRouteListeners();
			return true;
		}

		Log.d(TAG,	"RouteController.moveActiveWaypoint(Coordinate) returning false");
		return false;
	}

	public boolean deleteActiveWaypoint() {
		Log.d(TAG, "RouteController.deleteActiveWaypoint()");
		
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {

				public void run() {
					Log.d(TAG, "Thread.run() in deleteActiveWaypoint()");
					newCurrentRoute.deleteActiveWaypoint();
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean invertRoute() {
		Log.d(TAG, "invertRoute()");
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {

				public void run() {
					Log.d(TAG, "Thread.run() in invertRoute()");
					newCurrentRoute.invertRoute();
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean resetRoute() {
		Log.d(TAG, "RouteController.resetRoute()");
		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {

				public void run() {
					Log.d(TAG, "Thread.run() in resetRoute()");
					newCurrentRoute.resetRoute();
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	/*public boolean optimizeRoute() {
		Log.d(TAG, "RouteController.optimizeRoute()");

		if (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread (new Runnable() {

				public void run() {
					Log.d(TAG, "Thread.run() in optimizeRoute()");
					newCurrentRoute.optimizeRoute();
					RouteController.getInstance().replaceFullRoute(newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}*/

	public void replaceFullRoute(RouteInfo r) {
		Log.d(TAG, "RouteController.replaceFullRoute(Route)");
		this.currentRoute = (Route) r;
		this.notifyAllRouteListeners();
	}

	public boolean containsWaypoint(Waypoint w) {
		Log.d(TAG,
				"RouteController.containsWaypoint(Waypoint)");
		return this.currentRoute.containsWaypoint(w);
	}

	public boolean addRouteToFavorites(String name) {
		Log.d(TAG, "RouteController.addRouteToFavorites(RouteInfo, String)");
		return FavoriteManager.getInstance().addRouteToFavorites(currentRoute, name);
	}

	public boolean addLocationToFavorites(Location ri, String name) {
		Log.d(TAG,
				"RouteController.addLocationToFavorites(Location, String)");
		return FavoriteManager.getInstance().addLocationToFavorites(ri, name);
	}
}
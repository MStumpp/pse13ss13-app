package edu.kit.iti.algo2.pse2013.walkaround.client.controller;

import java.util.LinkedList;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

/**
 * This is class holds and manage the current Route
 *
 * @author Lukas Müller
 * @version 1.0
 *
 */
public class RouteController {

	private static final String TAG = RouteController.class.getSimpleName();
	private LinkedList<RouteListener> routeListeners;
	private Route currentRoute;
	private static Thread routeChanger;
	private static RouteController routeController;

	private RouteController() {
		Log.d(TAG, "RouteController Constructor");
		this.routeListeners = new LinkedList<RouteListener>();
		LinkedList<Coordinate> coordinateList = new LinkedList<Coordinate>();
		this.currentRoute = new Route(coordinateList);
	}

	public static RouteController getInstance() {
		if (routeController == null) {
			routeController = new RouteController();
		}
		return routeController;
	}

	public void registerRouteListener(RouteListener newRL) {
		Log.d(TAG, "RouteController.registerRouteListener(RouteListener "
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

	public void notifyAllRouteListeners() {
		Log.d(
			TAG,
			String.format(
				"RouteController.notifyAllRouteListeners() - sending Route with %d Coordinates and %d Waypoints to %d listeners.",
				currentRoute.getCoordinates().size(),
				currentRoute.getWaypoints().size(),
				routeListeners.size()
			)
		);
		Log.d(TAG, "Coordinates of Route: " + this.currentRoute);
		Waypoint activeWaypoint = this.currentRoute.getActiveWaypoint();
		if (this.currentRoute != null) {
			for (RouteListener rl : routeListeners) {
				Log.d(TAG, "Notify " + rl.getClass().getSimpleName());
				rl.onRouteChange(currentRoute);
			}
		} else {
			Log.d(TAG, String.format(
					"No RouteListener was notified, because %s!",
					currentRoute == null ? "currentRoute is null"
							: activeWaypoint == null ? "activeWaypoint is null"
									: "no RouteListener is registered"));
		}
	}

	/**
	 * Sets a Waypoint active by his id
	 *
	 * @param id
	 *            ID of the Waypoint
	 * @return if the operation was successful
	 */
	public boolean setActiveWaypoint(int id) {
		Log.d(TAG, "Routecontroller.setActiveWaypoint(id)");

		if (this.isRouteChangerInactive()) {
			this.currentRoute.setActiveWaypoint(id);
			this.notifyAllRouteListeners();
			return true;
		}
		return false;
	}

	public boolean setActiveWaypoint(final Waypoint wp) {
		Log.d(TAG, "RouteController.setActiveWaypoint(Waypoint)");
		
		if (this.isRouteChangerInactive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread(new Runnable() {
				@Override
				public void run() {
					Log.d(TAG, "Thread.run() in setActiveWaypoint(wp)");
					newCurrentRoute.setActiveWaypoint(wp);
					RouteController.getInstance().replaceFullRoute(
							newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean resetActiveWaypoint() {
		Log.d(TAG, "RouteController.resetActiveWaypoint()");

		if (this.isRouteChangerInactive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread(new Runnable() {

				@Override
				public void run() {
					Log.d(TAG, "Thread.run() in resetActiveWaypoint()");
					newCurrentRoute.resetActiveWaypoint();
					RouteController.getInstance().replaceFullRoute(
							newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;

	}

	/*
	 * public boolean moveActiveWaypointInOrder(final int i) { Log.d(TAG,
	 * "RouteController.moveActiveWaypointInOrder(int)"); if
	 * (RouteController.routeChanger == null ||
	 * !RouteController.routeChanger.isAlive()) { final Route newCurrentRoute =
	 * this.currentRoute; RouteController.routeChanger = new Thread (new
	 * Runnable() {
	 *
	 * public void run() { Log.d(TAG,
	 * "Thread.run() in moveActiveWaypointInOrder(int)");
	 * newCurrentRoute.moveActiveWaypointInOrder(i);
	 * RouteController.getInstance().replaceFullRoute(newCurrentRoute); } });
	 * RouteController.routeChanger.start(); return true; } return false; }
	 */

	public boolean addWaypoint(final Waypoint w) {
		Log.d(TAG,
				String.format("RouteController.addWaypoint(%s)", w.toString()));

		if (this.isRouteChangerInactive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread(new Runnable() {

				@Override
				public void run() {
					Log.d(TAG, String.format(
							"Thread.run() in RouteController.addWaypoint(%s)",
							w.toString()));

					newCurrentRoute.addWaypoint(w);
					replaceFullRoute(newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			notifyAllRouteListeners();
			return true;
		}
		Log.e(TAG,
				String.format(
						"%s wurde NICHT der Route hinzugefügt! Der routeChanger ist gerade beschäftigt.",
						w.toString()));
		return false;
	}

	public boolean addRoundtrip(final int profileID, final int length) {
		Log.d(TAG, "RouteController.addRoundtrip(int-profile " + profileID
				+ ", int-length " + length + ")");

		if (this.isRouteChangerInactive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread(new Runnable() {

				@Override
				public void run() {
					Log.d(TAG,String.format("Thread.run() in addRoundtrip(profile %d, length %d)", profileID, length));
					newCurrentRoute.addRoundtripAtActiveWaypoint(profileID,length);
					RouteController.getInstance().replaceFullRoute(
							newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean addRoute(final RouteInfo ri) {
		Log.d(TAG, "RouteController.addRoute(RouteInfo)");

		if (this.isRouteChangerInactive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread(new Runnable() {
				@Override
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

	public Coordinate c;

	public boolean moveActiveWaypointMoveOnly(Coordinate c) {
		Log.d(TAG,
				"RouteController.moveActiveWaypoint(Coordinate) METHOD START");

		if (this.isRouteChangerInactive()) {
			this.c = c;
			this.currentRoute.moveActiveWaypointMoveOnly(c);
			this.notifyAllRouteListeners();
			return true;
		}

		Log.d(TAG,
				"RouteController.moveActiveWaypoint(Coordinate) returning false");
		return false;
	}

	public boolean moveActiveWaypointComputeOnly() {
		// TODO Check c Coord.
		Log.d(TAG,
				"RouteController.moveActiveWaypointComputeOnly(Coordinate) METHOD START");

		if (c != null || this.isRouteChangerInactive()) {
			final Route newCurrentRoute = this.currentRoute;

			RouteController.routeChanger = new Thread(new Runnable() {
				@Override
				public void run() {
					Log.d(TAG, "Thread.run() in moveActiveWaypointComputeOnly()");
					newCurrentRoute.moveActiveWaypointComputeOnly(c);
					RouteController.getInstance().replaceFullRoute(
							newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}

		Log.d(TAG,
				"RouteController.moveActiveWaypointComputeOnly(Coordinate) returning false");
		return false;
	}

	
	
	public boolean deleteActiveWaypoint() {
		Log.d(TAG, "RouteController.deleteActiveWaypoint()");

		if (this.isRouteChangerInactive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread(new Runnable() {

				@Override
				public void run() {
					Log.d(TAG, "Thread.run() in deleteActiveWaypoint()");
					newCurrentRoute.deleteActiveWaypoint();
					RouteController.getInstance().replaceFullRoute(
							newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean invertRoute() {
		Log.d(TAG, "invertRoute()");
		if (this.isRouteChangerInactive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread(new Runnable() {

				@Override
				public void run() {
					Log.d(TAG, "Thread.run() in invertRoute()");
					newCurrentRoute.invertRoute();
					RouteController.getInstance().replaceFullRoute(
							newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	public boolean resetRoute() {
		Log.d(TAG, "RouteController.resetRoute()");
		if (this.isRouteChangerInactive()) {
			final Route newCurrentRoute = this.currentRoute;
			RouteController.routeChanger = new Thread(new Runnable() {

				@Override
				public void run() {
					Log.d(TAG, "Thread.run() in resetRoute()");
					newCurrentRoute.resetRoute();
					RouteController.getInstance().replaceFullRoute(
							newCurrentRoute);
				}
			});
			RouteController.routeChanger.start();
			return true;
		}
		return false;
	}

	/*
	 * public boolean optimizeRoute() { Log.d(TAG,
	 * "RouteController.optimizeRoute()");
	 *
	 * if (RouteController.routeChanger == null ||
	 * !RouteController.routeChanger.isAlive()) { final Route newCurrentRoute =
	 * this.currentRoute; RouteController.routeChanger = new Thread (new
	 * Runnable() {
	 *
	 * public void run() { Log.d(TAG, "Thread.run() in optimizeRoute()");
	 * newCurrentRoute.optimizeRoute();
	 * RouteController.getInstance().replaceFullRoute(newCurrentRoute); } });
	 * RouteController.routeChanger.start(); return true; } return false; }
	 */

	private void replaceFullRoute(RouteInfo r) {
		Log.d(TAG, "RouteController.replaceFullRoute(Route)");
		this.currentRoute = (Route) r;
		this.notifyAllRouteListeners();
	}

	public boolean containsWaypoint(Waypoint w) {
		Log.d(TAG, "RouteController.containsWaypoint(Waypoint)");
		return this.currentRoute.containsWaypoint(w);
	}

	public boolean addRouteToFavorites(String name) {
		Log.d(TAG, "RouteController.addRouteToFavorites(RouteInfo, String)");
		return FavoriteManager.getInstance().addRouteToFavorites(currentRoute,
				name);
	}

	public boolean addLocationToFavorites(Location ri, String name) {
		Log.d(TAG, "RouteController.addLocationToFavorites(Location, String)");
		return FavoriteManager.getInstance().addLocationToFavorites(ri, name);
	}

	public RouteInfo getCurrentRoute() {
		return this.currentRoute;
	}

	public void deleteActiveWaypoint(int id) {
		Waypoint w = getWaypoint(id);
		if (w != null) {
			setActiveWaypoint(id);
			deleteActiveWaypoint();
		}
	}

	public interface RouteListener {

		public void onRouteChange(RouteInfo currentRoute);

	}

	public Waypoint getWaypoint(int id) {
		if (this.currentRoute != null) {
			for (Waypoint w : currentRoute.getWaypoints()) {
				if (w.getId() == id) {
					return w;
				}
			}
		}
		return null;
	}
	
	private boolean isRouteChangerInactive() {
		return (RouteController.routeChanger == null || !RouteController.routeChanger.isAlive());
	}
	
	
	
	/**
	 * Changes the order of the waypoints on the route to the new, given order.
	 * @param newOrder
	 */
	public void changeOrderOfWaypoints(LinkedList<Waypoint> newOrder) {
		Log.d(TAG, "RouteController.changeOrderOfWaypoints(LinkedList<Waypoint> newOrder)");
		if (this.isRouteChangerInactive()) {
			if (newOrder != null && newOrder.size() > 1) {
				final Route newCurrentRoute = this.currentRoute;
				RouteController.routeChanger = new Thread(new Runnable() {
					
					@Override
					public void run() {
						Log.d(TAG, "Thread.run() in resetRoute()");
						newCurrentRoute.resetRoute();
						RouteController.getInstance().replaceFullRoute(
								newCurrentRoute);
					}
				});
				RouteController.routeChanger.start();
			}
		}
		
	}
	

	/**
	 * Shifts the given Waypoint by x positions down (left) or up (right) the order of all waypoints.
	 * @param w
	 * @param shift
	 */
	public void changeOrderOfWaypointsSHIFTbyX(int id, int shift) {
		Log.d(TAG, "RouteController.changeOrderOfWaypointsSHIFTbyX(Waypoint w, int shift)");
		Waypoint shiftedWP = this.getWaypoint(id);
		
		if (this.isRouteChangerInactive() && shiftedWP != null) {
			LinkedList<Waypoint> waypoints = this.currentRoute.getWaypoints();
			if (waypoints != null && waypoints.contains(shiftedWP)) {
				int indexOfShiftedWP = waypoints.indexOf(shiftedWP);
				if (shift < 0) {
					// Case left-shift:
					waypoints.remove(indexOfShiftedWP);
					if ((indexOfShiftedWP + shift) < 0) {
						// Case shift to start of list:
						waypoints.addFirst(shiftedWP);
					} else {
						// Case shift within list:
						waypoints.add(indexOfShiftedWP + shift, shiftedWP);
					}
				} else if (shift > 0) {
					// Case right-shift:
					if ((indexOfShiftedWP + shift + 2) > waypoints.size()) {
						// Case shift to end of list:
						waypoints.addLast(shiftedWP);
					} else {
						// Case shift within list:
						waypoints.add(indexOfShiftedWP + shift + 2, shiftedWP);
					}
					waypoints.remove(indexOfShiftedWP);
				}
				Log.d(TAG, "RouteController.changeOrderOfWaypointsSHIFTbyX(Waypoint w, int shift) calling general change order Method");
				this.changeOrderOfWaypoints(waypoints);
			}

		}
	}
	
	
	
	
	
	
	
	

	/* OLD METHOD VERSION - REPLACED BY changeOrderOfWaypointsSHIFTbyX
	/**
	 * Shifts the given Waypoint by one position down or up the order of all waypoints.
	 * @param w
	 * @param shift
	 
	public void changeOrderOfWaypointsSHIFTbyONE(int id, int shift) {
		Log.d(TAG, "RouteController.changeOrderOfWaypointsSHIFTbyONE(Waypoint w, int dir)");
		Waypoint shiftedWP = this.getWaypoint(id);
		
		if (this.isRouteChangerInactive() && shiftedWP != null) {
			LinkedList<Waypoint> waypoints = this.currentRoute.getWaypoints();
			if (waypoints != null && waypoints.contains(shiftedWP)) {
				int indexOfShiftedWP = waypoints.indexOf(shiftedWP);
				if (shift == 1 && !waypoints.getLast().equals(shiftedWP)) {
					waypoints.add(indexOfShiftedWP + 2, shiftedWP);
					waypoints.remove(indexOfShiftedWP);
				} else if (shift == -1 && !waypoints.getFirst().equals(shiftedWP)) {
					// TODO:
					
				}
				Log.d(TAG, "RouteController.changeOrderOfWaypointsSHIFTbyONE(Waypoint w, int dir) calling general change Order Method");
				this.changeOrderOfWaypoints(waypoints);
			}

		}
	}
	*/
	
	
	
	
	
	
	
	
}
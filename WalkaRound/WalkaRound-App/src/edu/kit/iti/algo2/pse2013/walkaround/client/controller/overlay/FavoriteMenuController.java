package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import java.util.List;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class FavoriteMenuController {
	private static final String TAG = FavoriteMenuController.class.getSimpleName();
	private static FavoriteMenuController me;

	private FavoriteMenuController() {
		// TODO: Implement, if necessary...
	}

	/**
	 * Singleton-method
	 *
	 * @return everytime the same instance of {@link FavoriteMenuController}
	 */
	public static FavoriteMenuController getInstance() {
		if (me == null) { // Wenn ich eine Null bin...^^
			return me = new FavoriteMenuController();
		}
		return me;
	}

	/**
	 *
	 * @return names of all favorite routes
	 */
	public List<String> getNamesOfFavoriteRoutes() {
		return FavoriteManager.getInstance().getNamesOfFavoriteRoutes();
	}

	/**
	 *
	 * @return names of all favorite locations
	 */
	public List<String> getNamesOfFavoriteLocations() {
		return FavoriteManager.getInstance().getNamesOfFavoriteLocations();
	}

	/**
	 * Removes the route with the given index from the favorites.
	 *
	 * @return boolean as specified by {@link FavoriteManager#deleteRoute(int)}
	 *         .
	 * @see FavoriteManager#deleteRoute(int)
	 */
	public boolean deleteRoute(String name) {
		return FavoriteManager.getInstance().deleteRoute(name);
	}

	/**
	 * Removes the location with the given index from the favorites.
	 *
	 * @return boolean as specified by
	 *         {@link FavoriteManager#deleteLocation(int)}.
	 * @see FavoriteManager#deleteLocation(int)
	 */
	public boolean deleteLocation(String name) {
		return FavoriteManager.getInstance().deleteLocation(name);
	}

	/**
	 * Appends the favorite route with the given index to the currently active
	 * route.
	 *
	 * @param index
	 *            the index of the route to add
	 */
	public void appendFavoriteRouteToRoute(String name) {
		Log.d(TAG, String.format("Favorisierte Route '%s' wird der Route hinzugefügt.", name));
		RouteInfo route = FavoriteManager.getInstance().getFavoriteRoute(name);
		if (route == null || route.getCoordinates().size() <= 0) {
			Log.e(TAG, "Die selektierte Route hat keine Koordinaten.");
			return;
		}
		RouteController.getInstance().addRoute(route);
		MapController.getInstance().setCenter(route.getCoordinates().getLast());
	}

	/**
	 * Appends the favorite route with the given index to the currently active
	 * route.
	 *
	 * @param index
	 *            the index of the route to add
	 */
	public void appendFavoriteLocationToRoute(String name) {
		Log.d(TAG, String.format("Favorisierte Location '%s' wird der Route hinzugefügt.", name));
		Location loc = FavoriteManager.getInstance().getFavoriteLocation(name);
		RouteController.getInstance().addWaypoint(new Waypoint(loc.getLatitude(), loc.getLongitude(), loc.getName(), loc.getAddress()));
		MapController.getInstance().setCenter(loc);
	}
}
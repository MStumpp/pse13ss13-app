package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoritesManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

public class FavoriteMenuController {
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
	 * @return the list of favorite routes as returned by
	 *         {@link FavoritesManager#getFavoriteRoutes()}
	 * @see FavoritesManager#getFavoriteRoutes()
	 */
	public List<RouteInfo> getFavoriteRoutes() {
		return FavoritesManager.getInstance().getFavoriteRoutes();
	}

	/**
	 * @return the list of favorite locations as returned by
	 *         {@link FavoritesManager#getFavoriteLocations()}
	 * @see FavoritesManager#getFavoriteLocations()
	 */
	public List<Location> getFavoriteLocations() {
		return FavoritesManager.getInstance().getFavoriteLocations();
	}

	/**
	 * 
	 * @return names of all favorite routes
	 */
	public List<String> getNamesOfFavoriteRoutes() {
		return FavoritesManager.getInstance().getNamesOfFavoriteRoutes();
	}

	/**
	 * 
	 * @return names of all favorite locations
	 */
	public List<String> getNamesOfFavoriteLocations() {
		return FavoritesManager.getInstance().getNamesOfFavoriteLocations();
	}

	/**
	 * Removes the route with the given index from the favorites.
	 * 
	 * @return boolean as specified by {@link FavoritesManager#deleteRoute(int)}
	 *         .
	 * @see FavoritesManager#deleteRoute(int)
	 */
	public boolean deleteRoute(String name) {
		return FavoritesManager.getInstance().deleteRoute(name);
	}

	/**
	 * Removes the location with the given index from the favorites.
	 * 
	 * @return boolean as specified by
	 *         {@link FavoritesManager#deleteLocation(int)}.
	 * @see FavoritesManager#deleteLocation(int)
	 */
	public boolean deleteLocation(String name) {
		return FavoritesManager.getInstance().deleteLocation(name);
	}

	/**
	 * Appends the favorite route with the given index to the currently active
	 * route.
	 * 
	 * @param index
	 *            the index of the route to add
	 */
	public void appendFavoriteRouteToRoute(String name) {
		RouteController.getInstance().addRoute(
				FavoritesManager.getInstance().getFavoriteRoute(name));
	}

	/**
	 * Appends the favorite route with the given index to the currently active
	 * route.
	 * 
	 * @param index
	 *            the index of the route to add
	 */
	public void appendFavoriteLocationToRoute(String name) {
		RouteController.getInstance().addWaypoint(
				FavoritesManager.getInstance().getFavoriteLocation(name));
	}
}
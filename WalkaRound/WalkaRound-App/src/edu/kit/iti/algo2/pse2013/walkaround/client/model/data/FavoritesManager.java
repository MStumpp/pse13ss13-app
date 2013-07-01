package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import java.util.ArrayList;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

/**
 * This class manages all saved locations and routes.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class FavoritesManager {

	/**
	 * Instance of the FavoritesManager.
	 */
	private static FavoritesManager instance;

	/**
	 * List of all saved routes.
	 */
	private ArrayList<RouteInfo> savedRoutes;

	/**
	 * List of all saved locations.
	 */
	private ArrayList<Location> savedLocations;

	/**
	 * Constructs a new manager for the favorites.
	 */
	private FavoritesManager() {
		savedRoutes = new ArrayList<RouteInfo>();
		savedLocations = new ArrayList<Location>();
	}

	/**
	 * Singleton getInstance method.
	 * 
	 * @return an instance of the FavoritesManager
	 */
	public static FavoritesManager getInstance() {
		if (instance == null) {
			instance = new FavoritesManager();
		}
		return instance;
	}

	/**
	 * Returns a list of all saved routes.
	 * 
	 * @return a list of all saved routes
	 */
	public ArrayList<RouteInfo> getFavoriteRoutes() {
		return savedRoutes;
	}

	/**
	 * Returns a list of all saved locations.
	 * 
	 * @return a list of all saved locations
	 */
	public ArrayList<Location> getFavoriteLocations() {
		return savedLocations;
	}

	/**
	 * Returns a favorite route by ID.
	 * 
	 * @param index
	 *            index of the favorite route.
	 * @return RouteInfo
	 */
	public RouteInfo getFavoriteRoute(int index) {
		return savedRoutes.get(index);
	}

	/**
	 * Returns a favorite location by ID.
	 * 
	 * @param index
	 *            index of the favorite location.
	 * @return Location
	 */
	public Location getFavoriteLocation(int index) {
		return savedLocations.get(index);
	}

	// changed boolean return to void
	/**
	 * Removes a favorite route from the list.
	 * 
	 * @param index
	 *            index of the favorite route
	 */
	public void deleteRoute(int index) {
		savedRoutes.remove(index);
	}

	// changed boolean return to void
	/**
	 * Removes a favorite location from the list.
	 * 
	 * @param index
	 *            index of the favorite location
	 */
	public void deleteLocation(int index) {
		savedLocations.remove(index);
	}

	// changed boolean return to void
	/**
	 * Adds a route to the list of favorite routes.
	 * 
	 * @param routeToSave
	 *            RouteInfo to be saved
	 * @param name
	 *            name for the favorite
	 */
	public void addRouteToFavorites(RouteInfo routeToSave, String name) {
		if (!savedRoutes.contains(routeToSave)) {
			savedRoutes.add(routeToSave);
		}
	}

	// changed boolean return to void
	/**
	 * Adds a location to the list of favorite locations.
	 * 
	 * @param locationToSave
	 *            Location to be saved
	 * @param name
	 *            name for the favorite
	 */
	public void addLocationToFavorites(Location locationToSave, String name) {
		if (!savedLocations.contains(locationToSave)) {
			savedLocations.add(locationToSave);
		}
	}

	/**
	 * Returns whether the given RouteInfo is a favorite.
	 * 
	 * @param routeInfo
	 *            RouteInfo to check
	 * @return true if it is a favorite, false otherwise
	 */
	public boolean containsRoute(RouteInfo routeInfo) {
		return savedRoutes.contains(routeInfo);
	}

	/**
	 * Returns whether the given Location is a favorite.
	 * 
	 * @param location
	 *            Location to check
	 * @return true if it is a favorite, false otherwise
	 */
	public boolean containsLocation(Location location) {
		return savedLocations.contains(location);
	}
	
	//Nach schließen der app müssen favoriten gespeichert bleiben
}

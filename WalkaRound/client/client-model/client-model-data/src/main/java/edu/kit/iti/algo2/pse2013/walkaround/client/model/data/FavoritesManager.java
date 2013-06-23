package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import java.util.ArrayList;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

public class FavoritesManager {

	private static FavoritesManager instance;

	private ArrayList<RouteInfo> savedRoutes;

	private ArrayList<Location> savedLocations;

	private FavoritesManager() {

	}

	public static FavoritesManager getInstance() {
		if (instance == null) {
			instance = new FavoritesManager();
		}
		return instance;
	}

	public ArrayList<RouteInfo> getFavoriteRoutes() {
		return null;
	}

	public ArrayList<Location> getFavoriteLocations() {
		return null;
	}

	public RouteInfo getFavoriteRoute(int id) {
		return null;
	}

	public Location getFavoriteLocation(int index) {
		return null;
	}

	// changed boolean return to void
	public void deleteRoute(int index) {

	}

	// changed boolean return to void
	public void deleteLocation(int index) {

	}

	// changed boolean return to void
	public void addRouteToFavorites(RouteInfo routeToSave, String name) {

	}

	// changed boolean return to void
	public void addLocationToFavorites(Location locationToSave, String name) {

	}

	// changed boolean return to void
	public void containsRoute(RouteInfo routeInfo) {

	}

	// changed boolean return to void
	public void containsLocation(Location location) {

	}
}

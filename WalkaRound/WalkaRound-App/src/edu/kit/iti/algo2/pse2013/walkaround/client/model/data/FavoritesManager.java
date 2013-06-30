package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import java.util.ArrayList;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

public class FavoritesManager {

	private static FavoritesManager instance;

	private ArrayList<RouteInfo> savedRoutes;

	private ArrayList<Location> savedLocations;

	private FavoritesManager() {
		savedRoutes = new ArrayList<RouteInfo>();
		savedLocations = new ArrayList<Location>();
	}

	public static FavoritesManager getInstance() {
		if (instance == null) {
			instance = new FavoritesManager();
		}
		return instance;
	}

	public ArrayList<RouteInfo> getFavoriteRoutes() {
		return savedRoutes;
	}

	public ArrayList<Location> getFavoriteLocations() {
		return savedLocations;
	}

	public RouteInfo getFavoriteRoute(int index) {
		return savedRoutes.get(index);
	}

	public Location getFavoriteLocation(int index) {
		return savedLocations.get(index);
	}

	// changed boolean return to void
	public void deleteRoute(int index) {
		savedRoutes.remove(index);
	}

	// changed boolean return to void
	public void deleteLocation(int index) {
		savedLocations.remove(index);
	}

	// changed boolean return to void
	public void addRouteToFavorites(RouteInfo routeToSave, String name) {
		if (!routeToSave.isFavorite()) {
			savedRoutes.add(routeToSave);
		}
	}

	// changed boolean return to void
	public void addLocationToFavorites(Location locationToSave, String name) {
		if (!locationToSave.isFavorite()) {
			savedLocations.add(locationToSave);
		}
	}

	public boolean containsRoute(RouteInfo routeInfo) {
		return savedRoutes.contains(routeInfo);
	}

	public boolean containsLocation(Location location) {
		return savedLocations.contains(location);
	}
}

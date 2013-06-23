package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import java.util.ArrayList;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

public class POIManager {

	private static POIManager instance;

	private LocationDataIO locationDataIO;

	private ArrayList<Integer> activeCategories;

	private POIManager(LocationDataIO locationDataIO) {
		this.locationDataIO = locationDataIO;
		activeCategories = new ArrayList<Integer>();
	}

	public static POIManager getInstance(LocationDataIO locationDataIO) {
		if (instance == null) {
			instance = new POIManager(locationDataIO);
		}
		return instance;
	}

	public ArrayList<Integer> getActiveCategories() {
		return activeCategories;
	}

	// int[] parameter gelöscht da aktive kategorien als attribut vorliegen
	public ArrayList<POI> getPOIsWithinRectangle(Coordinate upperLeft,
			Coordinate bottomRight, int levelOfDetail) {
		return null;
	}

	// int[] parameter gelöscht da aktive kategorien als attribut vorliegen
	public ArrayList<POI> getPOIsAlongRoute(RouteInfo routeInfo,
			int levelOfDetail) {
		return null;
	}

	public ArrayList<Location> searchPOIsByQuery(String query) {
		return null;
	}

	public ArrayList<Location> searchPOIsByAddress(Address address) {
		return null;
	}

	// changed boolean return to void
	public void addActivePOICategory(int id) {
		activeCategories.add(id);
	}

	// changed boolean return to void
	public void removeActivePOICategory(int id) {
		if (activeCategories.contains(id)) {
			activeCategories.remove(id);
		}
	}
}

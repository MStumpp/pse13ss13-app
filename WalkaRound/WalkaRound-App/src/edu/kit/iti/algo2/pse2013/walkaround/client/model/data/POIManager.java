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

	private int[] activeCategories;

	private POIManager(LocationDataIO locationDataIO) {
		this.locationDataIO = locationDataIO;
		activeCategories = new int[20];
	}

	public static POIManager getInstance(LocationDataIO locationDataIO) {
		if (instance == null) {
			instance = new POIManager(locationDataIO);
		}
		return instance;
	}

	public int[] getActiveCategories() {
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
		for (int i = 0; i < activeCategories.length; i++) {
			if(activeCategories[i] )
		}
	}

	// changed boolean return to void
	public void removeActivePOICategory(int id) {
		if (activeCategories.contains(id)) {
			activeCategories.remove(id);
		}
	}

	public static int computeLevenstheinDistance(String first, String second) {
		int matrix[][] = new int[first.length() + 1][second.length() + 1];
		for (int i = 0; i < first.length() + 1; i++) {
			matrix[i][0] = i;
		}
		for (int i = 0; i < second.length() + 1; i++) {
			matrix[0][i] = i;
		}
		for (int a = 1; a < first.length() + 1; a++) {
			for (int b = 1; b < second.length() + 1; b++) {
				int right = 0;
				if (first.charAt(a - 1) != second.charAt(b - 1)) {
					right = 1;
				}
				int min = matrix[a - 1][b] + 1;
				if (matrix[a][b - 1] + 1 < min) {
					min = matrix[a][b - 1] + 1;
				}
				if (matrix[a - 1][b - 1] + right < min) {
					min = matrix[a - 1][b - 1] + right;
				}
				matrix[a][b] = min;
			}
		}
		return matrix[first.length()][second.length()];
	}
}

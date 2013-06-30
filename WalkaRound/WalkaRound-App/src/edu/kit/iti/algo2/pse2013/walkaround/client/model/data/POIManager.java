package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import java.util.ArrayList;
import java.util.Iterator;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * This class manages requests about POIs.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class POIManager {

	/**
	 * Instance of the POIManager.
	 */
	private static POIManager instance;

	/**
	 * LocationDataIO where all POIs are stored.
	 */
	private LocationDataIO locationDataIO;

	/**
	 * IDs of categories that where activated in the POI menu.
	 */
	private int[] activeCategories;

	/**
	 * Constructs a new manager for POIs.
	 * 
	 * @param locationDataIO
	 *            LocationDataIO object
	 */
	private POIManager(LocationDataIO locationDataIO) {
		this.locationDataIO = locationDataIO;
		activeCategories = new int[20];
	}

	/**
	 * Singleton getInstance method.
	 * 
	 * @param locationDataIO
	 *            LocationDataIO object
	 * @return an instance of the POIManager
	 */
	public static POIManager getInstance(LocationDataIO locationDataIO) {
		if (instance == null) {
			instance = new POIManager(locationDataIO);
		}
		return instance;
	}

	/**
	 * Returns the IDs of all active categories.
	 * 
	 * @return int[] of IDs of all active categories
	 */
	public int[] getActiveCategories() {
		return activeCategories;
	}

	// int[] parameter gelöscht da aktive kategorien als attribut vorliegen
	/**
	 * Returns all POIs laying within a rectangle.
	 * 
	 * @param upperLeft
	 *            upperleft coordinate of the rectangle
	 * @param bottomRight
	 *            bottomright coordinate of the rectangle
	 * @param levelOfDetail
	 *            level of detail
	 * @return a list of all POIs laying within a rectangle
	 */
	public ArrayList<POI> getPOIsWithinRectangle(Coordinate upperLeft,
			Coordinate bottomRight, int levelOfDetail) {
		return null;
	}

	// int[] parameter gelöscht da aktive kategorien als attribut vorliegen
	/**
	 * Returns all POIs laying upon a route.
	 * 
	 * @param routeInfo
	 *            Route to search POIs in the near
	 * @param levelOfDetail
	 *            level of detail
	 * @return a list of all POIs laying upon a route
	 */
	public ArrayList<POI> getPOIsAlongRoute(RouteInfo routeInfo,
			int levelOfDetail) {
		return null;
	}

	// aus list Locations[] gemacht
	/**
	 * Returns three suggestions of locations searched by query.
	 * 
	 * @param query
	 *            query to search with
	 * @return a list of three suggestions of locations
	 */
	public Location[] searchPOIsByQuery(String query) {
		Location[] suggestions = new Location[3];
		for (Iterator<POI> iter = locationDataIO.getPOIs().iterator(); iter
				.hasNext();) {
			POI current = iter.next();
			int similarity = computeLevenstheinDistance(query,
					current.getName());

		}
		return null;
	}

	// aus list Locations[] gemacht
	/**
	 * Returns three suggestions of locations searched by an address.
	 * 
	 * @param address
	 *            address to search with
	 * @return a list of three suggestions of locations
	 */
	public Location[] searchPOIsByAddress(Address address) {
		Location[] suggestions = new Location[3];
		for (Iterator<POI> iter = locationDataIO.getPOIs().iterator(); iter
				.hasNext();) {
			POI current = iter.next();
			/*int similarity = computeLevenstheinDistance(,
					current.getName());*/

		}
		return null;
	}

	// changed boolean return to void
	/**
	 * Adds the ID of an active category.
	 * 
	 * @param id
	 *            id of the category to activate
	 */
	public void addActivePOICategory(int id) {
		if (activeCategories[id] == 0) {
			activeCategories[id] = id;
		}
	}

	// changed boolean return to void
	/**
	 * Removes the ID of an active category.
	 * 
	 * @param id
	 *            id of the category to deactivate
	 */
	public void removeActivePOICategory(int id) {
		if (activeCategories[id] == id) {
			activeCategories[id] = 0;
		}
	}

	/**
	 * Computes the difference between two strings.
	 * 
	 * @param first
	 *            first string to compare
	 * @param second
	 *            second string to compare
	 * @return an int of the difference between to strings
	 */
	private int computeLevenstheinDistance(String first, String second) {
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

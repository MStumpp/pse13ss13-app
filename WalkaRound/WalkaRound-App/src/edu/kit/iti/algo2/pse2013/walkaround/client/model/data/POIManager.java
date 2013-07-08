package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import android.content.Context;
import android.location.Geocoder;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * This class manages requests about POIs.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class POIManager {

	private final int MAX_DIFFERENCE_FOR_SEARCH = 3;

	private final int MAX_NUMBER_OF_SUGGESTIONS = 12;

	private final int NUMBER_OF_CATEGORIES = 20;
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
		activeCategories = new int[NUMBER_OF_CATEGORIES];
		for (int i = 0; i < activeCategories.length; i++) {
			activeCategories[i] = -1;
		}
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

	// int[] parameter gelÃ¶scht da aktive kategorien als attribut vorliegen
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
	public List<POI> getPOIsWithinRectangle(Coordinate upperLeft,
			Coordinate bottomRight, int levelOfDetail) {
		double minLat = bottomRight.getLatitude();
		double maxLat = upperLeft.getLatitude();
		double minLon = upperLeft.getLongitude();
		double maxLon = bottomRight.getLongitude();
		ArrayList<POI> poiList = new ArrayList<POI>();
		for (Iterator<POI> iter = locationDataIO.getPOIs().iterator(); iter
				.hasNext();) {
			POI current = iter.next();
			if ((current.getLatitude() >= minLat && current.getLatitude() <= maxLat)
					&& (current.getLongitude() >= minLon && current
							.getLongitude() <= maxLon)) {
				for (int i = 0; i < current.getPOICategories().length; i++) {
					if (activeCategories[current.getPOICategories()[i]] != -1) {
						poiList.add(current);
					}
				}
			}
		}
		return poiList;
		// wäre gut wenn POIs nach koordinaten geordnet wären oder iwie zur
		// besseren laufzeit
		// lvl of detail noch nicht eingebaut
	}

	// int[] parameter gelÃ¶scht da aktive kategorien als attribut vorliegen
	/**
	 * Returns all POIs laying upon a route.
	 * 
	 * @param routeInfo
	 *            Route to search POIs in the near
	 * @param levelOfDetail
	 *            level of detail
	 * @return a list of all POIs laying upon a route
	 */
	public List<POI> getPOIsAlongRoute(RouteInfo routeInfo,
			int levelOfDetail) {
		return null;
		// wäre gut wenn POIs nach koordinaten geordnet wären oder iwie zur
		// besseren laufzeit
	}

	// aus location poi gemacht
	/**
	 * Returns suggestions of locations searched by query.
	 * 
	 * @param query
	 *            query to search with
	 * @return a list of three suggestions of locations
	 */
	public List<POI> searchPOIsByQuery(String query) {
		TreeMap<Integer, ArrayList<POI>> suggestionsMap = new TreeMap<Integer, ArrayList<POI>>();
		ArrayList<POI> suggestions = new ArrayList<POI>();
		for (Iterator<POI> poiIter = locationDataIO.getPOIs().iterator(); poiIter
				.hasNext();) {
			POI currentPOI = poiIter.next();
			int difference = computeLevenstheinDistance(query,
					currentPOI.getName());
			if (difference <= MAX_DIFFERENCE_FOR_SEARCH) {
				if (suggestionsMap.containsKey(difference)) {
					suggestionsMap.get(difference).add(currentPOI);
				} else {
					suggestionsMap.put(difference, new ArrayList<POI>());
					suggestionsMap.get(difference).add(currentPOI);
				}
			}
		}
		for (Iterator<Integer> keyIter = suggestionsMap.keySet().iterator(); keyIter
				.hasNext();) {
			int currentKey = keyIter.next();
			suggestions.addAll(suggestionsMap.get(currentKey));
		}
		return suggestions;
	}

	// aus list<Location> list<address> gemacht , context parameter dazu
	// gemacht da auf
	// android funktion zugegriffen wird
	/**
	 * Returns suggestions of locations searched by an address.
	 * 
	 * @param address
	 *            address to search with
	 * @param context
	 *            context of the current activity
	 * @return a list of three suggestions of locations
	 */
	public List<Address> searchPOIsByAddress(Address address,
			Context context) {
		ArrayList<Address> suggestions = new ArrayList<Address>();
		Geocoder geocoder = new Geocoder(context, Locale.GERMANY);
		List<android.location.Address> addresses = new ArrayList<android.location.Address>();
		try {
			addresses = geocoder.getFromLocationName(address.toString(),
					MAX_NUMBER_OF_SUGGESTIONS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Iterator<android.location.Address> iter = addresses.iterator(); iter
				.hasNext();) {
			android.location.Address current = iter.next();
			suggestions.add(new Address(current.getThoroughfare(), current
					.getSubThoroughfare(), current.getLocality(), Integer
					.parseInt(current.getPostalCode())));
		}
		return suggestions;
	}

	// changed boolean return to void
	/**
	 * Adds the ID of an active category.
	 * 
	 * @param id
	 *            id of the category to activate
	 */
	public void addActivePOICategory(int id) {
		if (activeCategories[id] == -1) {
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
			activeCategories[id] = -1;
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

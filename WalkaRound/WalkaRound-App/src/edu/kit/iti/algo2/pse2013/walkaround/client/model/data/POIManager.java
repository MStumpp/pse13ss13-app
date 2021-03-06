package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import android.content.Context;
import android.location.Geocoder;
import android.os.Environment;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.activity.ProgressListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * This class manages requests about POIs.
 *
 * @author Thomas Kadow
 * @author ludwig Biermann
 * @version 2.0
 */
public class POIManager {

	private static final String TAG = POIManager.class.getSimpleName();
	private final int MAX_DIFFERENCE_FOR_SEARCH = 3;

	private final int MAX_NUMBER_OF_SUGGESTIONS = 10;

	private final int MAX_DIFFERENCE_OF_COORDINATES_IN_METER = 10;

	private final String fileString = File.separatorChar + "walkaround" + File.separatorChar + "poiData.pbf";
	private ProgressListener progress;

	/**
	 * Instance of the POIManager.
	 */
	private static POIManager instance;

	// private static Context applicationContext;

	/**
	 * LocationDataIO where all POIs are stored.
	 */
	private static LocationDataIO locationDataIO;

	/**
	 * IDs of categories that where activated in the POI menu.
	 */
	private boolean[] isCatActive;

	private static Context context;

	/**
	 * Constructs a new manager for POIs.
	 *
	 * @param locationDataIO
	 *            LocationDataIO object
	 */
	private POIManager(Context context) {
		isCatActive = new boolean[context.getResources().getStringArray(R.array.POICat).length];
		loadLocation();
	}

	/**
	 * Singleton getInstance method.
	 *
	 * @param context
	 *            a context for initialization
	 * @return an instance of the POIManager
	 */
	public static POIManager getInstance(Context context) {
		if (instance == null) {
			POIManager.context = context;
			instance = new POIManager(context);
		}
		return instance;
	}


	/**
	 * This class loads the Location Data from the Filesystem
	 *
	 * @author ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class LocationLoader implements Runnable {
		private final String TAG = LocationLoader.class.getSimpleName();
		private List<Integer> categories;
		public LocationLoader(List<Integer> categories) {
			this.categories = categories;
		}
		@Override
		public void run() {
			Log.d(TAG, "ExtFile: " + Environment.getExternalStorageDirectory());
			try {
				locationDataIO = LocationDataIO.load(new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ fileString), categories);
				Log.d(TAG, "location data io! " + locationDataIO.getPOIs().size());
			} catch (IOException e) {
				Log.d(TAG, e.toString());
			} catch (OutOfMemoryError e) {
				Log.d(TAG, e.toString());
				System.gc();
			}
			progress.hideProgress();
		}
	}

	/**
	 * lodas the LocationData IO
	 *
	 * @return true if sucess
	 */
	private Thread loadLocation() {
		return loadLocation(null);
	}

	/**
	 * lodas the LocationData IO
	 *
	 * @param force
	 *            true location will forced to be loaded
	 *
	 * @return true if sucess
	 */
	private Thread loadLocation(List<Integer> categories) {

		if (locationDataIO == null) {
			if (progress != null) {
				Log.d(TAG, "Show Progress");
				progress.showProgress();
			}
			Thread t = new Thread(new LocationLoader(null));
			t.start();
			return t;
		}
		return null;
	}

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
	public Set<POI> getPOIsWithinRectangle(Coordinate upperLeft,
			Coordinate bottomRight, float levelOfDetail) {

		Set<POI> poiSet = new HashSet<POI>();

		if (locationDataIO != null) {

			double minLat = bottomRight.getLatitude();
			double maxLat = upperLeft.getLatitude();
			double minLon = upperLeft.getLongitude();
			double maxLon = bottomRight.getLongitude();

			Iterator<POI> iter = locationDataIO.getPOIs().iterator();
			while (iter.hasNext()) {
				POI current = iter.next();
				if (current.getLatitude() >= minLat
						&& current.getLatitude() <= maxLat
						&& current.getLongitude() >= minLon
						&& current.getLongitude() <= maxLon) {
					for (int i = 0; i < current.getCategories().length; i++) {
						if (isCatActive[current.getCategories()[i]]) {
							poiSet.add(current);
						}
					}
				}
			}
			return poiSet;
		}

		return poiSet;
		//TODO
		// wäre gut wenn POIs nach koordinaten geordnet wären oder iwie zur
		// besseren laufzeit
		// lvl of detail noch nicht eingebaut
	}

	/**
	 * gives the number of active Categories back
	 */
	private int getNumActiveCategories() {
		int num = 0;
		for (boolean cat : isCatActive) {
			num += cat ? 1 : 0;
		}
		return num;
	}

	//TODO int[] parameter gelöscht da aktive kategorien als attribut vorliegen
	/**
	 * Returns all POIs laying upon a route.
	 *
	 * @param routeInfo
	 *            Route to search POIs in the near
	 * @param levelOfDetail
	 *            level of detail
	 * @return a list of all POIs laying upon a route
	 */
	public List<POI> getPOIsAlongRoute(RouteInfo routeInfo, float levelOfDetail) {
		ArrayList<POI> poiList = new ArrayList<POI>();

		if (locationDataIO != null) {
			LinkedList<Coordinate> coords = routeInfo.getCoordinates();

			for (Iterator<POI> iter = locationDataIO.getPOIs().iterator(); iter
					.hasNext();) {
				POI currentPOI = iter.next();
				for (Iterator<Coordinate> coordIter = coords.iterator(); coordIter
						.hasNext();) {
					Coordinate currentCoordinate = coordIter.next();
					if (CoordinateUtility.calculateDifferenceInMeters(
							currentCoordinate, currentPOI) <= MAX_DIFFERENCE_OF_COORDINATES_IN_METER) {
						poiList.add(currentPOI);
					}
				}
			}
			System.gc();
			return poiList;
		}
		return poiList;
		//TODO
		// wäre gut wenn POIs nach koordinaten geordnet wären oder iwie zur
		// besseren laufzeit
		// lvl of detail noch nicht eingebaut
	}

	//TODO aus location poi gemacht
	/**
	 * Returns suggestions of locations searched by query.
	 *
	 * @param query
	 *            query to search with
	 * @return a list of three suggestions of locations
	 */
	public List<POI> searchPOIsByQuery(String query) {

		ArrayList<POI> suggestions = new ArrayList<POI>();

		// loads Location IO
		Thread loadThread = loadLocation();
		if (loadThread != null) {
			try {
				loadThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (locationDataIO != null) {
			TreeMap<Integer, ArrayList<POI>> suggestionsMap = new TreeMap<Integer, ArrayList<POI>>();

			for (Iterator<POI> poiIter = locationDataIO.getPOIs().iterator(); poiIter.hasNext();) {
				POI currentPOI = poiIter.next();
				int difference = computeLevenstheinDistance(
						query.toLowerCase(Locale.getDefault()).trim().replaceAll(" ", ""),
						currentPOI.getName().toLowerCase(Locale.getDefault())
								.trim().replaceAll(" ", ""));
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
			int suggestionsCounter = MAX_NUMBER_OF_SUGGESTIONS;
			ArrayList<POI> suggestionsReduced = new ArrayList<POI>();
			for (Iterator<POI> suggestionsIter = suggestions.iterator(); suggestionsIter
					.hasNext();) {
				if (suggestionsCounter > 0) {
					POI currentSuggestion = suggestionsIter.next();
					suggestionsReduced.add(currentSuggestion);
					suggestionsCounter--;
				} else {

					if(this.getNumActiveCategories() <= 0)	{
						locationDataIO = null;
					}

					System.gc();
					return suggestionsReduced;
				}
			}
			// Log.d(TAG_POIMANAGER, "suggestions" + suggestions.get(0));
			if(this.getNumActiveCategories() <= 0)	{
				locationDataIO = null;
			}

			System.gc();
			return suggestionsReduced;
		}
		if(this.getNumActiveCategories() <= 0)	{
			locationDataIO = null;
		}

		System.gc();
		return suggestions;
	}

	//TODO
	// context parameter dazu
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
	public List<Location> searchPOIsByAddress(Context context, Address address) {

		ArrayList<Location> suggestions = new ArrayList<Location>();
		Geocoder geocoder = new Geocoder(context, Locale.GERMANY);
		List<android.location.Address> addresses = new ArrayList<android.location.Address>();
		try {
			addresses = geocoder.getFromLocationName(address.toString(),
					MAX_NUMBER_OF_SUGGESTIONS);
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		}
		for (Iterator<android.location.Address> iter = addresses.iterator(); iter
				.hasNext();) {
			android.location.Address current = iter.next();
			suggestions.add(new Location(current.getLatitude(), current
					.getLongitude(), current.getFeatureName(), new Address(
					current.getThoroughfare(), current.getSubThoroughfare(),
					current.getLocality(), address.getPostalCode())));
		}
		// Log.d(TAG_POIMANAGER, suggestions.get(0).toString());

		return suggestions;
	}

	/**
	 * Toggle the active-status of the category with the given id
	 *
	 * @param id
	 *            id of the category to activate
	 * @return the new status of the toggled category
	 */
	public boolean togglePOICategory(int id) {
		Log.d(TAG, "Schalte POI-Category '"
				+ context.getResources().getStringArray(R.array.POICat)[id]
				+ "' auf " + !isCatActive[id]);
		boolean zw = !isCatActive[id];
		isCatActive[id] = !isCatActive[id];

		loadLocation(getActiveCats());

		return zw;
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

	/**
	 * Return whether the active poi list is empty
	 *
	 * @return true is empty
	 */
	public boolean isEmpty() {
		boolean isEmpty = true;
		for (boolean cat : isCatActive) {
			isEmpty = isEmpty && !cat;
		}
		return isEmpty;
	}

	/**
	 * Register a Progress Listener
	 *
	 * @param listener the new Listener
	 */
	public void registerProgressListener(ProgressListener listener) {
		if (listener != null) {
			progress = listener;
		}
	}
	public List<Integer> getActiveCats() {
		List<Integer> cats = new Vector<Integer>();
		for (Integer i = 0; i < isCatActive.length; i++) {
			if (isCatActive[i]) {
				cats.add(i);
			}
		}
		return cats;
	}
}
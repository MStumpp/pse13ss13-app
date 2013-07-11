package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.util.Log;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.route.RouteInfo;

/**
 * This class manages all saved locations and routes.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class FavoritesManager implements Serializable {

	private static final String TAG_FAVORITE_MANAGER = FavoritesManager.class
			.getSimpleName();

	/**
	 * Serialization ID.
	 */
	private static final long serialVersionUID = 3773292311411426803L;

	/**
	 * Instance of the FavoritesManager.
	 */
	private static FavoritesManager instance;

	/**
	 * List of all saved routes.
	 */
	private HashMap<String, RouteInfo> savedRoutes;

	/**
	 * List of all saved locations.
	 */
	private HashMap<String, Location> savedLocations;

	private static Context applicationContext;

	private final static String FILENAME = "Favorites_Save";

	/**
	 * Constructs a new manager for the favorites.
	 */
	private FavoritesManager() {
		savedRoutes = new HashMap<String, RouteInfo>();
		savedLocations = new HashMap<String, Location>();
	}

	public static void initialize(Context context) {
		applicationContext = context.getApplicationContext();
	}

	/**
	 * Singleton getInstance method.
	 * 
	 * @return an instance of the FavoritesManager
	 */
	public static FavoritesManager getInstance() {
		if (instance == null) {
			try {
				FileInputStream fis = applicationContext
						.openFileInput(FILENAME);
				ObjectInputStream oos = new ObjectInputStream(fis);
				instance = (FavoritesManager) oos.readObject();
				return instance;
			} catch (FileNotFoundException e) {
				Log.d(TAG_FAVORITE_MANAGER, e.toString());
			} catch (IOException ioe) {
				Log.d(TAG_FAVORITE_MANAGER, ioe.toString());
			} catch (ClassNotFoundException cnfe) {
				Log.d(TAG_FAVORITE_MANAGER, cnfe.toString());
			}
			instance = new FavoritesManager();
		}
		return instance;
	}

	/**
	 * Returns a list of all saved routes.
	 * 
	 * @return a list of all saved routes
	 */
	public List<RouteInfo> getFavoriteRoutes() {
		ArrayList<RouteInfo> list = new ArrayList<RouteInfo>();
		list.addAll(savedRoutes.values());
		return list;
	}

	/**
	 * Returns a list of all saved locations.
	 * 
	 * @return a list of all saved locations
	 */
	public List<Location> getFavoriteLocations() {
		ArrayList<Location> list = new ArrayList<Location>();
		list.addAll(savedLocations.values());
		return list;
	}

	/**
	 * Returns a list of all names of saved routes.
	 * 
	 * @return a list of all names of saved routes
	 */
	public List<String> getNamesOfFavoriteRoutes() {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(savedRoutes.keySet());
		return list;
	}

	/**
	 * Returns a list of all names of saved routes.
	 * 
	 * @return a list of all names of saved routes
	 */
	public List<String> getNamesOfFavoriteLocations() {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(savedLocations.keySet());
		return list;
	}

	/**
	 * Returns a favorite route by ID.
	 * 
	 * @param index
	 *            index of the favorite route.
	 * @return RouteInfo
	 */
	public RouteInfo getFavoriteRoute(int index) {
		return savedRoutes.get(index).clone();
	}

	/**
	 * Returns a favorite location by ID.
	 * 
	 * @param index
	 *            index of the favorite location.
	 * @return Location
	 */
	public Location getFavoriteLocation(int index) {
		return savedLocations.get(index).clone();
	}

	/**
	 * Removes a favorite route from the list.
	 * 
	 * @param index
	 *            index of the favorite route
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public boolean deleteRoute(int index) {
		try {
			savedRoutes.remove(index);
			try {
				save(this);
			} catch (FileNotFoundException fnfe) {
				Log.d(TAG_FAVORITE_MANAGER, fnfe.toString());
			} catch (IOException e) {
				Log.d(TAG_FAVORITE_MANAGER, e.toString());
			}
			return true;
		} catch (IndexOutOfBoundsException e) {
			Log.d(TAG_FAVORITE_MANAGER, e.toString());
			return false;
		}
	}

	/**
	 * Removes a favorite location from the list.
	 * 
	 * @param index
	 *            index of the favorite location
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public boolean deleteLocation(int index) {
		try {
			savedLocations.remove(index);
			try {
				save(this);
			} catch (FileNotFoundException fnfe) {
				Log.d(TAG_FAVORITE_MANAGER, fnfe.toString());
			} catch (IOException e) {
				Log.d(TAG_FAVORITE_MANAGER, e.toString());
			}
			return true;
		} catch (IndexOutOfBoundsException e) {
			Log.d(TAG_FAVORITE_MANAGER, e.toString());
			return false;
		}
	}

	/**
	 * Adds a route to the list of favorite routes.
	 * 
	 * @param routeToSave
	 *            RouteInfo to be saved
	 * @param name
	 *            name for the favorite
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public boolean addRouteToFavorites(RouteInfo routeToSave, String name) {
		if (!savedRoutes.containsKey(name)) {
			savedRoutes.put(name, routeToSave);
			try {
				save(this);
			} catch (FileNotFoundException fnfe) {
				Log.d(TAG_FAVORITE_MANAGER, fnfe.toString());
			} catch (IOException e) {
				Log.d(TAG_FAVORITE_MANAGER, e.toString());
			}
			return true;
		}
		return false;
	}

	/**
	 * Adds a location to the list of favorite locations.
	 * 
	 * @param locationToSave
	 *            Location to be saved
	 * @param name
	 *            name for the favorite
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public boolean addLocationToFavorites(Location locationToSave, String name) {
		if (!savedLocations.containsKey(name)) {
			savedLocations.put(name, locationToSave);
			try {
				save(this);
			} catch (FileNotFoundException fnfe) {
				Log.d(TAG_FAVORITE_MANAGER, fnfe.toString());
			} catch (IOException e) {
				Log.d(TAG_FAVORITE_MANAGER, e.toString());
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns whether the given RouteInfo is a favorite.
	 * 
	 * @param routeInfo
	 *            RouteInfo to check
	 * @return true if it is a favorite, false otherwise
	 */
	public boolean containsRoute(RouteInfo routeInfo) {
		return savedRoutes.containsValue(routeInfo);
	}

	/**
	 * Returns whether the given Location is a favorite.
	 * 
	 * @param location
	 *            Location to check
	 * @return true if it is a favorite, false otherwise
	 */
	public boolean containsLocation(Location location) {
		return savedLocations.containsValue(location);
	}

	/**
	 * Saves the FavoritesManager object to an external file.
	 * 
	 * @param objectToSave
	 *            FavoritesManager object to save.
	 * @param destination
	 *            Location of output file on file system.
	 * @throws java.io.IOException
	 */
	private void save(FavoritesManager objectToSave)
			throws FileNotFoundException, IOException {
		FileOutputStream fos = applicationContext.openFileOutput(FILENAME,
				Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this);
		oos.flush();
		oos.close();
	}
}

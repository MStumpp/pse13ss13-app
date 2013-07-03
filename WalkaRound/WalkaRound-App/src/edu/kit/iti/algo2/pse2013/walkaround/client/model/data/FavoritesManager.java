package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

/**
 * This class manages all saved locations and routes.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class FavoritesManager implements Serializable {

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

	/**
	 * Location where the favorites manager is saved.
	 */
	private final File SAVE_LOCATION = new File("");

	/**
	 * Constructs a new manager for the favorites.
	 */
	private FavoritesManager() {
		savedRoutes = new HashMap<String, RouteInfo>();
		savedLocations = new HashMap<String, Location>();
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
		ArrayList<RouteInfo> list = new ArrayList<RouteInfo>();
		list.addAll(savedRoutes.values());
		return list;
	}

	/**
	 * Returns a list of all saved locations.
	 * 
	 * @return a list of all saved locations
	 */
	public ArrayList<Location> getFavoriteLocations() {
		ArrayList<Location> list = new ArrayList<Location>();
		list.addAll(savedLocations.values());
		return list;
	}

	/**
	 * Returns a list of all names of saved routes.
	 * 
	 * @return a list of all names of saved routes
	 */
	public ArrayList<String> getNamesOfFavoriteRoutes() {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(savedRoutes.keySet());
		return list;
	}

	/**
	 * Returns a list of all names of saved routes.
	 * 
	 * @return a list of all names of saved routes
	 */
	public ArrayList<String> getNamesOfFavoriteLocations() {
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
				save(this, SAVE_LOCATION);
			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} catch (IndexOutOfBoundsException e) {
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
				save(this, SAVE_LOCATION);
			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} catch (IndexOutOfBoundsException e) {
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
				save(this, SAVE_LOCATION);
			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
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
				save(this, SAVE_LOCATION);
			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
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
	private void save(FavoritesManager objectToSave, File destination)
			throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(destination)));
		oos.writeObject(objectToSave);
		oos.flush();
		oos.close();
	}
}

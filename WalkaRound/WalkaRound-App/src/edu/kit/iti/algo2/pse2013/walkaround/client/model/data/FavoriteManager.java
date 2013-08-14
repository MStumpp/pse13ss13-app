package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import android.content.Context;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.ProtobufConverter;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveFavLocation;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveFavorite;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveRoute;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveRoutepoint;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveWaypoint;

/**
 * This class manages all saved locations and routes.
 *
 * @author Thomas Kadow, Florian Sch&auml;fer
 * @version 1.0
 */
public class FavoriteManager {

	private static final String TAG = FavoriteManager.class.getSimpleName();

	/**
	 * Instance of the FavoritesManager.
	 */
	private static FavoriteManager instance;

	/**
	 * List of all saved routes.
	 */
	private TreeMap<String, RouteInfo> savedRoutes;

	/**
	 * List of all saved locations.
	 */
	private TreeMap<String, Location> savedLocations;

	private static Context applicationContext;

	private final static String FILENAME = "Favorites_Save";

	/**
	 * Constructs a new manager for the favorites.
	 */
	private FavoriteManager() {
		savedRoutes = new TreeMap<String, RouteInfo>();
		savedLocations = new TreeMap<String, Location>();
	}

	public static void initialize(Context context) {
		applicationContext = context.getApplicationContext();
	}

	/**
	 * Singleton getInstance method.
	 *
	 * @return an instance of the FavoritesManager
	 */
	public static FavoriteManager getInstance() {
		if (instance == null) {
			try {
				loadInstanceFromFile();
			} catch (FileNotFoundException e) {
				instance = new FavoriteManager();
			} catch (InvalidProtocolBufferException e) {
				Log.e(TAG, "The favorite-file was either obsolete or corrupted, I'll delete it now.");
				applicationContext.deleteFile(FILENAME);
				instance = new FavoriteManager();
			} catch (IOException e) {
				Log.e(TAG, "Error loading favorites", e);
			}
		}
		assert instance != null;
		return instance;
	}

	private static void loadInstanceFromFile() throws IOException {
		BufferedInputStream bis = new BufferedInputStream(applicationContext.openFileInput(FILENAME));
		SaveFavorite saveFav = Protos.SaveFavorite.parseFrom(bis);
		bis.close();
		instance = new FavoriteManager();
		for (SaveRoute sr : saveFav.getRouteList()) {
			LinkedList<Coordinate> coordinates = new LinkedList<Coordinate>();
			for (SaveRoutepoint saCo : sr.getRoutepointList()) {
				if (saCo.hasCoord()) {
					coordinates.add(ProtobufConverter.getCoordinate(saCo.getCoord()));
				} else if (saCo.hasWP()) {
					coordinates.add(ProtobufConverter.getWaypoint(saCo.getWP()));
				}
			}
			RouteInfo r = new Route(coordinates);
			instance.savedRoutes.put(sr.getName(), r);
		}
		for (SaveFavLocation saveFavLoc : saveFav.getLocationList()) {
			instance.addLocationToFavorites(ProtobufConverter.getLocation(saveFavLoc.getLocation()), saveFavLoc.getName());
		}
		Log.d(TAG,
			String.format(
				"The favorites (%d routes and %d locations) were loaded successfully",
				saveFav.getRouteCount(), saveFav.getLocationCount()
			)
		);
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
	public RouteInfo getFavoriteRoute(String name) {
		if(savedRoutes.get(name).clone() == null) {
			Log.d(TAG, "Klon ist null");
		}
		return savedRoutes.get(name).clone();
	}

	/**
	 * Returns a favorite location by ID.
	 *
	 * @param index
	 *            index of the favorite location.
	 * @return Location
	 */
	public Location getFavoriteLocation(String name) {
		return savedLocations.get(name).clone();
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
	public boolean deleteRoute(String name) {
		RouteInfo result = savedRoutes.remove(name);
		this.save();
		return result != null;
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
	public boolean deleteLocation(String name) {
		Location result = savedLocations.remove(name);
		this.save();
		return result != null;
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
			this.save();
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
			this.save();
			return true;
		}
		return false;
	}

	/**
	 * Returns whether the given name already exists.
	 *
	 * @param name name to check
	 * @return true if it exists, false otherwise
	 */
	public boolean containsName(String name) {
		if (savedRoutes.keySet().contains(name) || savedLocations.keySet().contains(name)) {
			return true;
		}
		return false;
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
	private void save() {
		SaveFavorite.Builder favBuilder = Protos.SaveFavorite.newBuilder();
		Iterator<String> keys = savedRoutes.keySet().iterator();
		while (keys.hasNext()) {
			String nextKey = keys.next();
			RouteInfo nextRoute = savedRoutes.get(nextKey);
			SaveRoute.Builder routeBuilder = Protos.SaveRoute.newBuilder().setName(nextKey);
			for (Coordinate c : nextRoute.getCoordinates()) {
				if (c instanceof Waypoint) {
					SaveWaypoint.Builder wp = SaveWaypoint.newBuilder()
						.setParentLocation(ProtobufConverter.getLocationBuilder((Location) c))
						.setProfile(((Waypoint) c).getProfile())
						.setFavorite(((Waypoint) c).isFavorite());
					if (((Waypoint) c).getPOI() != null) {
						wp.setPOI(ProtobufConverter.getPOIBuilder(((Waypoint) c).getPOI()));
					}
					routeBuilder.addRoutepoint(SaveRoutepoint.newBuilder().setWP(wp));
				} else {
					routeBuilder.addRoutepoint(SaveRoutepoint.newBuilder().setCoord(ProtobufConverter.getCoordinateBuilder(c)));
				}
			}
			favBuilder.addRoute(routeBuilder.build());
		}
		for (String name : savedLocations.keySet()) {
			favBuilder.addLocation(SaveFavLocation.newBuilder().setLocation(ProtobufConverter.getLocationBuilder(savedLocations.get(name))).setName(name));
		}
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(applicationContext.openFileOutput(FILENAME, Context.MODE_PRIVATE));
			favBuilder.build().writeTo(bos);
			bos.flush();
			bos.close();
			Log.d(TAG, "The favorites were loaded successfully");
		} catch (FileNotFoundException e) {
			Log.e(TAG, "The file " + FILENAME + " was not found!", e);
		} catch (IOException e) {
			Log.e(TAG, "The file " + FILENAME + " could not be read successfully!", e);
		}
	}
}

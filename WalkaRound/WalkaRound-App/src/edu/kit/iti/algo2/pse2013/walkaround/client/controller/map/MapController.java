package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

//Java library
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayPOI;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator.MapGen;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator.POIGen;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator.RouteGen;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.CompassListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.CurrentMapStyleModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateNormalizer;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateNormalizerException;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.PullUpView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
//Android library
//Walkaround library

/**
 * This Class controls the data flow between the System and the real View.
 *
 * @author Ludwig Biermann
 */
public class MapController implements RouteListener, PositionListener, CompassListener {

	/**
	 * default starting Coordinate if GPS is offline
	 */
	public static Coordinate defaultCoordinate = new Coordinate(49.0145, 8.419); // 211
	public static String TAG_MAP_CONTROLLER = MapController.class
			.getSimpleName();
	public static boolean defaultUserLock = true;

	/**
	 * The Controller
	 */
	private static MapController mapController;
	private RouteController routeController;

	/**
	 * Map Components
	 */
	private MapView mapView;

	/**
	 * permanent class variables
	 */
	private boolean lockUserPosition = defaultUserLock;

	/**
	 * DisplayWaypoints to display the Display Points
	 */
	private List<DisplayWaypoint> displayPoints;

	/**
	 * DisplayCoordinats to draw he Lines
	 */
	private List<DisplayCoordinate> lines;
	private RouteInfo currentRoute;
	private int currentActiveWaypoint;

	private Point size;

	private MapGen map;
	private Bitmap route;
	private POIGen poiGen;

	// Mutli fixes

	private BoundingBox coorBox;
	//private Coordinate center;

	/**
	 * gets the center Coordinate
	 *
	 * @return the Coordinate of the center
	 */
	public Coordinate getCenter() {
		return coorBox.getCenter();
	}

	/**
	 * sets the Center Coordinate
	 *
	 * @param center Coordinate
	 */
	public void setCenter(Coordinate center) {
		this.coorBox.setCenter(center);
	}

	/**
	 * sets the Center Coordinate
	 *
	 * @param center Coordinate
	 * @param lod Level Of Detail
	 */
	public void setCenter(Coordinate center, float lod) {
		this.coorBox.setCenter(center, lod);
	}

	/*
	 * -----------------Initialization-----------------
	 */

	/**
	 * Initializes the MapController. Needs the current mapView
	 *
	 * @param mapView
	 *            the mapView
	 * @return the mapController
	 */
	public static MapController initialize(TileFetcher tileFetcher, BoundingBox coorBox, Coordinate user) {
		if (mapController == null) {
			mapController = new MapController(tileFetcher, coorBox, user);
		}
		return mapController;
	}
	public static boolean isInitialized() {
		return mapController != null;
	}

	/**
	 * Gives back the unique Instance of the Map Controller
	 *
	 * @return the MapController
	 */
	public static MapController getInstance() {
		if (mapController == null) {
			Log.e(TAG_MAP_CONTROLLER, "getInstance(): you need to initialize MapController prior to getting an instance");
			return null;
		}
		return mapController;
	}

	/*
	 * -----------------Constructor-----------------
	 */

	public void startController(MapView mv) {
		this.mapView = mv;

		PositionManager.getInstance().registerPositionListener(this);
		PositionManager.getInstance().getCompassManager().registerCompassListener(this);

		this.routeController.registerRouteListener(this);

		PreferenceManager.getDefaultSharedPreferences(mv).registerOnSharedPreferenceChangeListener(map);

		this.map.generateMap(coorBox);
		this.updateUserPosition();
	}

	private MapController(TileFetcher tileFetcher, BoundingBox coorBox, Coordinate user) {
		this.user = user;

		// initialize Vars
		this.currentRoute = new Route(new LinkedList<Coordinate>());
		this.displayPoints = new LinkedList<DisplayWaypoint>();
		this.lines = new LinkedList<DisplayCoordinate>();

		this.routeGen = new Thread();
		this.userPos = new Thread();

		this.size = coorBox.getDisplaySize();
		this.coorBox = coorBox;

		this.route = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);

		// initialize Threads

		this.map = new MapGen(size, coorBox, tileFetcher);

		this.poiGen = new POIGen();
		//TODO poi Gen doesnt run as Thread ... why?
		Thread t = new Thread(poiGen);
		t.setName("POI Generator");
		t.setPriority(5);
		//t.run();

		// Controller

		this.routeController = RouteController.getInstance();
	}

	/*
	 * -----------------Getter Methods-----------------
	 */

	/**
	 * Gives the id of the current Active Waypoint back
	 *
	 * @return id of active Waypoint
	 */
	public int getActiveWaypointId() {
		return currentActiveWaypoint;
	}

	/**
	 * Gives the current Pull Up View back.
	 */
	public PullUpView getPullUpView() {
		return mapView.getPullUpView();
	}

	/**
	 * Gives back the current Level of Detail.
	 *
	 * @return current Level ofDetail.
	 */
	public float getCurrentLevelOfDetail() {
		return this.coorBox.getLevelOfDetail();
	}

	/**
	 * Gives the current Route back.
	 *
	 * @return current Route
	 */
	public List<DisplayCoordinate> getCurrentRouteLines() {
		return this.lines;
	}

	/*
	 * -----------------Forwarding To MapView-----------------
	 */

	/**
	 * Forward the Bitmap of the current Map
	 *
	 * @param b
	 *            the Bitmap of the current Map
	 */
	public void onMapOverlayImageChange(Bitmap b) {
		Log.d(TAG_MAP_CONTROLLER, "Bitmap Map Forwarding.");
		this.mapView.updateMapImage(b);
	}

	/**
	 * Forward the Bitmap of the Route
	 *
	 * @param b
	 *            the Bitmap of the Route
	 */
	public void onRouteOverlayImageChange(Bitmap b) {
		Log.d(TAG_MAP_CONTROLLER, "Bitmap Route Forwarding.");
		this.mapView.updateRouteOverlayImage(b);
	}

	/**
	 * Forward poi list to MapView
	 *
	 * @param poiList
	 *            the required list of pois
	 */
	public void onPOIChange(List<DisplayPOI> poiList) {
		mapView.updateDisplayCoordinate(poiList);
	}

	/*
	 * -----------------Forwarding To MapModel-----------------
	 */

	/**
	 * Forward a shift action to the Map Model. This contains: shifting the map
	 * shifting the Route drawing shifting the Display Waypoints
	 *
	 * @param distanceX
	 *            the x delta distance
	 * @param distanceY
	 *            the y delta distance
	 */
	public void onShift(float distanceX, float distanceY) {
		Log.d(TAG_MAP_CONTROLLER, "onShift(x: " + distanceX + ", y: " + distanceY + ")");

		if (this.lockUserPosition) {
			this.toggleLockUserPosition();
		}

		coorBox.shiftCenter(distanceX, distanceY);

		this.updateAll();
	}

	/**
	 * Zoom by a delta to a DisplayCoordinate
	 *
	 * @param delta
	 *            to the new ZoomLevel
	 * @param dc
	 *            the DisplayCoordinate
	 */
	public void onZoom(float delta, DisplayCoordinate dc) {
		/*Log.d(TAG_MAP_CONTROLLER, "The given Zoom Delta: " + delta + " to "
				+ dc.toString() + " will be forwarding to MapModel");
		if (this.coorBox.getLevelOfDetail() + delta <= CurrentMapStyleModel.getInstance()
				.getCurrentMapStyle().getMaxLevelOfDetail()
				&& this.coorBox.getLevelOfDetail() + delta >= CurrentMapStyleModel.getInstance()
						.getCurrentMapStyle().getMinLevelOfDetail()) {

			// set center before zooming!
			this.coorBox.setCenter(dc);
			this.coorBox.setLevelOfDetail(delta);

			this.updateAll();
		}*/
	}

	/**
	 * Zoom by a delta.
	 *
	 * @param delta
	 *            to the new ZoomLevel
	 */
	public void onZoom(final float delta) {
		Log.d(TAG_MAP_CONTROLLER, "The given Zoom Delta: " + delta
				+ " will be forwarding to MapModel");

		if (coorBox.getLevelOfDetail() + delta <= CurrentMapStyleModel.getInstance()
				.getCurrentMapStyle().getMaxLevelOfDetail()
				&& coorBox.getLevelOfDetail() + delta >= CurrentMapStyleModel.getInstance()
						.getCurrentMapStyle().getMinLevelOfDetail()) {

			coorBox.setLevelOfDetailByADelta(delta);
			updateAll();
		}

	}

	/*
	 * -----------------Calls to Map Controller-----------------
	 */

	/**
	 * Switch UserLock between true and false.
	 */
	public void toggleLockUserPosition() {
		if (this.lockUserPosition) {
			this.lockUserPosition = false;
		} else {
			this.lockUserPosition = true;
			this.setCenter(user);
			this.updateAll();
		}
		HeadUpController.getInstance().setUserPoisitionLock(
				this.lockUserPosition);
		Log.d(TAG_MAP_CONTROLLER, "Lock User Position: "
				+ this.lockUserPosition);
	}

	/**
	 *
	 */
	public void addPoiToView() {
		this.poiGen.generatePOIView(coorBox, size);
	}

	/*
	 * -----------------Forwarding To Route Controller-----------------
	 */

	/**
	 * Delete the Active Waypoint
	 *
	 * @param currentId
	 */
	public void onDeletePoint(int currentId) {
		Log.d(TAG_MAP_CONTROLLER, "Delete active Waypoint");
		if (this.currentActiveWaypoint == currentId) {
			this.lines.clear();
			this.displayPoints.clear();
			this.routeController.deleteActiveWaypoint();
			this.updateAll();

		}
	}

	/**
	 * Creates a new Point.
	 *
	 * @param dc
	 *            the DisplayCoordinats of the new Point
	 */
	public void onCreatePoint(DisplayCoordinate dc) {

		Log.d(TAG_MAP_CONTROLLER, "onCreatePoint(" + dc + ")");

		Coordinate next = CoordinateUtility
				.convertDisplayCoordinateToCoordinate(dc,
						coorBox.getTopLeft(),
						coorBox.getLevelOfDetail());

		try {
			CoordinateNormalizer.normalizeCoordinate(next,
					(int) this.getCurrentLevelOfDetail());
		} catch (IllegalArgumentException e) {
			Log.e(TAG_MAP_CONTROLLER,
					"Coordinate konnte nicht normalisiert werden!");
		} catch (CoordinateNormalizerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.routeController.addWaypoint(new Waypoint(next.getLatitude(), next.getLongitude(), "PLACEHOLDER"));
	}

	/**
	 *
	 * @param x
	 * @param y
	 */
	public void onMovePoint(float x, float y, int id) {

		double xDelta = CoordinateUtility.convertPixelsToDegrees(x,
				getCurrentLevelOfDetail(), CoordinateUtility.DIRECTION_X);
		double yDelta = CoordinateUtility.convertPixelsToDegrees(y,
				getCurrentLevelOfDetail(), CoordinateUtility.DIRECTION_Y);

		currentWaypoint = this.getWaypointById(id);
		currentWaypoint.setLatitude(currentWaypoint.getLatitude() - yDelta);
		currentWaypoint.setLongitude(currentWaypoint.getLongitude() + xDelta);

	}

	private Coordinate currentWaypoint;

	public void pushMovedWaypoint() {
		this.routeController.moveActiveWaypoint(currentWaypoint);
	}

	/**
	 * returns a Waypoint by his id
	 *
	 * @param id
	 *            of the Waypoint
	 * @return null if no Waypoint is available
	 */
	private Coordinate getWaypointById(int id) {

		for (Waypoint value : currentRoute.getWaypoints()) {
			if (value.getId() == id) {
				return value;
			}
		}
		return null;
	}

	/*
	 * -----------------Implemented Listener-----------------
	 */

	private Thread routeGen;

	/**
	 * Helper Method that updateRouteOverlay
	 */
	private void updateRouteOverlay() {

		this.lines = CoordinateUtility.extractDisplayCoordinatesOutOfRouteInfo(
				currentRoute, coorBox.getCenter(), size, this.coorBox.getLevelOfDetail());

		this.displayPoints = CoordinateUtility
				.extractDisplayWaypointsOutOfRouteInfo(currentRoute,
						coorBox.getCenter(), size, this.coorBox.getLevelOfDetail());

		mapView.updateDisplayWaypoints(displayPoints);

		if (this.currentRoute.getActiveWaypoint() == null) {
			return;
		}

		mapView.setActiveWaypoint(currentRoute.getActiveWaypoint().getId());

		if (routeGen.isAlive()) {
			routeGen.interrupt();
		}
		Thread routeGen = new Thread(new RouteGen(lines, route));
		routeGen.start();

	}

	public void onRouteChange(RouteInfo currentRoute) {
		Log.d(TAG_MAP_CONTROLLER, "Route Change! "
				+ currentRoute.getWaypoints().size());
		if (currentRoute != null) {
			this.currentRoute = currentRoute;
			this.currentActiveWaypoint = currentRoute.getActiveWaypoint()
					.getId();
		}
		updateRouteOverlay();
	}

	private Coordinate user;

	public void onPositionChange(Location androidLocation) {
		Log.d(TAG_MAP_CONTROLLER, "Position Change!");

		if (lockUserPosition) {
			Coordinate center = new Coordinate(androidLocation.getLatitude(),
					androidLocation.getLongitude());
			this.setCenter(center);
		}

		user = new Coordinate(androidLocation.getLatitude(),
				androidLocation.getLongitude());

		this.updateAll();
	}

	/**
	 *
	 */
	public void updateAll(){
		this.updateUserPosition();
		this.updateRouteOverlay();
		this.updatePOIView();
		this.map.generateMap(coorBox);
	}

	/**
	 *
	 */
	public void updatePOIView(){
		this.poiGen.generatePOIView(coorBox, size);
	}

	Thread userPos;

	/**
	 *
	 */
	public void updateUserPosition() {
		if(userPos.isAlive()){
			userPos.interrupt();
		}
		userPos = new Thread(new UserPos());
		userPos.start();
	}

	/**
	 * This class sets the USer Arrwow
	 *
	 * @author Ludwig Biermann
	 *
	 */
	private class UserPos implements Runnable {

		/**
		 *
		 */
		public UserPos() {
		}

		public void run() {

			double lon = -coorBox.getTopLeft().getLongitude() + user.getLongitude();
			double lat = -user.getLatitude() + coorBox.getTopLeft().getLatitude();

			DisplayCoordinate pos = new DisplayCoordinate(
					CoordinateUtility.convertDegreesToPixels(lon,
							coorBox.getLevelOfDetail(),
							CoordinateUtility.DIRECTION_LONGITUDE),
					CoordinateUtility.convertDegreesToPixels(lat,
							coorBox.getLevelOfDetail(),
							CoordinateUtility.DIRECTION_LATITUDE));

			Log.d(TAG_MAP_CONTROLLER + UserPos.class.getSimpleName(), "User Posi ist:" + user.toString());
			try {
				mapView.setUserPositionOverlayImage(pos.getX(), pos.getY());
			} catch(NullPointerException e) {
				Log.d(TAG_MAP_CONTROLLER, "mapView wurde noch nicht initialisiert");
			}
		}

	}

	/**
	 * forwards a set active action to route controller
	 *
	 * @param id
	 *            of the waypoint
	 *
	 */
	public void setActive(int id) {
		this.routeController.setActiveWaypoint(id);
	}

	public void onCompassChange(float direction) {
		this.mapView.setUserPositionOverlayImage(direction);
	}

	/**
	 *
	 * @return
	 */
	public POI getPOIById(int id) {
		return poiGen.getPOIInformationById(id);
	}

	public POI getPOI() {
		return mapView.getCurrentPOI();
	}


}
package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

//Java library
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayPOI;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.MapModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.CompassListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateNormalizer;
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
public class MapController implements RouteListener, PositionListener,
		CompassListener {

	// public static Coordinate defaultCoordinate = new Coordinate(49.00471,
	// 8.3858300); // Brauerstraße
	// public static Coordinate defaultCoordinate = new Coordinate(49.01,
	// 8.40333); // Marktplatz

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
	private MapModel mapModel;

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
	public static MapController initialize(MapView mapView) {
		if (mapController == null) {
			mapController = new MapController(mapView);
		}
		return mapController;
	}

	/**
	 * Gives back the unique Instance of the Map Controller
	 * 
	 * @return the MapController
	 */
	public static MapController getInstance() {
		if (mapController == null) {
			Log.d(TAG_MAP_CONTROLLER, "you need to initialice MapView first");
			return null;
		}
		return mapController;
	}

	/*
	 * -----------------Constructor-----------------
	 */

	/**
	 * private Constructor of the Map Controller
	 * 
	 * @param mv
	 *            the required MapView
	 */
	private MapController(MapView mv) {

		Log.d(TAG_MAP_CONTROLLER, "Map Controller will be initialice!");

		this.user = defaultCoordinate;
		this.currentRoute = new Route(new LinkedList<Coordinate>());

		this.mapView = mv;
		size = mv.getDisplaySize();

		Log.d(TAG_MAP_CONTROLLER, "Initialice List of Display Points!");
		displayPoints = new LinkedList<DisplayWaypoint>();
		lines = new LinkedList<DisplayCoordinate>();

		Log.d(TAG_MAP_CONTROLLER, "Initialice MapModel!");
		this.mapModel = MapModel.initialize(defaultCoordinate, this, size);

		Log.d(TAG_MAP_CONTROLLER, "Initialice and register routeController!");
		routeController = RouteController.getInstance();
		routeController.registerRouteListener(this);

		PositionManager.getInstance().registerPositionListener(this);
		PositionManager.getInstance().getCompassManager()
				.registerCompassListener(this);

		Log.d(TAG_MAP_CONTROLLER,
				"Add three Example Waypoints to routeController!");
		// routeController.addWaypoint(new Waypoint(49.01, 8.40333,
		// "Marktplatz"));
		// routeController.addWaypoint(new Waypoint(49.00471, 8.3858300,
		// "Brauerstraße"));
		// routeController.addWaypoint(new Waypoint(49.0145, 8.419, "211"));

		// CompassManager.getInstance().registerCompassListener(this);

		// mapView.setUserPositionOverlayImage(200,100);
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
		return this.mapModel.getCurrentLevelOfDetail();
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
		Log.d(TAG_MAP_CONTROLLER, "Shift Map by: " + distanceX + " : "
				+ distanceY);
		Log.d(TAG_MAP_CONTROLLER, "Shift Map by: " + this.lockUserPosition);

		if (this.lockUserPosition) {
			Log.d(TAG_MAP_CONTROLLER, "Shift Map by: " + this.lockUserPosition);
			HeadUpController.getInstance().toggleUserPositionLock();
			Log.d(TAG_MAP_CONTROLLER, "Shift Map by: " + this.lockUserPosition);
		}
		this.mapModel.shift(new DisplayCoordinate(distanceX, distanceY));
		this.updateRouteOverlay();
		this.updateUserPosition();

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
		Log.d(TAG_MAP_CONTROLLER, "The given Zoom Delta: " + delta + " to "
				+ dc.toString() + " will be forwarding to MapModel");
		this.mapModel.zoom(delta, dc);
		this.updateRouteOverlay();
	}

	/**
	 * Zoom by a delta.
	 * 
	 * @param delta
	 *            to the new ZoomLevel
	 */
	public void onZoom(float delta) {
		Log.d(TAG_MAP_CONTROLLER, "The given Zoom Delta: " + delta
				+ " will be forwarding to MapModel");
		this.mapModel.zoom(delta);
		this.updateRouteOverlay();
	}

	/*
	 * -----------------Calls to Map Controller-----------------
	 */

	/**
	 * Switch UserLock between true and false.
	 */
	public boolean toggleLockUserPosition() {
		if (this.lockUserPosition) {
			this.lockUserPosition = false;
		} else {
			this.lockUserPosition = true;
		}
		Log.d(TAG_MAP_CONTROLLER, "Lock User Position: "
				+ this.lockUserPosition);
		return this.lockUserPosition;
	}

	/**
	 * 
	 */
	public void addPoiToView() {
		this.mapModel.updatePOIofDisplay();
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
			this.updateRouteOverlay();
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
						mapModel.getUpperLeft(),
						mapModel.getCurrentLevelOfDetail());
		try {
			next = CoordinateNormalizer.normalizeCoordinate(next,
					(int) this.getCurrentLevelOfDetail());
		} catch (IllegalArgumentException e) {
			Log.e(TAG_MAP_CONTROLLER,
					"Coordinate konnte nicht normalisiert werden!");
		}
		this.routeController.addWaypoint(new Waypoint(next.getLatitude(), next
				.getLongitude(), "PLACEHOLDER"));
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

		Coordinate c = this.getWaypointById(id);
		c.setLatitude(c.getLatitude() - yDelta);
		c.setLongitude(c.getLongitude() + xDelta);

		this.routeController.moveActiveWaypoint(c);
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

	/**
	 * Helper Method that updateRouteOverlay
	 */
	private void updateRouteOverlay() {

		this.lines = CoordinateUtility.extractDisplayCoordinatesOutOfRouteInfo(
				currentRoute, this.mapModel.getUpperLeft(),
				this.mapModel.getCurrentLevelOfDetail());

		this.displayPoints = CoordinateUtility
				.extractDisplayWaypointsOutOfRouteInfo(currentRoute,
						this.mapModel.getUpperLeft(),
						this.mapModel.getCurrentLevelOfDetail());

		/*
		 * this.lines.clear(); this.displayPoints.clear(); this.lines.add(new
		 * DisplayCoordinate( 100, 100)); this.lines.add(new DisplayCoordinate(
		 * 150, 150)); this.lines.add(new DisplayCoordinate( 300, 600));
		 * this.lines.add(new DisplayCoordinate( 350, 700)); this.lines.add(new
		 * DisplayCoordinate( 800, 400)); this.displayPoints.add(new
		 * DisplayWaypoint(100,100,1)); this.displayPoints.add(new
		 * DisplayWaypoint(300,600,2)); this.displayPoints.add(new
		 * DisplayWaypoint(800,400,3));
		 */

		Log.d("test3","Amount " + currentRoute.getWaypoints().size());
		Log.d("test3","Amount " + displayPoints.size());

		
		mapView.updateDisplayWaypoints(displayPoints);
		//mapModel.shift(new DisplayCoordinate(0, 0));
		// mapView.setActive(2);
		mapModel.drawDisplayCoordinates(lines);

		if (this.currentRoute.getActiveWaypoint() == null) {
			return;
		}
		
		mapView.setActive(currentRoute.getActiveWaypoint().getId());

	}

	@Override
	public void onRouteChange(RouteInfo currentRoute) {
		Log.d(TAG_MAP_CONTROLLER, "Route Change! " + currentRoute.getWaypoints().size());
		if (currentRoute != null) {
			this.currentRoute = currentRoute;
			this.currentActiveWaypoint = currentRoute.getActiveWaypoint()
					.getId();
		}
		updateRouteOverlay();
	}

	Coordinate user;

	@Override
	public void onPositionChange(Location androidLocation) {
		Log.d(TAG_MAP_CONTROLLER, "Position Change!");
		user = new Coordinate(androidLocation.getLatitude(),
				androidLocation.getLongitude());

		updateUserPosition();
		// mapView.setUserPositionOverlayImage(200,200);
	}

	/**
	 * 
	 */
	public void updateUserPosition() {
		Log.d(TAG_MAP_CONTROLLER, "User Position is " + (user != null));
		Log.d(TAG_MAP_CONTROLLER, "MapModel is " + (mapModel != null));

		if (mapModel != null) {

			double lon = -this.mapModel.getUpperLeft().getLongitude()
					+ user.getLongitude();
			double lat = -user.getLatitude()
					+ this.mapModel.getUpperLeft().getLatitude();

			DisplayCoordinate pos = new DisplayCoordinate(
					CoordinateUtility.convertDegreesToPixels(lon,
							getCurrentLevelOfDetail(),
							CoordinateUtility.DIRECTION_LONGTITUDE),
					CoordinateUtility.convertDegreesToPixels(lat,
							getCurrentLevelOfDetail(),
							CoordinateUtility.DIRECTION_LATITUDE));

			Log.d(TAG_MAP_CONTROLLER, "User Posi ist:" + user.toString());
			mapView.setUserPositionOverlayImage(pos.getX(), pos.getY());

			if (this.lockUserPosition) {
				Log.d(TAG_MAP_CONTROLLER, "Position Shift Change!");

				this.mapModel.shift(new DisplayCoordinate(pos.getX(), pos
						.getY()));

				this.mapModel.shift(new DisplayCoordinate(-size.x / 2,
						-size.y / 2));
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
		// TODO route muss id als actove setzen lassen
		this.routeController.setActiveWaypoint(id);
	}

	@Override
	public void onCompassChange(float direction) {
		// TODO weiterleitung zu View sollte klappen
		// this.mapView.onPositionChange(direction);
		// this.mapView.onPositionChange(0.0f);
		//Log.d(TAG_MAP_CONTROLLER, "Grad: " + direction);
		this.mapView.setUserPositionOverlayImage(direction);
	}

	/**
	 * 
	 * @return
	 */
	public POI getPOIById(int id) {
		// Log.d(TAG_MAP_CONTROLLER, "CurrentPOI Name " + currentPOI.getName() +
		// " id: " + currentPOI.getId());
		return mapModel.getPOIInformationById(id);
	}

	public POI getPOI() {
		return mapView.getCurrentPOI();
	}

}
package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

//Java library
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.MapModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.PullUpView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
//Android library
//Walkaround library

/**
 * This Class controls the data flow between the System and the real View.
 *
 * @author Ludwig Biermann
 *
 */
public class MapController implements RouteListener, PositionListener {

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
	private boolean lockUserPosition = true;

	/**
	 * DisplayWaypoints to display the Display Points
	 */
	private List<DisplayWaypoint> displayPoints;

	/**
	 * DisplayCoordinats to draw he Lines
	 */
	//TODO Es gibt anscheinend zwei Methoden Wegpunkte zu bekommen es muss noch unterschieden werden welche zum Routen zeichnen welche zum Wegpunkt zeichnen benutzt werden.
	@SuppressWarnings("unused")
	private List<DisplayWaypoint> lines;

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

		this.mapView = mv;
		Point size = mv.getDisplaySize();

		Log.d(TAG_MAP_CONTROLLER, "Initialice List of Display Points!");
		displayPoints = new LinkedList<DisplayWaypoint>();

		Log.d(TAG_MAP_CONTROLLER, "Initialice MapModel!");
		this.mapModel = MapModel.initialize(defaultCoordinate, this, size);

		Log.d(TAG_MAP_CONTROLLER, "Initialice and register routeController!");
		routeController = RouteController.getInstance();
		routeController.registerRouteListener(this);

		Log.d(TAG_MAP_CONTROLLER,
				"Add three Example Waypoints to routeController!");
		routeController.addWaypoint(new Waypoint(49.01, 8.40333, "Marktplatz"));
		routeController.addWaypoint(new Waypoint(49.00471, 8.3858300,
				"Brauerstraße"));
		routeController.addWaypoint(new Waypoint(49.0145, 8.419, "211"));

	}

	/*
	 * -----------------Getter Methods-----------------
	 */

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
	public List<DisplayWaypoint> getCurrentRoute() {
		return this.displayPoints;
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
		Log.d(TAG_MAP_CONTROLLER, "Shift Map by: " + distanceX + " : " + distanceY);
		this.mapModel.shift(new DisplayCoordinate(distanceX, distanceY));
		if (displayPoints != null) {
			this.mapModel.drawDisplayCoordinates(displayPoints);
			this.mapView.updateDisplayCoordinate(displayPoints);
		}
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
		Log.d(TAG_MAP_CONTROLLER, "The given Zoom Delta: " + delta
				+ " to " + dc.toString() + " will be forwarding to MapModel");
		this.mapModel.zoom(delta, dc);
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
	}


	/*
	 * -----------------Calls to Map Controller-----------------
	 */

	/**
	 * Switch UserLock between true and false.
	 */
	public void onLockUserPosition() {
		if (this.lockUserPosition) {
			this.lockUserPosition = false;
		} else {
			this.lockUserPosition = true;
		}
		Log.d(TAG_MAP_CONTROLLER, "Lock User Position: "
				+ this.lockUserPosition);
	}

	/*
	 * -----------------Forwarding To Route Controller-----------------
	 */

	/**
	 * Delete the Active Waypoint
	 */
	public void onDeletePoint() {
		Log.d(TAG_MAP_CONTROLLER, "Delete active Waypoint");
		this.routeController.deleteActiveWaypoint();
	}

	/**
	 * Creates a new Point.
	 *
	 * @param dc
	 *            the DisplayCoordinats of the new Point
	 */
	public void onCreatePoint(DisplayCoordinate dc) {
		Log.d(TAG_MAP_CONTROLLER, "onCreatePoint(" + dc + ")");
		this.routeController.addWaypoint(CoordinateUtility
				.convertDisplayCoordinateToCoordinate(dc,
						mapModel.getUpperLeft(),
						mapModel.getCurrentLevelOfDetail()));
	}


	/*
	 * -----------------Implemented Listener-----------------
	 */

	@Override
	public void onRouteChange(RouteInfo currentRoute, Waypoint activeWaypoint) {
		Log.d(TAG_MAP_CONTROLLER, "Route Change!");
		// LinkedList<Waypoint> waypointList = currentRoute.getWaypoints();

		if (currentRoute.getWaypoints() == null) {
			return;
		}

		Log.d("TAG_MAPVIEW_DRAW", "current Route Anzahl "
				+ currentRoute.getWaypoints().size());

		displayPoints = CoordinateUtility.extractDisplayWaypointsOutOfRouteInfo(
				currentRoute, this.mapModel.getUpperLeft(),
				this.mapModel.getCurrentLevelOfDetail());

		mapView.updateDisplayCoordinate(displayPoints);
		mapModel.drawDisplayCoordinates(displayPoints);

		// TODO Auto-generated method stub
		// mapView.setActive(activeWaypoint.getId());

	}

	@Override
	public void onPositionChange(Location androidLocation) {
		Log.d(TAG_MAP_CONTROLLER, "Position Change!");
		// TODO Auto-generated method stub

	}

}
package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.MapModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.PullUpView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

/**
 * 
 * @author Ludwig Biermann
 * 
 */
public class MapController implements RouteListener {

	private static String MAP_CONTROLLER = "MAP_CONTROLLER";
	private static MapController mapController;

	private static RouteMenuController routeController;

	// public static Coordinate defaultCoordinate = new Coordinate(49.00471,
	// 8.3858300); // Brauerstraße
	public static Coordinate defaultCoordinate = new Coordinate(49.0145, 8.419); // 211
	// public static Coordinate defaultCoordinate = new Coordinate(49.01,
	// 8.40333); // Marktplatz

	private MapView mapView;
	private MapModel mapModel;

	private boolean lockUserPosition = true;

	/**
	 * 
	 * @param mapView
	 * @return
	 */
	public static MapController initialize(MapView mapView) {
		if (mapController == null) {
			mapController = new MapController(mapView);
		}
		return mapController;
	}

	/**
	 * 
	 * @return
	 */
	public static MapController getInstance() {
		if (mapController == null) {
			Log.d(MAP_CONTROLLER, "bitte initialisieren Sie zuerst MapView");
			// mapController = new MapController();
			return null;
		}
		return mapController;
	}

	/**
	 * 
	 */
	public PullUpView getPullUpView() {
		return mapView.getPullUpView();
	}

	/**
	 * 
	 * @param mv
	 */
	private MapController(MapView mv) {

		Log.d(MAP_CONTROLLER, "Map Controller wird initialisiert");
		DisplayWaypoint[] dw = new DisplayWaypoint[1];
		dw[0] = new DisplayWaypoint(-50, -50, 1);
		this.mapView = mv;

		Display display = mapView.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		this.mapModel = MapModel.initialize(defaultCoordinate, this, size);

		routeController = RouteMenuController.getInstance();
		routeController.registerRouteListener(this);
	}

	public void onMapOverlayImageChange(Bitmap b) {
		this.mapView.updateMapImage(b);
		//this.onRouteChange(null, null);
	}

	public void onRouteOverlayImageChange(Bitmap b) {
		mapView.updateRouteOverlayImage(b);
	}

	public void onDeletePoint(DisplayCoordinate dc) {

	}

	public void onCreatePoint(DisplayCoordinate dc) {
		routeController.addWaypoint(
				CoordinateUtility
				.convertDisplayCoordinateToCoordinate(dc,
						mapModel.getUpperLeft(),
						mapModel.getCurrentLevelOfDetail()));

		Log.d(MAP_CONTROLLER, "upper Left ist: " + mapModel.getUpperLeft());
		Log.d(MAP_CONTROLLER, "upper DisplayCoor ist: " + dc);
		Log.d(MAP_CONTROLLER,
				"Coordinate wird übernommen:"
						+ CoordinateUtility
								.convertDisplayCoordinateToCoordinate(dc,
										mapModel.getUpperLeft(),
										mapModel.getCurrentLevelOfDetail()));
	}

	public void onLongPressPoint(DisplayCoordinate dc) {
	}

	public void onShift(float distanceX, float distanceY) {
		mapModel.shift(new DisplayCoordinate(distanceX, distanceY));
		mapModel.drawDisplayCoordinates(dw);
	}

	public void containsWaypoint(DisplayCoordinate dc) {

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
		this.mapModel.zoom(delta, dc);
	}

	/**
	 * Zoom by a delta
	 * 
	 * @param delta
	 *            to the new ZoomLevel
	 */
	public void onZoom(float delta) {
		Log.d(MAP_CONTROLLER, "Gibt ZoomDelta " + delta + " zu MapModel weiter");
		this.mapModel.zoom(delta);
	}

	/**
	 * 
	 */
	public void onLockUserPosition() {
		Log.d(MAP_CONTROLLER, "Lock User Position");
		if (this.lockUserPosition) {
			this.lockUserPosition = false;
		} else {
			this.lockUserPosition = true;
		}

	}

	/**
	 * Gibt das aktuelle Level Of Detail zurück
	 * 
	 * @return aktuelles Level ofDetail
	 */
	public float getCurrentLevelOfDetail() {
		return this.mapModel.getCurrentLevelOfDetail();
	}

	DisplayWaypoint[] dw;

	@Override
	public void onRouteChange(RouteInfo currentRoute, Waypoint activeWaypoint) {
		Log.d(MAP_CONTROLLER, "Route Change!" + (currentRoute == null));
		LinkedList<Waypoint> waypointList = currentRoute.getWaypoints();

		//LinkedList<Waypoint> waypointList = new LinkedList<Waypoint>();
		waypointList.add(new Waypoint(49.01, 8.40333, 1, "Marktplatz"));
		waypointList.add(new Waypoint(49.00471, 8.3858300, 2, "Brauerstraße"));
		waypointList.add(new Waypoint(49.0145, 8.419, 3, "211"));

		DisplayWaypoint[] dw = new DisplayWaypoint[waypointList.size()];
		int a = 0;

		for (Waypoint value : waypointList) {
			Log.d(MAP_CONTROLLER, "Value " + value.toString());

			float x = (float) (value.getLongitude() - mapModel.getUpperLeft()
					.getLongitude());
			float y = (float) (value.getLatitude() - mapModel.getUpperLeft()
					.getLatitude());

			dw[a] = new DisplayWaypoint(

			CoordinateUtility.convertDegreesToPixels(x,
					mapModel.getCurrentLevelOfDetail(),
					CoordinateUtility.DIRECTION_LONGTITUDE),

			CoordinateUtility.convertDegreesToPixels(y,
					mapModel.getCurrentLevelOfDetail(),
					CoordinateUtility.DIRECTION_LATITUDE),

			value.getId());
			a++;

		}

		/*
		 * DisplayWaypoint[] dw = new DisplayWaypoint[4]; dw[0] = new
		 * DisplayWaypoint(-50, 550, 1); dw[1] = new DisplayWaypoint(250, 700,
		 * 2); dw[2] = new DisplayWaypoint(500, 800, 3); dw[3] = new
		 * DisplayWaypoint(300, 900, 4);
		 */

		// TODO
		mapView.updateDisplayCoordinate(dw);
		mapModel.drawDisplayCoordinates(dw);
		this.dw = dw.clone();
		// mapView.setActive(activeWaypoint.getId());
	}

}
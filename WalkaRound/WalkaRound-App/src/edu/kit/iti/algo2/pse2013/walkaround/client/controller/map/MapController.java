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
	public PullUpView getPullUpView(){
		return mapView.getPullUpView();
	}
	
	/**
	 * 
	 * @param mv
	 */
	private MapController(MapView mv) {

		Log.d(MAP_CONTROLLER, "Map Controller wird initialisiert");
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
	}

	public void onRouteOverlayImageChange(Bitmap b) {

	}

	public void onDeletePoint(DisplayCoordinate dc) {

	}

	public void onCreatePoint(DisplayCoordinate dc) {
		routeController.addWaypoint(CoordinateUtility
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

	@Override
	public void onRouteChange(RouteInfo currentRoute, Waypoint activeWaypoint) {
		Log.d(MAP_CONTROLLER, "Route Change!");
		LinkedList<Waypoint> waypointList = currentRoute.getWaypoints();
		DisplayWaypoint[] dw = new DisplayWaypoint[waypointList.size()];
		int a = 0;

		for (Waypoint value : waypointList) {
			Log.d(MAP_CONTROLLER, "Value " + value.toString());

			float x = (float) (value.getLongitude() - mapModel.getUpperLeft().getLongitude());
			float y = (float) (value.getLatitude() - mapModel.getUpperLeft().getLatitude());
			
			dw[a] = new DisplayWaypoint(
					

			CoordinateUtility.convertDegreesToPixels(
					x,
					mapModel.getCurrentLevelOfDetail(),
					CoordinateUtility.DIRECTION_LONGTITUDE),

			CoordinateUtility.convertDegreesToPixels(
					y,
					mapModel.getCurrentLevelOfDetail(),
					CoordinateUtility.DIRECTION_LATITUDE),

			value.getId());
			a++;
		}

		mapView.updateDisplayCoordinate(dw);

		// mapView.setActive(activeWaypoint.getId());
	}

}
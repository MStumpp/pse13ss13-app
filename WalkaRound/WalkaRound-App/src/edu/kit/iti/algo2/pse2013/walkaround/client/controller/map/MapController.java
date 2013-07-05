package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.MapModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
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
	
	//public static Coordinate defaultCoordinate = new Coordinate(49.00471, 8.3858300); // Brauerstraße
	public static Coordinate defaultCoordinate = new Coordinate(49.0145, 8.419); // 211
	//public static Coordinate defaultCoordinate = new Coordinate(49.01, 8.40333); // Marktplatz

	private MapView mapView;
	private MapModel mapModel;

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
	 * @param mv
	 */
	private MapController(MapView mv) {
		
		routeController = RouteMenuController.getInstance();
		routeController.registerRouteListener(this);
		
		Log.d(MAP_CONTROLLER, "Map Controller wird initialisiert");
		this.mapView = mv;

		Display display = mapView.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		this.mapModel = MapModel.initialize(defaultCoordinate, this, size);
	}


	public void onMapOverlayImageChange(Bitmap b) {
		this.mapView.updateMapImage(b);
	}

	public void onRouteOverlayImageChange(Bitmap b) {

	}

	public void onDeletePoint(DisplayCoordinate dc) {

	}

	public void onCreatePoint(DisplayCoordinate dc) {
		routeController.addWaypoint(CoordinateUtility.convertDisplayCoordinateToCoordinate(dc, mapModel.getUpperLeft(), mapModel.getCurrentLevelOfDetail()));
	}

	public void onLongPressPoint(DisplayCoordinate dc) {
	}

	public void onShift(DisplayCoordinate dc) {
		mapModel.shift(dc);
	}

	public void containsWaypoint(DisplayCoordinate dc) {

	}

	/**
	 * Zoom by a delta to a DisplayCoordinate
	 * 
	 * @param delta to the new ZoomLevel
	 * @param dc the DisplayCoordinate 
	 */
	public void onZoom(float delta, DisplayCoordinate dc) {
		this.mapModel.zoom(delta, dc);
	}

	/**
	 * Zoom by a delta
	 * 
	 * @param delta to the new ZoomLevel
	 */
	public void onZoom(float delta) {
		Log.d(MAP_CONTROLLER, "Gibt ZoomDelta " + delta + " zu MapModel weiter");
		this.mapModel.zoom(delta);
	}

	public void onLockUserPosition() {

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
		
	}

}
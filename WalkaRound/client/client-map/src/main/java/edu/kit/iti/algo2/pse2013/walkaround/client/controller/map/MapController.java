package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.MapModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

/**
 *
 * @author Ludwig Biermann
 *
 */
public class MapController {

	private static String MAP_CONTROLLER = "MAP_CONTROLLER";
	private static MapController mapController;

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

	}

	public void onLongPressPoint(DisplayCoordinate dc) {

	}

	public void onShift(DisplayCoordinate dc) {
		mapModel.generateMapOverlayImage();
	}

	public void containsWaypoint(DisplayCoordinate dc) {

	}

	public void onZoom(float z, DisplayCoordinate dc) {

	}

	public void onZoom(float z) {

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

}
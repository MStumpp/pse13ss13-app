package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.MapModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager;
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

	private static String TAG_MAP_CONTROLLER = "MAP_CONTROLLER";
	private static MapController mapController;

	private static RouteController routeController;

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
			Log.d(TAG_MAP_CONTROLLER, "bitte initialisieren Sie zuerst MapView");
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


		Log.d(TAG_MAP_CONTROLLER, "Map Controller wird initialisiert");
		//DisplayWaypoint[] dw = new DisplayWaypoint[1];
		//dw[0] = new DisplayWaypoint(-50, -50, 1);

		this.mapView = mv;

		
		Display display = mapView.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);


		dw = new DisplayWaypoint[0];
		Log.d("TAG_MAPVIEW_DRAW", "--------->!" + dw.length);

		this.mapModel = MapModel.initialize(defaultCoordinate, this, size);
		
		
		routeController = RouteController.getInstance();
		routeController.registerRouteListener(this);

		routeController.addWaypoint(new Waypoint(49.01,8.40333,1,"Marktplatz"));
		//Log.d("TAG_MAPVIEW_DRAW", "current Route Anzahl " + currentRoute.getWaypoints().size());
		routeController.addWaypoint(new Waypoint(49.00471, 8.3858300,2,"Brauerstraße"));
		//Log.d("TAG_MAPVIEW_DRAW", "current Route Anzahl " + currentRoute.getWaypoints().size());
		routeController.addWaypoint(new Waypoint(49.0145, 8.419,3,"211"));
		//Log.d("TAG_MAPVIEW_DRAW", "current Route Anzahl " + currentRoute.getWaypoints().size());
		

		
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
		Log.d(TAG_MAP_CONTROLLER, "onCreatePoint(" + dc + ")");
		routeController.addWaypoint(CoordinateUtility
				.convertDisplayCoordinateToCoordinate(dc,
						mapModel.getUpperLeft(),
						mapModel.getCurrentLevelOfDetail()));

		Log.d(TAG_MAP_CONTROLLER, "upper Left ist: " + mapModel.getUpperLeft());
		Log.d(TAG_MAP_CONTROLLER, "upper DisplayCoor ist: " + dc);
		Log.d(TAG_MAP_CONTROLLER,
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
		if(dw != null){
			Log.d("wtf2", "DisplayRoute wird gezeichnet");
			mapModel.drawDisplayCoordinates(dw.clone());
			mapView.updateDisplayCoordinate(dw.clone());
		}
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
		Log.d(TAG_MAP_CONTROLLER, "Gibt ZoomDelta " + delta + " zu MapModel weiter");
		this.mapModel.zoom(delta);
	}

	/**
	 *
	 */
	public void onLockUserPosition() {
		Log.d(TAG_MAP_CONTROLLER, "Lock User Position");
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

	public DisplayWaypoint[] convertRouteInfoToDisplayWapoints(RouteInfo currentRoute){

		Log.d("CONVERT_ROUTEINFO", " " + currentRoute.getWaypoints());
		
		
		DisplayWaypoint[] dw = new DisplayWaypoint[currentRoute.getWaypoints().size()];
		int a = 0;
		for (Waypoint value : currentRoute.getWaypoints()) {
			Log.d("CONVERT_ROUTEINFO", " " + value);
			Log.d("CONVERT_ROUTEINFO", " " + mapModel.getUpperLeft());

			float x = (float) (value.getLongitude() - mapModel.getUpperLeft().getLongitude());
			float y = (float) (value.getLatitude() - mapModel.getUpperLeft().getLatitude());

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
		
		return dw;
	}
	
	public DisplayWaypoint[] getCurrentRoute() {
		return this.dw;
	}
	
	@Override
	public void onRouteChange(RouteInfo currentRoute, Waypoint activeWaypoint) {
		Log.d(TAG_MAP_CONTROLLER, "Route Change!");
		//LinkedList<Waypoint> waypointList = currentRoute.getWaypoints();

		if(currentRoute.getWaypoints() == null){
			return;
		}

		Log.d("TAG_MAPVIEW_DRAW", "current Route Anzahl " + currentRoute.getWaypoints().size());
		
		dw = this.convertRouteInfoToDisplayWapoints(currentRoute).clone();

		// TODO
		mapView.updateDisplayCoordinate(dw);
		mapModel.drawDisplayCoordinates(dw);
		// mapView.setActive(activeWaypoint.getId());
		
	}

}
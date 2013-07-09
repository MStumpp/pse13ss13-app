package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
import android.content.Context;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.LocationManager;
import android.util.Log;

public class PositionManager implements Listener {

	private static String TAG_POSITION_MANAGER = PositionManager.class.getSimpleName();
	
	// Observers:
	private LinkedList<PositionListener> positionListeners;
	
	// Singleton Pattern:
	private static boolean intanceExists;
	private static PositionManager positionManager;
	
	// 
	private static LocationManager locationManager;
	private static GpsStatus gpsStatus;
	private static int lastGPSEvent;
	
	private PositionManager() {
	}
	
	public static void initialize(Context context) {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.initialize(Context)");
		locationManager = (LocationManager) context.getApplicationContext().getSystemService("LOCATION_SERVICE");
	}
	
	public static PositionManager getInstance() {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.getInstance()");
		assert (locationManager != null);
		if (!intanceExists) {
			positionManager = new PositionManager();
		}
		return positionManager;
	}
	

	
	// Observer Pattern:
	public void registerPositionListener(PositionListener newPL) {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.registerPositionListener(PositionListener " + newPL.getClass().getSimpleName() + ")");
		if (!this.positionListeners.contains(newPL)) {
			this.positionListeners.add(newPL);
		}
		this.notifyAllRouteListeners();
	}

	private void notifyAllRouteListeners() {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.notifyAllRouteListeners()");
		Coordinate gpsPosition;
		
		// TODO: wenn GPS status nicht null, bestimme Coord zur aktuelle GPS Position und übergebe diese
		
		for (PositionListener pl : this.positionListeners) {
			pl.onPositionChange(gpsPosition);
		}
		
		
	}
	
	
	public void onGpsStatusChanged(int event) {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.onGpsStatusChanged(int " + event + ")");
		lastGPSEvent = event;
		if (lastGPSEvent == 3 || lastGPSEvent == 4) {
			notifyAllRouteListeners();
		}
	}
	
	
	
	
}

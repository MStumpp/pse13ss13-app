package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;
import android.content.Context;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class PositionManager implements Listener {

	private static String TAG_POSITION_MANAGER = PositionManager.class.getSimpleName();
	
	// Observers:
	private LinkedList<PositionListener> positionListeners;
	
	// Singleton Pattern:
	private static PositionManager positionManager;
	
	private static LocationManager locationManager;
	private static int lastGPSEvent;
	private static Location lastKnownLocation;
	
	private PositionManager(Context context) {
		Log.d(TAG_POSITION_MANAGER, "Context da " + (context != null));
		locationManager = (LocationManager) context.getApplicationContext().getSystemService("LOCATION_SERVICE");
	}
	
	public static void initialize(Context context) {
		positionManager = new PositionManager(context);
		locationManager.addGpsStatusListener(positionManager);
	}
	
	public static PositionManager getInstance() {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.getInstance()");
		if (positionManager == null) {
			Log.d(TAG_POSITION_MANAGER, "PositionManager not initialized");
			return null;
		}
		return positionManager;
	}


	
	// Observer Pattern:
	public void registerPositionListener(PositionListener newPL) {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.registerPositionListener(PositionListener " + newPL.getClass().getSimpleName() + ")");
		if (!this.positionListeners.contains(newPL)) {
			this.positionListeners.add(newPL);
		}
		this.notifyAllPositionListeners();
	}

	private void notifyAllPositionListeners() {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.notifyAllPositionListeners()");		
		for (PositionListener pl : this.positionListeners) {
			pl.onPositionChange(lastKnownLocation);
		}
		
	}
	
	
	// Android GPS Listener method:
	public void onGpsStatusChanged(int event) {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.onGpsStatusChanged(int " + event + ")");
		lastGPSEvent = event;
		lastKnownLocation = locationManager.getLastKnownLocation("GPS");
		if (lastGPSEvent == 3 || lastGPSEvent == 4) {
			notifyAllPositionListeners();
		}
	}
	
	
	
	
}

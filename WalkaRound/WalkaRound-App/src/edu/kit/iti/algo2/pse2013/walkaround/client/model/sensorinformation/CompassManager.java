package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import android.util.Log;

public class CompassManager {
	
	private static String TAG_COMPASS_MANAGER = CompassManager.class.getSimpleName();

	private static CompassManager compassManager;
	
	private CompassManager() {
	}
	/*
	public static CompassManager getInstance() {
		if (compassManager == null) {
			compassManager = new CompassManager();
		}
		return compassManager;
	}

	public static CompassManager getInstance() {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.getInstance()");
		assert (locationManager != null);
		if (positionManager == null) {
			positionManager = new PositionManager();
		}
		lastKnownLocation = locationManager.getLastKnownLocation("GPS");
		return positionManager;
	}
	*/
	
	

}

package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;

import android.location.Location;
import android.util.Log;

public class CompassManager implements PositionListener {

	private static String TAG_COMPASS_MANAGER = CompassManager.class
			.getSimpleName();
	// Observers:
	private LinkedList<CompassListener> compassListeners;

	private static CompassManager compassManager;

	private double lastKnownBearing;

	private CompassManager() {
		lastKnownBearing = 0.0d;
		compassListeners = new LinkedList<CompassListener>();
	}

	public static CompassManager getInstance() {
		Log.d(TAG_COMPASS_MANAGER, "CompassManager.getInstance()");
		if (compassManager == null) {
			compassManager = new CompassManager();
			PositionManager.getInstance().registerPositionListener(
					compassManager);
		}
		return compassManager;
	}

	// Observer Pattern:
	public void registerCompassListener(CompassListener newCL) {
		Log.d(TAG_COMPASS_MANAGER,
				"CompassManager.registerCompassListener(CompassListener "
						+ newCL.getClass().getSimpleName() + ")");
		if (!this.compassListeners.contains(newCL)) {
			this.compassListeners.add(newCL);
		}
		this.notifyAllCompassListeners();
	}

	private void notifyAllCompassListeners() {
		Log.d(TAG_COMPASS_MANAGER, "CompassManager.notifyAllCompassListeners()");
		for (CompassListener cl : this.compassListeners) {
			cl.onCompassChange(lastKnownBearing);
		}
	}

	@Override
	public void onPositionChange(Location androidLocation) {
		//TODO Warum ist androidLocation null?
		try {
			lastKnownBearing = androidLocation.getBearing();
			notifyAllCompassListeners();
		} catch (NullPointerException e) {
			Log.e(TAG_COMPASS_MANAGER,
					"android Location ist null " + e.toString());
		}
	}

}

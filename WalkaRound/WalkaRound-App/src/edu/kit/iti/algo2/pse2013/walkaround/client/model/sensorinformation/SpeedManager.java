package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

public class SpeedManager extends Activity implements PositionListener {

	private static String TAG_SPEED_MANAGER = SpeedManager.class
			.getSimpleName();

	// Observers:
	private LinkedList<SpeedListener> speedListeners;

	private static SpeedManager speedManager;

	private static double lastKnownSpeed;

	private SpeedManager() {
	}

	public SpeedManager getInstance() {
		if (speedManager == null) {
			speedManager = new SpeedManager();
			PositionManager.getInstance(getApplicationContext()).registerPositionListener(speedManager);
		}
		return speedManager;
	}


	// Observer Pattern:
	public void registerSpeedListener(SpeedListener newSL) {
		Log.d(TAG_SPEED_MANAGER,
				"Speedmanager.registerSpeedListener(SpeedListener "
						+ newSL.getClass().getSimpleName() + ")");
		if (!this.speedListeners.contains(newSL)) {
			this.speedListeners.add(newSL);
		}
		this.notifyAllSpeedListeners();
	}

	private void notifyAllSpeedListeners() {
		Log.d(TAG_SPEED_MANAGER, "SpeedManager.notifyAllRouteListeners()");
		for (SpeedListener sl : this.speedListeners) {
			sl.onSpeedChange(lastKnownSpeed);
		}

	}

	@Override
	public void onPositionChange(Location androidLocation) {
		lastKnownSpeed = androidLocation.getSpeed();
	}
}

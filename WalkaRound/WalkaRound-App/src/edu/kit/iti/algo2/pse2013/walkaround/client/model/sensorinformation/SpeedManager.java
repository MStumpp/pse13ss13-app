package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;

import android.location.Location;
import android.util.Log;
/**
 * This class hold and return the last known Speed.
 * 
 * @author Lukas M�ller, Ludwig Biermann
 *
 */
public class SpeedManager implements PositionListener {

	/*
	 * 
	 */
	private static String TAG_SPEED_MANAGER = SpeedManager.class
			.getSimpleName();

	/*
	 * 
	 */
	private LinkedList<SpeedListener> speedListeners;

	/*
	 * 
	 */
	private static SpeedManager speedManager;

	/*
	 * 
	 */
	private static double lastKnownSpeed;

	/**
	 * 
	 */
	private SpeedManager() {
		speedListeners = new LinkedList<SpeedListener>();
		PositionManager.getInstance().registerPositionListener(speedManager);
	}

	/**
	 * 
	 * @return
	 */
	public SpeedManager getInstance() {
		if(PositionManager.getInstance() == null){
			Log.e(TAG_SPEED_MANAGER, "Position Manger is not initialiced");
		}
		if (speedManager == null) {
			speedManager = new SpeedManager();
		}
		return speedManager;
	}

	/**
	 * 
	 * @param newSL
	 */
	public void registerSpeedListener(SpeedListener newSL) {
		Log.d(TAG_SPEED_MANAGER,
				"Speedmanager.registerSpeedListener(SpeedListener "
						+ newSL.getClass().getSimpleName() + ")");
		if (!this.speedListeners.contains(newSL)) {
			this.speedListeners.add(newSL);
		}
		this.notifyAllSpeedListeners();
	}

	/**
	 * 
	 */
	private void notifyAllSpeedListeners() {
		Log.d(TAG_SPEED_MANAGER, "SpeedManager.notifyAllCompassListeners()");
		for (SpeedListener sl : this.speedListeners) {
			sl.onSpeedChange(lastKnownSpeed);
		}

	}

	@Override
	public void onPositionChange(Location androidLocation) {
		lastKnownSpeed = androidLocation.getSpeed();
		this.notifyAllSpeedListeners();
	}
}

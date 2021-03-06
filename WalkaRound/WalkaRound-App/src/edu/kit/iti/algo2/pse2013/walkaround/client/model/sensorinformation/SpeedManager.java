package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager.PositionListener;

import android.location.Location;
import android.util.Log;
/**
 * This class hold and return the last known Speed.
 *
 * @author Lukas Müller
 * @version 1.0
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
	//private static SpeedManager speedManager;

	/*
	 *
	 */
	private double lastKnownSpeed;

	/**
	 * @param pm a PositionManager
	 */
	public SpeedManager(PositionManager pm) {
		speedListeners = new LinkedList<SpeedListener>();
		lastKnownSpeed = 0.0d;
		pm.registerPositionListener(this);
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

	public interface SpeedListener {
		public void onSpeedChange(double speed);
	}
}

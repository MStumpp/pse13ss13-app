package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;

import android.location.Location;
import android.util.Log;

/**
 * This class hold and compare the last known orientation of the device
 * 
 * @author Lukas Müller, Ludwig Biermann
 * 
 */
public class CompassManager implements PositionListener {

	/*
	 * 
	 */
	private static String TAG_COMPASS_MANAGER = CompassManager.class
			.getSimpleName();

	/*
	 * 
	 */
	private LinkedList<CompassListener> compassListeners;

	/*
	 * 
	 */
	private float lastKnownBearing;

	/**
	 * 
	 */
	public CompassManager(PositionManager pm) {
		lastKnownBearing = 0.0f;
		compassListeners = new LinkedList<CompassListener>();
		pm.registerPositionListener(this);
	}

	/**
	 * 
	 * @return
	 *
	public static CompassManager getInstance() {
		Log.d(TAG_COMPASS_MANAGER, "CompassManager.getInstance()");
		if (compassManager == null) {
			compassManager = new CompassManager();
			PositionManager.getInstance().registerPositionListener(
					compassManager);
		}
		return compassManager;
	}/

	/**
	 * 
	 * @param newCL
	 */
	public void registerCompassListener(CompassListener newCL) {
		Log.d(TAG_COMPASS_MANAGER,
				"CompassManager.registerCompassListener(CompassListener "
						+ newCL.getClass().getSimpleName() + ")");
		if (!this.compassListeners.contains(newCL)) {
			this.compassListeners.add(newCL);
		}
		this.notifyAllCompassListeners();
	}

	/**
	 * 
	 */
	private void notifyAllCompassListeners() {
		Log.d(TAG_COMPASS_MANAGER, "CompassManager.notifyAllCompassListeners()");
		for (CompassListener cl : this.compassListeners) {
			cl.onCompassChange(lastKnownBearing);
		}
	}

	@Override
	public void onPositionChange(Location androidLocation) {
		lastKnownBearing = androidLocation.getBearing();
		notifyAllCompassListeners();
	}

}

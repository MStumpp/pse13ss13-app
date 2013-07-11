package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;
import android.content.Context;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * This class hold and return the last known detected Position from the android
 * file System
 * 
 * @author Lukas M�ller, Ludwig Biermann
 * 
 */
public class PositionManager implements Listener {

	/*
	 * 
	 */
	private static String TAG_POSITION_MANAGER = PositionManager.class
			.getSimpleName();

	/*
	 * 
	 */
	private LinkedList<PositionListener> positionListeners;

	/*
	 * 
	 */
	private static PositionManager positionManager;

	/*
	 * 
	 */
	private LocationManager locationManager;
	private int lastGPSEvent;
	private Location lastKnownLocation;

	/**
	 * 
	 * @param context
	 */
	private PositionManager(Context context) {
		locationManager = (LocationManager) context.getApplicationContext()
				.getSystemService(LocationManager.KEY_LOCATION_CHANGED);
		positionListeners = new LinkedList<PositionListener>();
		Log.d(TAG_POSITION_MANAGER, "locationManager is "
				+ (locationManager != null));
		locationManager.addGpsStatusListener(positionManager);
		this.getLastKnownPosition();
	}

	/**
	 * 
	 * @param context
	 */
	public static void initialize(Context context) {
		Log.d(TAG_POSITION_MANAGER, "Context is " + (context != null));
		positionManager = new PositionManager(context);
	}

	/**
	 * 
	 * @return
	 */
	public static PositionManager getInstance() {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.getInstance()");
		if (positionManager == null) {
			Log.d(TAG_POSITION_MANAGER, "PositionManager not initialized");
			return null;
		}
		return positionManager;
	}

	/**
	 * 
	 * @param newPL
	 */
	public void registerPositionListener(PositionListener newPL) {
		Log.d(TAG_POSITION_MANAGER,
				"PositionManager.registerPositionListener(PositionListener "
						+ newPL.getClass().getSimpleName() + ")");
		if (!this.positionListeners.contains(newPL)) {
			this.positionListeners.add(newPL);
		}
		this.getLastKnownPosition();
	}

	/**
	 * 
	 */
	private void notifyAllPositionListeners() {
		Log.d(TAG_POSITION_MANAGER,
				"PositionManager.notifyAllPositionListeners()");
		for (PositionListener pl : this.positionListeners) {
			pl.onPositionChange(lastKnownLocation);
		}
	}

	/**
	 * 
	 */
	public void onGpsStatusChanged(int event) {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.onGpsStatusChanged(int "
				+ event + ")");
		lastGPSEvent = event;
		if (lastGPSEvent == 3 || lastGPSEvent == 4) {
			this.getLastKnownPosition();
		}
	}

	/**
	 * the method return the last known position of the user if possible
	 */
	private void getLastKnownPosition() {
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			lastKnownLocation = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			notifyAllPositionListeners();
		} else if (locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			lastKnownLocation = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			notifyAllPositionListeners();
		}
	}

}

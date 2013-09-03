package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;

import android.content.Context;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * This class hold and return the last known detected Position from the android
 * file System
 *
 * @author Lukas Müller, Ludwig Biermann
 *
 */
public class PositionManager {

	private final int minMilliSecondsBetweenUpdates = 5000;
	private final int minMetersBetweenUpdates = 2;


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

	private CompassManager compass;
	private SpeedManager speed;

	/**
	 *
	 * @param context
	 */
	private PositionManager(Context context) {

		positionListeners = new LinkedList<PositionListener>();

		locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

		Log.d(TAG_POSITION_MANAGER, "GPS enabled: " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));

		// TODO: Check if Position Manager really sends updates:
		// Is this necessary?:
		// locationManager.addGpsStatusListener(positionManager);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minMilliSecondsBetweenUpdates, minMetersBetweenUpdates, locListener);
		this.getLastKnownPositionFromAndroid();

		//initialize other Sensors
		compass = new CompassManager(context.getApplicationContext());
		speed = new SpeedManager(this);
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
			Log.e(TAG_POSITION_MANAGER, "PositionManager not initialized");
			return null;
		}
		return positionManager;
	}

	/**
	 * Return the SpeedManager
	 *
	 * @return SpeedManager
	 */
	public SpeedManager getSpeedManager(){
		return speed;
	}

	/**
	 * Returns the CompassManager
	 *
	 * @return CompassManager
	 */
	public CompassManager getCompassManager(){
		return compass;
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
		this.getLastKnownPositionFromAndroid();
	}

	/**
	 *
	 */
	private void notifyAllPositionListeners() {
		Log.d(TAG_POSITION_MANAGER,	"PositionManager.notifyAllPositionListeners() Position is not null: " + (lastKnownLocation != null));
		if (this.lastKnownLocation != null) {
			Log.d(TAG_POSITION_MANAGER, "Sending the following Position to listeners: " + lastKnownLocation.toString());
			for (PositionListener pl : this.positionListeners) {
				Log.d(TAG_POSITION_MANAGER,	"notify position listener - pl is not null: " + (pl != null));
				pl.onPositionChange(lastKnownLocation);
			}
		}
	}

	public Location getLastKnownPosition(){
		return this.lastKnownLocation;
	}

	/**
	 *
	 */
	public void onGpsStatusChanged(int event) {
		Log.d(TAG_POSITION_MANAGER, "PositionManager.onGpsStatusChanged(int " + event + ")");
		lastGPSEvent = event;
		if (lastGPSEvent == 3 || lastGPSEvent == 4) {
			this.getLastKnownPositionFromAndroid();
		}
	}

	/**
	 * the method return the last known position of the user if possible
	 */
	private void getLastKnownPositionFromAndroid() {
		Log.d(TAG_POSITION_MANAGER, "getLastKnownPosition");
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Log.d(TAG_POSITION_MANAGER, "getLastKnownPositionFromAndroid() - GPS is enabled");
			lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			notifyAllPositionListeners();
		} else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Log.d(TAG_POSITION_MANAGER, "getLastKnownPositionFromAndroid() - GPS is enabled");
			lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			Log.d(TAG_POSITION_MANAGER, "GPS");
			notifyAllPositionListeners();
		}
	}

	private final LocationListener locListener = new LocationListener() {

		public void onLocationChanged(Location arg0) {
			lastKnownLocation = arg0;
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	};
}
package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;

import android.content.Context;
import android.location.Criteria;
import android.location.GpsStatus;
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
public class PositionManager implements GpsStatus.Listener {

	private static final int UPDATE_TIME = 5000;
	private static final int UPDATE_DISTANCE = 2;

	private static String TAG = PositionManager.class.getSimpleName();

	private LinkedList<PositionListener> positionListeners;

	private static PositionManager positionManager;

	private LocationManager locationManager;
	private Location lastKnownLocation;

	private CompassManager compass;
	private SpeedManager speed;
	
	private Criteria c;
	
	private LocListener listener;
	private String provider;

	/**
	 * 
	 * @param context
	 */
	private PositionManager(final Context context) {
		Log.d(TAG, "Position Manger initialize.");
		
		listener = new LocListener();
		
		positionListeners = new LinkedList<PositionListener>();

		
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

	    locationManager.addGpsStatusListener(this);
	    
		c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);
		c.setBearingAccuracy(Criteria.ACCURACY_FINE);
		c.setSpeedAccuracy(Criteria.ACCURACY_FINE);
		
		setBestProvider();

		// initialize other Sensors
		compass = new CompassManager(context);
		speed = new SpeedManager(this);
		
	}

	/**
	 * 
	 * @param context
	 */
	public static void initialize(Context context) {
		Log.d(TAG, "Context is " + (context != null));
		positionManager = new PositionManager(context);
	}

	
	/**
	 * 
	 * @return
	 */
	public static PositionManager getInstance() {
		Log.d(TAG, "PositionManager.getInstance()");
		if (positionManager == null) {
			Log.e(TAG, "PositionManager not initialized");
			return null;
		}
		return positionManager;
	}

	/**
	 * Return the SpeedManager
	 * 
	 * @return SpeedManager
	 */
	public SpeedManager getSpeedManager() {
		return speed;
	}

	/**
	 * Returns the CompassManager
	 * 
	 * @return CompassManager
	 */
	public CompassManager getCompassManager() {
		return compass;
	}

	/**
	 * 
	 * @param newPL
	 */
	public void registerPositionListener(PositionListener newPL) {
		Log.d(TAG, "PositionManager.registerPositionListener(PositionListener "
				+ newPL.getClass().getSimpleName() + ")");
		if (!this.positionListeners.contains(newPL)) {
			this.positionListeners.add(newPL);
		}
	}

	/**
	 *
	 */
	private void notifyAllPositionListeners(Location l) {
		for (PositionListener pl : this.positionListeners) {
			Log.d(TAG, "Location Provider: " + l.getProvider().toString() + " Position: " + l.getLatitude() + " : " + l.getLongitude());
			pl.onPositionChange(l);
		}

	}

	public Location getLastKnownPosition() {
		return locationManager.getLastKnownLocation(provider);
	}
	
	private void setBestProvider() {
		provider = locationManager.getBestProvider(c, true);
		
		if(provider != null && !provider.isEmpty()){
			Log.d(TAG, "Provider found!");
		locationManager.requestLocationUpdates(provider,
				UPDATE_TIME, UPDATE_DISTANCE, listener);
		} else {
			Log.e(TAG, "No Provider found!");
			provider = locationManager.getBestProvider(c, false);
			locationManager.requestLocationUpdates(provider,
				UPDATE_TIME, UPDATE_DISTANCE, listener);
		}
	}

	private class LocListener implements LocationListener {

		public void onLocationChanged(Location l) {
			if (l != null) {
				notifyAllPositionListeners(l);
			}
		}

		public void onProviderDisabled(String provider) {
			setBestProvider();
		}

		public void onProviderEnabled(String provider) {
			setBestProvider();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			setBestProvider();
		}
	}

	@Override
	public void onGpsStatusChanged(int event) {
		setBestProvider();
	};
	
	public interface PositionListener {
		public void onPositionChange(Location androidLocation);
	}

}
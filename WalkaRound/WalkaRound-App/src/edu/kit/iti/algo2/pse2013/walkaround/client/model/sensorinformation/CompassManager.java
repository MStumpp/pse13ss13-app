package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.util.Log;

/**
 * This class hold and compare the last known orientation of the device
 * 
 * @author Lukas M�ller, Ludwig Biermann
 * 
 */
public class CompassManager implements SensorEventListener {

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
	
	private SensorManager sensorManager;
	private Sensor aSensor;
	private Sensor mSensor;

	/**
	 * 
	 */
	public CompassManager(Context context) {
		Log.d(TAG_COMPASS_MANAGER, "Compass Manager Constructor");
		lastKnownBearing = 0.0f;
		compassListeners = new LinkedList<CompassListener>();
		this.sensorManager = (SensorManager) context.getSystemService("SENSOR_SERVICE");
		/*
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.sensorManager.registerListener(this, this.sensor, SensorManager.SENSOR_DELAY_NORMAL);*/
	}


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
		Log.d(TAG_COMPASS_MANAGER, "CompassManager.notifyAllCompassListeners() sending onCompassChange(" + this.lastKnownBearing + ") to listeners");
		for (CompassListener cl : this.compassListeners) {
			cl.onCompassChange(this.lastKnownBearing);
		}
	}

	
	
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Log.d(TAG_COMPASS_MANAGER, "Compass Manager onSensorChanged(SensorEvent)");
		this.lastKnownBearing = event.values[0];
		this.notifyAllCompassListeners();
	}

	
}

package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import java.util.LinkedList;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * This class hold and compare the last known orientation of the device
 * 
 * @author Lukas Müller, Ludwig Biermann
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
	private Sensor accellerometer;
	private Sensor magneticFieldSensor;
	private float[] accellerometerReadout;
	private float[] magneticFieldSensorReadout;
	private static int messagesPosted = 0;
	private static int messagePostingInterval = 1000;

	/**
	 * 
	 */
	public CompassManager(Context context) {
		Log.d(TAG_COMPASS_MANAGER, "Compass Manager Constructor");
		lastKnownBearing = 0.0f;
		compassListeners = new LinkedList<CompassListener>();

		this.sensorManager = (SensorManager) context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);		
		
		Log.d(TAG_COMPASS_MANAGER, "" + sensorManager.getSensorList(Sensor.TYPE_ALL));
		
		this.accellerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		this.sensorManager.registerListener(this, this.accellerometer, SensorManager.SENSOR_DELAY_GAME);
		this.sensorManager.registerListener(this, this.magneticFieldSensor, SensorManager.SENSOR_DELAY_GAME);
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
		if (this.messagesPosted % this.messagePostingInterval == 0) {
			Log.d(TAG_COMPASS_MANAGER, "CompassManager.notifyAllCompassListeners() sending onCompassChange(" + this.lastKnownBearing + ") to listeners");
		}
		for (CompassListener cl : this.compassListeners) {
			cl.onCompassChange(this.lastKnownBearing);
		}
		this.messagesPosted++;
	}

	
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (this.messagesPosted % this.messagePostingInterval == 0) {
			Log.d(TAG_COMPASS_MANAGER, "Compass Manager onSensorChanged(SensorEvent)");
		}
		if (event.sensor.getType() == this.accellerometer.getType()) {
			this.accellerometerReadout = event.values.clone();
		}
		if (event.sensor.getType() == this.magneticFieldSensor.getType()) {
			this.magneticFieldSensorReadout = event.values;
		}
		
		if (this.accellerometerReadout != null && this.magneticFieldSensorReadout != null) {
			float[] R = new float[9];
			float[] I = new float[9];
			boolean processed = SensorManager.getRotationMatrix(R, I, this.accellerometerReadout, this.magneticFieldSensorReadout);
			if (processed) {
				float[] orientation = new float[3];
				SensorManager.getOrientation(R, orientation);
				this.lastKnownBearing = (float) Math.toDegrees(orientation[0]);
			}
		}
		this.notifyAllCompassListeners();
	}
	
	

	
}

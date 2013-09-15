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
 * @author Lukas Müller
 * @author Ludwig Biermann
 * @version 2.5
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


	private SensorManager sensorManager;
	private Sensor accellerometer;
	private Sensor magneticFieldSensor;

	private float[] valuesAccelerometer = new float[3];;
	private float[] valuesMagneticField = new float[3];;

	private float[] matrixR = new float[9];;
	private float[] matrixI = new float[9];;
	private float[] matrixValues = new float[3];;

	/**
	 * @param context
	 */
	public CompassManager(Context context) {
		Log.d(TAG_COMPASS_MANAGER, "Compass Manager Constructor");
		compassListeners = new LinkedList<CompassListener>();

		this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

		Log.d(TAG_COMPASS_MANAGER,
				"" + sensorManager.getSensorList(Sensor.TYPE_ALL));

		this.accellerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.magneticFieldSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		this.sensorManager.registerListener(this, this.accellerometer,
				SensorManager.SENSOR_DELAY_GAME);
		this.sensorManager.registerListener(this, this.magneticFieldSensor,
				SensorManager.SENSOR_DELAY_GAME);
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
	}

	/**
	 *
	 */
	private void notifyAllCompassListeners(float bearing) {
		for (CompassListener cl : this.compassListeners) {
			cl.onCompassChange(bearing);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// do nothing
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			for (int i = 0; i < 3; i++) {
				valuesAccelerometer[i] = event.values[i];
			}
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			for (int i = 0; i < 3; i++) {
				valuesMagneticField[i] = event.values[i];
			}
			break;
		}

		boolean success = SensorManager.getRotationMatrix(matrixR, matrixI,
				valuesAccelerometer, valuesMagneticField);

		if (success) {
			SensorManager.getOrientation(matrixR, matrixValues);

			double azimuth = Math.toDegrees(matrixValues[0]);
			@SuppressWarnings("unused")
			double pitch = Math.toDegrees(matrixValues[1]);
			@SuppressWarnings("unused")
			double roll = Math.toDegrees(matrixValues[2]);

			this.notifyAllCompassListeners((float)azimuth);
		}
	}

	public interface CompassListener {
		public void onCompassChange(float direction);
	}

}

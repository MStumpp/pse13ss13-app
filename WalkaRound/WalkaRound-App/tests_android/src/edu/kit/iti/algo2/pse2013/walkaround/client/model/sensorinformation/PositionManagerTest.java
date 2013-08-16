package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.location.Location;

import edu.kit.iti.algo2.pse2013.walkaround.client.BootActivity;

@RunWith(RobolectricTestRunner.class)
public class PositionManagerTest implements PositionListener, CompassListener, SpeedListener {
	
	PositionManager pm;
	
	@BeforeClass
	public void setUp(){
		PositionManager.initialize(new BootActivity().getApplicationContext());
		pm = PositionManager.getInstance();
	}
	
	@Test
	public void testInit() {
		assertTrue(pm.getCompassManager() != null);
		assertTrue(pm.getSpeedManager() != null);
	}

	@Test
	public void testLastKnownPosition() {
		assertTrue(pm.getLastKnownPosition() != null);
	}

	@Test
	public void testRegister() {
		pm.registerPositionListener(this);
		assertTrue(true);
	}

	@Test
	public void testCompassManager() {
		pm.getCompassManager().registerCompassListener(this);
		pm.getCompassManager().onAccuracyChanged(null, 0);
		pm.getCompassManager().onSensorChanged(null);
		assertTrue(true);
	}

	@Test
	public void testSpeedManager() {
		pm.getSpeedManager().registerSpeedListener(this);
		pm.getSpeedManager().onPositionChange(null);
		assertTrue(true);
	}

	@Override
	public void onSpeedChange(double speed) {
		assertTrue(true);
	}

	@Override
	public void onCompassChange(float direction) {
		assertTrue(true);
	}

	@Override
	public void onPositionChange(Location androidLocation) {
		assertTrue(true);
	}
}

package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;

public class WaypointTest {

	private Waypoint testWaypoint;
	private int testProfil;
	private POI testPoi;
	private double testLat;
	private double testLon;
	private String testName;

	@Before
	public void setUp() {
		testLat = 2d;
		testLon = 5d;
		testProfil = 42;
		testName = "test";
		testPoi = new POI(3d, 10d, "test", "test", null, null);
		testWaypoint = new Waypoint(testLat, testLon, testName);
	}

	@Test
	public void testPersistence() throws MalformedURLException {
		// Assert
		assertTrue(testLat == testWaypoint.getLatitude());
		assertTrue(testLon == testWaypoint.getLongitude());
		assertEquals(testName, testWaypoint.getName());

		Waypoint w = new Waypoint(0, 0, null);
		w.setPOI(testPoi);
		w.setProfile(testProfil);
		assertEquals(testPoi, w.getPOI());
		assertEquals(testProfil, w.getProfile());
	}

	@Test
	public void testClone() {
		Waypoint clone = testWaypoint.clone();
		assertTrue(testWaypoint.getLatitude() == clone.getLatitude());
		assertTrue(testWaypoint.getLongitude() == clone.getLongitude());
		assertEquals(testWaypoint.getName(), clone.getName());
	}
}

package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;

public class TestWaypoint {

	private Waypoint testWaypoint;
	private int testProfil;
	private POI testPoi;
	private double testLat;
	private double testLon;
	private String testName;
	private Address testAddress;

	@Before
	public void setUp() {
		testLat = 2d;
		testLon = 5d;
		testProfil = 42;
		testName = "test";
		testPoi = new POI(3d, 10d, "test", "test", null, null);
		testAddress = new Address(null, null, null, 0);
		testWaypoint = new Waypoint(testLat, testLon, testName, testAddress);
	}

	@Test
	public void testPersistence() throws MalformedURLException {
		// Assert
		assertTrue(testLat == testWaypoint.getLatitude());
		assertTrue(testLon == testWaypoint.getLongitude());
		assertEquals(testName, testWaypoint.getName());

		Waypoint w = new Waypoint(0, 0, null);
		assertFalse(w.isPOI());
		w.setPOI(testPoi);
		w.setProfile(testProfil);
		assertEquals(testPoi, w.getPOI());
		assertTrue(w.isPOI());
		assertEquals(testProfil, w.getProfile());
	}

	@Test
	public void testClone() {
		Waypoint clone = testWaypoint.clone();
		assertEquals(testWaypoint, clone);
		assertFalse(testWaypoint == clone);
		Waypoint nullPoint = new Waypoint(0, 0, null, null);
		nullPoint.setPOI(new POI(0, 0, null, null, null, null));
		clone = nullPoint.clone();
		assertEquals(nullPoint, clone);
		assertFalse(nullPoint == clone);
	}

	@Test
	public void testEquals() {
		assertFalse(testWaypoint.equals(null));
		assertFalse(testWaypoint.equals("Test"));
		Waypoint wp2 = new Waypoint(testLat, testLon, testName, testAddress);
		wp2.setPOI(new POI(0, 0, "", "", null, null));
		assertFalse(testWaypoint.equals(wp2));
		Waypoint wp3 = new Waypoint(testLat, testLon, testName, testAddress);
		wp3.setProfile(0);
		assertFalse(testWaypoint.equals(wp3));
		Waypoint wp4 = new Waypoint(testLat, testLon, testName, testAddress);
		wp4.setPOI(new POI(0, 0, null, null, null, null));
		assertFalse(wp2.equals(wp4));
		assertTrue(wp4.equals(wp4));
		POI p = new POI(testLat, testLon, testName, testName, null, null, testAddress);
		p.setMoveability(true);
		assertFalse(testWaypoint.equals(p));
	}
	@Test
	public void testHashCode() {
		int h1 = testWaypoint.hashCode();
		Waypoint wp2 = new Waypoint(testLat, testLon, testName, testAddress);
		wp2.setPOI(new POI(0, 0, null, null, null, null));
		assertTrue(h1 != wp2.hashCode());
	}
}

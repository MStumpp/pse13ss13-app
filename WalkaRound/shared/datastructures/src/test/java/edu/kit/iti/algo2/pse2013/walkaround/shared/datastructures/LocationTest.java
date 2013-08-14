package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LocationTest {

	private Location testLocation;
	private double testLat;
	private double testLon;
	private String testName;
	private Address testAddress;

	@Before
	public void setUp() {
		testLat = 2d;
		testLon = 5d;
		testName = "Test-name";
		testAddress = new Address("Test-street", "Test-city", "Test-number",
				54321);
		testLocation = new Location(testLat, testLon, testName, testAddress);
	}

	@Test
	public void testClone() {
		Location clone = testLocation.clone();
		Location clone2 = clone.clone();
		assertNotEquals(testLocation, clone);
		assertNotEquals(clone, clone2);
		assertNotEquals(clone2, testLocation);

		assertTrue(testLocation.getLatitude() == clone.getLatitude());
		assertTrue(testLocation.getLongitude() == clone.getLongitude());
		assertEquals(testLocation.getName(), clone.getName());
		assertTrue(testLocation.getId() != clone.getId());
		assertTrue(testLocation.getAddress() != clone.getAddress());
	}

	@Test
	public void testHash() {
		int hashA = testLocation.hashCode();
		int hashB = testLocation.clone().hashCode();
		int hashC = new Location(testLat, testLon, testName, testAddress)
				.hashCode();
		assertNotEquals(hashA, hashB);
		assertNotEquals(hashB, hashC);
		assertNotEquals(hashA, hashC);
	}

	@Test
	public void testEquals() throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		assertFalse(testLocation.equals(null));
		assertFalse(testLocation.equals("Hello world"));
		Location locationA = new Location(2d, 2d, "test", testAddress);
		Location locationB = new Location(2d, 2d, "test", testAddress);
		assertFalse(locationA.equals(locationB));

		assertTrue(testLocation.equals(testLocation));
	}
}

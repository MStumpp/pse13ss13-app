package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestLocation {

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
    @Ignore
	public void testClone() {
		Location clone = testLocation.clone();
		Location clone2 = clone.clone();
		assertTrue(testLocation != clone);
		assertTrue(clone !=clone2);
		assertTrue(clone2 != testLocation);

		assertEquals(testLocation, clone);
		assertEquals(clone2, clone);
		assertEquals(testLocation, clone2);
	}

	@Test
	public void testHash() {
		int hashA = testLocation.hashCode();
		int hashB = testLocation.clone().hashCode();
		int hashC = new Location(testLat + 1, testLon, testName, testAddress).hashCode();
		assertEquals(hashA, hashB);
		assertNotEquals(hashB, hashC);
		assertNotEquals(hashA, hashC);
	}

	@Test
	public void testEquals() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		assertFalse(testLocation.equals(null));
		assertFalse(testLocation.equals("Hello world"));
		Location locationA = new Location(2d, 2d, "test", testAddress);
		Location locationB = new Location(2d, 2d, "test", testAddress);
		assertTrue(locationA.equals(locationB));
		assertFalse(locationA == locationB);

		assertTrue(testLocation.equals(testLocation));
	}
}

package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AddressTest {

	private Address testAddress;
	private String testStreet;
	private String testCity;
	private String testNumber;
	private int testPostalCode;

	@Before
	public void setUp() {
		testStreet = "Test-street";
		testCity = "Test-city";
		testNumber = "Test-number";
		testPostalCode = 12345;
		testAddress = new Address(testStreet, testNumber, testCity,
				testPostalCode);
	}

	@Test
	public void testToString() {
		assertTrue(testAddress.toString().contains(testStreet)
				&& testAddress.toString().contains(testCity)
				&& testAddress.toString().contains(testNumber)
				&& testAddress.toString().contains("" + testPostalCode));
	}

	@Test
	public void testClone() {
		Address clone = testAddress.clone();
		assertEquals(testAddress.getCity(), clone.getCity());
		assertEquals(testAddress.getStreet(), clone.getStreet());
		assertEquals(testAddress.getPostalCode(), clone.getPostalCode());
		assertEquals(testAddress.getHouseNumber(), clone.getHouseNumber());
	}

	@Test
	public void testHash() {
		int hashA = testAddress.hashCode();
		int hashB = testAddress.clone().hashCode();
		int hashC = new Address(testStreet, testNumber, testCity,
				12345).hashCode();
		assertEquals(hashA, hashB);
		assertEquals(hashB, hashC);
		assertEquals(hashA, hashC);
	}

	@Test
	public void testEquals() throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		assertFalse(testAddress.equals(null));
		assertFalse(testAddress.equals("Hello world"));

		assertTrue(testAddress.equals(testAddress));
	}
}

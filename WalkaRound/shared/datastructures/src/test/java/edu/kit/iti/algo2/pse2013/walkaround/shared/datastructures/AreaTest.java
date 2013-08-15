package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * This class test the class Area.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class AreaTest {

	private Area testArea;

	private int[] testCats;

	private ArrayList<Coordinate> testCoordinateListA;

	private ArrayList<Coordinate> testCoordinateListB;

	private ArrayList<Coordinate> testCoordinateListC;

	private ArrayList<Coordinate> testCoordinateListTooShort;

	@Before
	public void setUp() {
		testCoordinateListA = new ArrayList<Coordinate>();
		testCoordinateListA.add(new Coordinate(1, 2));
		testCoordinateListA.add(new Coordinate(3, 4));
		testCoordinateListA.add(new Coordinate(5, 6));

		testCoordinateListB = new ArrayList<Coordinate>();
		testCoordinateListB.add(new Coordinate(13, 12));
		testCoordinateListB.add(new Coordinate(11, 10));
		testCoordinateListB.add(new Coordinate(9, 8));
		testCoordinateListB.add(new Coordinate(56, 56));
		testCoordinateListB.add(new Coordinate(72, 23));
		testCoordinateListB.add(new Coordinate(65, 41));

		testCoordinateListC = new ArrayList<Coordinate>();
		testCoordinateListC.add(new Coordinate(-15, 15));
		testCoordinateListC.add(new Coordinate(-20, 20));
		testCoordinateListC.add(new Coordinate(-37, 37));
		testCoordinateListC.add(new Coordinate(-42, 65));

		testCoordinateListTooShort = new ArrayList<Coordinate>();
		testCoordinateListTooShort.add(new Coordinate(1, 1));
		testCoordinateListTooShort.add(new Coordinate(20, 40));

		testCats = new int[] { 1, 2, 3 };
		testArea = new Area(testCats, testCoordinateListA);
	}

	@Test
	public void testValidConstructionsOfAreas() {
		new Area(null, new ArrayList<Coordinate>(testCoordinateListA));
		new Area(new int[] { 1, 2, 3, 10 }, new ArrayList<Coordinate>(
				testCoordinateListB));
		new Area(new int[] { 1, 2, 3, 12, 4 }, new ArrayList<Coordinate>(
				testCoordinateListC));
	}

	@Test
	public void testPersistence() throws MalformedURLException {
		// Assert
		assertEquals(testCats, testArea.getAreaCategories());
		assertEquals(testCoordinateListA, testArea.getAreaCoordinates());

		Area a = new Area(testCats, testCoordinateListB);
		int[] testCatsOther = new int[] { 123, 321, 432 };
		a.setAreaCategories(testCatsOther);

		assertEquals(testCatsOther, a.getAreaCategories());
	}

	@Test
	public void testHash() {
		int hashA = testArea.hashCode();
		int hashC = new Area(new int[] { 42 }, testCoordinateListC).hashCode();

		assertNotEquals(hashA, hashC);
	}

	@Test
	public void testEquals() throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		assertFalse(testArea.equals(null));
		assertFalse(testArea.equals("Hello world"));

		assertTrue(testArea.equals(testArea));
	}
}

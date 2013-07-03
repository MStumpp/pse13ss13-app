package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * This class test the class Area.
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public class AreaTest {

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
	public void testInvalidConstructionsOfAreas() {

	}
}

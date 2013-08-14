package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DisplayCoordinateTest {

	private DisplayCoordinate testCoord;
	private float testX;
	private float testY;

	@Before
	public void setUp() {
		testX = 2f;
		testY = 5f;
		testCoord = new DisplayCoordinate(testX, testY);
		testCoord.setX(testX);
		testCoord.setY(testY);
	}

	@Test
	public void testToString() {
		assertTrue(testCoord.toString().contains("" + testCoord.getX())
				&& testCoord.toString().contains("" + testCoord.getY()));
	}
}

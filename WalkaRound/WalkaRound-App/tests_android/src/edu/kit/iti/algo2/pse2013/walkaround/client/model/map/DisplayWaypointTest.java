package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DisplayWaypointTest {

	@Test
	public void testPersistence() {
		DisplayWaypoint dw = new DisplayWaypoint(2f, 5f, 42);
		assertTrue(dw.getX() == 2f);
		assertTrue(dw.getY() == 5f);
		assertTrue(dw.getId() == 42);
	}
}

package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

@RunWith(RobolectricTestRunner.class)
public class RouteTest {

	private Route route;

	@Before
	public void setUp() {
		route = new Route(new LinkedList<Coordinate>());
	}

	@Test
	public void testPersistence() {
		Waypoint wp = new Waypoint(2d, 5d, "test");
		route.addWaypoint(wp);
		assertTrue(route.containsWaypoint(wp));

		assertEquals(wp, route.getStart());
		assertEquals(wp, route.getEnd());

		assertNotNull(route.getWaypoints());
		assertNotNull(route.getCoordinates());
	}

	@Test
	public void testAddingTwoWaypoints() {
		route.addWaypoint(new Waypoint(2d, 5d, "test"));
		route.addWaypoint(new Waypoint(3d, 4d, "test2"));
		assertTrue(route.getWaypoints().size() == 2);
	}

	@Test
	public void testAddingThreeWaypoints() {
		route.addWaypoint(new Waypoint(2d, 5d, "test"));
		route.addWaypoint(new Waypoint(3d, 4d, "test2"));
		route.addWaypoint(new Waypoint(4d, 6d, "test3"));
		assertTrue(route.getWaypoints().size() == 3);
	}

	@Test
	public void testdeleteWaypoints() {
		route.addWaypoint(new Waypoint(2d, 5d, "test"));
		Waypoint second = new Waypoint(3d, 4d, "test2");
		route.addWaypoint(second);
		route.addWaypoint(new Waypoint(4d, 6d, "test3"));
		assertTrue(route.getWaypoints().size() == 3);
		route.setActiveWaypoint(second.getId());
		route.deleteActiveWaypoint();
		assertTrue(route.getWaypoints().size() == 2);
	}

	@Test
	public void testMoveWaypoint() {
		route.addWaypoint(new Waypoint(2d, 5d, "test"));
		Waypoint second = new Waypoint(3d, 4d, "test2");
		route.addWaypoint(second);
		route.addWaypoint(new Waypoint(4d, 6d, "test3"));
		route.setActiveWaypoint(second.getId());
		route.moveActiveWaypoint(new Coordinate(10d, 20d));
		assertTrue(route.getActiveWaypoint().getLatitude() == 10d);
		assertTrue(route.getActiveWaypoint().getLongitude() == 20d);
	}

	@Test
	public void testResetRoute() {
		route.addWaypoint(new Waypoint(2d, 5d, "test"));
		route.addWaypoint(new Waypoint(3d, 4d, "test2"));
		route.addWaypoint(new Waypoint(4d, 6d, "test3"));
		assertNotNull(route.getWaypoints());

		route.resetRoute();
		assertTrue(route.getWaypoints().size() == 0);
	}

	@Test
	public void testInvertRoute() {
		route.addWaypoint(new Waypoint(2d, 5d, "test"));
		route.addWaypoint(new Waypoint(3d, 4d, "test2"));
		route.addWaypoint(new Waypoint(4d, 6d, "test3"));
		assertEquals("test", route.getWaypoints().get(0).getName());
		assertEquals("test2", route.getWaypoints().get(1).getName());
		assertEquals("test3", route.getWaypoints().get(2).getName());
		route.invertRoute();
		assertEquals("test3", route.getWaypoints().get(0).getName());
		assertEquals("test2", route.getWaypoints().get(1).getName());
		assertEquals("test", route.getWaypoints().get(2).getName());
	}
}

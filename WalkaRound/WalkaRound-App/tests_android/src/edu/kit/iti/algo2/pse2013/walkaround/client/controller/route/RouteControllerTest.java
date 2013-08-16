package edu.kit.iti.algo2.pse2013.walkaround.client.controller.route;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

@RunWith(RobolectricTestRunner.class)
public class RouteControllerTest {

	private RouteController routeCont;

	@Before
	public void setUp() {
		routeCont = RouteController.getInstance();
	}

	@Ignore
	public void testAddFavorites() {
		routeCont.addLocationToFavorites(new Location(2d, 5d, "test"), "test");
		routeCont.addRouteToFavorites("testRoute");

	}

	@Test
	public void testWaypoints() {
		Waypoint wp = new Waypoint(2d, 5d, "test");
		routeCont.addWaypoint(wp);
		assertTrue(routeCont.containsWaypoint(wp));
		assertTrue(routeCont.moveActiveWaypoint(new Coordinate(4d, 8d)));
		routeCont.deleteActiveWaypoint();
		assertFalse(routeCont.containsWaypoint(wp));
	}
}

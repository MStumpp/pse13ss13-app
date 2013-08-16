package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.client.BootActivity;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.POIMenuControllerTest;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

@RunWith(RobolectricTestRunner.class)
public class RouteGenTest {

	@Before
	public void setUp() {
		POIMenuControllerTest.boot();
	}

	@Test
	public void updateRoute() {
		LinkedList<Coordinate> link = new LinkedList<Coordinate>();
		link.add(new Coordinate(0,0));
		link.add(new Coordinate(20,20));
		MapController.getInstance().onRouteChange((RouteInfo) new Route(link));
		assertTrue(true);
	}

	@After
	public void tearDown() {
		Robolectric.buildActivity(BootActivity.class).get().finish();
	}

}

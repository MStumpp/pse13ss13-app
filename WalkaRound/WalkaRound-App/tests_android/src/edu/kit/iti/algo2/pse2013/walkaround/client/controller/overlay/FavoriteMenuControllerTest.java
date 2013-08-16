package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.FavoriteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

@RunWith(RobolectricTestRunner.class)
public class FavoriteMenuControllerTest {

	/**
	 * Nicht getestet, da der Android-interne Speicher benötigt wird
	 */
	@Ignore
	public void testGeneral() {
		RouteController.getInstance().addWaypoint(new Waypoint(48, 8, "1"));
		RouteController.getInstance().addWaypoint(new Waypoint(49, 9, "2"));
		RouteController.getInstance().addRouteToFavorites("testRoute");
		RouteController.getInstance().addLocationToFavorites(new Location(49d, 8d, "testLoc"), "testLoc");
		Assert.assertEquals(FavoriteMenuController.getInstance()
				.getNamesOfFavoriteLocations(), FavoriteManager.getInstance()
				.getNamesOfFavoriteLocations());
		Assert.assertEquals(FavoriteManager.getInstance().getNamesOfFavoriteRoutes()
				.get(0), FavoriteMenuController.getInstance()
				.getNamesOfFavoriteRoutes().get(0));
		Assert.assertTrue(FavoriteMenuController.getInstance().deleteRoute("testRoute"));
		Assert.assertTrue(FavoriteMenuController.getInstance().deleteLocation("testLoc"));
	}

	@Test
	public void testNothing() {
		assertTrue(true);
	}
}

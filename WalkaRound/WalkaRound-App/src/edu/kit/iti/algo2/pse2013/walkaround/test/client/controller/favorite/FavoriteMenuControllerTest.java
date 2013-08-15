package edu.kit.iti.algo2.pse2013.walkaround.test.client.controller.favorite;

import java.util.LinkedList;

import android.test.AndroidTestCase;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.FavoriteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

public class FavoriteMenuControllerTest extends AndroidTestCase {

	public void testGeneral() {
		RouteController.getInstance().addRouteToFavorites("testRoute");
		RouteController.getInstance().addLocationToFavorites(
				new Location(49d, 8d, "testLoc"), "testLoc");
		assertEquals(FavoriteMenuController.getInstance()
				.getNamesOfFavoriteLocations(), FavoriteManager.getInstance()
				.getNamesOfFavoriteLocations());
		assertEquals(FavoriteManager.getInstance().getNamesOfFavoriteRoutes()
				.get(0), FavoriteMenuController.getInstance()
				.getNamesOfFavoriteRoutes().get(0));
		assertTrue(FavoriteMenuController.getInstance()
				.deleteRoute("testRoute"));
		assertTrue(FavoriteMenuController.getInstance().deleteLocation(
				"testLoc"));
	}
}

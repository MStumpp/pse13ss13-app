package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import android.test.AndroidTestCase;

public class FavoriteManagerTest extends AndroidTestCase {

	public void testPersistence() {
		RouteInfo testRoute = new Route(new LinkedList<Coordinate>());
		Location testLoc = new Location(2d, 5d, "abc");
		FavoriteManager.getInstance()
				.addLocationToFavorites(testLoc, "testLoc");
		FavoriteManager.getInstance().addRouteToFavorites(testRoute,
				"testRoute");
		assertTrue(FavoriteManager.getInstance().containsName("testLoc"));
		assertTrue(FavoriteManager.getInstance().containsName("testRoute"));
		assertFalse(FavoriteManager.getInstance().containsName("xxx"));

		assertTrue(1 == FavoriteManager.getInstance()
				.getNamesOfFavoriteLocations().size());
		assertTrue(1 == FavoriteManager.getInstance()
				.getNamesOfFavoriteRoutes().size());

		assertEquals(testLoc, FavoriteManager.getInstance()
				.getFavoriteLocation("tesLoc"));
		assertEquals(testRoute, FavoriteManager.getInstance()
				.getFavoriteLocation("tesRoute"));

		FavoriteManager.getInstance().deleteLocation("testLoc");
		FavoriteManager.getInstance().deleteLocation("testRoute");
		assertFalse(FavoriteManager.getInstance().containsName("testLoc"));
		assertFalse(FavoriteManager.getInstance().containsName("testRoute"));

		FavoriteManager.getInstance().getNamesOfFavoriteLocations();
	}
}

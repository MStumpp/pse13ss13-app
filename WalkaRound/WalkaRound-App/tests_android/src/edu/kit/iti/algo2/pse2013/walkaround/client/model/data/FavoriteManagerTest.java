package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

@RunWith(RobolectricTestRunner.class)
public class FavoriteManagerTest {

	@Test
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

package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.client.BootActivity;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

@RunWith(RobolectricTestRunner.class)
public class POIManagerTest  {

	private POIManager poiManag = POIManager.getInstance();

	@Before
	public void reset() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field loc = POIManager.class.getDeclaredField("locationDataIO");
		loc.setAccessible(true);
		loc.set(null, null);
		Field ins = POIManager.class.getDeclaredField("instance");
		ins.setAccessible(true);
		ins.set(null, null);
		POIManager.initialize(new BootActivity().getApplicationContext());
		poiManag = POIManager.getInstance();
	}

	@Test
	public void testPersistence() {
		assertTrue(poiManag.isEmpty());
		poiManag.addActivePOICategory(1);
		assertFalse(poiManag.isEmpty());
		poiManag.removeActivePOICategory(1);
		assertTrue(poiManag.isEmpty());
	}

	@Test
	public void testSearchByQuery() {
		List<POI> suggestions = poiManag.searchPOIsByQuery("Schloss Karlsruhe");
		assertEquals("Schloss Karlsruhe", suggestions.get(0).getName());
	}

	@Test
	public void testSearchByAddress() {
		Address testAddress = new Address("Kaiserstraße", "12", "Karlsruhe",
				76133);
		List<Location> suggestions = poiManag.searchPOIsByAddress(testAddress);
		assertTrue(suggestions.get(0).getAddress().toString()
				.contains("Kaiserstraße")
				&& suggestions.get(0).getAddress().toString().contains("12")
				&& suggestions.get(0).getAddress().toString()
						.contains("Karlsruhe")
				&& suggestions.get(0).getAddress().toString()
						.contains("" + 76133));
	}

	@Test
	public void testPoisInaRectangle() {
		poiManag.addActivePOICategory(1);
		poiManag.addActivePOICategory(2);
		poiManag.addActivePOICategory(3);
		poiManag.addActivePOICategory(4);
		poiManag.addActivePOICategory(5);
		List<POI> pois = poiManag.getPOIsWithinRectangle(new Coordinate(
				49.00912d, 8.39872d), new Coordinate(49.00179d, 8.41477d), 16f);
		assertTrue(pois.isEmpty());
	}

	@Test
	public void testPoisInAlongRoute() {
		LinkedList<Coordinate> route = new LinkedList<Coordinate>();
		route.add(new Coordinate(49.00179d, 8.41477d));
		route.add(new Coordinate(49.00579d, 8.21477d));
		route.add(new Coordinate(49.00179d, 8.41477d));
		route.add(new Coordinate(49.00279d, 8.44477d));
		route.add(new Coordinate(49.00129d, 8.41299d));
		List<POI> pois = poiManag.getPOIsAlongRoute(new Route(route), 16f);
		assertFalse(pois.isEmpty());
	}
}

package edu.kit.iti.algo2.pse2013.walkaround.test.client.model.data;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import android.test.AndroidTestCase;

public class POIManagerTest extends AndroidTestCase {

	private POIManager poiManag;

	@Before
	public void setUp() {
		poiManag = POIManager.getInstance();
	}

	public void testPersistence() {
		assertTrue(poiManag.isEmpty());
		poiManag.addActivePOICategory(1);
		assertFalse(poiManag.isEmpty());
		poiManag.removeActivePOICategory(1);
		assertTrue(poiManag.isEmpty());
	}

	public void testSearchByQuery() {
		List<POI> suggestions = poiManag.searchPOIsByQuery("Schloss Karlsruhe");
		assertEquals("Schloss Karlsruhe", suggestions.get(0).getName());
	}

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

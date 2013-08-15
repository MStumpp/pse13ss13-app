package edu.kit.iti.algo2.pse2013.walkaround.client.controller.search;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.SearchMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;

@RunWith(RobolectricTestRunner.class)
public class SearchMenuControllerTest {

	@Test
	public void testRequestByAddress() {
		Address address = new Address("12", "Karlsruhe", "Kaiserstraße", 76133);
		assertEquals(
				POIManager.getInstance().searchPOIsByAddress(address),
				SearchMenuController.getInstance().requestSuggestionsByAddress(
						76133, "Karlsruhe", "Kaiserstraße", "12"));
	}

	@Test
	public void testRequestByQuery() {
		assertEquals(
				POIManager.getInstance().searchPOIsByQuery("Schloss Karlsruhe"),
				SearchMenuController.getInstance().requestSuggestionsByText(
						"Schloss Karlsruhe"));
	}
}

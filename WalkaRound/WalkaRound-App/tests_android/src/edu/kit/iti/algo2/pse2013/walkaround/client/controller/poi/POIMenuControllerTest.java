package edu.kit.iti.algo2.pse2013.walkaround.client.controller.poi;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.POIMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;

public class POIMenuControllerTest {

	@Test
	public void testAddRemoveCategory() {
		POIMenuController.getInstance().addActiveCategory(1);
		assertFalse(POIManager.getInstance().isEmpty());
		POIMenuController.getInstance().removeActiveCategory(1);
		assertTrue(POIManager.getInstance().isEmpty());
	}
}

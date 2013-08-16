package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.client.BootActivity;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.POIMenuControllerTest;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

@RunWith(RobolectricTestRunner.class)
public class POIGenTest {
	protected Coordinate fixture1 = new Coordinate(48, 8);

	@Before
	public void setUp() {
		POIMenuControllerTest.boot();
	}

	@Test
	public void addPOItoView() {
		MapController.getInstance().updatePOIView();
		MapController.getInstance().addPoiToView();
		assertTrue(true);
	}
	
	@After
	public void tearDown() {
		Robolectric.buildActivity(BootActivity.class).get().finish();
	}
}

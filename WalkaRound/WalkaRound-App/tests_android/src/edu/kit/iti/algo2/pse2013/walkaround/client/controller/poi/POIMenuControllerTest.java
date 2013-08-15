package edu.kit.iti.algo2.pse2013.walkaround.client.controller.poi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.graphics.Point;
import edu.kit.iti.algo2.pse2013.walkaround.client.BootActivity;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.POIMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

@RunWith(RobolectricTestRunner.class)
public class POIMenuControllerTest {

	@Before
	public void setUp() {
		System.out.println("setUp started");
		BootActivity activity = Robolectric.buildActivity(BootActivity.class).get();
		System.out.println("BootActivity finished!");
		MapController.initialize(new TileFetcher(), new BoundingBox(new Coordinate(0, 0), new Point(1920, 1080), 16), new Coordinate(0, 0));
		System.out.println("MapController finished!");
		PositionManager.initialize(activity.getApplicationContext());
		System.out.println("PositionManager finished!");
		MapView mv = Robolectric.buildActivity(MapView.class).create().get();
		MapController.getInstance().startController(mv);
		System.out.println("SetUp finished!");
	}

	@Test
	public void testAddRemoveCategory() {
		POIMenuController.getInstance().addActiveCategory(1);
		assertFalse(POIManager.getInstance().isEmpty());
		POIMenuController.getInstance().removeActiveCategory(1);
		assertTrue(POIManager.getInstance().isEmpty());
	}
}
package edu.kit.iti.algo2.pse2013.walkaround.client.controller.poi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.content.Intent;
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
	private static boolean alreadyBooted = false;

	@Before
	public void setUp() {
		BootActivity activity = Robolectric.buildActivity(BootActivity.class).get();
		MapController.initialize(new TileFetcher(), new BoundingBox(new Coordinate(0, 0), new Point(1920, 1080), 16), new Coordinate(0, 0));
		Intent in = new Intent(activity, MapView.class);
		activity.startActivity(in);
		MapController.getInstance().startController(Robolectric.buildActivity(MapView.class).create().get());
		PositionManager.initialize(activity.getApplicationContext());
		boot();
	}

	@Test
	public void testAddRemoveCategory() {
		POIMenuController.getInstance().addActiveCategory(1);
		assertFalse(POIManager.getInstance().isEmpty());
		POIMenuController.getInstance().removeActiveCategory(1);
		assertTrue(POIManager.getInstance().isEmpty());
	}

	public static void boot() {
		if (!alreadyBooted) {
			alreadyBooted = true;
			Coordinate center = new Coordinate(48, 8);
			TileFetcher tf = new TileFetcher();
			Point size = new Point(1920, 1080);
			BoundingBox bb = new BoundingBox(center, size, 18);
			MapController.initialize(tf, bb, center);
			PositionManager.initialize(Robolectric.buildActivity(BootActivity.class).get().getApplicationContext());
		}
	}
}
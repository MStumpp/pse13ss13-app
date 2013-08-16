package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.graphics.Point;
import edu.kit.iti.algo2.pse2013.walkaround.client.BootActivity;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.POIMenuControllerTest;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator.MapGen;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;


@RunWith(RobolectricTestRunner.class)
public class MapGenTest {

	protected Coordinate fixture1 = new Coordinate(48, 8);
	private MapGen model;

	@Before
	public void setUp() {
		POIMenuControllerTest.boot();
		model = new MapGen(new Point(1920, 1080), new BoundingBox(new Coordinate(0, 0), new Point(1920, 1080), 16));
	}

	@Test
	public void testZoom() {
		fixture1 = MapController.getInstance().getCenter();
		MapController.getInstance().onZoom(2.5f);
		Assert.assertTrue(MapController.getInstance().getCenter().equals(fixture1));
//TODO: Einmal zoomen klappt, beim mehrfachen zoomen kommt eine NullPointerException
		//MapController.getInstance().onZoom(-1f);
		//assertTrue(MapController.getInstance().getCenter().equals(fixture1));
//		model.zoom(-3f);
//		assertTrue(model.getCenter().equals(fixture1));
//		model.zoom(1f);
//		assertTrue(model.getCenter().equals(fixture1));
	}
	@Test
	public void testShift() {
		MapController.getInstance().onShift(10, 10);
		Assert.assertTrue(!MapController.getInstance().getCenter().equals(fixture1));

	}

	
	@After
	public void tearDown() {
		Robolectric.buildActivity(BootActivity.class).get().finish();
	}
}

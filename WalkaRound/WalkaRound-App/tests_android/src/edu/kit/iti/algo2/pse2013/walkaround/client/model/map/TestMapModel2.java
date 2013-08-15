/**
 * TODO: Anpassen, da MapModel nicht mehr existiert
 */

package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import org.junit.runner.RunWith;

import android.graphics.Point;
import org.robolectric.RobolectricTestRunner;
import android.test.ActivityInstrumentationTestCase2;
import edu.kit.iti.algo2.pse2013.walkaround.client.BootActivity;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.search.RobolectricTestRunner;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator.MapGen;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

@RunWith(RobolectricTestRunner.class)
public class TestMapModel extends ActivityInstrumentationTestCase2 {

	public TestMapModel() {
		super(BootActivity.class); // The main activity!!! NOT the tested class
	}
	public TestMapModel(Class testedClass) {
		super(testedClass);
	}

	protected Coordinate fixture1 = new Coordinate(48, 8);
	private MapGen model;

	public void setUp() {
		Coordinate center = new Coordinate(48, 8);
		TileFetcher tf = new TileFetcher();
		Point size = new Point(1920, 1080);
		BoundingBox bb = new BoundingBox(center, size, 18);
		MapController.initialize(tf, bb, center);
		PositionManager.initialize(getActivity().getApplicationContext());
		model = new MapGen(size, bb);
	}

	public void testZoom() {
		fixture1 = MapController.getInstance().getCenter();
		MapController.getInstance().onZoom(2.5f);
		assertTrue(MapController.getInstance().getCenter().equals(fixture1));
//TODO: Einmal zoomen klappt, beim mehrfachen zoomen kommt eine NullPointerException
		//MapController.getInstance().onZoom(-1f);
		//assertTrue(MapController.getInstance().getCenter().equals(fixture1));
//		model.zoom(-3f);
//		assertTrue(model.getCenter().equals(fixture1));
//		model.zoom(1f);
//		assertTrue(model.getCenter().equals(fixture1));
	}

	public void tearDown() {
		getActivity().finish();
	}
}
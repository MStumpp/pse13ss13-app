/**
 * TODO: Anpassen, da MapModel nicht mehr existiert
 */

package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator.MapGen;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

public class TestMapModel extends ActivityInstrumentationTestCase2 {
	
	public TestMapModel() {
		super(MapView.class); // The main activity!!! NOT the tested class
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
		MapController.initialize(tf, size, bb, center);
		MapController mc = MapController.getInstance();
		model = new MapGen(size, bb);
	}

	public void testZoom() {
		fixture1 = MapController.getInstance().getCenter();
		MapController.getInstance().onZoom(2.5f);
		assertTrue(MapController.getInstance().getCenter().equals(fixture1));
//TODO: Einmal zoomen klappt, beim mehrfachen zoomen kommt eine CalledFromWrongThreadException: "Only the original thread that created a view hierarchy can touch its views."
//		model.zoom(-1f);
//		assertTrue(model.getCenter().equals(fixture1));
//		model.zoom(-3f);
//		assertTrue(model.getCenter().equals(fixture1));
//		model.zoom(1f);
//		assertTrue(model.getCenter().equals(fixture1));
	}

	public void tearDown() {
		getActivity().finish();
	}
}
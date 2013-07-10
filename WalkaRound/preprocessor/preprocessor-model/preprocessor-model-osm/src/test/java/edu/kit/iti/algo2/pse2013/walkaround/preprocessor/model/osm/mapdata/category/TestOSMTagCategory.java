package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMWay;

public class TestOSMTagCategory {
	@Test
	public void simpleTest() {
		OSMWay way = new OSMWay(1);
		way.addTag("highway", "footway");
		way.addTag("highway", "track");
		way.addTag("sidewalk", "left");
		OSMTagCategory test = new OSMTagCategory();
		test.addTag("highway", "footway");
		test.addTag("sidewalk", "left");
		assertTrue(test.accepts(way));
	}
}

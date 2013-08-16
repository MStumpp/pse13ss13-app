package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMNode;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMWay;

public class TestOSMTagCategory {
	@Test
	public void simpleTest() {
		OSMWay way = new OSMWay(1);
		way.addTag("highway", "footway");
		way.addTag("highway", "track");
		way.addTag("sidewalk", "left");
		assertTrue(OSMCategoryFactory.createFootwayCategory().accepts(way));
	}

	@Test
	public void testInverted() {
		OSMTagCategory cat = new OSMTagCategory(true);
		cat.addTag("abc", "def");
		cat.addTag("ghi", "jkl");
		OSMNode node = new OSMNode(0);
		node.addTag("mno", "pqr");
		assertTrue(cat.accepts(node));
		node.addTag("abc", "def");
		assertFalse(cat.accepts(node));
	}

	@Test
	public void testDecorated() {
		OSMTagCategory cat2 = new OSMTagCategory();
		cat2.addTag("ghi", "jkl");
		OSMTagCategory cat = new OSMTagCategory();
		cat.addTag("abc", "def");
		cat.decorated = cat2;
		OSMNode node = new OSMNode(0);
		node.addTag("ghi", "jkl");
		assertTrue(cat.accepts(node));
	}
}

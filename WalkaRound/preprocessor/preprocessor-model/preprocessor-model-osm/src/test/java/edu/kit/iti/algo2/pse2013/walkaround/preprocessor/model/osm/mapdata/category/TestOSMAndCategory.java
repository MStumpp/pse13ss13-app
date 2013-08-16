package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMNode;

public class TestOSMAndCategory {

	@Test
	public void testOr() {
		OSMTagCategory cat1 = new OSMTagCategory();
		cat1.addTag("a", "a");
		cat1.addTag("b", "b");
		OSMTagCategory cat2 = new OSMTagCategory();
		cat2.addTag("a", "a");
		cat2.addTag("c", "c");
		OSMAndCategory and = new OSMAndCategory(cat1, cat2);

		OSMNode n1 = new OSMNode(0);
			n1.addTag("a", "a");
		OSMNode n2 = new OSMNode(0);
			n2.addTag("b", "b");
		OSMNode n3 = new OSMNode(0);
			n3.addTag("c", "c");
		OSMNode n4 = new OSMNode(0);
			n4.addTag("d", "d");

		assertEquals(cat1.accepts(n1) && cat2.accepts(n1), and.accepts(n1));
		assertEquals(cat1.accepts(n2) && cat2.accepts(n2), and.accepts(n2));
		assertEquals(cat1.accepts(n3) && cat2.accepts(n3), and.accepts(n3));
		assertEquals(cat1.accepts(n4) && cat2.accepts(n4), and.accepts(n4));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNull() {
		OSMAndCategory and = new OSMAndCategory(null, null);
	}

}

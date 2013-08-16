package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMNode;

public class TestOSMOrCategory {

	@Test
	public void testOr() {
		OSMTagCategory cat1 = new OSMTagCategory();
		cat1.addTag("a", "a");
		cat1.addTag("b", "b");
		OSMTagCategory cat2 = new OSMTagCategory();
		cat2.addTag("a", "a");
		cat2.addTag("c", "c");
		OSMOrCategory or = new OSMOrCategory(cat1, cat2);

		OSMNode n1 = new OSMNode(0);
			n1.addTag("a", "a");
		OSMNode n2 = new OSMNode(0);
			n2.addTag("b", "b");
		OSMNode n3 = new OSMNode(0);
			n3.addTag("c", "c");
		OSMNode n4 = new OSMNode(0);
			n4.addTag("d", "d");

		assertEquals(cat1.accepts(n1) || cat2.accepts(n1), or.accepts(n1));
		assertEquals(cat1.accepts(n2) || cat2.accepts(n2), or.accepts(n2));
		assertEquals(cat1.accepts(n3) || cat2.accepts(n3), or.accepts(n3));
		assertEquals(cat1.accepts(n4) || cat2.accepts(n4), or.accepts(n4));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNull() {
		OSMOrCategory or = new OSMOrCategory(null, null);
	}

}

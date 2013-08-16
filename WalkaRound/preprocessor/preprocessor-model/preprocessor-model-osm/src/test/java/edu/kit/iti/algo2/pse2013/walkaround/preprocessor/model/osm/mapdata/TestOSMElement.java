package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestOSMElement {
	@Test
	public void testID() {
		OSMNode node = new OSMNode(5);
		assertEquals(5, node.getID());
	}

	@Test
	public void testHashCode() {
		OSMNode n1 = new OSMNode(1);
		OSMNode n2 = new OSMNode(2);
		assertTrue(n1.hashCode() != n2.hashCode());
	}
}

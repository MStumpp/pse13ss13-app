package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestEdge {
	@Test(expected=IllegalArgumentException.class)
	public void testNullInit() {
		new Edge(null, null);
	}
	@Test
	public void testEdgeInit() {
		Edge e = new Edge(new Vertex(0, 0), new Vertex(1, 1));
		Edge e2 = new Edge(e);
		assertTrue(e != e2);
		assertEquals(e, e2);
	}
}
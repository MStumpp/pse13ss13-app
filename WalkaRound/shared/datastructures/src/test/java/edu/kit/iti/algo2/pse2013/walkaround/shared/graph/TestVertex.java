package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestVertex {

	@Test
	public void testCompare() {
		Vertex v = new Vertex(0, 0);
		Vertex v2 = new Vertex(1, 1);
		assertTrue(v.compareTo(v2) < 0);
		assertTrue(v2.compareTo(v) > 0);
		assertTrue(v2.compareTo(v2) == 0);
	}

	@Test
	public void testCurrentWeightedLength() {
		Vertex v = new Vertex(0, 0);
		assertEquals(0, v.getCurrentWeightedLength(), 1e-323);
		v.setCurrentWeightedLength(42);
		assertEquals(42, v.getCurrentWeightedLength(), 1e-323);
	}

	@Test
	public void testValueForDimension() {
		Vertex v = new Vertex(0, 0);
		try {
			v.valueForDimension(-1);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			v.valueForDimension(42);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testOutgoingEdges() {
		Vertex v = new Vertex(0, 0);
		Vertex v2 = new Vertex(1, 1);
		Vertex v3 = new Vertex(2, 2);
		Edge e = new Edge(v, v2);
		Edge e2 = new Edge(v, v3);
		Edge e3 = new Edge(v2, v3);
		v.addOutgoingEdge(e);
		v2.addOutgoingEdge(e);
		e.equals(v2.getOutgoingEdge(v));
		e.equals(v.getOutgoingEdge(v2));
	}

	@Test
	public void testParent() {
		Vertex v = new Vertex(0, 0);
		Vertex v2 = new Vertex(1, 1);
		assertEquals(null, v.getParent());
		v.setParent(v2);
		assertEquals(v2, v.getParent());
	}

	@Test
	public void testRun() {
		Vertex v = new Vertex(0, 0);
		assertEquals(0, v.getRun());
		v.setRun(42);
		assertEquals(42, v.getRun());
	}

	@Test
	public void testHashCode() {
		Vertex v = new Vertex(0, 0);
		Vertex v2 = new Vertex(1, 1);
		assertTrue(v.hashCode() != v2.hashCode());
	}

	@Test
	public void testNumberNodes() {
		Vertex v = new Vertex(0, 0);
		Vertex v2 = new Vertex(1, 1);
		assertEquals(1, v.numberNodes());
		assertEquals(1, v2.numberNodes());
	}
}
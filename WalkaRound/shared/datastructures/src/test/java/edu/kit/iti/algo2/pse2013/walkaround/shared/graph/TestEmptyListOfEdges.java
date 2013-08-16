package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import org.junit.Test;

public class TestEmptyListOfEdges {
	@Test
	public void testInit() {
		EmptyListOfEdgesException eloee = new EmptyListOfEdgesException(null);
		eloee = new EmptyListOfEdgesException("Test");
	}
}
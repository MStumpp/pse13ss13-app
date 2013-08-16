package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import org.junit.Test;

public class TestNoVertexForIDExistsException {

	@Test
	public void testException() {
		NoVertexForIDExistsException nvfidee = new NoVertexForIDExistsException("Test");
		nvfidee = new NoVertexForIDExistsException(null);
	}

}

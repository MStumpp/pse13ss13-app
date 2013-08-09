package edu.kit.iti.algo2.pse2013.walkaround.shared.pbf;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

public class TestProtobufConverter {
	private static final File TMP_GRAPHDATA_FILE = new File(System.getProperty("java.io.tmpdir") + File.separatorChar + "graphData.pbf");

	@Test
	public void testConvertGraphData() throws IOException {
		GraphDataIO graphData = new GraphDataIO();
		Vertex a1 = new Vertex(0, 0);
		Vertex b1 = new Vertex(1, 1);
		Vertex c1 = new Vertex(2, 2);
		Vertex a2 = new Vertex(2, 0);
		Vertex c2 = new Vertex(0, 2);
		Edge e1 = new Edge(a1, b1);
		Edge e2 = new Edge(c1, b1);
		Edge e3 = new Edge(b1, a2);
		Edge e4 = new Edge(c2, b1);
		graphData.addEdge(e1);
		graphData.addEdge(e2);
		graphData.addEdge(e3);
		graphData.addEdge(e4);

		GraphDataIO.save(graphData, TMP_GRAPHDATA_FILE);
		GraphDataIO reload = GraphDataIO.load(TMP_GRAPHDATA_FILE);

		Assert.assertTrue(reload.getEdges().containsAll(graphData.getEdges()));
		Assert.assertTrue(graphData.getEdges().containsAll(reload.getEdges()));
	}
}
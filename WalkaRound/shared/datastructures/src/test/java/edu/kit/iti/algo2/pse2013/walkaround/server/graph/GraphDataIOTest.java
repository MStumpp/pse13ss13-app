package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.pbf.ProtobufConverter;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGraphData;

/**
 * GraphDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GraphDataIOTest {

	private static final File GRAPH_DATA_FILE = new File(System.getProperty("java.io.tmpdir")
			+ File.separatorChar + "walkaround"
			+ File.separatorChar + "graphData.io");

	@Test
	public void testSandAndLoad() {
		GraphDataIO graphDataIO = getGraphDataIO();
		int size = graphDataIO.getEdges().size();

		try {
			OutputStream fos = new BufferedOutputStream(new FileOutputStream(GRAPH_DATA_FILE));
			ProtobufConverter.getGraphDataBuilder(graphDataIO).build().writeTo(fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Assert.assertTrue(GRAPH_DATA_FILE.exists());

		graphDataIO = null;
		try {
			InputStream fis = new BufferedInputStream(new FileInputStream(GRAPH_DATA_FILE));
			ProtobufConverter.getGraphData(SaveGraphData.parseFrom(fis));
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(graphDataIO);
		Assert.assertEquals(graphDataIO.getEdges().size(), size);
	}

	private GraphDataIO getGraphDataIO() {

		GraphDataIO graphDataIO = new GraphDataIO();
		Edge edge1 = new Edge(new Vertex(1.d, 2.d), new Vertex(3.d, 4.d));
		Edge edge2 = new Edge(new Vertex(1.d, 2.d), new Vertex(3.d, 4.d));
		graphDataIO.addEdge(edge1);
		graphDataIO.addEdge(edge2);

		return graphDataIO;
	}

}

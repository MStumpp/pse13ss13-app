package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.FileUtil;

/**
 * GraphDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GraphDataIOTest {


	private static final File REAL_GRAPH_DATA_FILE = FileUtil.getFile("graphData.pbf");
	private static final File TMP_GRAPH_DATA_FILE = new File(System.getProperty("java.io.tmpdir") + File.separatorChar + "graphData.pbf");

	@Before
	public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field idCounter = Vertex.class.getDeclaredField("idCounter");
		idCounter.setAccessible(true);
		idCounter.setInt(null, 0);
	}

	@Test
	public void testSaveAndLoad() {
		GraphDataIO graphDataIO = getGraphDataIO();

		try {
			GraphDataIO.save(graphDataIO, TMP_GRAPH_DATA_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertTrue(TMP_GRAPH_DATA_FILE.exists());

		GraphDataIO reload = null;
		try {
			reload = GraphDataIO.load(TMP_GRAPH_DATA_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertNotNull(graphDataIO);
		assertTrue(graphDataIO.getEdges().containsAll(reload.getEdges()));
		assertTrue(reload.getEdges().containsAll(graphDataIO.getEdges()));
	}

    @Test
    public void testSaveAndLoad2() {
        GraphDataIO graphDataIO = getGraphDataIO();
        int size = graphDataIO.getEdges().size();

        try {
            GraphDataIO.save(graphDataIO, TMP_GRAPH_DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(TMP_GRAPH_DATA_FILE.exists());

        graphDataIO = null;
        try {
            graphDataIO = GraphDataIO.load(TMP_GRAPH_DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GraphDataIO graphDataIO2 = null;
        try {
            graphDataIO2 = GraphDataIO.load(TMP_GRAPH_DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(graphDataIO);
        Assert.assertEquals(graphDataIO.getEdges().size(), size);
        Assert.assertEquals(graphDataIO2.getEdges().size(), size);
    }

	@Test
    public void testSaveAndLoadWithRealDataSet() throws IOException {
		System.out.println("### TestSaveAndLoadWithRealDataSet() ###");
		System.out.println(REAL_GRAPH_DATA_FILE);

		Assert.assertNotNull(REAL_GRAPH_DATA_FILE);
		Assert.assertTrue(REAL_GRAPH_DATA_FILE.exists());
		Assert.assertTrue(REAL_GRAPH_DATA_FILE.canWrite());
		Assert.assertTrue(REAL_GRAPH_DATA_FILE.canRead());

		long stopwatch = System.currentTimeMillis();
		System.out.print("Load real graph-dataset... ");
		GraphDataIO graphData = GraphDataIO.load(REAL_GRAPH_DATA_FILE);
		System.out.println(System.currentTimeMillis() - stopwatch + " ms");

		int numEdges = graphData.getEdges().size();
		int numVertices = graphData.getVertices().size();
		System.out.println(numEdges + " edges and " + numVertices + " vertices");

		stopwatch = System.currentTimeMillis();
		System.out.print("Save to temporary file... ");
		GraphDataIO.save(graphData, TMP_GRAPH_DATA_FILE);
		System.out.println(System.currentTimeMillis() - stopwatch + " ms");

		stopwatch = System.currentTimeMillis();
		System.out.print("Load again... ");
		graphData = GraphDataIO.load(TMP_GRAPH_DATA_FILE);
		System.out.println(System.currentTimeMillis() - stopwatch + " ms");

		System.out.println(numEdges + " edges and " + numVertices + " vertices");
		Assert.assertEquals(numEdges, graphData.getEdges().size());
		Assert.assertEquals(numVertices, graphData.getVertices().size());
	}

	@Test
	public void testAddEdges() {
		GraphDataIO gdio = getGraphDataIO();
		gdio.addEdges(new LinkedList<Edge>());
	}

	@Test
	public void testHashCode() {
		GraphDataIO gdio = getGraphDataIO();
		GraphDataIO gdio2 = new GraphDataIO();
		assertTrue(gdio.hashCode() != gdio2.hashCode());
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
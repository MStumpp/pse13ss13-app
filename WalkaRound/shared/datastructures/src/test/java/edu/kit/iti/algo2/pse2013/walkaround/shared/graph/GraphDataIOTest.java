package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * GraphDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GraphDataIOTest {


	private static final File REAL_GRAPH_DATA_FILE = new File(GraphDataIO.class.getResource("/graphData.pbf").getFile());
	private static final File TMP_GRAPH_DATA_FILE = new File(System.getProperty("java.io.tmpdir") + File.separatorChar + "graphData.pbf");

	@Before
	public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field instance = Graph.class.getDeclaredField("instance");
		instance.setAccessible(true);
		instance.set(null, null);

		Field idCounter = Vertex.class.getDeclaredField("idCounter");
		idCounter.setAccessible(true);
		idCounter.setInt(null, 0);
	}

	@Test
	public void testSaveAndLoad() {
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

		Assert.assertNotNull(graphDataIO);
		Assert.assertEquals(graphDataIO.getEdges().size(), size);
	}

	@Test
	public void testSaveAndLoadWithRealDataSet() throws IOException {
		System.out.println("### TestSaveAndLoadWithRealDataSet() ###");

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
	public void testNumOfGraphPartitions() throws IOException {
		Assert.assertNotNull(REAL_GRAPH_DATA_FILE);
		Assert.assertTrue(REAL_GRAPH_DATA_FILE.exists());
		Assert.assertTrue(REAL_GRAPH_DATA_FILE.canRead());

		TreeSet<Edge> edges =new TreeSet<Edge>(GraphDataIO.load(REAL_GRAPH_DATA_FILE).getEdges());
		System.out.println(edges.size() + " edges in dataset");
		int numPartitions = 0;
		while (edges.size() > 0) {
			int numEdgesInPartition = 1;
			Edge curEdge = edges.pollFirst();
			numEdgesInPartition = addAdjacentEdges(curEdge.getHead(), numEdgesInPartition, edges);
			numEdgesInPartition = addAdjacentEdges(curEdge.getTail(), numEdgesInPartition, edges);
			numPartitions++;
			System.out.println(String.format("Partition %d hat %d Elemente", numPartitions, numEdgesInPartition));
		}
		System.out.println(numPartitions + " Partitionen");
	}

	private int addAdjacentEdges(Vertex v, int numEdgesInPartition, TreeSet<Edge> source) {
		List<Edge> outgoing = v.getOutgoingEdges();
		for (Edge e : outgoing) {
			if (source.remove(e)) {
				numEdgesInPartition++;
				numEdgesInPartition = addAdjacentEdges(e.getHead(), numEdgesInPartition, source);
				numEdgesInPartition = addAdjacentEdges(e.getTail(), numEdgesInPartition, source);
			}
		}
		return numEdgesInPartition;
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
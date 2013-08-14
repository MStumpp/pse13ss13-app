package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.FileUtil;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Profile;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * RoundtripProcessorTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class RoundtripProcessorTest {


    private static final File REAL_GRAPH_DATA_FILE = FileUtil.getFile("graphData.pbf");

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = RoundtripProcessor.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        instance = ShortestPathTreeProcessor.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        Field idCounter = Vertex.class.getDeclaredField("idCounter");
        idCounter.setAccessible(true);
        idCounter.setInt(null, 0);

        idCounter = Edge.class.getDeclaredField("idCounter");
        idCounter.setAccessible(true);
        idCounter.setInt(null, 0);
    }


    @Test
    public void testInit() {
        GraphDataIO graphDataIO = getGraph();
        try {
            Assert.assertNotNull(RoundtripProcessor.init(graphDataIO));
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGetInstance() throws InstantiationException {
        GraphDataIO graphDataIO = getGraph();
        try {
            Assert.assertNotNull(RoundtripProcessor.init(graphDataIO));
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(RoundtripProcessor.getInstance());
    }


    @Test
    public void testComputesRoundtrip() throws InstantiationException {

        GraphDataIO graphDataIO = null;
        try {
            graphDataIO = GraphDataIO.load(REAL_GRAPH_DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(graphDataIO);

        Graph graph = null;
        try {
            graph = new Graph(graphDataIO);
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(graph);

        Vertex source = null;
        try {
            source = graph.getVertexByID(350005);
        } catch (NoVertexForIDExistsException e) {
            e.printStackTrace();
        }

        try {
            Assert.assertNotNull(RoundtripProcessor.init(graphDataIO, 2));
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }

        RoundtripProcessor roundtripProcessor = RoundtripProcessor.getInstance();

        List<Vertex> route = null;
        try {
            route = roundtripProcessor.computeRoundtrip(source, new int[] { 1 }, 1000);
        } catch (RoundtripComputationNoSlotsException e) {
            e.printStackTrace();
        } catch (RoundtripComputeException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(route);
    }

    private GraphDataIO getGraph() {

        GraphDataIO graphDataIO = new GraphDataIO();
        Vertex vertex0 = new Vertex(0.d, 0.d);
        Vertex vertex1 = new Vertex(0.d, 1.d);
        Vertex vertex2 = new Vertex(0.d, 2.d);
        Vertex vertex3 = new Vertex(0.d, 3.d);
        Vertex vertex4 = new Vertex(0.d, 4.d);
        Vertex vertex5 = new Vertex(0.d, 5.d);

        Edge edge1 = new Edge(vertex0, vertex1);
        Edge edge2 = new Edge(vertex0, vertex2);
        Edge edge5 = new Edge(vertex2, vertex3);
        Edge edge6 = new Edge(vertex2, vertex5);
        Edge edge7 = new Edge(vertex3, vertex0);
        Edge edge8 = new Edge(vertex4, vertex2);
        Edge edge9 = new Edge(vertex4, vertex5);
        Edge edge10 = new Edge(vertex5, vertex3);

        graphDataIO.addEdge(edge1);
        graphDataIO.addEdge(edge2);
        graphDataIO.addEdge(edge5);
        graphDataIO.addEdge(edge6);
        graphDataIO.addEdge(edge7);
        graphDataIO.addEdge(edge8);
        graphDataIO.addEdge(edge9);
        graphDataIO.addEdge(edge10);

        return graphDataIO;
    }

}

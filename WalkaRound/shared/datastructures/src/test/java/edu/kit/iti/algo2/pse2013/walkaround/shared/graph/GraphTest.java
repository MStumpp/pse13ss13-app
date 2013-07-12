package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

/**
 * GraphDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GraphTest {

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
    public void testInit() {

        GraphDataIO graphDataIO = getGraphDataIO();
        Graph graph = null;
        try {
            Graph.init(graphDataIO.getEdges());
            graph = Graph.getInstance();
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        try {
            Assert.assertNotNull(graph.getVertexByID(0));
            Assert.assertNotNull(graph.getVertexByID(1));
            Assert.assertNotNull(graph.getVertexByID(2));
            Assert.assertNotNull(graph.getVertexByID(3));
            Assert.assertNotNull(graph.getVertexByID(4));
            Assert.assertNotNull(graph.getVertexByID(5));

            Assert.assertEquals(3, graph.getVertexByID(0).getOutgoingEdges().size());
            Assert.assertEquals(3, graph.getVertexByID(1).getOutgoingEdges().size());
            Assert.assertEquals(5, graph.getVertexByID(2).getOutgoingEdges().size());
            Assert.assertEquals(3, graph.getVertexByID(3).getOutgoingEdges().size());
            Assert.assertEquals(3, graph.getVertexByID(4).getOutgoingEdges().size());
            Assert.assertEquals(3, graph.getVertexByID(5).getOutgoingEdges().size());

        } catch (NoVertexForIDExistsException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testInitGraphWithRealDataSet() {

        File file = new File(System.getProperty("java.io.tmpdir") + "/graphData.pbf");
        Assert.assertNotNull(file);
        Assert.assertTrue(file.exists());

        GraphDataIO graphDataIO = null;
        try {
            graphDataIO = GraphDataIO.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(graphDataIO);

        Graph graph = null;
        try {
            Graph.init(graphDataIO.getEdges());
            graph = Graph.getInstance();
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(graph);

//        for (int i = 0; i <= graph.vertices.length; i++)
//            System.out.println("i: " + i + " vertex: " + graph.vertices[i]);
    }


    private GraphDataIO getGraphDataIO() {

        GraphDataIO graphDataIO = new GraphDataIO();
        Vertex vertex1 = new Vertex(1.d, 1.d);
        Vertex vertex2 = new Vertex(1.d, 2.d);
        Vertex vertex3 = new Vertex(1.d, 3.d);
        Vertex vertex4 = new Vertex(1.d, 4.d);
        Vertex vertex5 = new Vertex(1.d, 5.d);
        Vertex vertex6 = new Vertex(1.d, 6.d);

        Edge edge1 = new Edge(vertex1, vertex2);
        edge1.setLength(5.d);
        Edge edge2 = new Edge(vertex1, vertex3);
        edge2.setLength(1.d);
        Edge edge3 = new Edge(vertex2, vertex5);
        edge3.setLength(2.d);
        Edge edge4 = new Edge(vertex3, vertex2);
        edge4.setLength(2.d);
        Edge edge5 = new Edge(vertex3, vertex4);
        edge5.setLength(3.d);
        Edge edge6 = new Edge(vertex3, vertex6);
        edge6.setLength(8.d);
        Edge edge7 = new Edge(vertex4, vertex1);
        edge7.setLength(1.d);
        Edge edge8 = new Edge(vertex5, vertex3);
        edge8.setLength(1.d);
        Edge edge9 = new Edge(vertex5, vertex6);
        edge9.setLength(2.d);
        Edge edge10 = new Edge(vertex6, vertex4);
        edge10.setLength(2.d);

        graphDataIO.addEdge(edge1);
        graphDataIO.addEdge(edge2);
        graphDataIO.addEdge(edge3);
        graphDataIO.addEdge(edge4);
        graphDataIO.addEdge(edge5);
        graphDataIO.addEdge(edge6);
        graphDataIO.addEdge(edge7);
        graphDataIO.addEdge(edge8);
        graphDataIO.addEdge(edge9);
        graphDataIO.addEdge(edge10);

        return graphDataIO;
    }

}

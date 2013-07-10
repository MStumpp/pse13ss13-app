package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.RouteInfoTransfer;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.EmptyListOfEdgesException;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Graph;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.NoVertexForIDExistsException;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.RouteInfoTransfer;

/**
 * ShortestPathProcessorTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class ShortestPathProcessorTest {

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = Graph.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        instance = ShortestPathProcessor.class.getDeclaredField("instance");
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
        Graph graph = getGraph();
        Assert.assertNotNull(ShortestPathProcessor.init(graph));
    }


    @Test
    public void testGetInstance() {
        Graph graph = getGraph();
        Assert.assertNotNull(ShortestPathProcessor.init(graph));
        Assert.assertNotNull(ShortestPathProcessor.getInstance());
    }


    @Test
    public void testComputesShortestPath() {

        Graph graph = getGraph();
        Vertex source = null;
        Vertex target = null;
        try {
            source = graph.getVertexByID(0);
            target = graph.getVertexByID(4);
        } catch (NoVertexForIDExistsException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(ShortestPathProcessor.init(graph));
        ShortestPathProcessor shortestPathProcessor = ShortestPathProcessor.getInstance();
        RouteInfoTransfer path = null;
        try {
            path = shortestPathProcessor.computeShortestPath(source, target);
        } catch (NoShortestPathExistsException e) {
            e.printStackTrace();
        } catch (ShortestPathComputeException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(path);
        Assert.assertEquals(path.getCoordinates().size(), 4);

        Assert.assertEquals(((Vertex) path.getCoordinates().get(0)).getID(), 0);
        Assert.assertEquals(((Vertex) path.getCoordinates().get(1)).getID(), 2);
        Assert.assertEquals(((Vertex) path.getCoordinates().get(2)).getID(), 1);
        Assert.assertEquals(((Vertex) path.getCoordinates().get(3)).getID(), 4);
    }


    private Graph getGraph() {

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

        Graph graph = null;
        try {
            Graph.init(graphDataIO.getEdges());
            graph = Graph.getInstance();
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }

        return graph;
    }

}

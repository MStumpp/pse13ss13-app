package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.FileUtil;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * ShortestPathProcessorTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class ShortestPathProcessorTest {

    private static final File REAL_GRAPH_DATA_FILE = FileUtil.getFile("graphData.pbf");

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = ShortestPathProcessor.class.getDeclaredField("instance");
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
            Assert.assertNotNull(ShortestPathProcessor.init(graphDataIO));
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGetInstance() throws InstantiationException {
        GraphDataIO graphDataIO = getGraph();
        try {
            Assert.assertNotNull(ShortestPathProcessor.init(graphDataIO));
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(ShortestPathProcessor.getInstance());
    }


    @Test
    public void testComputesShortestPath() throws InstantiationException {

        GraphDataIO graphDataIO = getGraph();
        Assert.assertNotNull(graphDataIO);

        Graph graph = null;
        try {
            graph = new Graph(graphDataIO);
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(graph);

        Vertex source = null;
        Vertex target = null;
        try {
            source = graph.getVertexByID(0);
            target = graph.getVertexByID(4);
        } catch (NoVertexForIDExistsException e) {
            e.printStackTrace();
        }

        try {
            Assert.assertNotNull(ShortestPathProcessor.init(graphDataIO, 5));
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }
        ShortestPathProcessor shortestPathProcessor = ShortestPathProcessor.getInstance();
        List<Vertex> route = null;
        try {
            route = shortestPathProcessor.computeShortestPath(source, target);
        } catch (NoShortestPathExistsException e) {
            e.printStackTrace();
        } catch (ShortestPathComputeException e) {
            e.printStackTrace();
        } catch (ShortestPathComputationNoSlotsException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(route);
        Assert.assertEquals(3, route.size());

        Assert.assertEquals(0, (route.get(0)).getID());
        Assert.assertEquals(2, (route.get(1)).getID());
        Assert.assertEquals(4, (route.get(2)).getID());
    }


    @Test
    public void testComputesShortestPathWithRealDataSet() throws InstantiationException {

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

        Random rand = new Random();
        int min = 0;
        int max = graphDataIO.getVertices().size()-1;

        List<Vertex> vertices = new ArrayList<Vertex>();
        int minNumVertices = 10;
        int maxNumVertices = 100;

        Vertex source = null;
        Vertex target = null;
        boolean run = true;
        while (run) {
            try {
                vertices.clear();
                source = graph.getVertexByID(rand.nextInt(max-min+1)+min);
                if (source == null)
                    continue;
                vertices.add(source);
                target = source;

                // get outgoing edges of current source
                List<Edge> outgoingEdges = target.getOutgoingEdges();
                while (outgoingEdges.size() > 0 && vertices.size() <= maxNumVertices) {
                    System.out.print(" out: " + outgoingEdges.size());

                    target = outgoingEdges.get(0).getHead();
                    vertices.add(target);
                    System.out.print(" -> head: " + target.getID());

                    // get outgoing edges of next target
                    outgoingEdges = target.getOutgoingEdges();
                    // remove vertices already visited
                    Iterator<Edge> iter = outgoingEdges.iterator();
                    Edge edge;
                    while (iter.hasNext()) {
                        edge = iter.next();
                        if (vertices.contains(edge.getHead()))
                            iter.remove();
                    }
                }
                System.out.println(" ");

                if (minNumVertices <= vertices.size())
                    run = false;
            } catch (NoVertexForIDExistsException e) {
                e.printStackTrace();
            }
        }

        // if no vertices found, silently quit
        if (source == null || target == null)
            return;

        try {
            Assert.assertNotNull(ShortestPathProcessor.init(graphDataIO, 5));
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }
        ShortestPathProcessor shortestPathProcessor = ShortestPathProcessor.getInstance();
        List<Vertex> route = null;
        try {
            route = shortestPathProcessor.computeShortestPath(source, target);
        } catch (NoShortestPathExistsException e) {
            e.printStackTrace();
        } catch (ShortestPathComputeException e) {
            e.printStackTrace();
        } catch (ShortestPathComputationNoSlotsException e) {
            e.printStackTrace();
        }
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

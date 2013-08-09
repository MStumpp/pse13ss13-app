package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
    public void testGetInstance() throws InstantiationException {
        Graph graph = getGraph();
        Assert.assertNotNull(ShortestPathProcessor.init(graph));
        Assert.assertNotNull(ShortestPathProcessor.getInstance());
    }


    @Test
    public void testComputesShortestPath() throws InstantiationException {

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
        List<Vertex> route = null;
        try {
            route = shortestPathProcessor.computeShortestPath(source, target);
        } catch (NoShortestPathExistsException e) {
            e.printStackTrace();
        } catch (ShortestPathComputeException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(route);
        Assert.assertEquals(route.size(), 3);

        Assert.assertEquals((route.get(0)).getID(), 0);
        Assert.assertEquals((route.get(1)).getID(), 2);
        Assert.assertEquals((route.get(2)).getID(), 4);
    }


    @Test
    public void testComputesShortestPathWithRealDataSet() throws InstantiationException {

        File file = new File("/Users/Matthias/Workspace/PSE/pse13ss13-app/WalkaRound/shared/datastructures/src/main/resources/graphData.io");
        //File file = new File(getClass().getResource("/graphData.io").getFile());
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

        Assert.assertNotNull(ShortestPathProcessor.init(graph));
        ShortestPathProcessor shortestPathProcessor = ShortestPathProcessor.getInstance();
        List<Vertex> route = null;
        try {
            route = shortestPathProcessor.computeShortestPath(source, target);
        } catch (NoShortestPathExistsException e) {
            e.printStackTrace();
        } catch (ShortestPathComputeException e) {
            e.printStackTrace();
        }

        //Assert.assertEquals(route.size(), numVerticesOnRoute);
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
        Edge edge2 = new Edge(vertex1, vertex3);
        Edge edge3 = new Edge(vertex2, vertex5);
        Edge edge4 = new Edge(vertex3, vertex2);
        Edge edge5 = new Edge(vertex3, vertex4);
        Edge edge6 = new Edge(vertex3, vertex6);
        Edge edge7 = new Edge(vertex4, vertex1);
        Edge edge8 = new Edge(vertex5, vertex3);
        Edge edge9 = new Edge(vertex5, vertex6);
        Edge edge10 = new Edge(vertex6, vertex4);

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
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return graph;
    }

}

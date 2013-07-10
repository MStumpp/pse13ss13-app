package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * GraphDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GraphDataIOTest {

    private static String fileLocation = System.getProperty("java.io.tmpdir") + File.separator + "graphDataIO";


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
    @Ignore
    public void testSandAndLoad() {
        GraphDataIO graphDataIO = getGraphDataIO();
        int size = graphDataIO.getEdges().size();

        try {
            GraphDataIO.save(graphDataIO, new File(fileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f = new File(fileLocation);
        Assert.assertTrue(f.exists());

        graphDataIO = null;
        try {
            graphDataIO = GraphDataIO.load(new File(fileLocation));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
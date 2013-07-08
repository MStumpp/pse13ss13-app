package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * GraphDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GraphDataIOTest {

    private static String fileLocaton = System.getProperty("java.io.tmpdir") + File.separator + "graphDataIO";

    @Test
    public void testSandAndLoad() {
        GraphDataIO graphDataIO = getGraphDataIO();
        int size = graphDataIO.getEdges().size();

        try {
            GraphDataIO.save(graphDataIO, new File(fileLocaton));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f = new File(fileLocaton);
        Assert.assertTrue(f.exists());

        graphDataIO = null;
        try {
            graphDataIO = GraphDataIO.load(new File(fileLocaton));
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

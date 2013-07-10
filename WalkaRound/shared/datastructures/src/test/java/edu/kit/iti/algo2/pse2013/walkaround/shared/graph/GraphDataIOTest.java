package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;

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
    public void testSandAndLoad() {
//        GraphDataIO graphDataIO = getGraphDataIO();
//        int size = graphDataIO.getEdges().size();
//
//        try {
//            OutputStream fos = new BufferedOutputStream(new FileOutputStream(GRAPH_DATA_FILE));
//            ProtobufConverter.getGraphDataBuilder(graphDataIO).build().writeTo(fos);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Assert.assertTrue(GRAPH_DATA_FILE.exists());
//
//        graphDataIO = null;
//        try {
//            InputStream fis = new BufferedInputStream(new FileInputStream(GRAPH_DATA_FILE));
//            graphDataIO = ProtobufConverter.getGraphData(SaveGraphData.parseFrom(fis));
//            fis.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Assert.assertNotNull(graphDataIO);
//        Assert.assertEquals(graphDataIO.getEdges().size(), size);
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

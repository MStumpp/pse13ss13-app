package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.kit.iti.algo2.pse2013.walkaround.shared.FileUtil;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryComputationNoSlotsException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryProcessorException;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

/**
 * GeometryProcessorTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryProcessorTest {

    private static final Logger logger = LoggerFactory.getLogger(GeometryProcessorTest.class);

    private static GraphDataIO graphDataIO;

    @BeforeClass
    public static void doSetUp() {
        File graphDataio = FileUtil.getFile("graphData.pbf");
        try {
            graphDataIO = GraphDataIO.load(graphDataio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void resetSingletonBefore() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = GeometryProcessor.class.getDeclaredField("instance");
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
    public void testInit() throws MalformedURLException {
        GeometryDataIO geometryDataIO = getGeometryDataIOOnePerNode();
        Assert.assertNotNull(GeometryProcessor.init(geometryDataIO));
    }


    @Test
    public void testGetInstance() throws InstantiationException, MalformedURLException {
        GeometryDataIO geometryDataIO = getGeometryDataIOOnePerNode();
        Assert.assertNotNull(GeometryProcessor.init(geometryDataIO));
        Assert.assertNotNull(GeometryProcessor.getInstance());
    }


    @Test
    public void testGetNearestVertexOnePerNodeOneThread() throws InstantiationException, MalformedURLException {

        GeometryDataIO geometryDataIO = getGeometryDataIOOnePerNode();

        Assert.assertNotNull(GeometryProcessor.init(geometryDataIO));
        GeometryProcessor geometryProcessor = GeometryProcessor.getInstance();
        Coordinate search = new Coordinate(5.5, 3.9);
        Geometrizable geometrizable = null;
        try {
            geometrizable = geometryProcessor.getNearestVertex(search);
        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        } catch (GeometryComputationNoSlotsException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(geometrizable);
        Assert.assertEquals(1, ((Vertex) geometrizable).getID());
    }


    @Test
    public void testGetNearestVertexOnePerNodeMultiThreaded() throws InstantiationException, MalformedURLException {

        GeometryDataIO geometryDataIO = getGeometryDataIOOnePerNode();
        Assert.assertNotNull(GeometryProcessor.init(geometryDataIO, 5));
        GeometryProcessor geometryProcessor = GeometryProcessor.getInstance();
        Coordinate search = new Coordinate(5.5, 3.9);
        Geometrizable geometrizable = null;
        try {
            geometrizable = geometryProcessor.getNearestVertex(search);
        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        } catch (GeometryComputationNoSlotsException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(geometrizable);
        Assert.assertEquals(1, ((Vertex) geometrizable).getID());
    }


    @Test
    public void testGetNearestVertexDefaultPerNodeOneThread() throws InstantiationException, MalformedURLException {

        GeometryDataIO geometryDataIO = getGeometryDataIODefaultPerNode();
        Assert.assertNotNull(GeometryProcessor.init(geometryDataIO));
        GeometryProcessor geometryProcessor = GeometryProcessor.getInstance();
        Coordinate search = new Coordinate(5.5, 3.9);
        Geometrizable geometrizable = null;
        try {
            geometrizable = geometryProcessor.getNearestVertex(search);
        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        } catch (GeometryComputationNoSlotsException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(geometrizable);
        Assert.assertEquals(1, ((Vertex) geometrizable).getID());
    }


    @Test
    public void testGetNearestVertexDefaultPerNodeMultiThreaded() throws InstantiationException, MalformedURLException {

        GeometryDataIO geometryDataIO = getGeometryDataIODefaultPerNode();
        Assert.assertNotNull(GeometryProcessor.init(geometryDataIO, 5));
        GeometryProcessor geometryProcessor = GeometryProcessor.getInstance();
        Coordinate search = new Coordinate(5.5, 3.9);
        Geometrizable geometrizable = null;
        try {
            geometrizable = geometryProcessor.getNearestVertex(search);
        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        } catch (GeometryComputationNoSlotsException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(geometrizable);
        Assert.assertEquals(1, ((Vertex) geometrizable).getID());
    }


    @Test
    public void testGetNearestVertexOnePerNodeWithRealDataSetOneThread() throws InstantiationException {

        Assert.assertNotNull(graphDataIO);

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.
                preprocessGeometryDataIO(new ArrayList<Geometrizable>(graphDataIO.getVertices()), 1);

        Assert.assertNotNull(GeometryProcessor.init(geometryDataIO));
        GeometryProcessor geometryProcessor = GeometryProcessor.getInstance();
        Coordinate search1 = new Coordinate(49.2323, 8.2334);
        Coordinate search2 = new Coordinate(49.004, 8.345345);
        Coordinate search3 = new Coordinate(49.0145, 8.2424);
        Coordinate search4 = new Coordinate(49.2323, 8.345435);
        Coordinate search5 = new Coordinate(49.3424, 8.0024234);
        Coordinate search6 = new Coordinate(49.1312, 8.2424);
        Coordinate search7 = new Coordinate(49.324524, 8.456456);
        Coordinate search8 = new Coordinate(100.24223434, 9.234234);
        Coordinate search9 = new Coordinate(10.234324, 7.3434324);
        Coordinate search10 = new Coordinate(49.00936, 8.42705);             // 48.659722 8.0823974

        Geometrizable geometrizable = null;
        try {
            long startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search1);
            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search1 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search2);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search2 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search3);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search3 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search4);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search4 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search5);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search5 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search6);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search6 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search7);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search7 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search8);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search8 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search9);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search9 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search10);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search10 + " to: " + geometrizable.toString());

        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        } catch (GeometryComputationNoSlotsException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGetNearestVertexOnePerNodeWithRealDataSetMultiThread() throws InstantiationException {

        Assert.assertNotNull(graphDataIO);

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.
                preprocessGeometryDataIO(new ArrayList<Geometrizable>(graphDataIO.getVertices()), 1);

        Assert.assertNotNull(GeometryProcessor.init(geometryDataIO, 5));
        GeometryProcessor geometryProcessor = GeometryProcessor.getInstance();
        Coordinate search1 = new Coordinate(49.2323, 8.2334);
        Coordinate search2 = new Coordinate(49.004, 8.345345);
        Coordinate search3 = new Coordinate(49.0145, 8.2424);
        Coordinate search4 = new Coordinate(49.2323, 8.345435);
        Coordinate search5 = new Coordinate(49.3424, 8.0024234);
        Coordinate search6 = new Coordinate(49.1312, 8.2424);
        Coordinate search7 = new Coordinate(49.324524, 8.456456);
        Coordinate search8 = new Coordinate(100.24223434, 9.234234);
        Coordinate search9 = new Coordinate(10.234324, 7.3434324);
        Coordinate search10 = new Coordinate(49.00936, 8.42705);             // 48.659722 8.0823974

        Geometrizable geometrizable = null;
        try {
            long startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search1);
            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search1 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search2);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search2 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search3);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search3 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search4);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search4 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search5);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search5 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search6);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search6 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search7);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search7 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search8);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search8 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search9);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search9 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestVertex(search10);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search10 + " to: " + geometrizable.toString());

        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        } catch (GeometryComputationNoSlotsException e) {
            e.printStackTrace();
        }
    }


    private GeometryDataIO getGeometryDataIOOnePerNode() throws MalformedURLException {
        GraphDataIO graphDataIO = new GraphDataIO();
        graphDataIO.addEdge(new Edge(new Vertex(2.d, 3.d), new Vertex(5.d, 4.d)));
        graphDataIO.addEdge(new Edge(new Vertex(9.d, 6.d), new Vertex(4.d, 7.d)));
        graphDataIO.addEdge(new Edge(new Vertex(8.d, 1.d), new Vertex(7.d, 2.d)));

        LocationDataIO locationDataIO = new LocationDataIO();
        locationDataIO.addPOI(new POI(1.d, 2.d, "poi 1", "info 1", new URL("https://de.wikipedia.org/w/index.php?printable=yes&title=Wikipedia"), new int[] { 0, 1 }));
        locationDataIO.addPOI(new POI(3.d, 4.d, "poi 1", "info 1", new URL("https://de.wikipedia.org/w/index.php?printable=yes&title=Wikipedia"), new int[] { 0, 1 }));
        locationDataIO.addPOI(new POI(5.d, 7.d, "poi 1", "info 1", new URL("https://de.wikipedia.org/w/index.php?printable=yes&title=Wikipedia"), new int[] { 0, 1 }));

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.
                preprocessGeometryDataIO(new ArrayList<Geometrizable>(graphDataIO.getVertices()), 1);
        return geometryDataIO;
    }


    private GeometryDataIO getGeometryDataIODefaultPerNode() throws MalformedURLException {
        GraphDataIO graphDataIO = new GraphDataIO();
        graphDataIO.addEdge(new Edge(new Vertex(2.d, 3.d), new Vertex(5.d, 4.d)));
        graphDataIO.addEdge(new Edge(new Vertex(9.d, 6.d), new Vertex(4.d, 7.d)));
        graphDataIO.addEdge(new Edge(new Vertex(8.d, 1.d), new Vertex(7.d, 2.d)));

        LocationDataIO locationDataIO = new LocationDataIO();
        locationDataIO.addPOI(new POI(1.d, 2.d, "poi 1", "info 1", new URL("https://de.wikipedia.org/w/index.php?printable=yes&title=Wikipedia"), new int[] { 0, 1 }));
        locationDataIO.addPOI(new POI(3.d, 4.d, "poi 1", "info 1", new URL("https://de.wikipedia.org/w/index.php?printable=yes&title=Wikipedia"), new int[] { 0, 1 }));
        locationDataIO.addPOI(new POI(5.d, 7.d, "poi 1", "info 1", new URL("https://de.wikipedia.org/w/index.php?printable=yes&title=Wikipedia"), new int[] { 0, 1 }));

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.
                preprocessGeometryDataIO(new ArrayList<Geometrizable>(graphDataIO.getVertices()));
        return geometryDataIO;
    }

}

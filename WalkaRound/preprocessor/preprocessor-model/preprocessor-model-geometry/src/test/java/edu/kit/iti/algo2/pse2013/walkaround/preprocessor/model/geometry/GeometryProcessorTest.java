package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.shared.FileUtil;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.*;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * GeometryProcessorTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryProcessorTest {

    private static final Logger logger = LoggerFactory.getLogger(GeometryProcessorTest.class);

    private static GraphDataIO graphDataIO;

    private static LocationDataIO locationDataIO;

    @BeforeClass
    public static void doSetUp() {
        File graphDataio = FileUtil.getFile("graphData.pbf");
        try {
            graphDataIO = GraphDataIO.load(graphDataio);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File locationDataio = FileUtil.getFile("locationData.pbf");
        try {
            locationDataIO = LocationDataIO.load(locationDataio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void resetSingletonBefore() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = GeometryProcessorVertex.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        instance = GeometryProcessorEdge.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        instance = GeometryProcessorPOI.class.getDeclaredField("instance");
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
    public void testInitVertex() throws MalformedURLException {
        GeometryDataIO geometryDataIO = getGeometryDataIOVerticesOnePerNode();
        Assert.assertNotNull(GeometryProcessorEdge.init(geometryDataIO));
    }


    @Test
    public void testGetInstanceVertex() throws InstantiationException, MalformedURLException {
        GeometryDataIO geometryDataIO = getGeometryDataIOVerticesOnePerNode();
        Assert.assertNotNull(GeometryProcessorVertex.init(geometryDataIO));
        Assert.assertNotNull(GeometryProcessorVertex.getInstance());
    }


    @Test
    public void testInitEdge() throws MalformedURLException {
        GeometryDataIO geometryDataIO = getGeometryDataIOEdgesOnePerNode();
        Assert.assertNotNull(GeometryProcessorEdge.init(geometryDataIO));
    }


    @Test
    public void testGetInstanceEdge() throws InstantiationException, MalformedURLException {
        GeometryDataIO geometryDataIO = getGeometryDataIOEdgesOnePerNode();
        Assert.assertNotNull(GeometryProcessorEdge.init(geometryDataIO));
        Assert.assertNotNull(GeometryProcessorEdge.getInstance());
    }


    @Test
    public void testInitPOI() throws MalformedURLException {
        GeometryDataIO geometryDataIO = getGeometryDataPOIIODefaultPerNode();
        Assert.assertNotNull(GeometryProcessorPOI.init(geometryDataIO));
    }


    @Test
    public void testGetInstancePOI() throws InstantiationException, MalformedURLException {
        GeometryDataIO geometryDataIO = getGeometryDataPOIIODefaultPerNode();
        Assert.assertNotNull(GeometryProcessorPOI.init(geometryDataIO));
        Assert.assertNotNull(GeometryProcessorPOI.getInstance());
    }


    @Test(expected=IllegalArgumentException.class)
    public void testNearestGeometrizableIsNull() throws IOException, IllegalArgumentException,
            GeometryProcessorException, GeometryComputationNoSlotsException {
        GeometryDataIO geometryDataIO = getGeometryDataIOVerticesDefaultPerNode();
        GeometryProcessor gp = new GeometryProcessor(geometryDataIO, 1);
        gp.getNearestGeometrizable(new GeometrySearch(new double[]{49.2, 8.5}));
        assertTrue(true);
        gp.getNearestGeometrizable(null);
    }


    @Test
    public void testGetNearestVertexOnePerNodeOneThread() throws InstantiationException, MalformedURLException {

        GeometryDataIO geometryDataIO = getGeometryDataIOVerticesOnePerNode();

        Assert.assertNotNull(GeometryProcessorVertex.init(geometryDataIO));
        GeometryProcessorVertex geometryProcessor = GeometryProcessorVertex.getInstance();
        Geometrizable geometrizable = null;
        try {
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {5.5, 3.9}));
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

        GeometryDataIO geometryDataIO = getGeometryDataIOVerticesOnePerNode();
        Assert.assertNotNull(GeometryProcessorVertex.init(geometryDataIO, 5));
        GeometryProcessorVertex geometryProcessor = GeometryProcessorVertex.getInstance();
        Geometrizable geometrizable = null;
        try {
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {5.5, 3.9}));
        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        } catch (GeometryComputationNoSlotsException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(geometrizable);
        Assert.assertEquals(1, ((Vertex) geometrizable).getID());
    }


    @Test
    @Ignore
    public void testGetNearestVertexDefaultPerNodeOneThread() throws InstantiationException, MalformedURLException {

        GeometryDataIO geometryDataIO = getGeometryDataIOVerticesDefaultPerNode();

        Assert.assertNotNull(GeometryProcessorVertex.init(geometryDataIO));
        GeometryProcessorVertex geometryProcessor = GeometryProcessorVertex.getInstance();
        Geometrizable geometrizable = null;
        try {
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {5.5, 3.9}));
        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        } catch (GeometryComputationNoSlotsException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(geometrizable);
        Assert.assertEquals(1, ((Vertex) geometrizable).getID());
    }


    @Test
    @Ignore
    public void testGetNearestVertexDefaultPerNodeMultiThreaded() throws InstantiationException, MalformedURLException {

        GeometryDataIO geometryDataIO = getGeometryDataIOVerticesDefaultPerNode();
        Assert.assertNotNull(GeometryProcessorVertex.init(geometryDataIO, 5));
        GeometryProcessorVertex geometryProcessor = GeometryProcessorVertex.getInstance();
        Geometrizable geometrizable = null;
        try {
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {5.5, 3.9}));
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

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(new ArrayList<Geometrizable>(graphDataIO.getVertices()), 1);

        Assert.assertNotNull(GeometryProcessorVertex.init(geometryDataIO));
        GeometryProcessorVertex geometryProcessor = GeometryProcessorVertex.getInstance();
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
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search1.getLatitude(), search1.getLongitude()}));
            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search1 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search2.getLatitude(), search2.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search2 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search3.getLatitude(), search3.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search3 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search4.getLatitude(), search4.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search4 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search5.getLatitude(), search5.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search5 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search6.getLatitude(), search6.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search6 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search7.getLatitude(), search7.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search7 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search8.getLatitude(), search8.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search8 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search9.getLatitude(), search9.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search9 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search10.getLatitude(), search10.getLongitude()}));
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
    @Ignore
    public void testGetNearestVertexDefaultPerNodeWithRealDataSetMultiThread() throws InstantiationException {

        Assert.assertNotNull(graphDataIO);

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(new ArrayList<Geometrizable>(graphDataIO.getVertices()));

        Assert.assertNotNull(GeometryProcessorVertex.init(geometryDataIO, 5));
        GeometryProcessorVertex geometryProcessor = GeometryProcessorVertex.getInstance();
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
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search1.getLatitude(), search1.getLongitude()}));
            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search1 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search2.getLatitude(), search2.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search2 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search3.getLatitude(), search3.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search3 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search4.getLatitude(), search4.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search4 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search5.getLatitude(), search5.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search5 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search6.getLatitude(), search6.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search6 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search7.getLatitude(), search7.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search7 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search8.getLatitude(), search8.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search8 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search9.getLatitude(), search9.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search9 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search10.getLatitude(), search10.getLongitude()}));
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
    public void testGetNearestEdgeOnePerNodeMultiThreaded() throws InstantiationException, MalformedURLException {

        GeometryDataIO geometryDataIO = getGeometryDataIOEdgesOnePerNode();
        Assert.assertNotNull(GeometryProcessorEdge.init(geometryDataIO, 5));
        GeometryProcessorEdge geometryProcessor = GeometryProcessorEdge.getInstance();
        Geometrizable geometrizable = null;
        try {
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {5.5, 3.9}));
        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        } catch (GeometryComputationNoSlotsException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(geometrizable);
    }


    @Test
    @Ignore
    public void testGetNearestEdgeDefaultPerNodeMultiThreaded() throws InstantiationException, MalformedURLException {

        GeometryDataIO geometryDataIO = getGeometryDataIOEdgesDefaultPerNode();
        Assert.assertNotNull(GeometryProcessorEdge.init(geometryDataIO, 5));
        GeometryProcessorEdge geometryProcessor = GeometryProcessorEdge.getInstance();
        Geometrizable geometrizable = null;
        try {
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {5.5, 3.9}));
        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        } catch (GeometryComputationNoSlotsException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(geometrizable);
    }


    @Test
    @Ignore
    public void testGetNearestEdgeDefaultPerNodeWithRealDataSetMultiThread() throws InstantiationException {

        Assert.assertNotNull(graphDataIO);

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(new ArrayList<Geometrizable>(graphDataIO.getEdges()));

        Assert.assertNotNull(GeometryProcessorEdge.init(geometryDataIO, 5));
        GeometryProcessorEdge geometryProcessor = GeometryProcessorEdge.getInstance();
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
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search1.getLatitude(), search1.getLongitude()}));
            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search1 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search2.getLatitude(), search2.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search2 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search3.getLatitude(), search3.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search3 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search4.getLatitude(), search4.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search4 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search5.getLatitude(), search5.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search5 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search6.getLatitude(), search6.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search6 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search7.getLatitude(), search7.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search7 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search8.getLatitude(), search8.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search8 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search9.getLatitude(), search9.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search9 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search10.getLatitude(), search10.getLongitude()}));
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
    @Ignore
    public void testGetNearestPOIDefaultPerNodeWithRealDataSetMultiThread() throws InstantiationException {

        Assert.assertNotNull(locationDataIO);

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(new ArrayList<Geometrizable>(locationDataIO.getPOIs()));

        Assert.assertNotNull(GeometryProcessorPOI.init(geometryDataIO, 5));
        GeometryProcessorPOI geometryProcessor = GeometryProcessorPOI.getInstance();

        Coordinate search1 = new Coordinate(49.2323, 8.2334);
        Coordinate search2 = new Coordinate(49.004, 8.345345);
        Coordinate search3 = new Coordinate(49.0145, 8.2424);
        Coordinate search4 = new Coordinate(49.2323, 8.345435);
        Coordinate search5 = new Coordinate(49.3424, 8.0024234);
        Coordinate search6 = new Coordinate(49.1312, 8.2424);
        Coordinate search7 = new Coordinate(49.324524, 8.456456);
        Coordinate search8 = new Coordinate(100.24223434, 9.234234);
        Coordinate search9 = new Coordinate(10.234324, 7.3434324);
        Coordinate search10 = new Coordinate(49.00936, 8.42705);

        Geometrizable geometrizable = null;
        try {
            long startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search1.getLatitude(), search1.getLongitude()}));
            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search1 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search2.getLatitude(), search2.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search2 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search3.getLatitude(), search3.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search3 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search4.getLatitude(), search4.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search4 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search5.getLatitude(), search5.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search5 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search6.getLatitude(), search6.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search6 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search7.getLatitude(), search7.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search7 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search8.getLatitude(), search8.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search8 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search9.getLatitude(), search9.getLongitude()}));
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(geometrizable);
            logger.info("project: " + search9 + " to: " + geometrizable.toString());

            startTime = System.currentTimeMillis();
            geometrizable = geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search10.getLatitude(), search10.getLongitude()}));
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
    @Ignore
    public void testGetNearestPOIWithCategoryDefaultPerNodeWithRealDataSetMultiThread() throws InstantiationException, MalformedURLException {

        GeometryDataIO geometryDataIO = getGeometryDataPOIIODefaultPerNode();
        Assert.assertNotNull(geometryDataIO);

        GeometrizablePOIConstraint constPOI_1 = new GeometrizablePOIConstraint(new int[] { 0 });
        GeometrizablePOIConstraint constPOI_2 = new GeometrizablePOIConstraint(new int[] { 3 });
        GeometrizablePOIConstraint constPOI_3 = new GeometrizablePOIConstraint(new int[] { 4 });
        GeometrizablePOIConstraint constPOI_NONE = new GeometrizablePOIConstraint(new int[] { 8 });
        GeometrizablePOIConstraint constPOI_1_2 = new GeometrizablePOIConstraint(new int[] { 1 });
        GeometrizablePOIConstraint constPOI_1_3 = new GeometrizablePOIConstraint(new int[] { 0, 4 });
        GeometrizablePOIConstraint constPOI_2_3 = new GeometrizablePOIConstraint(new int[] { 3, 4 });

        Assert.assertNotNull(GeometryProcessorPOI.init(geometryDataIO, 5));
        GeometryProcessorPOI geometryProcessor = GeometryProcessorPOI.getInstance();

        Coordinate search1 = new Coordinate(49.2323, 8.2334);
        Coordinate search2 = new Coordinate(49.004, 8.345345);
        Coordinate search3 = new Coordinate(49.0145, 8.2424);
        Coordinate search4 = new Coordinate(49.2323, 8.345435);
        Coordinate search5 = new Coordinate(49.3424, 8.0024234);
        Coordinate search6 = new Coordinate(49.1312, 8.2424);
        Coordinate search7 = new Coordinate(49.324524, 8.456456);

        POI poi = null;
        try {
            long startTime = System.currentTimeMillis();
            poi = (POI) geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search1.getLatitude(), search1.getLongitude()}), constPOI_1);
            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(poi);
            Assert.assertEquals("POI_1", poi.getName());
            logger.info("project: " + search1 + " to: " + poi.toString());

            startTime = System.currentTimeMillis();
            poi = (POI) geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search2.getLatitude(), search2.getLongitude()}), constPOI_2);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(poi);
            Assert.assertEquals("POI_2", poi.getName());
            logger.info("project: " + search2 + " to: " + poi.toString());

            startTime = System.currentTimeMillis();
            poi = (POI) geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search3.getLatitude(), search3.getLongitude()}), constPOI_3);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(poi);
            Assert.assertEquals("POI_3", poi.getName());
            logger.info("project: " + search3 + " to: " + poi.toString());

            startTime = System.currentTimeMillis();
            poi = (POI) geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search4.getLatitude(), search4.getLongitude()}), constPOI_NONE);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNull(poi);

            startTime = System.currentTimeMillis();
            poi = (POI) geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search5.getLatitude(), search5.getLongitude()}), constPOI_1_2);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertNotNull(poi);
            Assert.assertEquals("POI_1", poi.getName());
            logger.info("project: " + search5 + " to: " + poi.toString());

            startTime = System.currentTimeMillis();
            poi = (POI) geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search6.getLatitude(), search6.getLongitude()}), constPOI_1_3);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertEquals("POI_1", poi.getName());
            Assert.assertNotNull(poi);
            logger.info("project: " + search6 + " to: " + poi.toString());

            startTime = System.currentTimeMillis();
            poi = (POI) geometryProcessor.getNearestGeometrizable(new GeometrySearch(new double[] {search7.getLatitude(), search7.getLongitude()}), constPOI_2_3);
            stopTime = System.currentTimeMillis();
            runTime = stopTime - startTime;
            logger.info("Run time: " + runTime);
            Assert.assertEquals("POI_3", poi.getName());
            Assert.assertNotNull(poi);
            logger.info("project: " + search7 + " to: " + poi.toString());

        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        } catch (GeometryComputationNoSlotsException e) {
            e.printStackTrace();
        }
    }


    private GeometryDataIO getGeometryDataIOVerticesOnePerNode() throws MalformedURLException {
        GraphDataIO graphDataIO = new GraphDataIO();
        graphDataIO.addEdge(new Edge(new Vertex(2.d, 3.d), new Vertex(5.d, 4.d)));
        graphDataIO.addEdge(new Edge(new Vertex(9.d, 6.d), new Vertex(4.d, 7.d)));
        graphDataIO.addEdge(new Edge(new Vertex(8.d, 1.d), new Vertex(7.d, 2.d)));

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(new ArrayList<Geometrizable>(graphDataIO.getVertices()), 1);
        return geometryDataIO;
    }


    private GeometryDataIO getGeometryDataIOVerticesDefaultPerNode() throws MalformedURLException {
        GraphDataIO graphDataIO = new GraphDataIO();
        graphDataIO.addEdge(new Edge(new Vertex(2.d, 3.d), new Vertex(5.d, 4.d)));
        graphDataIO.addEdge(new Edge(new Vertex(9.d, 6.d), new Vertex(4.d, 7.d)));
        graphDataIO.addEdge(new Edge(new Vertex(8.d, 1.d), new Vertex(7.d, 2.d)));

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(new ArrayList<Geometrizable>(graphDataIO.getVertices()));
        return geometryDataIO;
    }


    private GeometryDataIO getGeometryDataIOEdgesOnePerNode() throws MalformedURLException {
        GraphDataIO graphDataIO = new GraphDataIO();
        graphDataIO.addEdge(new Edge(new Vertex(2.d, 3.d), new Vertex(5.d, 4.d)));
        graphDataIO.addEdge(new Edge(new Vertex(9.d, 6.d), new Vertex(4.d, 7.d)));
        graphDataIO.addEdge(new Edge(new Vertex(8.d, 1.d), new Vertex(7.d, 2.d)));

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(new ArrayList<Geometrizable>(graphDataIO.getEdges()), 1);
        return geometryDataIO;
    }


    private GeometryDataIO getGeometryDataIOEdgesDefaultPerNode() throws MalformedURLException {
        GraphDataIO graphDataIO = new GraphDataIO();
        graphDataIO.addEdge(new Edge(new Vertex(2.d, 3.d), new Vertex(5.d, 4.d)));
        graphDataIO.addEdge(new Edge(new Vertex(9.d, 6.d), new Vertex(4.d, 7.d)));
        graphDataIO.addEdge(new Edge(new Vertex(8.d, 1.d), new Vertex(7.d, 2.d)));

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(new ArrayList<Geometrizable>(graphDataIO.getEdges()));
        return geometryDataIO;
    }


    private GeometryDataIO getGeometryDataPOIIODefaultPerNode() throws MalformedURLException {
        LocationDataIO locationDataIO = new LocationDataIO();
        locationDataIO.addPOI(new POI(49.2334, 8.2356, "POI_1", "info 1", new URL("https://de.wikipedia.org/w/index.php?printable=yes&title=Wikipedia"), new int[] { 0, 1, 2 }));
        locationDataIO.addPOI(new POI(50.2323, 8.3454, "POI_2", "info 2", new URL("https://de.wikipedia.org/w/index.php?printable=yes&title=Wikipedia"), new int[] { 1, 2, 3 }));
        locationDataIO.addPOI(new POI(48.9792, 8.6863, "POI_3", "info 3", new URL("https://de.wikipedia.org/w/index.php?printable=yes&title=Wikipedia"), new int[] { 4, 5, 6 }));

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(new ArrayList<Geometrizable>(locationDataIO.getPOIs()));
        return geometryDataIO;
    }

}

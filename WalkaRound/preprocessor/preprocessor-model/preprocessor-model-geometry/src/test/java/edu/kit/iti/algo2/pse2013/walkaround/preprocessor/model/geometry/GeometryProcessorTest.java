package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Graph;
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


    @Before
    public void resetSingletonBefore() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = Graph.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        instance = GeometryProcessor.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        Field idCounter = Vertex.class.getDeclaredField("idCounter");
        idCounter.setAccessible(true);
        idCounter.setInt(null, 0);

        idCounter = Edge.class.getDeclaredField("idCounter");
        idCounter.setAccessible(true);
        idCounter.setInt(null, 0);
    }


    @After
    public void resetSingletonAfter() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = Graph.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        instance = GeometryProcessor.class.getDeclaredField("instance");
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
        GeometryDataIO geometryDataIO = getGeometryDataIO();
        Assert.assertNotNull(GeometryProcessor.init(geometryDataIO));
    }


    @Test
    public void testGetInstance() throws InstantiationException {
        GeometryDataIO geometryDataIO = getGeometryDataIO();
        Assert.assertNotNull(GeometryProcessor.init(geometryDataIO));
        Assert.assertNotNull(GeometryProcessor.getInstance());
    }


    @Test
    public void testGetNearestVertex() throws InstantiationException {

        GeometryDataIO geometryDataIO = getGeometryDataIO();
        Assert.assertNotNull(GeometryProcessor.init(geometryDataIO));
        GeometryProcessor geometryProcessor = GeometryProcessor.getInstance();
        Coordinate search = new Coordinate(5.5, 3.9);
        Geometrizable geometrizable = null;
        try {
            geometrizable = geometryProcessor.getNearestVertex(search);
        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(geometrizable);
        Assert.assertEquals(((Vertex) geometrizable).getID(), 1);
    }


    @Test
    public void testGetNearestVertexWithRealDataSet() throws InstantiationException {

        File graphDataio = new File(System.getProperty("java.io.tmpdir") + File.separatorChar + "graphData.pbf");
        Assert.assertNotNull(graphDataio);
        Assert.assertTrue(graphDataio.exists());

        File locationDataio = new File(System.getProperty("java.io.tmpdir") + File.separatorChar + "locationData.pbf");
        Assert.assertNotNull(locationDataio);
        Assert.assertTrue(locationDataio.exists());

        GraphDataIO graphDataIO = null;
        try {
            graphDataIO = GraphDataIO.load(graphDataio);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(graphDataIO);

        LocationDataIO locationData = null;
        try {
            locationData = LocationDataIO.load(locationDataio);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(locationData);

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(graphDataIO, locationData);

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
        Coordinate search9 = new Coordinate(49.00936, 8.42705);

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

        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        }
    }


    private GeometryDataIO getGeometryDataIO() {
        GraphDataIO graphDataIO = new GraphDataIO();
        graphDataIO.addEdge(new Edge(new Vertex(2.d, 3.d), new Vertex(5.d, 4.d)));
        graphDataIO.addEdge(new Edge(new Vertex(9.d, 6.d), new Vertex(4.d, 7.d)));
        graphDataIO.addEdge(new Edge(new Vertex(8.d, 1.d), new Vertex(7.d, 2.d)));

        LocationDataIO locationDataIO = new LocationDataIO();
        locationDataIO.addPOI(new POI(1.d, 2.d, "poi 1", "info 1", "url 1", new int[]{0, 1}));
        locationDataIO.addPOI(new POI(3.d, 4.d, "poi 2", "info 2", "url 2", new int[]{0, 1}));
        locationDataIO.addPOI(new POI(5.d, 7.d, "poi 3", "info 3", "url 3", new int[]{0, 1}));

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(graphDataIO, locationDataIO);
        return geometryDataIO;
    }

}

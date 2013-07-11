package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;


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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * GeometryProcessorTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryProcessorTest {

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
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

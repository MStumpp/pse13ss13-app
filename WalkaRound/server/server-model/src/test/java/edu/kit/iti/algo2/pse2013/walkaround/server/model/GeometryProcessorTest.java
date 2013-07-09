package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * GeometryProcessorTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryProcessorTest {

    @Test
    public void testGetInstance() {
        GeometryDataIO geometryDataIO = getGeometryDataIO();
        GeometryProcessor geometryProcessor = GeometryProcessor.getInstance(geometryDataIO);
        Assert.assertNotNull(geometryProcessor);
    }


    @Test
    public void testGetNearestVertex() {

        GeometryDataIO geometryDataIO = getGeometryDataIO();
        GeometryProcessor geometryProcessor = GeometryProcessor.getInstance(geometryDataIO);
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
        locationDataIO.addPOI(new POI(1.d, 2.d, 1, "poi 1", "info 1", "url 1", new int[]{0, 1}));
        locationDataIO.addPOI(new POI(3.d, 4.d, 2, "poi 2", "info 2", "url 2", new int[]{0, 1}));
        locationDataIO.addPOI(new POI(5.d, 7.d, 3, "poi 3", "info 3", "url 3", new int[]{0, 1}));

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(graphDataIO, locationDataIO);
        return geometryDataIO;
    }

}

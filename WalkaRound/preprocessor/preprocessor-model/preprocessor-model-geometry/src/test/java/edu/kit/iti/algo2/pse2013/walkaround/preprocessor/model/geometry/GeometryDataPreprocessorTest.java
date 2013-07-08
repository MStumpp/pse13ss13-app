package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import org.junit.Assert;
import org.junit.Test;


/**
 * GeometryDataPreprocessorTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryDataPreprocessorTest {

    @Test
    public void testPreprocessGraphDataIO() {
        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(getGraphDataIO(), getLocationDataIO());

        // check that root is not null
        Assert.assertNotNull(geometryDataIO.getRoot());

        // check number of dimensions is 2
        Assert.assertEquals(geometryDataIO.getNumDimensions(), 2);

        GeometryNode root_1 = geometryDataIO.getRoot();
        Assert.assertEquals(root_1.getSplitValue(), 5.d, 0.d);
        Assert.assertNull(root_1.getGeometrizable());
        Assert.assertNull(root_1.getParent());
        Assert.assertNotNull(root_1.getLeftNode());
        Assert.assertNotNull(root_1.getRightNode());

        GeometryNode depth_1_root_left_2 = root_1.getLeftNode();
        Assert.assertEquals(depth_1_root_left_2.getSplitValue(), 4.d, 0.d);
        Assert.assertNull(depth_1_root_left_2.getGeometrizable());
        Assert.assertEquals(depth_1_root_left_2.getParent(), root_1);
        Assert.assertNotNull(depth_1_root_left_2.getLeftNode());
        Assert.assertNotNull(depth_1_root_left_2.getRightNode());

        GeometryNode depth_1_root_right_3 = root_1.getRightNode();
        Assert.assertEquals(depth_1_root_right_3.getSplitValue(), 2.d, 0.d);
        Assert.assertNull(depth_1_root_right_3.getGeometrizable());
        Assert.assertEquals(depth_1_root_right_3.getParent(), root_1);
        Assert.assertNotNull(depth_1_root_right_3.getLeftNode());
        Assert.assertNotNull(depth_1_root_right_3.getRightNode());

        GeometryNode depth_2_2_left_4 = depth_1_root_left_2.getLeftNode();
        Assert.assertEquals(depth_2_2_left_4.getSplitValue(), 2.d, 0.d);
        Assert.assertNull(depth_2_2_left_4.getGeometrizable());
        Assert.assertEquals(depth_2_2_left_4.getParent(), depth_1_root_left_2);
        Assert.assertNotNull(depth_2_2_left_4.getLeftNode());
        Assert.assertNotNull(depth_2_2_left_4.getRightNode());

        GeometryNode depth_2_2_right_5 = depth_1_root_left_2.getRightNode();
        Assert.assertEquals(Double.NaN, depth_2_2_right_5.getSplitValue(), 0.d);
        Assert.assertNotNull(depth_2_2_right_5.getGeometrizable());
        Assert.assertEquals(depth_2_2_right_5.getParent(), depth_1_root_left_2);
        Assert.assertNull(depth_2_2_right_5.getLeftNode());
        Assert.assertNull(depth_2_2_right_5.getRightNode());
        Assert.assertEquals(depth_2_2_right_5.getGeometrizable().valueForDimension(0), 4.d, 0.d);
        Assert.assertEquals(depth_2_2_right_5.getGeometrizable().valueForDimension(1), 7.d, 0.d);
        Assert.assertNotNull(depth_2_2_right_5.getGeometrizable());

        GeometryNode depth_3_4_right_6 = depth_2_2_left_4.getLeftNode();
        Assert.assertEquals(Double.NaN, depth_3_4_right_6.getSplitValue(), 0.d);
        Assert.assertNotNull(depth_3_4_right_6.getGeometrizable());
        Assert.assertEquals(depth_3_4_right_6.getGeometrizable().valueForDimension(0), 2.d, 0.d);
        Assert.assertEquals(depth_3_4_right_6.getGeometrizable().valueForDimension(1), 3.d, 0.d);
        Assert.assertEquals(depth_3_4_right_6.getParent(), depth_2_2_left_4);
        Assert.assertNull(depth_3_4_right_6.getLeftNode());
        Assert.assertNull(depth_3_4_right_6.getRightNode());

        GeometryNode depth_3_4_right_7 = depth_2_2_left_4.getRightNode();
        Assert.assertEquals(Double.NaN, depth_3_4_right_7.getSplitValue(), 0.d);
        Assert.assertNotNull(depth_3_4_right_7.getGeometrizable());
        Assert.assertEquals(depth_3_4_right_7.getGeometrizable().valueForDimension(0), 5.d, 0.d);
        Assert.assertEquals(depth_3_4_right_7.getGeometrizable().valueForDimension(1), 4.d, 0.d);
        Assert.assertEquals(depth_3_4_right_7.getParent(), depth_2_2_left_4);
        Assert.assertNull(depth_3_4_right_7.getLeftNode());
        Assert.assertNull(depth_3_4_right_7.getRightNode());

        GeometryNode depth_2_3_left_8 = depth_1_root_right_3.getLeftNode();
        Assert.assertEquals(depth_2_3_left_8.getSplitValue(), 7.d, 0.d);
        Assert.assertNull(depth_2_3_left_8.getGeometrizable());
        Assert.assertEquals(depth_2_3_left_8.getParent(), depth_1_root_right_3);
        Assert.assertNotNull(depth_2_3_left_8.getLeftNode());
        Assert.assertNotNull(depth_2_3_left_8.getRightNode());

        GeometryNode depth_2_3_right_9 = depth_1_root_right_3.getRightNode();
        Assert.assertEquals(Double.NaN, depth_2_3_right_9.getSplitValue(), 0.d);
        Assert.assertNotNull(depth_2_3_right_9.getGeometrizable());
        Assert.assertEquals(depth_2_3_right_9.getParent(), depth_1_root_right_3);
        Assert.assertNull(depth_2_3_right_9.getLeftNode());
        Assert.assertNull(depth_2_3_right_9.getRightNode());
        Assert.assertEquals(depth_2_3_right_9.getGeometrizable().valueForDimension(0), 9.d, 0.d);
        Assert.assertEquals(depth_2_3_right_9.getGeometrizable().valueForDimension(1), 6.d, 0.d);
        Assert.assertNotNull(depth_2_3_right_9.getGeometrizable());

        GeometryNode depth_3_8_right_10 = depth_2_3_left_8.getLeftNode();
        Assert.assertEquals(Double.NaN, depth_3_8_right_10.getSplitValue(), 0.d);
        Assert.assertNotNull(depth_3_8_right_10.getGeometrizable());
        Assert.assertEquals(depth_3_8_right_10.getGeometrizable().valueForDimension(0), 7.d, 0.d);
        Assert.assertEquals(depth_3_8_right_10.getGeometrizable().valueForDimension(1), 2.d, 0.d);
        Assert.assertEquals(depth_3_8_right_10.getParent(), depth_2_3_left_8);
        Assert.assertNull(depth_3_8_right_10.getLeftNode());
        Assert.assertNull(depth_3_8_right_10.getRightNode());

        GeometryNode depth_3_8_right_11 = depth_2_3_left_8.getRightNode();
        Assert.assertEquals(Double.NaN, depth_3_8_right_11.getSplitValue(), 0.d);
        Assert.assertNotNull(depth_3_8_right_11.getGeometrizable());
        Assert.assertEquals(depth_3_8_right_11.getGeometrizable().valueForDimension(0), 8.d, 0.d);
        Assert.assertEquals(depth_3_8_right_11.getGeometrizable().valueForDimension(1), 1.d, 0.d);
        Assert.assertEquals(depth_3_8_right_11.getParent(), depth_2_3_left_8);
        Assert.assertNull(depth_3_8_right_11.getLeftNode());
        Assert.assertNull(depth_3_8_right_11.getRightNode());
    }

    private GraphDataIO getGraphDataIO() {
        GraphDataIO graphDataIO = new GraphDataIO();
        graphDataIO.addEdge(new Edge(new Vertex(2.d, 3.d), new Vertex(5.d, 4.d)));
        graphDataIO.addEdge(new Edge(new Vertex(9.d, 6.d), new Vertex(4.d, 7.d)));
        graphDataIO.addEdge(new Edge(new Vertex(8.d, 1.d), new Vertex(7.d, 2.d)));
        return graphDataIO;
    }

    private LocationDataIO getLocationDataIO() {
        LocationDataIO locationDataIO = new LocationDataIO();
        locationDataIO.addPOI(new POI(1.d, 2.d, 1, "poi 1", "info 1", "url 1", new int[]{0, 1}));
        locationDataIO.addPOI(new POI(3.d, 4.d, 2, "poi 2", "info 2", "url 2", new int[]{0, 1}));
        locationDataIO.addPOI(new POI(5.d, 7.d, 3, "poi 3", "info 3", "url 3", new int[]{0, 1}));
        return locationDataIO;
    }

}

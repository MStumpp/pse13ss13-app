package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * GeometryNodeTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryNodeTest {

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field idCounter = Vertex.class.getDeclaredField("idCounter");
        idCounter.setAccessible(true);
        idCounter.setInt(null, 0);
    }


    @Test
    public void testInitialization1() {

        GeometryNode parent = new GeometryNode(1.d);
        int depth = 0;
        Vertex vertex = new Vertex(1.d, 2.d);
        GeometryNode node = new GeometryNode(parent, depth, vertex);

        Assert.assertEquals(node.getParent(), parent);
        Assert.assertEquals(node.getDepth(), depth);
        //Assert.assertEquals(node.getGeometrizable(), vertex);
        Assert.assertEquals(node.getSplitValue(), Double.NaN, 0.d);
    }


    @Test
    public void testInitialization2() {

        GeometryNode parent = new GeometryNode(1.d);
        int depth = 0;
        double splitValue = 3.5;
        GeometryNode node = new GeometryNode(parent, depth, splitValue);

        Assert.assertEquals(node.getParent(), parent);
        Assert.assertEquals(node.getDepth(), depth);
        //Assert.assertNull(node.getGeometrizable());
        Assert.assertEquals(node.getSplitValue(), splitValue, 0.d);
    }

}

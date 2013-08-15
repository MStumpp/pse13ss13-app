package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import java.lang.reflect.Field;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

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
        Vertex vertex = new Vertex(1.d, 2.d);
        LinkedList<Geometrizable> list = new LinkedList<Geometrizable>();
        list.add(vertex);
        GeometryNode node = new GeometryNode(parent, list);

        Assert.assertEquals(node.getParent(), parent);
        Assert.assertEquals(node.getDepth(), 0);
        Assert.assertTrue(node.getGeometrizables().containsAll(list));
        Assert.assertTrue(list.containsAll(node.getGeometrizables()));
        Assert.assertEquals(node.getSplitValue(), Double.NaN, 0.d);
    }


    @Test
    public void testInitialization2() {

        GeometryNode parent = new GeometryNode(1.d);
        double splitValue = 3.5;
        GeometryNode node = new GeometryNode(parent, splitValue);

        Assert.assertEquals(node.getParent(), parent);
        Assert.assertEquals(node.getDepth(), 0);
        Assert.assertNull(node.getGeometrizable());
        Assert.assertEquals(node.getSplitValue(), splitValue, 0.d);
    }

}

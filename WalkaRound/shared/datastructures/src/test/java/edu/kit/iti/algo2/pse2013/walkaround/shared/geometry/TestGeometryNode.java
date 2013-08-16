package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

/**
 * GeometryNodeTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class TestGeometryNode {

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
        Assert.assertTrue(node.getGeometrizables().size() <= 0);
        Assert.assertEquals(node.getSplitValue(), splitValue, 0.d);
    }

    @Test
    public void testToString() {
    	GeometryNode gn = new GeometryNode(5, new LinkedList<Geometrizable>());
    	String str = gn.toString();
    	assertTrue(str.contains("" + gn.getDepth()));
    	assertTrue(str.contains("" + gn.getSplitValue()));
    	str.contains("no vertex");
    	gn.addGeometrizable(new Edge(new Vertex(0, 0), new Vertex(0, 0)));
    	str = gn.toString();
    	assertTrue(str.contains("geometrizable"));
    	assertFalse(str.contains("leftNode"));
    	assertFalse(str.contains("rightNode"));
    	GeometryNode gn_left = new GeometryNode(4);
    	GeometryNode gn_right = new GeometryNode(6);
    	gn.setLeftNode(gn_left);
    	str = gn.toString();
    	assertTrue(str.contains("leftNode"));
    	gn.setRightNode(gn_right);
    	str = gn.toString();
    	assertTrue(str.contains("rightNode"));
    	assertTrue(str.contains("leftNode"));
    }

    @Test
    public void testNearestGeom() {
    	LinkedList<Geometrizable> list = new LinkedList<Geometrizable>();
    	Vertex v = new Vertex(0, 0);
    	list.add(v);
    	GeometryNode gn = new GeometryNode(null, 5, list);
    	assertEquals(v, gn.getNearestGeometrizable(5, null, 0));
    }

    @Test
    public void testHashCode() {
    	GeometryNode gn = new GeometryNode(5, new LinkedList<Geometrizable>());
    	GeometryNode gn2 = new GeometryNode(5, new LinkedList<Geometrizable>());
    	GeometryNode gn3 = new GeometryNode(6, new LinkedList<Geometrizable>());
    	assertEquals(gn.hashCode(), gn2.hashCode());
    	assertNotEquals(gn.hashCode(), gn3.hashCode());
    	assertNotEquals(gn3.hashCode(), gn2.hashCode());
    	GeometryNode gn4 = new GeometryNode(6, null);
    	GeometryNode gn5 = new GeometryNode(6, null);
    	gn4.setLeftNode(new GeometryNode(1));
    	gn5.setRightNode(new GeometryNode(2));
    	assertNotEquals(gn4.hashCode(), gn5.hashCode());
    }
}

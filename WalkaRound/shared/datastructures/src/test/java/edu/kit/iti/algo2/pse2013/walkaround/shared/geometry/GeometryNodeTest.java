package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * GeometryNodeTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryNodeTest {

    @Test
    public void testInit1() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        GeometryNode node2 = new GeometryNode(node1, geoms);
    }

    @Test
    public void testInit2() {
        GeometryNode node1 = new GeometryNode(null, 1.d);
    }

    @Test
    public void testInit3() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        GeometryNode node2 =  new GeometryNode(node1, 1.d, geoms);
    }

    @Test
    public void testInit4() {
        GeometryNode node1 =  new GeometryNode(1.d);
    }

    @Test
    public void testInit5() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 =  new GeometryNode(1.d, geoms);
    }

    @Test
    public void testGetNoSplitValue() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        GeometryNode node2 =  new GeometryNode(node1, 1.d, geoms);
        Assert.assertEquals(1.d, node2.getSplitValue(), 0.d);
    }

    @Test
    public void testGetSplitValue() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        GeometryNode node2 =  new GeometryNode(node1, 1.d, geoms);
        Assert.assertNotEquals(Double.NaN, node2.getSplitValue());
    }

    @Test
    public void testGetNoParent() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        GeometryNode node2 =  new GeometryNode(node1, 1.d, geoms);
        Assert.assertNull(node1.getParent());
        Assert.assertNotNull(node2.getParent());
    }

    @Test
    public void testGetParent() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        GeometryNode node2 =  new GeometryNode(node1, 1.d, geoms);
        Assert.assertEquals(node1, node2.getParent());
    }

    @Test
    public void testGetDepth() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        Assert.assertEquals(0, node1.getDepth());
    }

    @Test
    public void testGetLeftNode() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        GeometryNode node2 = new GeometryNode(null, geoms);
        Assert.assertNull(node1.getLeftNode());
        node1.setLeftNode(node2);
        Assert.assertNotNull(node1.getLeftNode());
    }

    @Test
    public void testSetLeftNode() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        GeometryNode node2 = new GeometryNode(null, geoms);
        Assert.assertNull(node1.getLeftNode());
        node1.setLeftNode(node2);
        Assert.assertNotNull(node1.getLeftNode());
    }

    @Test
    public void testGetRightNode() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        GeometryNode node2 = new GeometryNode(null, geoms);
        Assert.assertNull(node1.getRightNode());
        node1.setRightNode(node2);
        Assert.assertNotNull(node1.getRightNode());
    }

    @Test
    public void testSetRightNode() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        GeometryNode node2 = new GeometryNode(null, geoms);
        Assert.assertNull(node1.getRightNode());
        node1.setRightNode(node2);
        Assert.assertNotNull(node1.getRightNode());
    }

    @Test
    public void testGetNearestGeometrizable() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);
        geoms.add(poi2);

        GeometryNode node1 = new GeometryNode(null, geoms);
        Assert.assertNotNull(node1.getNearestGeometrizable(3.d, null, 0));
        Assert.assertNotNull(node1.getNearestGeometrizable(3.d, null, 1));
    }

    @Test
    public void testAddGeometrizable() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);

        GeometryNode node1 = new GeometryNode(null, geoms);
        Assert.assertEquals(1, node1.getGeometrizables().size());
        geoms.add(poi2);
        Assert.assertEquals(2, node1.getGeometrizables().size());
    }

    @Test
    public void testIsRoot() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);

        GeometryNode node1 = new GeometryNode(null, geoms);
        Assert.assertTrue(node1.isRoot());
    }

    @Test
    public void testIsLeaf() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);

        GeometryNode node1 = new GeometryNode(null, geoms);
        Assert.assertTrue(node1.isLeaf());
    }

    @Test
    public void testToString() {
        POI poi1 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        POI poi2 = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        List<Geometrizable> geoms = new ArrayList<Geometrizable>();
        geoms.add(poi1);

        GeometryNode node1 = new GeometryNode(null, geoms);
        Assert.assertNotNull(node1.toString());
    }

}

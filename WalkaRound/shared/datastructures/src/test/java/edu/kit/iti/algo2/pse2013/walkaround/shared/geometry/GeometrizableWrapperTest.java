package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import org.junit.Assert;
import org.junit.Test;

/**
 * GeometrizableWrapperTest
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometrizableWrapperTest {

    @Test
    public void testInit() {
        POI poi = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        GeometrizableWrapper instance = new GeometrizableWrapper(poi, 1);
    }

    @Test
    public void testNumberDimensions() {
        POI poi = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        GeometrizableWrapper instance = new GeometrizableWrapper(poi, 1);
        Assert.assertEquals(2, instance.numberDimensions());
    }

    @Test
    public void testNumberNodes() {
        POI poi = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        GeometrizableWrapper instance = new GeometrizableWrapper(poi, 1);
        Assert.assertEquals(1, instance.numberNodes());
    }

    @Test
    public void testNodeNotNull1() {
        POI poi = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        GeometrizableWrapper instance = new GeometrizableWrapper(poi, 1);
        Assert.assertNotNull(instance.getNode(0));
    }

    @Test
    public void testNodeNotNullGeometrizable() {
        POI poi = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        GeometrizableWrapper instance = new GeometrizableWrapper(poi, 1);
        Assert.assertNotNull(instance.getGeometrizable());
    }

    @Test
    public void testToString() {
        POI poi = new POI(1.d, 2.d, "name", "info", null, new int[]{1}, null);
        GeometrizableWrapper instance = new GeometrizableWrapper(poi, 1);
        Assert.assertNotNull(instance.toString());
    }

}

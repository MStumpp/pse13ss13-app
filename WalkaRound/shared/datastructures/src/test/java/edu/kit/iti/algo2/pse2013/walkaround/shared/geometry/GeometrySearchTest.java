package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import org.junit.Assert;
import org.junit.Test;

/**
 * GeometrySearchTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometrySearchTest {

    @Test
    public void testInit() {
        new GeometrySearch(new double[] { 1.d, 2.d });
    }

    @Test
    public void testInit2() {
        GeometrySearch geom = new GeometrySearch(new double[] { 1.d, 2.d });
        Assert.assertEquals(1.d, geom.valueForDimension(0), 0.d);
        Assert.assertEquals(2.d, geom.valueForDimension(1), 0.d);
    }

    @Test
    public void testInit3() {
        GeometrySearch geom = new GeometrySearch(new double[] { 1.d, 2.d });
        Assert.assertEquals(2, geom.getNumberDimensions());
    }

}

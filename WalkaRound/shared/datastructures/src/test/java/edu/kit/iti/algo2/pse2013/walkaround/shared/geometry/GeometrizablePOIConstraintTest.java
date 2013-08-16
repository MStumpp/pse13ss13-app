package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * GeometrizablePOIConstraintTest
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometrizablePOIConstraintTest {

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

    }

    @Test
    public void testInitialization() {
        new GeometrizablePOIConstraint(new int[] {1, 2});
    }

    @Test
    public void testIsValidMethod() {
        GeometrizablePOIConstraint instance = new GeometrizablePOIConstraint(new int[] {1, 2, 3, 4, 5});
        Assert.assertTrue(instance.isValid(new POI(1.d, 2.d, "name", "info", null, new int[] { 1 }, null)));
    }

    @Test
    public void testIsNotValidMethod() {
        GeometrizablePOIConstraint instance = new GeometrizablePOIConstraint(new int[] {1, 2, 3, 4, 5});
        Assert.assertTrue(!instance.isValid(new POI(1.d, 2.d, "name", "info", null, new int[] { 6 }, null)));
    }

}
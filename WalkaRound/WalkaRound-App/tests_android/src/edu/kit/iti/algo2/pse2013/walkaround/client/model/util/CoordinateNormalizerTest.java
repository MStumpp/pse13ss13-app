package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

@RunWith(RobolectricTestRunner.class)
public class CoordinateNormalizerTest {

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentException()
			throws CoordinateNormalizerException, InterruptedException {
		CoordinateNormalizer.normalizeCoordinate(null, 10);
	}

	@Test(expected = CoordinateNormalizerException.class)
	public void testCoordinateNormalizerException()
			throws CoordinateNormalizerException, InterruptedException {
		CoordinateNormalizer.normalizeCoordinate(new Coordinate(2d, 5d), 10);
	}
}

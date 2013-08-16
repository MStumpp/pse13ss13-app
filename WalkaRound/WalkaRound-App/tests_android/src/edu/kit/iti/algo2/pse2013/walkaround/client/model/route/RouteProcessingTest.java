package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

@RunWith(RobolectricTestRunner.class)
public class RouteProcessingTest {

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentException1() throws RouteProcessingException, InterruptedException {
		RouteProcessing.getInstance().computeRoundtrip(null, 2, 300);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentException2() throws RouteProcessingException, InterruptedException {
		RouteProcessing.getInstance().computeRoundtrip(new Coordinate(2d, 5d), -1, 500);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentException3() throws RouteProcessingException, InterruptedException {
		RouteProcessing.getInstance().computeRoundtrip(null, 2, 50);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentException4() throws RouteProcessingException, InterruptedException {
		RouteProcessing.getInstance().computeShortestPath(null, null);
	}
}

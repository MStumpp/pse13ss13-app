package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RouteProcessingExceptionTest {
	
	@Test
	public void testException() {
		RouteProcessingException rex = new RouteProcessingException("test");
		rex = new RouteProcessingException(null);
	}
}

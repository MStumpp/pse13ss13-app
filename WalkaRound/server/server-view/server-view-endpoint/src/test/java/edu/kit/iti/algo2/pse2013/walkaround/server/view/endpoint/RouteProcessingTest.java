package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint;

import edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint.client.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint.client.RouteProcessing;
import edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint.client.RouteProcessingException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * RouteProcessingTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class RouteProcessingTest {

    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void testException1() throws IllegalArgumentException, RouteProcessingException {
        RouteProcessing routeProcessing = RouteProcessing.getInstance();
        Coordinate coordinate1 = new Coordinate(2.d, 3.d);
        routeProcessing.computeShortestPath(coordinate1, null);
    }


    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void testException2() throws IllegalArgumentException, RouteProcessingException {
        RouteProcessing routeProcessing = RouteProcessing.getInstance();
        Coordinate coordinate2 = new Coordinate(2.d, 3.d);
        routeProcessing.computeShortestPath(null, coordinate2);
    }


    @Test
    @Ignore
    public void testRouteReturned() throws IllegalArgumentException, RouteProcessingException {
        RouteProcessing routeProcessing = RouteProcessing.getInstance();
        Coordinate coordinate1 = new Coordinate(2.d, 3.d);
        Coordinate coordinate2 = new Coordinate(2.d, 3.d);
        RouteInfo route = routeProcessing.computeShortestPath(coordinate1, coordinate2);
        Assert.assertNotNull(route);
    }


    @Test
    @Ignore
    public void testRouteReturnedSizeAtLeast2() throws IllegalArgumentException, RouteProcessingException {
        RouteProcessing routeProcessing = RouteProcessing.getInstance();
        Coordinate coordinate1 = new Coordinate(2.d, 3.d);
        Coordinate coordinate2 = new Coordinate(2.d, 3.d);
        RouteInfo route = routeProcessing.computeShortestPath(coordinate1, coordinate2);
        Assert.assertNotEquals(route.getCoordinates().size(), 0);
        Assert.assertNotEquals(route.getCoordinates().size(), 1);
    }


    @Test
    @Ignore
    public void testRouteReturnedTwoWaypoints() throws IllegalArgumentException, RouteProcessingException {
        RouteProcessing routeProcessing = RouteProcessing.getInstance();
        Coordinate coordinate1 = new Coordinate(2.d, 3.d);
        Coordinate coordinate2 = new Coordinate(2.d, 3.d);
        RouteInfo route = routeProcessing.computeShortestPath(coordinate1, coordinate2);
        Assert.assertEquals(route.getWaypoints().size(), 2);
    }


    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void testComputeRoundtripException() throws IllegalArgumentException, RouteProcessingException {
        RouteProcessing routeProcessing = RouteProcessing.getInstance();
        routeProcessing.computeRoundtrip(null, 1, 100);
    }


    @Test
    @Ignore
    public void testComputeRoundtripRouteReturned() throws IllegalArgumentException, RouteProcessingException {
        RouteProcessing routeProcessing = RouteProcessing.getInstance();
        Coordinate coordinate = new Coordinate(2.d, 3.d);
        RouteInfo route = routeProcessing.computeRoundtrip(coordinate, 1, 100);
        Assert.assertNotNull(route);
    }


    @Test
    @Ignore
    public void testComputeRoundtripRouteReturnedSizeAtLeast2() throws IllegalArgumentException, RouteProcessingException {
        RouteProcessing routeProcessing = RouteProcessing.getInstance();
        Coordinate coordinate = new Coordinate(2.d, 3.d);
        RouteInfo route = routeProcessing.computeRoundtrip(coordinate, 1, 100);
        Assert.assertNotEquals(route.getCoordinates().size(), 0);
        Assert.assertNotEquals(route.getCoordinates().size(), 1);
    }


    @Test
    @Ignore
    public void testComputeRoundtripRouteReturnedTwoWaypoints() throws IllegalArgumentException, RouteProcessingException {
        RouteProcessing routeProcessing = RouteProcessing.getInstance();
        Coordinate coordinate = new Coordinate(2.d, 3.d);
        RouteInfo route = routeProcessing.computeRoundtrip(coordinate, 1, 100);
        Assert.assertEquals(route.getWaypoints().size(), 2);
    }

}

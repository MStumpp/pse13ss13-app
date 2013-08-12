package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class TestRoute {
	
	
	
	
	@Test
	public void testClone() {
		LinkedList<Coordinate> coordList = new LinkedList<Coordinate>();
		Waypoint wp1 = new Waypoint(47.00234, 8.023240, "name 1");
		Coordinate cor1 = new Coordinate(47.02234, 8.3240);
		Waypoint wp2 = new Waypoint(47.02454, 8.4240, "name 2");
		
		coordList.add(wp1);
		coordList.add(cor1);
		coordList.add(wp2);
		Route testRoute = new Route(coordList);
		
		Route clonedTestroute = (Route) testRoute.clone();
		
		assertTrue(testRoute != clonedTestroute);
		
		assertTrue(testRoute.getCoordinates().size() == clonedTestroute.getCoordinates().size());
		for (int i = 0; i < testRoute.getCoordinates().size(); i++) {
			assertTrue(testRoute.getCoordinates().get(i) != clonedTestroute.getCoordinates().get(i));
			
		}
		
		
	}
	
	
	
	
}

package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class TestRoute {
	
	private final int minNumberOfWaypoints = 0;
	private final int maxNumberOfWaypoints = 15;
	
	
	@Test
	public void testSetActiveWaypointByID() {
		
	}
	
	@Test
	public void testSetActiveWaypointByReference() {
		
	}
	
	@Test
	public void testResetActiveWaypoint() {
		
	}
	
	@Test
	public void moveActiveWaypointInOrder() {
		
	}
	
	
	
	
	
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
	
	
	
	
	
	public Route getRandomKarlsruheRoute() {
		LinkedList<Coordinate> coordsList = new LinkedList<Coordinate>();
		double randomNumberOfWaypoints = this.getRandomNumberWithinBounds(this.minNumberOfWaypoints, this.maxNumberOfWaypoints);
		
		for (int i = 0; i < randomNumberOfWaypoints; i++) {
			Coordinate tempCoord = this.getRandomKarlsruheCoordinate();
			coordsList.add(new Waypoint(tempCoord.getLatitude(), tempCoord.getLongitude(), ""));
		}
		
		return new Route(coordsList);
	}
	
	
	
	
	
	private Coordinate getRandomKarlsruheCoordinate() {
		double randomLatitude;
		double randomLongitude;
		
		// UpperLeftBound:
		double upperLeftLimitLatitude = 49.039668;
		double upperLeftLimitLongitude = 8.346176;
		
		// LowerRightBound:
		double lowerRightLimitLatitude = 48.972104;
		double lowerRightLimitLongitude = 8.471145;
		
		double latitudeDifference = upperLeftLimitLatitude - lowerRightLimitLatitude;
		double longitudeDifference = upperLeftLimitLongitude - lowerRightLimitLongitude;
		
		randomLatitude = upperLeftLimitLatitude + Math.random() * latitudeDifference;
		randomLongitude = upperLeftLimitLongitude + Math.random() * longitudeDifference;
		
		return new Coordinate(randomLatitude, randomLongitude);
	}
	
	private int getRandomNumberWithinBounds(int lowerBound, int upperBound) {
		int output = 0;
		
		if (lowerBound < 0 || upperBound < 0) {
			return output;
		}
		
		double difference = upperBound - lowerBound;
		
		return lowerBound + (int) (difference *	Math.random());
		
	}
	
	
	
	
	
	
}

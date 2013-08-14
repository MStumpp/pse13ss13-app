package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class TestRoute {
	
	private final int minNumberOfWaypoints = 0;
	private final int maxNumberOfWaypoints = 15;
	
	private final int testIterations = 10;
	
	@Test
	public void testSetActiveWaypointByID() {
		// TODO:
	}
	
	@Test
	public void testSetActiveWaypointByReference() {
		// TODO:
	}
	
	@Test
	public void testResetActiveWaypoint() {
		// TODO:
	}
	
	@Test
	public void testMoveActiveWaypointInOrder() {
		// Not important!
	}
	
	@Test
	public void testAddWaypoint() {
			Route randomRoute = this.getRandomKarlsruheRoute();
			Coordinate randomCoord = this.getRandomKarlsruheCoordinate();
			
			Route testRoute = randomRoute;
			testRoute.setActiveWaypoint(testRoute.getEnd());
			
			Waypoint wp = new Waypoint(randomCoord.getLatitude(), randomCoord.getLongitude(), "");
			testRoute.addWaypoint(wp);
			
			assertTrue(randomRoute.getWaypoints().size() + 1 == testRoute.getWaypoints().size());
	}
	
	@Test
	public void testAddRoundtripAtActiveWaypoint() {
		// TODO:
	}
	
	@Test
	public void testAddRoute() {
		// TODO:
	}
	
	
	@Test
	public void testMoveActiveWaypoint() {
		// TODO:
	}
	
	
	@Test
	public void testDeleteActiveWaypoint() {
		// TODO:
	}
	
	
	@Test
	public void testInvertRoute() {
		// TODO:
	}
	
	
	@Test
	public void testResetRoute() {
		// TODO:
	}
	
	@Test
	public void testOptimizeRoute() {
		// TODO:
	}
	
	@Test
	public void testGetName() {
		// TODO:
	}
	
	@Test
	public void testGetStart() {
		// TODO:
	}
	
	@Test
	public void testGetEnd() {
		// TODO:
	}
	
	@Test
	public void testGetActiveWaypoint() {
		// TODO:
	}
	
	@Test
	public void testGetWaypoints() {
		// TODO:
	}
	
	@Test
	public void testIsFavorite() {
		// TODO:
	}
	
	@Test
	public void testContainsWaypoint() {
		// TODO:
	}
	
	@Test
	public void testGetCoordinates() {
		// TODO:
	}
	
	@Test
	public void testToString() {
		// TODO:
		
		
	}
	
	
	
	
	@Test
	public void testClone() {
		// TODO:
		/*
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
		*/
		
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

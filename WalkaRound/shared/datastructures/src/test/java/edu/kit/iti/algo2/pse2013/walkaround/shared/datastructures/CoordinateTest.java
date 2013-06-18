package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoordinateTest {
	@Test
	public void testValidLonValues() {
		new Coordinate(0, -180);
		new Coordinate(0, Math.E*-50);
		new Coordinate(0, -90);
		new Coordinate(0, 0);
		new Coordinate(0, Math.PI);
		new Coordinate(0, 90);
		new Coordinate(0, 180);
	}
	@Test
	public void testValidLatValues() {
		new Coordinate(-45, 0);
		new Coordinate(Math.E*-25, 0);
		new Coordinate(-90, 0);
		new Coordinate(Math.PI, 0);
		new Coordinate(90, 0);
		new Coordinate(45, 0);
	}
	@Test
	(expected = IllegalArgumentException.class)
	public void testLonOverflow() {
		new Coordinate(0, 180.0000000000001);
	}
	@Test
	(expected = IllegalArgumentException.class)
	public void testLonUnderflow() {
		new Coordinate(0, -180.0000000000001);
	}
	@Test
	(expected = IllegalArgumentException.class)
	public void testLatOverflow() {
		new Coordinate(90.00000000000001, 0);
	}
	@Test
	(expected = IllegalArgumentException.class)
	public void testLatUnderflow() {
		new Coordinate(-90.00000000000001, 0);
	}
	@Test
	public void testLonPersistence() {
		for (double lon = -180; lon <= 180; lon+= 30) {
			for (double lat = -90; lat <= 90; lat += 90) {
				assertTrue(lon == new Coordinate(lat, lon).getLongtitude());
			}
		}
	}
	@Test
	public void testLatPersistence() {
		for (double lon = -180; lon <= 180; lon+= 90) {
			for (double lat = -90; lat <= 90; lat += 30) {
				assertTrue(lat == new Coordinate(lat, lon).getLatitude());
			}
		}
	}
}
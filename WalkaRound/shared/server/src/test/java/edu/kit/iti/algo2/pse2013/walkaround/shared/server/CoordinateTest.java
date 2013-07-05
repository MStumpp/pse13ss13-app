package edu.kit.iti.algo2.pse2013.walkaround.shared.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoordinateTest {
	/**
	 * Erst eine Überschreitung der Höchstwerte (90° Breite bzw. 180° Länge) in dieser Größenordnung
	 * wird von {@link Coordinate} als Overflow bewertet und entsprechend behandelt.
	 */
	private final static double OVERFLOW = 1e-13;
	/**
	 * Das delta, um das der Erwartungswert und der tatsächliche Wert bei der Overflow-Prüfung differieren dürfen.
	 */
	private final static double OVERFLOW_PRECISION = 1e-323;

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
	public void testLonOverflow() {
		assertEquals(-180 + OVERFLOW, new Coordinate(0, 180 + OVERFLOW).getLongtitude(), OVERFLOW_PRECISION);
	}
	@Test
	public void testLonUnderflow() {
		assertEquals(180 - OVERFLOW, new Coordinate(0, -180 - OVERFLOW).getLongtitude(), OVERFLOW_PRECISION);
	}
	@Test
	public void testLatOverflow() {
		assertEquals(-90 + OVERFLOW, new Coordinate(90 + OVERFLOW, 0).getLatitude(), OVERFLOW_PRECISION);
	}
	@Test
	public void testLatUnderflow() {
		assertEquals(90 - OVERFLOW, new Coordinate(-90 - OVERFLOW, 0).getLatitude(), OVERFLOW_PRECISION);
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
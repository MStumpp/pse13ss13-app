package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

public class TestCoordinate {
	/**
	 * Erst eine Überschreitung der Höchstwerte (90° Breite bzw. 180° Länge) in dieser Größenordnung
	 * wird von {@link Coordinate} als Overflow bewertet und entsprechend behandelt.
	 */
	private final static double OVERFLOW = 1e-13;
	/**
	 * Das delta, um das der Erwartungswert und der tatsächliche Wert bei der Overflow-Prüfung differieren dürfen.
	 */
	private final static double OVERFLOW_PRECISION = 1e-323;
	private final static double REFERENCE_PRECISION = OVERFLOW_PRECISION;

	@Test
	public void testReferenceConstructor() {
		double lat = 48.1234567;
		double latDelta = 1.23456789;
		double lon = 8.987654321;
		double lonDelta = 9.87654321;
		Coordinate c = new Coordinate(lat, lon);
		Coordinate c2 = new Coordinate(c, latDelta, lonDelta);
		assertEquals(lat + latDelta, c2.getLatitude(), OVERFLOW_PRECISION);
		assertEquals(lon + lonDelta, c2.getLongitude(), OVERFLOW_PRECISION);
	}

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
		assertEquals(-180 + OVERFLOW, new Coordinate(0, 180 + OVERFLOW).getLongitude(), OVERFLOW_PRECISION);
	}
	@Test
	public void testLonUnderflow() {
		assertEquals(180 - OVERFLOW, new Coordinate(0, -180 - OVERFLOW).getLongitude(), OVERFLOW_PRECISION);
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
				assertTrue(lon == new Coordinate(lat, lon).getLongitude());
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

	@Test
	public void testClone() {
		Coordinate c = new Coordinate(48.1234567, 9.987654321);
		assertTrue(c != c.clone());
		c = new Coordinate(48.1234567, 9.987654321, new CrossingInformation(new float[]{1.0f, 2.0f, 3.0f}));
		assertTrue(c != c.clone());
	}

	@Test
    @Ignore
	public void testCrossPersistence() {
		CrossingInformation ci = new CrossingInformation(new float[]{1.0f, 2.0f, 3.0f});
		Coordinate c = new Coordinate(48.1234567, 9.987654321, ci);
		assertTrue(c.getCrossingInformation() == ci);
	}

	@Test
    @Ignore
    public void testHashCode() {
		Coordinate c = new Coordinate(48.1234567, 9.987654321);
		Coordinate c2 = new Coordinate(48.1234567, 9.987654321, new CrossingInformation(new float[]{1.0f, 2.0f, 3.0f}));
		assertTrue(c.hashCode() != c2.hashCode());
	}

	@Test
    @Ignore
	public void testToString() {
		testToString(new Coordinate(48.1234567, 9.987654321));
		testToString(new Coordinate(-48.1234567, 9.987654321));
		testToString(new Coordinate(48.1234567, -9.987654321));
		testToString(new Coordinate(-48.1234567, -9.987654321));
		testToString(new Coordinate(-48.1234567, -9.987654321, new CrossingInformation(new float[]{-1, 0, 1, 2, 42})));
	}
	private void testToString(Coordinate c) {
		assertTrue(c.toString().contains(String.format("%.5f",c.getLatitude())));
		assertTrue(c.toString().contains(String.format("%.5f",c.getLongitude())));
		assertTrue(c.toString().contains("Coord"));
		if (c.getCrossingInformation() != null) {
			String angles = "";
			for (float f : c.getCrossingInformation().getCrossingAngles()) {
				angles += " " + f;
			}
			assertTrue(c.toString().contains(angles.trim()));
		}
	}

	@Test
	public void testEquals() {
		Coordinate c = new Coordinate(48.1234567, 9.987654321);
		CrossingInformation cInfo = new CrossingInformation(new float[]{0f});
		Coordinate c2 = new Coordinate(48.1234567, 9.987654321, cInfo);
		Coordinate c3 = new Coordinate(48.7654321, 9.123456789);
		Coordinate c4 = new Coordinate(48.1234567, 9.987654321, new CrossingInformation(new float[]{1f}));
		Coordinate c5 = new Coordinate(48.1234567, 9.987654321, cInfo);
		Coordinate c6 = new Coordinate(48.1234567, 9.123456789);
		assertTrue(!c.equals(null));
		assertTrue(!c.equals("42"));
		assertTrue(c.equals(c));
		assertTrue(c.equals(c2));
		assertTrue(!c.equals(c3));
		assertTrue(c2.equals(c4));
		assertTrue(c2.equals(c5));
		assertTrue(!c.equals(c6));
	}
}
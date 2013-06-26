package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoordinateUtilityTest {
	private final static double FRACTION_DELTA = 1e-4; // TODO: Can this be lowered any further by improving the conversion-method?
	private final static double INTEGER_DELTA = 1e-64;
	/**
	 * Testet den Umgang der Methode {@link CoordinateUtility#convertPixelsToDegrees(float, float)}
	 * mit ganzzahligen "level of detail".
	 */
	@Test
	public void testPixelDegreeConversionIntegerLevelsOfDetail() {
		for (int i = 0; i <= 18; i++) {
			assertEquals(180 / Math.pow(2, i) , CoordinateUtility.convertPixelsToDegrees(256, i), INTEGER_DELTA);
			assertEquals(180 / (Math.pow(2, i) * 256) , CoordinateUtility.convertPixelsToDegrees(1, i), INTEGER_DELTA);
		}
	}
	/**
	 * Testet den Umgang der Methode {@link CoordinateUtility#convertDegreesToPixels(float, float)}
	 * mit ganzzahligen "level of detail"
	 */
	@Test
	public void testDegreePixelConversionIntegerLevelsOfDetail() {
		for (int i = 0; i <= 18; i++) {
			assertEquals(256 , CoordinateUtility.convertDegreesToPixels((float) (180 / Math.pow(2, i)), i), INTEGER_DELTA);
			assertEquals(1 , CoordinateUtility.convertDegreesToPixels((float) (180 / (Math.pow(2, i) * 256)), i), INTEGER_DELTA);
		}
	}
	/**
	 * Testet den Umgang der Methode {@link CoordinateUtility#convertPixelsToDegrees(float, float)}
	 * mit brüchigen "level of detail" (Scherz, natürlich mit Fließkommazahlen als "level of detail").
	 */
	@Test
	public void testPixelDegreeConversionFractionLevelsOfDetail() {
		for (float i = 0; i <= 18; i+= Math.PI / 3.5) {
			assertEquals(256 , CoordinateUtility.convertDegreesToPixels((float) (180 / Math.pow(2, i)), i), FRACTION_DELTA);
			assertEquals(1 , CoordinateUtility.convertDegreesToPixels((float) (180 / (Math.pow(2, i) * 256)), i), FRACTION_DELTA);
		}
	}
	/**
	 * Testet den Umgang der Methode {@link CoordinateUtility#convertDegreesToPixels(float, float)}
	 * mit Fließkommazahlen als "level of detail".
	 */
	@Test
	public void testDegreePixelConversionFractionLevelsOfDetail() {
		for (float i = 0; i <= 18; i+= Math.PI / 3.5) {
			assertEquals(180 / Math.pow(2, i) , CoordinateUtility.convertPixelsToDegrees(256, i), FRACTION_DELTA);
			assertEquals(180 / (Math.pow(2, i) * 256) , CoordinateUtility.convertPixelsToDegrees(1, i), FRACTION_DELTA);
		}
	}
}
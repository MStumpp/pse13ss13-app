package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;


@RunWith(RobolectricTestRunner.class)
public class CoordinateUtilityTest extends TestCase {
	private final static double FRACTION_DELTA = 1e-4; // TODO: Can this be lowered any further by improving the conversion-method?
	private final static double INTEGER_DELTA = 1e-64;
	/**
	 * Testet den Umgang der Methode {@link CoordinateUtility#convertPixelsToDegrees(float, float)}
	 * mit ganzzahligen "level of detail".
	 */
	@Test
	public void testPixelDegreeConversionIntegerLevelsOfDetail() {
		assertEquals(360,CoordinateUtility.convertPixelsToDegrees(256,0,CoordinateUtility.DIRECTION_HORIZONTAL),1);
		assertEquals(180,CoordinateUtility.convertPixelsToDegrees(256,1,CoordinateUtility.DIRECTION_HORIZONTAL),1);
		assertEquals(90,CoordinateUtility.convertPixelsToDegrees(256,2,CoordinateUtility.DIRECTION_HORIZONTAL),1);

		assertEquals(180,CoordinateUtility.convertPixelsToDegrees(128,0,CoordinateUtility.DIRECTION_HORIZONTAL),1);
		assertEquals(90,CoordinateUtility.convertPixelsToDegrees(128,1,CoordinateUtility.DIRECTION_HORIZONTAL),1);
		assertEquals(45,CoordinateUtility.convertPixelsToDegrees(128,2,CoordinateUtility.DIRECTION_HORIZONTAL),1);

		for (int i = 0; i <= 18; i++) {
			assertEquals(180 / Math.pow(2, i) , CoordinateUtility.convertPixelsToDegrees(256, i, CoordinateUtility.DIRECTION_VERTICAL), INTEGER_DELTA);
			assertEquals(180 / (Math.pow(2, i) * 256) , CoordinateUtility.convertPixelsToDegrees(1, i, CoordinateUtility.DIRECTION_VERTICAL), INTEGER_DELTA);
		}
	}
	/**
	 * Testet den Umgang der Methode {@link CoordinateUtility#convertDegreesToPixels(float, float)}
	 * mit ganzzahligen "level of detail"
	 */
	@Test
	public void testDegreePixelConversionIntegerLevelsOfDetail() {
		for (int i = 0; i <= 18; i++) {
			assertEquals(256 , CoordinateUtility.convertDegreesToPixels((float) (180 / Math.pow(2, i)), i, CoordinateUtility.DIRECTION_VERTICAL), INTEGER_DELTA);
			assertEquals(1 , CoordinateUtility.convertDegreesToPixels((float) (180 / (Math.pow(2, i) * 256)), i, CoordinateUtility.DIRECTION_VERTICAL), INTEGER_DELTA);
		}
	}
	/**
	 * Testet den Umgang der Methode {@link CoordinateUtility#convertPixelsToDegrees(float, float)}
	 * mit brüchigen "level of detail" (Scherz, natürlich mit Fließkommazahlen als "level of detail").
	 */
	@Test
	public void testPixelDegreeConversionFractionLevelsOfDetail() {
		for (float i = 0; i <= 18; i+= Math.PI / 3.5) {
			assertEquals(256 , CoordinateUtility.convertDegreesToPixels((float) (180 / Math.pow(2, i)), i, CoordinateUtility.DIRECTION_VERTICAL), FRACTION_DELTA);
			assertEquals(1 , CoordinateUtility.convertDegreesToPixels((float) (180 / (Math.pow(2, i) * 256)), i, CoordinateUtility.DIRECTION_VERTICAL), FRACTION_DELTA);
		}
	}
	/**
	 * Testet den Umgang der Methode {@link CoordinateUtility#convertDegreesToPixels(float, float)}
	 * mit Fließkommazahlen als "level of detail".
	 */
	@Test
	public void testDegreePixelConversionFractionLevelsOfDetail() {
		for (float i = 0; i <= 18; i+= Math.PI / 3.5) {
			assertEquals(180 / Math.pow(2, i) , CoordinateUtility.convertPixelsToDegrees(256, i, CoordinateUtility.DIRECTION_VERTICAL), FRACTION_DELTA);
			assertEquals(180 / (Math.pow(2, i) * 256) , CoordinateUtility.convertPixelsToDegrees(1, i, CoordinateUtility.DIRECTION_VERTICAL), FRACTION_DELTA);
		}
	}
	
	@Test
	public void testCalculateDifferenceInMeters(){
		assertTrue(CoordinateUtility.calculateDifferenceInMeters(new Coordinate(0,0), new Coordinate(0,0)) == 0);
	}
	
	@Test
	public void convertDisplayCoordinates(){
		CoordinateUtility.convertDisplayCoordinateToCoordinate(new DisplayCoordinate(0,0), new Coordinate(0,0), 5);
		CoordinateUtility.convertCoordinateToDisplayCoordinate(new Coordinate(0,0), new Coordinate(0,0), 6);
		assertTrue(true);
	}
}
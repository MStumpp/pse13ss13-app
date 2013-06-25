package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoordinateUtilityTest {
	@Test
	public void testPixelDegreeConversionFullLevelsOfDetail() {
		for (int i = 0; i <= 18; i++) {
			assertEquals(180 / Math.pow(2, i) , CoordinateUtility.convertPixelsToDegrees(256, i), 1e-64);
		}
	}
	@Test
	public void testPixelDegreeConversionFractionLevelsOfDetail() {
		//TODO: Tests mit nicht-ganzen
	}
}

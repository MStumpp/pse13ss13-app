package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

@RunWith(RobolectricTestRunner.class)
public class TileUtiltyTest {
	private Coordinate defaultCoordinate = new Coordinate(49.0145, 8.419); // 211

	@Test
	public void setUp(){

		int[] xy = new int[] {
				(int) Math.floor((defaultCoordinate.getLongitude() + 180) / 360
						* (1 << 6)),
				(int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(defaultCoordinate
						.getLatitude()))
						+ 1
						/ Math.cos(Math.toRadians(defaultCoordinate.getLatitude()))) / Math.PI)
						/ 2 * (1 << 6)) };
		
		assertEquals(TileUtility.getXYTileIndex(defaultCoordinate, 6), xy);
	}

}

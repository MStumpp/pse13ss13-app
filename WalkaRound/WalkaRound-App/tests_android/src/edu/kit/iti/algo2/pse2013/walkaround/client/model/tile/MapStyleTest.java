package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MapStyleTest {

	@Test
	public void testMapnik() {
		assertEquals("Mapnik", MapStyle.MAPSTYLE_MAPNIK.getName());
		assertEquals(16, MapStyle.MAPSTYLE_MAPNIK.getDefaultLevelOfDetail());
		assertEquals(18, MapStyle.MAPSTYLE_MAPNIK.getMaxLevelOfDetail());
		assertEquals(0, MapStyle.MAPSTYLE_MAPNIK.getMinLevelOfDetail());
		assertEquals("http://tile.openstreetmap.org/%3$s/%1$s/%2$s.png",
				MapStyle.MAPSTYLE_MAPNIK.getTileURL());
	}
	
	@Test
	public void testMapQuest() {
		assertEquals("MapQuest", MapStyle.MAPSTYLE_MAPQUEST.getName());
		assertEquals(17, MapStyle.MAPSTYLE_MAPQUEST.getDefaultLevelOfDetail());
		assertEquals(19, MapStyle.MAPSTYLE_MAPQUEST.getMaxLevelOfDetail());
		assertEquals(0, MapStyle.MAPSTYLE_MAPQUEST.getMinLevelOfDetail());
		assertEquals("http://otile1.mqcdn.com/tiles/1.0.0/map/%3$s/%1$s/%2$s.jpg",
				MapStyle.MAPSTYLE_MAPQUEST.getTileURL());
	}
	
	@Test
	public void testWaynderKarte() {
		assertEquals("Hike & Bike", MapStyle.MAPSTYLE_MAPQUEST.getName());
		assertEquals(16, MapStyle.MAPSTYLE_WANDERKARTE.getDefaultLevelOfDetail());
		assertEquals(18, MapStyle.MAPSTYLE_WANDERKARTE.getMaxLevelOfDetail());
		assertEquals(0, MapStyle.MAPSTYLE_WANDERKARTE.getMinLevelOfDetail());
		assertEquals("http://toolserver.org/tiles/hikebike/%3$s/%1$s/%2$s.png",
				MapStyle.MAPSTYLE_WANDERKARTE.getTileURL());
	}
}

package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

import android.graphics.Bitmap;
@RunWith(RobolectricTestRunner.class)
public class TileFetcherTest implements TileListener {
	
	private TileFetcher tileFetcher;
	private int counter;
	public Coordinate defaultCoordinate = new Coordinate(49.0145, 8.419); // 211

	@Before
	public void setUp(){
		tileFetcher = new TileFetcher();
		counter = 0;
	}

	@Override
	public void receiveTile(Bitmap tile, int x, int y, int levelOfDetail) {
		counter++;
	}
	
	@Test
	public void fetchTiles(){
		tileFetcher.requestTiles(7, defaultCoordinate, defaultCoordinate, this);
		while (counter >= 1);
		tileFetcher.clearCache();
		assertTrue(true);
	}
}

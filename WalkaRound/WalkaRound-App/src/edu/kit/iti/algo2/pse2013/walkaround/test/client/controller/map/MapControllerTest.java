package edu.kit.iti.algo2.pse2013.walkaround.test.client.controller.map;

import java.net.MalformedURLException;

import org.junit.Before;

import android.graphics.Point;
import android.test.AndroidTestCase;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.MapStyle;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;


public class MapControllerTest extends AndroidTestCase {
	private Coordinate center; 
	private final float lod = MapStyle.MAPSTYLE_MAPNIK.getDefaultLevelOfDetail();
	private Point display;
	private final TileFetcher tl = new TileFetcher();
	private BoundingBox box;
	private MapController mc;
	
	@Before
	public void setUp() {
		center = new Coordinate(49.0145, 8.419);
		display = new Point(1025, 600);
		box = new BoundingBox(center, display, lod);
		mc = MapController.initialize(tl, box, center);
	}

	/**
	 * Testet die BoundingBox auf allgemeine Funktionalität
	 * 
	 * @throws MalformedURLException
	 */
	public void testGeneral() {
		
	}
	
}
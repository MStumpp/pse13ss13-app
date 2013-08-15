package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import java.net.MalformedURLException;

import android.graphics.Point;
import android.test.AndroidTestCase;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.MapStyle;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;


public class MapControllerTest extends AndroidTestCase {
	private Coordinate center = new Coordinate(49.0145, 8.419);; 
	private final float lod = MapStyle.MAPSTYLE_MAPNIK.getDefaultLevelOfDetail();
	private Point display =  new Point(1025, 600);;
	private final TileFetcher tl = new TileFetcher();
	private BoundingBox box = new BoundingBox(center, display, lod);;
	private MapController mc =  MapController.initialize(tl, box, center);;
	

	/**
	 * Testet die BoundingBox auf allgemeine Funktionalität
	 * 
	 * @throws MalformedURLException
	 */
	public void testGeneral() {
		
	}
	
}
package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import java.net.MalformedURLException;
import java.util.LinkedList;

import android.graphics.Point;
import android.test.AndroidTestCase;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.Route;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.MapStyle;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;


public class MapControllerTest extends AndroidTestCase {
	private Coordinate center; 
	private float lod;
	private Point display;
	private TileFetcher tl;
	private BoundingBox box;
	private MapController mc ;
	private Route r;
	private MapView mv;
	

	@Override
	protected void setUp() throws Exception{
		center = new Coordinate(49.0145, 8.419); 
		lod = MapStyle.MAPSTYLE_MAPNIK.getDefaultLevelOfDetail();
		display =  new Point(1025, 600);
		TileFetcher tl = new TileFetcher();
		box = new BoundingBox(center, display, lod);
		mv = new MapView();
		mc =  MapController.initialize(tl, box, center);
		mc.startController(mv);
		mc.onRouteChange(r);
		
		super.setUp();
	}
	/**
	 * 
	 */
	public void testGeneral() {
		assertTrue(mc.getCenter() == center);
		//assertTrue(mc.getActiveWaypointId() == 0);
		assertTrue(mc.getCurrentLevelOfDetail() == lod);
		assertTrue(mc.getCurrentRouteLines().isEmpty());
		assertTrue(mc.getPOIById(0) == null);
		assertTrue(mc.getInstance() != null);
	}
	
}
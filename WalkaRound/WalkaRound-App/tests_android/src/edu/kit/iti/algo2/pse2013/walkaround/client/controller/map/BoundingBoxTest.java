package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.graphics.Point;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.MapStyle;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

@RunWith(RobolectricTestRunner.class)
public class BoundingBoxTest  {
	private Point display;
	private Coordinate center;
	private float lod;

	@Before
	protected void setUp() {
		display = new Point(1025, 600);
		center =  new Coordinate(49.0145, 8.419);
		lod = MapStyle.MAPSTYLE_MAPNIK.getDefaultLevelOfDetail();
	}

	/**
	 * Testet die BoundingBox auf allgemeine Funktionalität
	 *
	 * @throws MalformedURLException
	 */
	@Test
	public void testGeneral() {

		BoundingBox box = new BoundingBox(center, display, lod);

		assertTrue(box.getCenter() == center);
		assertTrue(box.getLevelOfDetail() == lod);
		assertTrue(box.getDisplaySize() == display);
		assertTrue(box.getTopLeft() != null);
		assertTrue(box.getTopRight() != null);
		assertTrue(box.getBottomLeft() != null);
		assertTrue(box.getBottomRight() != null);
	}

	/**
	 * Testet das verändern des Zoomlevels
	 */
	@Test
	public void testZoom() {

		BoundingBox box = new BoundingBox(center, display, lod);

		box.setLevelOfDetail(8);
		assertTrue(box.getLevelOfDetail() == 8 && box.getCenter() == center);

		box.setLevelOfDetailByADelta(1);
		assertTrue(box.getLevelOfDetail() == 9 && box.getCenter() == center);

	}

	/**
	 * test das verändern der Center Coordinate
	 */
	@Test
	public void testCenter(){

		BoundingBox box = new BoundingBox(center, display, lod);
		Coordinate center  = new Coordinate(10,10);

		Coordinate topLeft  = box.getTopLeft();
		Coordinate topRight  = box.getTopRight();
		Coordinate bottomLeft  = box.getBottomLeft();
		Coordinate botoomRight  = box.getBottomRight();

		box.setCenter(center);
		assertTrue(box.getCenter() == center);
		assertTrue(box.getTopLeft() != topLeft);
		assertTrue(box.getTopRight() != topRight);
		assertTrue(box.getBottomLeft() != bottomLeft);
		assertTrue(box.getBottomRight() != botoomRight);
		box.setCenter(center,4);
		assertTrue(box.getCenter() == center);
		box.setCenter(new DisplayCoordinate(0,0));
		box.getBottomLeft();
		box.getBottomRight();
		box.toString();

	}
	
	/**
	 * 
	 */
	@Test
	public void testShift(){
		BoundingBox box = new BoundingBox(center, display, lod);
		box.shiftCenter(0, 0);
	}
}
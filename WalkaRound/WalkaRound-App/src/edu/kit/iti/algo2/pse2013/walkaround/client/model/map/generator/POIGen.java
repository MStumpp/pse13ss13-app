package edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Point;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayPOI;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * This Class compute and generate the POI Points on the map
 * 
 * @author Ludwig Biermann
 * 
 */
public class POIGen implements Runnable {

	/**
	 * Debug Tag
	 */
	private static String TAG_POIGen = POIGen.class.getSimpleName();

	/**
	 * The List of POI´s on the map
	 */
	private List<POI> poiList = new LinkedList<POI>();

	/**
	 * Bottom right Coordinate
	 */
	private Coordinate bottomRight;

	/**
	 * Top Left Coordinate
	 */
	private Coordinate topLeft;

	/**
	 * Construckts a new POIGen
	 */
	public POIGen() {

	}

	@Override
	public void run() {
		// running endless
		while (true)
			;
	}

	/**
	 * Returns the upperLeft Coordinate
	 * 
	 * @return the top left geo-oordinate
	 */
	private Coordinate getTopLeft(Coordinate center, Point size, float lod) {
		return new Coordinate(center, CoordinateUtility.convertPixelsToDegrees(
				size.y / 2f, lod, CoordinateUtility.DIRECTION_LATITUDE),
				-CoordinateUtility.convertPixelsToDegrees(size.x / 2f, lod,
						CoordinateUtility.DIRECTION_LONGITUDE));
	}

	/**
	 * Returns the upperLeft Coordinate
	 * 
	 * @return the top left geo-oordinate
	 */
	private Coordinate getBottomRight(Coordinate center, Point size, float lod) {
		return new Coordinate(center,
				-CoordinateUtility.convertPixelsToDegrees(size.y / 2f, lod,
						CoordinateUtility.DIRECTION_LATITUDE),

				CoordinateUtility.convertPixelsToDegrees(size.x / 2f, lod,
						CoordinateUtility.DIRECTION_LONGITUDE));
	}

	/**
	 * Gives the POI Information by their ids back
	 * 
	 * @param id
	 *            id of the POI
	 * @return the POI and its Informations
	 */
	public POI getPOIInformationById(int id) {
		for (POI value : this.poiList) {
			if (value.getId() == id) {
				return value;
			}
		}
		return null;
	}

	/**
	 * update the POI of the Display
	 */
	public void generatePOIView(Coordinate center, float lod, Point size) {
		if (!POIManager.getInstance().isEmpty()) {

			this.topLeft = this.getTopLeft(center, size, lod);
			this.bottomRight = this.getBottomRight(center, size, lod);

			poiList = POIManager.getInstance().getPOIsWithinRectangle(topLeft,
					bottomRight, lod);

			Log.d(TAG_POIGen, "POI Anzahl" + poiList.size());

			LinkedList<DisplayPOI> poi = new LinkedList<DisplayPOI>();

			for (POI value : poiList) {
				poi.add(new DisplayPOI(CoordinateUtility
						.convertDegreesToPixels(
								value.getLongitude() - topLeft.getLongitude(),
								lod, CoordinateUtility.DIRECTION_X),
						CoordinateUtility.convertDegreesToPixels(
								topLeft.getLatitude() - value.getLatitude(),
								lod, CoordinateUtility.DIRECTION_Y), value
								.getId()));
			}
			if (MapController.getInstance() != null) {
				MapController.getInstance().onPOIChange(poi);
			}
		}
	}
}
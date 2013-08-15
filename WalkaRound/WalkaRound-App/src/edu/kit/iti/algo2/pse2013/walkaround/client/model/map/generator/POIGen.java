package edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Point;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayPOI;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
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
	public void generatePOIView(BoundingBox coorBox, Point size) {
		if (!POIManager.getInstance().isEmpty()) {

			poiList = POIManager.getInstance().getPOIsWithinRectangle(
					coorBox.getTopLeft(), coorBox.getBottomRight(),
					coorBox.getLevelOfDetail());

			Log.d(TAG_POIGen, "POI Anzahl" + poiList.size());

			LinkedList<DisplayPOI> poi = new LinkedList<DisplayPOI>();

			for (POI value : poiList) {

				double lon = -coorBox.getCenter().getLongitude()
						+ value.getLongitude();
				double lat = -value.getLatitude()
						+ coorBox.getCenter().getLatitude();

				float x = CoordinateUtility.convertDegreesToPixels(lon,
						coorBox.getLevelOfDetail(),
						CoordinateUtility.DIRECTION_LONGITUDE);

				float y = CoordinateUtility.convertDegreesToPixels(lat,
						coorBox.getLevelOfDetail(),
						CoordinateUtility.DIRECTION_LATITUDE);

				Log.d("UserPos", " x: " + x + " y: " + y);

				x = coorBox.getDisplaySize().x / 2 + x;
				y = coorBox.getDisplaySize().y / 2 + y;

				Log.d("UserPos", " x: " + x + " y: " + y);

				poi.add(new DisplayPOI(x, y, value.getId()));
			}
			if (MapController.getInstance() != null) {
				MapController.getInstance().onPOIChange(poi);
			}
		}
	}
}
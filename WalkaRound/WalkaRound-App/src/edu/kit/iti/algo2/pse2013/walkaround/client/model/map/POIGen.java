package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Point;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

public class POIGen implements Runnable{

	private static String TAG_MAP_MODEL = POIGen.class.getSimpleName();
	
	private List<POI> poiList = new LinkedList<POI>();
	
	private Coordinate bottomRight;
	private Coordinate topLeft;
	
	
	public POIGen(){
		
	}
	
	@Override
	public void run() {
		while(true);
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
				-CoordinateUtility.convertPixelsToDegrees(
				size.y / 2f, lod, CoordinateUtility.DIRECTION_LATITUDE),
				
				CoordinateUtility.convertPixelsToDegrees(size.x / 2f, lod,
						CoordinateUtility.DIRECTION_LONGITUDE));
	}

	/**
	 *
	 * @param id
	 * @return
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

			poiList = POIManager.getInstance().getPOIsWithinRectangle(
					topLeft, bottomRight, lod);

			Log.d(TAG_MAP_MODEL, "POI Anzahl" + poiList.size());

			LinkedList<DisplayPOI> poi = new LinkedList<DisplayPOI>();

			for (POI value : poiList) {
				poi.add(new DisplayPOI(CoordinateUtility
						.convertDegreesToPixels(value.getLongitude()
								- topLeft.getLongitude(),
								lod,
								CoordinateUtility.DIRECTION_X),
						CoordinateUtility.convertDegreesToPixels(
								topLeft.getLatitude()
										- value.getLatitude(),
								lod,
								CoordinateUtility.DIRECTION_Y), value.getId()));
			}
			if(MapController.getInstance() != null){
				MapController.getInstance().onPOIChange(poi);
			}
		}
	}
	
}

package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

/**
 *
 * @author Ludwig Biermann
 *
 */
public class MapModel {

	private static MapModel mapModel;
	private Coordinate upperLeft;
	private static Coordinate defaultCoordinate = new Coordinate(49.0047200, 8.3858300);

	/**
	 *
	 */
	private MapModel(){
		this(defaultCoordinate);
	}

	/**
	 *
	 * @param c
	 */
	private MapModel(Coordinate c){
		this.upperLeft = c;
	}

	/**
	 *
	 * @param c
	 * @return
	 */
	public MapModel getInstance(Coordinate c){
		if(mapModel != null){
			mapModel = new MapModel(c);
		}
		return mapModel;

	}

	/**
	 *
	 * @return
	 */
	public MapModel getInstance(){
		if(mapModel != null){
			mapModel = new MapModel();
		}
		return mapModel;
	}

	/**
	 *
	 * @param c
	 */
	public void shift(Coordinate c) {
		this.upperLeft = new Coordinate(c.getLatitude(),c.getLongtitude());
	}

	/**
	 *
	 */
	public void generateMapOverlayImage() {

	}

	/**
	 *
	 */
	public void generateRouteOverlayImage() {

	}

	/**
	 *
	 */
	public void setNewStyle() {

	}

	/**
	 *
	 * @param lod
	 * @param c
	 */
	public void zoom(float lod, Coordinate c) {

	}

	/**
	 *
	 * @param dc
	 * @param category
	 * @param profile
	 * @return
	 */
	public LinkedList<DisplayCoordinate> getPOIofDisplay(DisplayCoordinate dc, int[] category , int profile) {
		return null;
	}

	/**
	 *
	 * @return
	 */
	public int getCurrentLevelOfDetail() {
		return 0;
	}

	/**
	 *
	 * @param lod
	 */
	public void setCurrentLevelOfDetail(int lod) {

	}

	/**
	 *
	 */
	public void onChangeOfActivePOICategories() {

	}

	/**
	 *
	 * @param c
	 * @return
	 */
	public Location getNearbyLocation(Coordinate c) {
		return null;
	}
}

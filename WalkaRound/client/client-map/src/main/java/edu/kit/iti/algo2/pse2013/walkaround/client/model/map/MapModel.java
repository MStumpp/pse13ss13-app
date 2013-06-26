package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.CurrentMapStyleModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

/**
 *
 * @author Ludwig Biermann
 *
 */
public class MapModel implements TileListener {

	private static String TAG_MAP_MODEL = "MAP_MODEL";
	private static MapModel mapModel;

	private float currentLevelOfDetail;
	private MapController mapController;
	private Coordinate upperLeft;
	private Coordinate bottomRight;
	private Point size;
	private TileFetcher tileFetcher;

	private Bitmap map;

	// Test Bitmaps
	private Bitmap eins = null;

	/**
	 *
	 * @param c
	 * @return
	 */
	public static MapModel initialize(Coordinate c,MapController mapController, Point size) {
		if(mapModel == null){
			mapModel = new MapModel(c, mapController, size);
		}
		return mapModel;
	}

	/**
	 *
	 * @return
	 */
	public static MapModel getInstance(){
		if(mapModel != null){
			Log.d(TAG_MAP_MODEL, "bitte initialisieren Sie zuerst MapView");
			return null;
		}
		return mapModel;
	}

	/**
	 *
	 * @param c
	 */
	private MapModel(Coordinate c, MapController mapController, Point size){
		Log.d(TAG_MAP_MODEL, "Map Model wird initialisiert");
		this.upperLeft = c;
		this.size = size;
		this.mapController = mapController;
		this.tileFetcher = new TileFetcher();
		this.tileFetcher.setTileListener(this);
		this.map = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
		this.currentLevelOfDetail = CurrentMapStyleModel.getInstance().getCurrentMapStyle().getDefaultLevelOfDetail();
		this.computeBottomRight();
		//Log.d(TAG_MAP_MODEL, "Tile werden angeforder: " + this.tileFetcher.requestTiles(Math.round(currentLevelOfDetail), c, c));
		Log.d(TAG_MAP_MODEL, "Map Model wurde initialisiert");
	}

	/**
	 *
	 */
	private void computeBottomRight() {
		this.bottomRight = this.upperLeft;
	}

	/**
	 *
	 * @param c
	 */
	public void shift(Coordinate c) {
		refetchTiles();
	}

	public void refetchTiles() {
		this.upperLeft = getUpperLeft();
		int numTilesX = (int) Math.ceil(size.x / getCurrentTileWidth());
		int numTilesY = (int) Math.ceil(size.y / getCurrentTileWidth());
		this.tileFetcher.requestTiles(Math.round(this.getCurrentLevelOfDetail()), getUpperLeft(), numTilesX, numTilesY);
	}

	private Coordinate getUpperLeft() {
		return upperLeft;
	}

	/**
	 *
	 */
	public void generateMapOverlayImage() {
		this.mapController.onMapOverlayImageChange(map);
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
		refetchTiles();
	}

	/**
	 *
	 * @param lod
	 * @param c
	 */
	public void zoom(float levelOfDetail, Coordinate c) {
		this.currentLevelOfDetail = levelOfDetail;
		refetchTiles();
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
	 * Gibt den aktuellen Level Of Detail zurück
	 *
	 * @return aktuellen Level ofDetail
	 */
	public float getCurrentLevelOfDetail() {
		return this.currentLevelOfDetail;
	}

	/**
	 *
	 * @param lod
	 */
	public void setCurrentLevelOfDetail(float levelOfDetail) {
		this.currentLevelOfDetail = levelOfDetail;
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

	/**
	 * @return the current tile-width for the current level of detail
	 */
	public float getCurrentTileWidth() {
		return (float) (256*Math.pow(2, getCurrentLevelOfDetail()) / Math.pow(2, Math.round(getCurrentLevelOfDetail())));
	}

	@Override
	public void receiveTile(Bitmap tile, int x, int y, int levelOfDetail) {
		Log.d(TAG_MAP_MODEL, "Receive Tile");
	}
}

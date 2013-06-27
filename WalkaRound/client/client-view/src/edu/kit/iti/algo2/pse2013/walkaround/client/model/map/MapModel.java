package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.CurrentMapStyleModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.MapStyle;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
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
	private final static int DEFAULT_TILE_SIZE = 256;

	private float currentLevelOfDetail;
	private MapController mapController;
	private Coordinate upperLeft;
	// private Coordinate lowerRight;
	private Coordinate mid;
	private Point size;
	private TileFetcher tileFetcher;

	private Bitmap map;
	int xAmount;
	int yAmount;

	int xZoomBorder;
	int yZoomBorder;

	// Test Bitmaps
	// Bitmap fog = BitmapFactory.decodeResource(getResources(),
	// DEFAULT_PATTERN);

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
	public static MapModel getInstance() {
		// TODO RUUNN KITY RUN
		if (mapModel != null) {
			Log.d(TAG_MAP_MODEL, "bitte initialisieren Sie zuerst MapView");
			return null;
		}
		return mapModel;
	}

	/**
	 *
	 * @param c
	 */
	private MapModel(Coordinate c, MapController mapController, Point size) {
		Log.d(TAG_MAP_MODEL, "DisplayMaße: " + size.x + " * " + size.y);
		Log.d(TAG_MAP_MODEL, "Map Model wird initialisiert");

		Log.d(TAG_MAP_MODEL, "Referenzen werden gesetzt");
		this.mapController = mapController;
		this.tileFetcher = new TileFetcher();
		this.tileFetcher.setTileListener(this);

		Log.d(TAG_MAP_MODEL, "wichtige Punkte werden berechner");
		this.size = size;
		this.upperLeft = c;

		Log.d(TAG_MAP_MODEL, "Default LOD wird gesetzt");
		this.currentLevelOfDetail = CurrentMapStyleModel.getInstance()
				.getCurrentMapStyle().getDefaultLevelOfDetail();

		Log.d(TAG_MAP_MODEL, "Anzahl der Tiles werden ausgerechnet.");
		xAmount = (int) Math.ceil(size.x / 256);
		yAmount = (int) Math.ceil(size.y / 256);

		// TODO hier stimmit etwas nicht
		xAmount++;
		yAmount++;
		Log.d(TAG_MAP_MODEL, "Amount x " + xAmount + " y " + yAmount);

		Log.d(TAG_MAP_MODEL, "ZoomLevel werden analysiert ");
		final MapStyle mapStyle = CurrentMapStyleModel.getInstance()
				.getCurrentMapStyle();
		boolean run = true;

		for (int a = mapStyle.getMinLevelOfDetail(); a <= mapStyle
				.getMaxLevelOfDetail() && run; a++) {
			if (Math.pow(2d, (double) a) >= xAmount) {
				xZoomBorder = a;
				run = false;
			}
		}

		run = true;

		for (int a = mapStyle.getMinLevelOfDetail(); a <= mapStyle
				.getMaxLevelOfDetail() && run; a++) {
			if (Math.pow(2d, (double) a) >= yAmount) {
				yZoomBorder = a;
				run = false;
			}
		}

		this.generateMapOverlayImage();
		/*Log.d(TAG_MAP_MODEL,
				"Tile werden angefordert: "
						+ this.tileFetcher.requestTiles(
								Math.round(currentLevelOfDetail), upperLeft,
								xAmount, yAmount));
		this.fetchTiles();*/
		Log.d(TAG_MAP_MODEL, "Map Model wurde initialisiert ");
	}

	/**
	 * berechnet die Koordinate anhand einer DisplayKoordinate relativ zu oberen
	 * Ecke
	 *
	 * @param dc
	 *            die zu konvertierende DisplayCoordinate
	 * @return geographische Koordiante
	 */
	private Coordinate computeCoordinateByDisplayCoordinate(DisplayCoordinate dc) {

		return new Coordinate(this.upperLeft,
				CoordinateUtility.convertPixelsToDegrees(dc.getX(),
						currentLevelOfDetail),
				CoordinateUtility.convertPixelsToDegrees(dc.getY(),
						currentLevelOfDetail));
	}

	/**
	 * berechnet die Mitte und den unteren RechtenRand
	 */
	private void computeMid() {
		this.mid = computeCoordinateByDisplayCoordinate(new DisplayCoordinate(
				size.x / 2, size.y / 2));
	}

	/*
	 * private void computeLowerRight() { this.lowerRight = new
	 * Coordinate(this.upperLeft,
	 * CoordinateUtility.convertPixelsToDegrees(size.x, currentLevelOfDetail),
	 * CoordinateUtility.convertPixelsToDegrees(size.y, currentLevelOfDetail));
	 *
	 * }
	 */

	/**
	 *
	 * @param c
	 */
	public void shift(Coordinate c) {
		this.upperLeft = new Coordinate(c.getLatitude(), c.getLongtitude());
		refetchTiles();
	}

	public void refetchTiles() {
		this.upperLeft = getUpperLeft();
		int numTilesX = (int) Math.ceil(size.x / getCurrentTileWidth());
		int numTilesY = (int) Math.ceil(size.y / getCurrentTileWidth());
		this.tileFetcher.requestTiles(Math.round(this.getCurrentLevelOfDetail()), getUpperLeft(), numTilesX, numTilesY);
	}

	/**
	 *
	 */
	public void generateMapOverlayImage() {

		Log.d(TAG_MAP_MODEL, "LOD " + currentLevelOfDetail + " x "
				+ xZoomBorder + " y " + yZoomBorder);
		Log.d(TAG_MAP_MODEL, "MapStyle: " + CurrentMapStyleModel.getInstance().getCurrentMapStyle().getName());

		if (this.currentLevelOfDetail < this.xZoomBorder
				&& this.currentLevelOfDetail < this.yZoomBorder) {
			Log.d(TAG_MAP_MODEL, "generiere Bitmap kleiner display ");
			final int size = ((int) Math.pow(2, this.currentLevelOfDetail))
					* DEFAULT_TILE_SIZE;
			this.map = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
			this.fetchTiles();
			return;
		} else if (this.currentLevelOfDetail < this.xZoomBorder) {
			Log.d(TAG_MAP_MODEL, "generiere Bitmap kleiner x achse ");
			final int sizeX = ((int) Math.pow(2, this.currentLevelOfDetail))
					* DEFAULT_TILE_SIZE;
			this.map = Bitmap.createBitmap(sizeX, size.y,
					Bitmap.Config.ARGB_8888);
			this.fetchTiles();
			return;
		} else if (this.currentLevelOfDetail < this.yZoomBorder) {
			Log.d(TAG_MAP_MODEL, "generiere Bitmap kleiner achse ");
			final int sizeY = ((int) Math.pow(2, this.currentLevelOfDetail))
					* DEFAULT_TILE_SIZE;
			this.map = Bitmap.createBitmap(size.x, sizeY,
					Bitmap.Config.ARGB_8888);
			this.fetchTiles();
			return;
		}

		Log.d(TAG_MAP_MODEL, "create Bitmap greater than Display ");
		this.map = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
		this.fetchTiles();

		// this.mapController.onMapOverlayImageChange(map);
	}

	private Coordinate getUpperLeft() {
		return upperLeft;
	}
	private void fetchTiles() {
		Log.d(TAG_MAP_MODEL,
				"Tile werden angefordert: "
						+ this.tileFetcher.requestTiles(
								Math.round(currentLevelOfDetail), upperLeft,
								xAmount, yAmount) + " x " + xAmount + " y " + yAmount);
	}
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
	 * @param delta
	 * @param c
	 */
	public boolean zoom(float delta, DisplayCoordinate c) {
		Log.d(TAG_MAP_MODEL, "ZOOM um " +delta + " auf " + c.toString());
		return this.zoom(delta, this.computeCoordinateByDisplayCoordinate(c));
	}

	/**
	 *
	 * @param delta
	 */
	public boolean zoom(float delta) {
		this.computeMid();
		//Log.d(TAG_MAP_MODEL, "ZOOM um " + delta + " auf " + mid.toString());
		return this.zoom(delta, mid);
	}

	/**
	 *
	 * @param delta
	 * @param c
	 */
	public boolean zoom(float delta, Coordinate c) {
		
		Log.d(TAG_MAP_MODEL, "ZOOM um " + delta + " auf " + mid.toString());
		
		//TODO
		
		final MapStyle mapStyle = CurrentMapStyleModel.getInstance()
				.getCurrentMapStyle();
		final float nextLevelOfDetail = this.currentLevelOfDetail + delta;
		
		Log.d(TAG_MAP_MODEL, "ZOOM von " + this.currentLevelOfDetail + " auf " + nextLevelOfDetail + " wird gepr�ft");
		if (nextLevelOfDetail > mapStyle.getMaxLevelOfDetail()
				|| nextLevelOfDetail < mapStyle.getMinLevelOfDetail()) {
			Log.d(TAG_MAP_MODEL, "ZOOM wird abgebrochen");
			return false;
		}

		Log.d(TAG_MAP_MODEL, "ZOOM auf " + nextLevelOfDetail + " wird ausgef�hrt");
		this.currentLevelOfDetail = nextLevelOfDetail;

		final double deltaL = c.getLatitude() - this.upperLeft.getLatitude();
		final double deltaLong = c.getLongtitude() - this.upperLeft.getLongtitude();
		
		Log.d(TAG_MAP_MODEL, "Deltas : " + deltaL + " " + deltaLong);
		
		float deltaLatitude = CoordinateUtility.convertDegreesToPixel(deltaL, nextLevelOfDetail);
		float deltaLongitude = CoordinateUtility.convertDegreesToPixel(deltaLong, nextLevelOfDetail);
		deltaLatitude = CoordinateUtility.convertPixelsToDegrees(-deltaLatitude, nextLevelOfDetail);
		deltaLongitude = CoordinateUtility.convertPixelsToDegrees(-deltaLongitude, nextLevelOfDetail);

		Log.d(TAG_MAP_MODEL, "UpperLeft war: " + this.upperLeft);
		this.upperLeft = new Coordinate(c,-deltaLatitude,-deltaLongitude);
		Log.d(TAG_MAP_MODEL, "Neuer upperLeft ist: " + this.upperLeft);
		
		this.generateMapOverlayImage();
		return true;
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
		//Log.d(TAG_MAP_MODEL, "Receive Tile: " + (tile != null) + " x " + x + " y " + y);

		if (tile != null && levelOfDetail == currentLevelOfDetail) {

			int[] xy = TileUtility.getXYTileIndex(upperLeft,
					Math.round(currentLevelOfDetail));

			x -= xy[0];
			y -= xy[1];
			Log.d(TAG_MAP_MODEL, "Normalise Tile:  x " + x + " y " + y);

			Canvas canvas = new Canvas(map);

			// Malt leider nicht die Ränder aus
			canvas.drawBitmap(tile, (x * tile.getWidth()),
					(y * tile.getWidth()), null);

			/*
			 * int left = x * tile.getWidth(); int right = left +
			 * tile.getWidth(); int top = y * tile.getHeight(); int bottom = top
			 * + tile.getHeight();
			 *
			 * // canvas.drawBitmap(tile, new
			 * Rect(0,0,tile.getWidth(),tile.getWidth()), new Rect(left, top
			 * ,right, bottom), null);
			 */
			Log.d("MAP_VIEW", "Fog wurde erstellt.");

			// Bitmap newTile = tile.copy(tile.getConfig(), tile.isMutable());
			this.mapController.onMapOverlayImageChange(map);
		}
	}
}

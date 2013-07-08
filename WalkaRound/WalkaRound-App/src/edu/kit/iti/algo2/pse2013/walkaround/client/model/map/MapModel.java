package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
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

	private static String TAG_MAP_MODEL = MapModel.class.getSimpleName();
	private static MapModel mapModel;
	private final static int DEFAULT_TILE_SIZE = 256;

	private float currentLevelOfDetail;
	private MapController mapController;
	private Coordinate upperLeft;
	private Coordinate mid;

	private Point size;
	private TileFetcher tileFetcher;

	private Bitmap map;
	DisplayCoordinate mapOffset;
	int xAmount;
	int yAmount;
	float currentTileWidth;

	int xZoomBorder;
	int yZoomBorder;

	/**
	 *
	 * @param c
	 * @return
	 */
	public static MapModel initialize(Coordinate c,
			MapController mapController, Point size) {
		if (mapModel == null) {
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
			Log.d(TAG_MAP_MODEL, "Bitte initialisieren Sie zuerst MapView");
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

		Log.d("WTF", "upperleft " +  upperLeft);
		
		//this.computeMid();
		Log.d("WTF", "mid " +  mid);
		
		//this.upperLeft = new Coordinate(this.upperLeft,
		//		-Math.abs(this.mid.getLatitude() - this.upperLeft.getLatitude()),
		//		Math.abs(this.mid.getLongitude() - this.upperLeft.getLongitude()));

		Log.d("WTF", "upperLeft2 " +  upperLeft);
		
		Log.d(TAG_MAP_MODEL, "Default LOD wird gesetzt");
		this.currentLevelOfDetail = CurrentMapStyleModel.getInstance()
				.getCurrentMapStyle().getDefaultLevelOfDetail();

		Log.d(TAG_MAP_MODEL, "Anzahl der Tiles werden ausgerechnet.");
		computeAmountsOfTiles();

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

		Log.d(TAG_MAP_MODEL, "Map wird initialisiert");
		this.generateMapOverlayImage();

		//this.mapController.onMapOverlayImageChange(map);
		
		
		Log.d(TAG_MAP_MODEL, "Map Model wurde initialisiert");
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
		Log.d("WTF", "CoordDiffY: "+-CoordinateUtility.convertPixelsToDegrees(dc.getY(),
				currentLevelOfDetail, CoordinateUtility.DIRECTION_Y));
		Log.d("WTF", "CoordDiffX: "+CoordinateUtility.convertPixelsToDegrees(dc.getX(),
				currentLevelOfDetail, CoordinateUtility.DIRECTION_X));
		Log.d("WTF","PixelDiffX: "+dc.getX());
		Log.d("WTF","PixelDiffY: "+dc.getY());
		return new Coordinate(this.upperLeft,
				-CoordinateUtility.convertPixelsToDegrees(dc.getY(),
						currentLevelOfDetail, CoordinateUtility.DIRECTION_Y),
				CoordinateUtility.convertPixelsToDegrees(dc.getX(),
						currentLevelOfDetail, CoordinateUtility.DIRECTION_X));
	}

	/**
	 * berechnet die Mitte der karte
	 */
	private void computeMid() {
		this.mid = computeCoordinateByDisplayCoordinate(new DisplayCoordinate(
				size.x / 2, size.y / 2));
	}

	/**
	 *
	 * @param c
	 */
	public void shift(DisplayCoordinate delta) {
		// TODO

		Log.d("MAP_TOUCH_SROLL", "Ecke vorher " + this.upperLeft.toString());

		this.upperLeft = new Coordinate(this.upperLeft,
				-CoordinateUtility.convertPixelsToDegrees(delta.getY(),
						this.currentLevelOfDetail,
						CoordinateUtility.DIRECTION_Y),

				CoordinateUtility.convertPixelsToDegrees(delta.getX(),
						this.currentLevelOfDetail,
						CoordinateUtility.DIRECTION_X)

				);
		Log.d("MAP_TOUCH_SROLL", "Ecke Nachher " + this.upperLeft.toString());
		this.fetchTiles();
	}

	/**
	 *
	 */
	public void generateRouteOverlayImage() {

	}

	/**
	 *
	 */
	public void generateMapOverlayImage() {
		
		Log.d(TAG_MAP_MODEL, "LOD " + currentLevelOfDetail + " x " + xZoomBorder + " y " + yZoomBorder);
		Log.d(TAG_MAP_MODEL, "MapStyle: " + CurrentMapStyleModel.getInstance().getCurrentMapStyle().getName());

		if (this.currentLevelOfDetail < this.xZoomBorder && this.currentLevelOfDetail < this.yZoomBorder) {
			Log.d(TAG_MAP_MODEL, "generiere Bitmap kleiner display ");
			final int size = ((int) Math.pow(2, this.currentLevelOfDetail))
					* DEFAULT_TILE_SIZE;
			//this.map.recycle();
			this.map = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
			this.map.prepareToDraw();
			this.fetchTiles();
			return;
		} else if (this.currentLevelOfDetail < this.xZoomBorder) {
			Log.d(TAG_MAP_MODEL, "generiere Bitmap kleiner x Achse ");
			final int sizeX = ((int) Math.pow(2, this.currentLevelOfDetail))
					* DEFAULT_TILE_SIZE;
			//this.map.recycle();
			this.map = Bitmap.createBitmap(sizeX, size.y,
					Bitmap.Config.ARGB_8888);
			this.map.prepareToDraw();
			this.fetchTiles();
			return;
		} else if (this.currentLevelOfDetail < this.yZoomBorder) {
			Log.d(TAG_MAP_MODEL, "generiere Bitmap kleiner y Achse ");
			final int sizeY = ((int) Math.pow(2, this.currentLevelOfDetail))
					* DEFAULT_TILE_SIZE;
			//this.map.recycle();
			this.map = Bitmap.createBitmap(size.x, sizeY,
					Bitmap.Config.ARGB_8888);
			this.map.prepareToDraw();
			this.fetchTiles();
			return;
		}

		Log.d(TAG_MAP_MODEL, "create Bitmap greater than Display ");
		//this.map.recycle();
		this.map = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
		this.map.prepareToDraw();
		this.fetchTiles();
	}

	private float getCurrentTileWidthInPixels() {
		return (float) ((256 * Math.pow(2, getCurrentLevelOfDetail())) /
		Math.pow(2, Math.round(getCurrentLevelOfDetail())));
	}

	/**
	 *
	 */
	private void computeAmountsOfTiles() {
		currentTileWidth = getCurrentTileWidthInPixels();

		xAmount = (int) Math.ceil(size.x / currentTileWidth);
		yAmount = (int) Math.ceil(size.y / currentTileWidth);
		xAmount++;
		yAmount++;
	}

	/**
	 *
	 * @return
	 */
	private boolean fetchTiles() {
		this.mapOffset = this.getTileOffset();
		final boolean result = tileFetcher.requestTiles(
				Math.round(currentLevelOfDetail), upperLeft, xAmount, yAmount);
		Log.d(TAG_MAP_MODEL, "Tiles werden angefordert: " + result + " x "
				+ xAmount + " y " + yAmount);
		return result;
	}

	/**
	 *
	 * @return
	 */
	public Coordinate getUpperLeft() {
		return upperLeft;
	}

	/**
	 *
	 */
	public void setNewStyle() {
		this.generateMapOverlayImage();
	}

	/**
	 *
	 * @param delta
	 * @param c
	 */
	public boolean zoom(float delta, DisplayCoordinate c) {
		Log.d(TAG_MAP_MODEL, "ZOOM um " + delta + " auf " + c.toString());
		return this.zoom(delta, this.computeCoordinateByDisplayCoordinate(c));
	}

	/**
	 *
	 * @param delta
	 */
	public boolean zoom(float delta) {
		this.computeMid();
		// Log.d(TAG_MAP_MODEL, "ZOOM um " + delta + " auf " + mid.toString());
		return this.zoom(delta, mid);
	}

	/**
	 *
	 * @param delta
	 * @param c
	 */
	public boolean zoom(float delta, Coordinate c) {

		Log.d(TAG_MAP_MODEL, "ZOOM um " + delta + " auf " + c.toString());

		// TODO

		final MapStyle mapStyle = CurrentMapStyleModel.getInstance()
				.getCurrentMapStyle();
		final float nextLevelOfDetail = this.currentLevelOfDetail + delta;

		Log.d(TAG_MAP_MODEL, "ZOOM von " + this.currentLevelOfDetail + " auf "
				+ nextLevelOfDetail + " wird geprüft");
		if (nextLevelOfDetail > mapStyle.getMaxLevelOfDetail()
				|| nextLevelOfDetail < mapStyle.getMinLevelOfDetail()) {
			Log.d(TAG_MAP_MODEL, "ZOOM wird abgebrochen");
			return false;
		}

		Log.d(TAG_MAP_MODEL, "ZOOM auf " + nextLevelOfDetail
				+ " wird ausgeführt");
		this.currentLevelOfDetail = nextLevelOfDetail;

		final double deltaX = c.getLatitude() - this.upperLeft.getLatitude();
		final double deltaY = c.getLongitude()
				- this.upperLeft.getLongitude();

		Log.d(TAG_MAP_MODEL, "Deltas : " + deltaX + " " + deltaY);

		/*
		 * float deltaLatitude = CoordinateUtility.convertDegreesToPixel(deltaX,
		 * nextLevelOfDetail); float deltaLongitude =
		 * CoordinateUtility.convertDegreesToPixel(deltaY, nextLevelOfDetail);
		 * deltaLatitude =
		 * CoordinateUtility.convertPixelsToDegrees(-deltaLatitude,
		 * nextLevelOfDetail); deltaLongitude =
		 * CoordinateUtility.convertPixelsToDegrees(-deltaLongitude,
		 * nextLevelOfDetail);
		 */

		Log.d(TAG_MAP_MODEL, "UpperLeft war: " + this.upperLeft);
		this.upperLeft = new Coordinate(c, -deltaY, -deltaY);
		Log.d(TAG_MAP_MODEL, "Neuer upperLeft ist: " + this.upperLeft);

		this.computeAmountsOfTiles();
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
	public LinkedList<DisplayCoordinate> getPOIofDisplay(DisplayCoordinate dc,
			int[] category, int profile) {
		return null;
	}

	/**
	 * Gibt das aktuelle Level Of Detail zurück
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
	 *
	 *
	 * @return
	 */
	public DisplayCoordinate getTileOffset(){
		float latDiff = (float) ((upperLeft.getLatitude() + 90) % (180 / Math.pow(2, currentLevelOfDetail)));
		float lonDiff = (float) ((upperLeft.getLongitude() + 180) % (360 / Math.pow(2, currentLevelOfDetail)));
		float yDiff = CoordinateUtility.convertDegreesToPixels(latDiff, currentLevelOfDetail, CoordinateUtility.DIRECTION_VERTICAL);
		float xDiff = CoordinateUtility.convertDegreesToPixels(lonDiff, currentLevelOfDetail, CoordinateUtility.DIRECTION_HORIZONTAL);
		Log.d(TAG_MAP_MODEL, String.format(
				"TileOffset: x: %.8fdp y: %.8fdp\n" +
				"TileOffset: lon: %.8f lat: %.8f\n" +
				"UpperLeft: %s\n" +
				"LevelOfDetail: %.8f",
				xDiff, yDiff, lonDiff, latDiff, upperLeft, currentLevelOfDetail));
		return new DisplayCoordinate(xDiff, yDiff);
	}


	@Override
	public void receiveTile(final Bitmap tile, final int x, final int y, final int levelOfDetail) {
		// Log.d(TAG_MAP_MODEL, "Receive Tile: " + (tile != null) + " x " + x + " y " + y);

		//if (tile != null && levelOfDetail == currentLevelOfDetail) {
			
			int[] xy = TileUtility.getXYTileIndex(upperLeft, Math.round(currentLevelOfDetail));

			int localX = x - xy[0];
			int localY = y - xy[1];
			Log.d(TAG_MAP_MODEL, "Normalise Tile:  x " + localX + " y " + localY);

			Canvas canvas = new Canvas(map);

			Log.d(TAG_MAP_MODEL, "ZEICHNE!");
			canvas.drawBitmap(tile, (localX * tile.getWidth()) - mapOffset.getX(),
					(localY * tile.getWidth()) + mapOffset.getY(), null);

			/*
			 * int left = x * tile.getWidth(); int right = left +
			 * tile.getWidth(); int top = y * tile.getHeight(); int bottom = top
			 * + tile.getHeight();
			 *
			 * // canvas.drawBitmap(tile, new
			 * Rect(0,0,tile.getWidth(),tile.getWidth()), new Rect(left, top
			 * ,right, bottom), null);
			 */
			// Log.d("MAP_VIEW", "Fog wurde erstellt.");

			// Bitmap newTile = tile.copy(tile.getConfig(), tile.isMutable());
			
			this.mapController.onMapOverlayImageChange(map);
		//}
	}
}

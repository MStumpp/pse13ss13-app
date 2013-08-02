package edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

public class MapGen extends Thread implements TileListener {

	// static

	/**
	 * Debug Tag
	 */
	private static String TAG_MAP_MODEL = MapGen.class.getSimpleName();

	/**
	 *
	 */
	public static int defaultBackground = Color.rgb(227, 227, 227);

	/**
	 *
	 */
	public static int defaultBackgroundEmpty = Color.argb(0, 0, 0, 0);

	// non Static

	/**
	 *
	 */
	private MapController mapController;

	/**
	 *
	 */
	private TileFetcher tileFetcher;

	/**
	 *
	 */
	private Bitmap map;

	/**
	 *
	 */
	private Point size;

	/**
	 *
	 */
	private Point amount;

	/**
	 *
	 */
	private Coordinate center;

	/**
	 *
	 */
	private Coordinate topLeft;

	/**
	 *
	 */
	private float lod;

	/**
	 *
	 */
	private float currentTileWidth;

	/**
	 *
	 */
	private DisplayCoordinate mapOffset;

	/**
	 *
	 */
	private int[] indexXY;

	/**
	 *
	 */
	private Bitmap empty = Bitmap.createBitmap(new int[] { 0x00000000 }, 1, 1,
			Bitmap.Config.ARGB_8888);

	/**
	 *
	 */
	public MapGen(Point size, MapController mc, Coordinate center, float lod) {
		Log.d("bash", "" + (center == null) + " " + (size == null) + " "
				+ (mc == null));

		// initialisiere Vars
		this.center = center;

		this.lod = lod;

		this.indexXY = new int[2];

		this.mapOffset = new DisplayCoordinate(0, 0);

		this.size = size;

		this.amount = new Point();

		this.mapController = mc;

		this.topLeft = this.getTopLeft(center);

		this.tileFetcher = new TileFetcher();

		this.map = Bitmap.createBitmap(this.size.x, this.size.y,
				Bitmap.Config.ARGB_8888);

	}

	/**
	 * Compute and gives the Tile Offset back
	 *
	 * @return Tile Offset
	 */
	private DisplayCoordinate computeTileOffset() {

		double lonDiff = ((topLeft.getLongitude() + 180) % (360 / Math.pow(2,
				lod)));

		double latDiff = ((topLeft.getLatitude() + 90) % (180 / Math
				.pow(2, lod)));

		float xDiff = CoordinateUtility.convertDegreesToPixels(lonDiff, lod,
				CoordinateUtility.DIRECTION_HORIZONTAL);

		float yDiff = CoordinateUtility.convertDegreesToPixels(latDiff, lod,
				CoordinateUtility.DIRECTION_VERTICAL);

		// TODO beim nach oben schieben muss yDiff auf dem Display kleiner
		// werden!
		// ich denke hier ist möglicherweise auch ein RundungsFehler!
		yDiff = (currentTileWidth - 1) - yDiff;
		// yDiff = 256-yDiff;

		Log.d(TAG_MAP_MODEL, String.format("TileOffset: x: %.8fdp y: %.8fdp\n"
				+ "TileOffset: lon: %.8f lat: %.8f\n" + "Center: %s\n"
				+ "LevelOfDetail: %.8f", xDiff, yDiff, lonDiff, latDiff,
				center, lod));

		Log.d("latDiff", "" + (yDiff - 255 - 100));
		Log.d("lonDiff", "" + (xDiff - 10));
		return new DisplayCoordinate(xDiff, yDiff);
	}

	/**
	 * compute the amount tiles needed to fill the display
	 */
	private void computeAmountOfTiles() {
		currentTileWidth = CoordinateUtility
				.computeCurrentTileWidthInPixels(this.lod);

		amount.set((int) Math.ceil(size.x / currentTileWidth) + 1,
				(int) Math.ceil(size.y / currentTileWidth) + 1);
	}

	/**
	 * Returns the upperLeft Coordinate
	 *
	 * @return the top left geo-oordinate
	 */
	private Coordinate getTopLeft(Coordinate center) {
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
	private Coordinate getBottomRight(Coordinate center) {
		return new Coordinate(center, CoordinateUtility.convertPixelsToDegrees(
				size.y / 2f, lod, CoordinateUtility.DIRECTION_LATITUDE),
				CoordinateUtility.convertPixelsToDegrees(size.x / 2f, lod,
						CoordinateUtility.DIRECTION_LONGITUDE));
	}

	/**
	 * This should be the only Method to get a new Map
	 *
	 * @param center
	 * @param load
	 */
	public void generateMap(final Coordinate center, final float lod) {

		// n�tige Daten aufbauen
		this.lod = lod;
		this.center = center;
		this.topLeft = this.getTopLeft(this.center);
		this.mapOffset = this.computeTileOffset();
		this.indexXY = TileUtility.getXYTileIndex(this.topLeft,
				Math.round(this.lod));
		clearBitmap();
		this.computeAmountOfTiles();
		// Tiles requesten
		tileFetcher.requestTiles(
				(int) this.lod,
				new Coordinate(this.topLeft.getLatitude()
						+ CoordinateUtility.convertPixelsToDegrees(
								currentTileWidth, lod,
								CoordinateUtility.DIRECTION_LATITUDE),
						this.topLeft.getLongitude()), amount.x, amount.y, this);
	}

	/**
	 * recycle and creates a new map recycle and creates a new routeOverlay
	 *
	 * @param width
	 *            of the map and routeOverlay
	 * @param height
	 *            of the map and routeOverlay
	 */
	private void clearBitmap() {

		Log.d(TAG_MAP_MODEL, "clear Map Bitmap");

		// mapController.onMapOverlayImageChange(empty);
		this.map.eraseColor(defaultBackground);
		this.pushMap();
		this.map.prepareToDraw();
	}

	/**
	 *
	 */
	private void pushMap() {
		this.mapController.onMapOverlayImageChange(map);
	}

	// Schnittstelle
	@Override
	public void run() {
		// generate the first View
		this.generateMap(mapController.getCenter(), mapController.getLoD());
		while (true)
			;
	}

	@Override
	public void receiveTile(Bitmap tile, int x, int y, int levelOfDetail) {
		Thread t = new Thread(new DrawingQueue(tile, x, y));
		t.start();
	}

	private class DrawingQueue implements Runnable {

		final Bitmap tile;
		int x;
		int y;

		public DrawingQueue(final Bitmap tile, int x, int y) {
			this.tile = tile;
			this.x = x;
			this.y = y;
		}

		@Override
		public void run() {
			Log.d(TAG_MAP_MODEL, "Receive Tile!");

			int tileX = x - indexXY[0];
			int tileY = (y - indexXY[1]);

			Log.d(TAG_MAP_MODEL, "Normalise Tile:  x " + tileX + " y " + tileY);

			if (!map.isRecycled() && tile != null) {
				Canvas canvas = new Canvas(map);
				Log.d(TAG_MAP_MODEL, "ZEICHNE! ");
				if (tileY == 0 && tileX == 0) {
					Log.d(TAG_MAP_MODEL,
							"ZEICHNE OFF! "
									+ ((tileX * currentTileWidth) - mapOffset
											.getX())
									+ " : "
									+ ((tileY * currentTileWidth) - mapOffset
											.getY()));
					Log.d(TAG_MAP_MODEL, "ZEICHNE OFF! " + tileX + " " + tileY);
				}
				// synchronized (map) {
				canvas.drawBitmap(
						Bitmap.createScaledBitmap(tile,
								Math.round(currentTileWidth),
								Math.round(currentTileWidth), false),
						(tileX * currentTileWidth) - mapOffset.getX(),
						(tileY * currentTileWidth) - mapOffset.getY(), null);
				// }
			}

			pushMap();
		}

	}
}

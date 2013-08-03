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

/**
 * This Class generate a Bitmap that represent a part of the OSM-Map.
 * A Coordinate is the fix Point of the map at the center. Out of this Coordinate
 * this Class build a bounding box between the top left corner and the bottom
 * right corner.   
 * 
 * @author Ludwig Biermann
 *
 */
public class MapGen extends Thread implements TileListener {

	/**
	 * Debug Tag
	 */
	private static String TAG_MapGen = MapGen.class.getSimpleName();

	/**
<<<<<<< HEAD
	 * The default Background Color
=======
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	public static int defaultBackground = Color.rgb(227, 227, 227);

	/**
<<<<<<< HEAD
	 * The default empty Background Color
	 */
	public static int defaultBackgroundEmpty = Color.argb(0, 0, 0, 0);

	/**
	 * The Tile Fetcher. This is the location to get the tile parts of the map
=======
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
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	private TileFetcher tileFetcher;

	/**
<<<<<<< HEAD
	 * The Bitmap that represent the map on the display.
	 * Its essential that the Map is created on time.
=======
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	private Bitmap map;

	/**
<<<<<<< HEAD
	 * the size of the Display
=======
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	private Point size;

	/**
<<<<<<< HEAD
	 * the amount of tile parts which fit in the display
=======
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	private Point amount;

	/**
<<<<<<< HEAD
	 * the current fix center Coordinate 
=======
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	private Coordinate center;

	/**
<<<<<<< HEAD
	 * the current topLeft Coordinate
=======
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	private Coordinate topLeft;

	/**
<<<<<<< HEAD
	 * the Level of Detail
=======
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	private float lod;

	/**
<<<<<<< HEAD
	 * the current Tile Width
=======
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	private float currentTileWidth;

	/**
<<<<<<< HEAD
	 * the Offset of the tile parts
=======
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	private DisplayCoordinate mapOffset;

	/**
<<<<<<< HEAD
	 * the index of the tile on the top Left corner
=======
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	//TODO maybe it should be the center tile
	private int[] indexXY;

	/**
<<<<<<< HEAD
	 * COntstructs a new MapGenerator
	 * @param size the display size
	 * @param mc the MapController
	 * @param center the start center Coordinate
	 * @param lod the start Level of Detail
=======
	 *
	 */
	private Bitmap empty = Bitmap.createBitmap(new int[] { 0x00000000 }, 1, 1,
			Bitmap.Config.ARGB_8888);

	/**
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	public MapGen(Point size, Coordinate center, float lod) {
		this.center = center;

		this.lod = lod;

		this.indexXY = new int[2];

		this.mapOffset = new DisplayCoordinate(0, 0);

		this.size = size;

		this.amount = new Point();


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

		yDiff = (currentTileWidth - 1) - yDiff;

		Log.d(TAG_MapGen, String.format("TileOffset: x: %.8fdp y: %.8fdp\n"
				+ "TileOffset: lon: %.8f lat: %.8f\n" + "Center: %s\n"
				+ "LevelOfDetail: %.8f", xDiff, yDiff, lonDiff, latDiff,
				center, lod));

		return new DisplayCoordinate(xDiff, yDiff);
	}

	/**
	 * compute the amount tiles needed to fill the display
	 */
	private void computeAmountOfTiles() {
		this.currentTileWidth = CoordinateUtility
				.computeCurrentTileWidthInPixels(this.lod);

		this.amount.set((int) Math.ceil(size.x / currentTileWidth) + 1,
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
<<<<<<< HEAD
	 * Returns the bottomRight Coordinate
	 * 
=======
	 * Returns the upperLeft Coordinate
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 * @return the top left geo-oordinate
	 */
	//TODO bounding box is missing
	@SuppressWarnings("unused")
	private Coordinate getBottomRight(Coordinate center) {
		return new Coordinate(center, CoordinateUtility.convertPixelsToDegrees(
				size.y / 2f, lod, CoordinateUtility.DIRECTION_LATITUDE),
				CoordinateUtility.convertPixelsToDegrees(size.x / 2f, lod,
						CoordinateUtility.DIRECTION_LONGITUDE));
	}

	/**
<<<<<<< HEAD
	 * This Method starts the compute of a new Map.
	 * 
	 * @param center the new center Coordinate
	 * @param lod the Level of Detail
=======
	 * This should be the only Method to get a new Map
	 *
	 * @param center
	 * @param load
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	public void generateMap(final Coordinate center, final float lod) {

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
		Log.d(TAG_MapGen, "clear Map Bitmap");

		this.map.eraseColor(defaultBackground);
		this.pushMap();
		this.map.prepareToDraw();
	}

	/**
<<<<<<< HEAD
	 *  push the map to the View
=======
	 *
>>>>>>> 2c44cbad8a3839e1980d3278d27893f3775d3493
	 */
	private void pushMap() {
		MapController.getInstance().onMapOverlayImageChange(map);
	}

	//TODO no need anymore pls delete Threading
	@Override
	public void run() {
		this.generateMap(MapController.getInstance().getCenter(), MapController.getInstance().getLoD());
		while (true)
			;
	}

	@Override
	public void receiveTile(Bitmap tile, int x, int y, int levelOfDetail) {
		// starts a new Thread to draw the map
		Thread t = new Thread(new TileDrawer(tile, x, y));
		t.start();
	}

	/**
	 * This class draws a part of the map
	 * 
	 * @author Ludwig Biermann
	 *
	 */
	private class TileDrawer implements Runnable {

		/**
		 * the new Tile to draw
		 */
		final Bitmap tile;
		
		/**
		 * x position
		 */
		int x;
		
		/**
		 * Y position
		 */
		int y;

		/**
		 * Constructs a new Tile Drawer
		 * 
		 * @param tile the new tile to draw
		 * @param x x-Position
		 * @param y y Position
		 */
		public TileDrawer(final Bitmap tile, int x, int y) {
			this.tile = tile;
			this.x = x;
			this.y = y;
		}

		@Override
		public void run() {
			Log.d(TAG_MapGen, "Receive Tile!");

			int tileX = x - indexXY[0];
			int tileY = (y - indexXY[1]);

			Log.d(TAG_MapGen, "Normalise Tile:  x " + tileX + " y " + tileY);

			if (!map.isRecycled() && tile != null) {
				Canvas canvas = new Canvas(map);
				Log.d(TAG_MapGen, "ZEICHNE! ");
				if (tileY == 0 && tileX == 0) {
					Log.d(TAG_MapGen,
							"ZEICHNE OFF! "
									+ ((tileX * currentTileWidth) - mapOffset
											.getX())
									+ " : "
									+ ((tileY * currentTileWidth) - mapOffset
											.getY()));
					Log.d(TAG_MapGen, "ZEICHNE OFF! " + tileX + " " + tileY);
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

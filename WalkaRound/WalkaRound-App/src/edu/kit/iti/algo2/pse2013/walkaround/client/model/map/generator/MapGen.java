package edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

/**
 * This Class generate a Bitmap that represent a part of the OSM-Map. A
 * Coordinate is the fix Point of the map at the center. Out of this Coordinate
 * this Class build a bounding box between the top left corner and the bottom
 * right corner.
 * 
 * @author Ludwig Biermann
 * 
 */
public class MapGen implements TileListener {

	/**
	 * Debug Tag
	 */
	private static String TAG_MapGen = MapGen.class.getSimpleName();

	/**
	 * 
	 * The default Background Color
	 * 
	 * 
	 */
	public static int defaultBackground = Color.rgb(227, 227, 227);

	/**
	 * 
	 * The default empty Background Color
	 */
	public static int defaultBackgroundEmpty = Color.argb(0, 0, 0, 0);

	/**
	 * The Tile Fetcher. This is the location to get the tile parts of the map
	 * 
	 */
	private TileFetcher tileFetcher;

	/**
	 * 
	 * The Bitmap that represent the map on the display. Its essential that the
	 * Map is created on time.
	 * 
	 * 
	 */
	private Bitmap map;

	/**
	 * the size of the Display
	 * 
	 */
	private Point size;

	/**
	 * the amount of tile parts which fit in the display
	 * 
	 */
	private Point amount;

	/**
	 * the current Coordinates
	 * 
	 */
	private BoundingBox coorBox;


	/**
	 * the Level of Detail
	 * 
	 */
	private float lod;

	/**
	 * the current Tile Width
	 * 
	 */
	private float currentTileWidth;

	/**
	 * the Offset of the tile parts
	 * 
	 */
	private DisplayCoordinate mapOffset;

	/**
	 * the index of the tile on the top Left corner
	 * 
	 */
	// TODO maybe it should be the center tile
	private int[] indexXY;

	/**
	 * 
	 * COntstructs a new MapGenerator
	 * 
	 * @param size
	 *            the display size
	 * @param mc
	 *            the MapController
	 * @param center
	 *            the start center Coordinate
	 * @param lod
	 *            the start Level of Detail
	 */
	public MapGen(Point size, BoundingBox coorBox, float lod) {
		this.coorBox = coorBox;

		this.lod = lod;

		this.indexXY = new int[2];

		this.mapOffset = new DisplayCoordinate(0, 0);

		this.size = size;

		this.amount = new Point();

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

		double lonDiff = ((coorBox.getTopLeft().getLongitude() + 180) % (360 / Math.pow(2,
				lod)));

		double latDiff = ((coorBox.getTopLeft().getLatitude() + 90) % (180 / Math
				.pow(2, lod)));

		float xDiff = CoordinateUtility.convertDegreesToPixels(lonDiff, lod,
				CoordinateUtility.DIRECTION_HORIZONTAL);

		float yDiff = CoordinateUtility.convertDegreesToPixels(latDiff, lod,
				CoordinateUtility.DIRECTION_VERTICAL);

		yDiff = (currentTileWidth - 1) - yDiff;

		Log.d(TAG_MapGen, String.format("TileOffset: x: %.8fdp y: %.8fdp\n"
				+ "TileOffset: lon: %.8f lat: %.8f\n" + "Center: %s\n"
				+ "LevelOfDetail: %.8f", xDiff, yDiff, lonDiff, latDiff,
				coorBox.getCenter(), lod));

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
	 * Returns the bottomRight Coordinate
	 * 
	 * @return the top left geo-oordinate
	 */
	// TODO bounding box is missing
	@SuppressWarnings("unused")
	private Coordinate getBottomRight(Coordinate center) {
		return new Coordinate(center, CoordinateUtility.convertPixelsToDegrees(
				size.y / 2f, lod, CoordinateUtility.DIRECTION_LATITUDE),
				CoordinateUtility.convertPixelsToDegrees(size.x / 2f, lod,
						CoordinateUtility.DIRECTION_LONGITUDE));
	}

	/**
	 * 
	 * This Method starts the compute of a new Map.
	 * 
	 * @param center
	 *            the new center Coordinate
	 * @param lod
	 *            the Level of Detail
	 */
	public void generateMap(final BoundingBox coorBox, final float lod) {

		this.lod = lod;
		this.coorBox = coorBox;
		this.mapOffset = this.computeTileOffset();
		this.indexXY = TileUtility.getXYTileIndex(coorBox.getTopLeft(),
				Math.round(this.lod));
		clearBitmap();
		this.computeAmountOfTiles();
		// Tiles requesten
		tileFetcher.requestTiles(
				(int) this.lod,
				new Coordinate(this.coorBox.getTopLeft().getLatitude()
						+ CoordinateUtility.convertPixelsToDegrees(
								currentTileWidth, lod,
								CoordinateUtility.DIRECTION_LATITUDE),
						this.coorBox.getTopLeft().getLongitude()), amount.x, amount.y, this);
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
	 * push the map to the View
	 * 
	 */
	private void pushMap() {
		MapController.getInstance().onMapOverlayImageChange(map);
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
		 * @param tile
		 *            the new tile to draw
		 * @param x
		 *            x-Position
		 * @param y
		 *            y Position
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

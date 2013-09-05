package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher.TileListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

public class MapView extends View implements TileListener {

	private Handler h;
	private final int FRAME_RATE = 25;

	public static Coordinate defaultCoordinate = new Coordinate(49.0145, 8.419); // 211

	private Runnable r = new Runnable() {
		// Override
		public void run() {
			invalidate();
		}
	};

	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.coorBox = BoundingBox.getInstance(context);
		h = new Handler();

		// this.tileFetcher = new TileFetcher();
		this.indexXY = new int[2];
		this.mapOffset = new DisplayCoordinate(0, 0);

		Log.d("test", " " + (coorBox.getDisplaySize() != null));
		size = coorBox.getDisplaySize();

		// this.coorBox = new BoundingBox(defaultCoordinate, size,
		// CurrentMapStyleModel.getInstance().getCurrentMapStyle().getDefaultLevelOfDetail());

		Log.d("test", " " + (size != null));

		this.amount = new Point();
		this.computeParams();
	}

	protected void onDraw(Canvas c) {
		for (int i = 0; i < tileT.size(); i++) {
			tileT.get(i).start();
			// tileT.remove(t);
		}
		tileT.clear();

		synchronized (tileHolder) {
			for (int i = 0; i < tileHolder.size(); i++) {
				c.drawBitmap(
						tileHolder.get(i).b,
						(tileHolder.get(i).x * currentTileWidth)
								+ mapOffset.getX(),
						(tileHolder.get(i).y * currentTileWidth)
								+ mapOffset.getY(), null);
			}
		}
		// tileHolder.clear();
		// c.drawBitmap(map, 0, 0, null);

		h.postDelayed(r, FRAME_RATE);
	}

	/**
	 * Debug Tag
	 */
	private static String TAG = MapView.class.getSimpleName();

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
	// private TileFetcher tileFetcher;

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
	 * the Points per Offset Point
	 */
	float pPerDiff;

	/**
	 * the index of the tile on the top Left corner
	 * 
	 */
	private int[] indexXY;

	/**
	 * Compute and gives the Tile Offset back
	 * 
	 * @return Tile Offset
	 */
	private DisplayCoordinate computeTileOffset() {

		double lonDiff = ((coorBox.getCenter().getLongitude() + 180) % (360 / Math
				.pow(2, this.coorBox.getLevelOfDetail())));

		// double latDiff = ((coorBox.getCenter().getLatitude() + 90) % (180 /
		// Math
		// .pow(2, this.coorBox.getLevelOfDetail())));
		int[] index = TileUtility.getXYTileIndex(coorBox.getCenter(),
				(int) coorBox.getLevelOfDetail());

		double n = Math.PI - (2.0 * Math.PI * index[1])
				/ Math.pow(2.0, this.coorBox.getLevelOfDetail());
		n = Math.toDegrees(Math.atan(Math.sinh(n)));

		Log.d("wrong", "Tile Lat " + n);
		;
		double latDiff = Math.abs(coorBox.getCenter().getLatitude() - n);

		float xDiff = CoordinateUtility.convertDegreesToPixels(lonDiff,
				this.coorBox.getLevelOfDetail(),
				CoordinateUtility.DIRECTION_HORIZONTAL);

		float yDiff = CoordinateUtility.convertDegreesToPixels(latDiff,
				this.coorBox.getLevelOfDetail(),
				CoordinateUtility.DIRECTION_VERTICAL);

		yDiff = yDiff * pPerDiff;
		Log.d(TAG, "RealOffset: x " + xDiff + " y: " + yDiff);

		// yDiff = (currentTileWidth - 1) - yDiff;

		xDiff = size.x / 2 - Math.abs(xDiff);
		yDiff = size.y / 2 - Math.abs(yDiff);

		// yDiff = (yDiff - 25) % this.currentTileWidth;

		Log.d(TAG, String.format("TileOffset: x: %.8fdp y: %.8fdp\n"
				+ "TileOffset: lon: %.8f lat: %.8f\n" + "Center: %s\n"
				+ "LevelOfDetail: %.8f", xDiff, yDiff, lonDiff, latDiff,
				coorBox.getCenter(), this.coorBox.getLevelOfDetail()));

		return new DisplayCoordinate(xDiff, yDiff);
	}

	/**
	 * compute the amount tiles needed to fill the display
	 */
	private void computeAmountOfTiles() {
		this.currentTileWidth = CoordinateUtility
				.computeCurrentTileWidthInPixels(this.coorBox
						.getLevelOfDetail());

		this.pPerDiff = currentTileWidth / 334;

		this.amount.set((int) Math.ceil(size.x / currentTileWidth) + 1,
				(int) Math.ceil(size.y / currentTileWidth) + 1);
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
	public void computeParams() {

		// compute new Parameter

		this.mapOffset = this.computeTileOffset();
		this.indexXY = TileUtility.getXYTileIndex(coorBox.getCenter(),
				Math.round(this.coorBox.getLevelOfDetail()));
		this.computeAmountOfTiles();
		tileT.clear();
		tileHolder.clear();

		// erase the map

		// if (this.map.isMutable()) {
		// map.eraseColor(defaultBackground);
		// }

		// this.map.prepareToDraw();

		// Tiles requesten
		// tileFetcher.requestTiles((int) this.coorBox.getLevelOfDetail(),
		// indexXY[0]-1, indexXY[1]-1, indexXY[0]+1, indexXY[1]+1, this);
	}

	/**
	 * push the map to the View
	 * 
	 * 
	 * private void pushMap() { try {
	 * MapController.getInstance().onMapOverlayImageChange(map); } catch
	 * (NullPointerException e) { Log.e(TAG,
	 * "MapController existiert noch nicht"); } }
	 */

	LinkedList<Thread> tileT = new LinkedList<Thread>();

	LinkedList<TilePaaring> tileHolder = new LinkedList<TilePaaring>();

	private class TilePaaring {

		public Bitmap b;
		public int y;
		public int x;

		public TilePaaring(Bitmap b, int x, int y) {
			this.b = Bitmap.createScaledBitmap(b, Math.round(currentTileWidth),
					Math.round(currentTileWidth), false);
			this.x = x;
			this.y = y;
		}

	}

	public void receiveTile(Bitmap tile, int x, int y, int levelOfDetail) {

		int tileX = x - indexXY[0];
		int tileY = (y - indexXY[1]);

		tileHolder.add(new TilePaaring(tile, tileX, tileY));

		// starts a new Thread to draw the map
		// Thread t = new Thread(new TileDrawer(tile, x, y));
		// tileT.add(t);
	}
}

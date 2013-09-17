package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.CenterListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.LevelOfDetailListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.ScaleListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher.TileListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

/**
 * The View draw the map.
 * 
 * @author Ludwig Biermann
 * @version 1.8
 * 
 */
public class MapView extends ImageView implements TileListener, CenterListener,
		LevelOfDetailListener {

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
	 * Helper Variable to update the map
	 */
	private Handler h;
	private Runnable r = new Runnable() {
		@Override
		public void run() {
			invalidate();
		}
	};

	/**
	 * The Frame Rate of the map
	 */
	private static final int FRAME_RATE = 25;

	/**
	 * default Coordinate to initialize
	 */
	public static Coordinate defaultCoordinate = new Coordinate(49.0145, 8.419); // 211

	/**
	 * Listener lists
	 */
	private LinkedList<Thread> tileT = new LinkedList<Thread>();
	private LinkedList<TilePaaring> tileHolder = new LinkedList<TilePaaring>();

	/**
	 * This create a new MapView.
	 * 
	 * @param context
	 *            the context of the app
	 * @param attrs
	 *            the needed attributes
	 */
	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);

		
		this.coorBox = BoundingBox.getInstance(context);
		
		this.currentTileWidth = CoordinateUtility
				.computeCurrentTileWidthInPixels(this.coorBox
						.getLevelOfDetail());
		
		coorBox.registerLevelOfDetailListener(this);
		coorBox.registerCenterListener(this);
		h = new Handler();

		this.indexXY = new int[2];
		this.mapOffset = new DisplayCoordinate(0, 0);

		Log.d("test", " " + (coorBox.getDisplaySize() != null));
		size = coorBox.getDisplaySize();

		Log.d("test", " " + (size != null));

		this.amount = new Point();
		this.computeParams();
		this.buildDrawingCache();
		this.setDrawingCacheEnabled(true);

	}
	

	@Override
	protected void onDraw(Canvas c) {
		for (int i = 0; i < tileT.size(); i++) {
			tileT.get(i).start();
		}
		tileT.clear();

		//c.scale(x, y);
		
		float scale = BoundingBox.getInstance().getScale();
		PointF p = BoundingBox.getInstance().getPivot();
		
		c.scale(scale, scale, p.x, p.y);
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

		h.postDelayed(r, FRAME_RATE);
	}

	/**
	 * Compute and gives the Tile Offset back
	 * 
	 * @return Tile Offset
	 */
	private DisplayCoordinate computeTileOffset() {

		double lonDiff = ((coorBox.getCenter().getLongitude() + 180) % (360 / Math
				.pow(2, this.coorBox.getLevelOfDetail())));

		int[] index = TileUtility.getXYTileIndex(coorBox.getCenter(),
				(int) coorBox.getLevelOfDetail());

		double n = Math.PI - (2.0 * Math.PI * index[1])
				/ Math.pow(2.0, this.coorBox.getLevelOfDetail());
		n = Math.toDegrees(Math.atan(Math.sinh(n)));

		double latDiff = Math.abs(coorBox.getCenter().getLatitude() - n);

		float xDiff = CoordinateUtility.convertDegreesToPixels(lonDiff,
				this.coorBox.getLevelOfDetail(),
				CoordinateUtility.DIRECTION_HORIZONTAL);

		float yDiff = CoordinateUtility.convertDegreesToPixels(latDiff,
				this.coorBox.getLevelOfDetail(),
				CoordinateUtility.DIRECTION_VERTICAL);

		yDiff = yDiff * pPerDiff;

		xDiff = size.x / 2 - Math.abs(xDiff);
		yDiff = size.y / 2 - Math.abs(yDiff);

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
		this.amount.set((int) Math.ceil(size.x / currentTileWidth) + 1,
				(int) Math.ceil(size.y / currentTileWidth) + 1);
	}

	/**
	 * This Method computes the offset and index and clears the current variable
	 */
	public void computeParams() {
		this.mapOffset = this.computeTileOffset();
		this.indexXY = TileUtility.getXYTileIndex(coorBox.getCenter(),
				Math.round(this.coorBox.getLevelOfDetail()));
		this.tileT.clear();
		this.tileHolder.clear();

		this.pPerDiff = currentTileWidth / 334;
		
		//this.computeAmountOfTiles();
	}

	/**
	 * A little Helper Class to associate Bitmaps with a Coordinate
	 * 
	 * @author Ludwig Biermann
	 * @version 1.0
	 * 
	 */
	private class TilePaaring {

		public Bitmap b;
		public int y;
		public int x;

		/**
		 * Construct a new Tile Paring
		 * 
		 * @param b
		 *            the bitmap
		 * @param x
		 *            x-coordinate
		 * @param y
		 *            y-coordinate
		 */
		public TilePaaring(Bitmap b, int x, int y) {
			this.b = b;
			this.x = x;
			this.y = y;
		}

	}

	@Override
	public void receiveTile(Bitmap tile, int x, int y, int levelOfDetail) {

		int tileX = x - indexXY[0];
		int tileY = (y - indexXY[1]);
		try {
			this.tileHolder.add(new TilePaaring(tile, tileX, tileY));
		} catch (OutOfMemoryError e) {
			Log.e(TAG, e.toString());
			System.gc();
		}

	}

	@Override
	public void onLevelOfDetailChange(float levelOfDetail) {
		this.computeParams();
		TileFetcher.getInstance().requestTiles(coorBox, this);
	}

	@Override
	public void onCenterChange(Coordinate center) {
		this.computeParams();
		TileFetcher.getInstance().requestTiles(coorBox, this);
	}

	@Override
	public String toString() {
		return MapView.class.getSimpleName();
	}
}

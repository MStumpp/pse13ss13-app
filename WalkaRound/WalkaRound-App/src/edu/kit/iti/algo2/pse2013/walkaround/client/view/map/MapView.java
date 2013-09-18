package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.CenterListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.LevelOfDetailListener;
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
	 * the current Coordinates
	 * 
	 */
	private BoundingBox coorBox;

	/**
	 * the current Tile Width
	 * 
	 */
	private static float currentTileWidth;

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
	public static final int FRAME_RATE = 1;

	/**
	 * Holds the References and Coordinates of Tiles
	 */
	private static Map<Point, Bitmap> tiles = new LinkedHashMap<Point, Bitmap>();
	
	/**
	 * Thread which computes the offset
	 */
	private Thread offset = new Thread(new OffsetComputer());

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

		currentTileWidth = CoordinateUtility
				.computeCurrentTileWidthInPixels(this.coorBox
						.getLevelOfDetail());
		this.pPerDiff = currentTileWidth / 334;

		coorBox.registerLevelOfDetailListener(this);
		coorBox.registerCenterListener(this);
		h = new Handler();

		this.indexXY = new int[2];
		this.mapOffset = new DisplayCoordinate(0, 0);

		Log.d("test", " " + (coorBox.getDisplaySize() != null));

		this.computeParams();
		this.buildDrawingCache();
		this.setDrawingCacheEnabled(true);
		this.setLayerType(View.LAYER_TYPE_HARDWARE, null);

	}

	@Override
	protected void onDraw(Canvas c) {
		
		float scale = BoundingBox.getInstance().getScale();
		PointF p = BoundingBox.getInstance().getPivot();
		c.scale(scale, scale, p.x, p.y);
		synchronized (tiles) {
			Iterator<Entry<Point, Bitmap>> it = tiles.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<Point, Bitmap> pairs = (Map.Entry<Point, Bitmap>) it.next();
				c.drawBitmap(
						pairs.getValue(),
						(pairs.getKey().x * currentTileWidth)
								+ mapOffset.getX(),
						(pairs.getKey().y * currentTileWidth)
								+ mapOffset.getY(), null);
			}			
		}
		h.postDelayed(r, FRAME_RATE);
	}
	
	@Override
	public void receiveTile(Bitmap tile, int x, int y, int levelOfDetail) {

		int tileX = x - indexXY[0];
		int tileY = (y - indexXY[1]);
		
		Point xy = new Point(tileX, tileY);
		
		synchronized(tiles) {
		try {
			tiles.put(xy, tile);
		} catch (OutOfMemoryError e) {
			Log.e(TAG, e.toString());
			System.gc();
		}
		}

	}
	

	/**
	 * Compute and gives the Tile Offset back
	 * 
	 * @return Tile Offset
	 */
	private void computeTileOffset() {
		if(offset.isAlive()) {
			offset.interrupt();
		}
		offset = new Thread(new OffsetComputer());
		offset.start();
	}

	/**
	 * Computes the Tile Offset
	 * 
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class OffsetComputer implements Runnable {

		@Override
		public void run() {

			double lonDiff = ((coorBox.getCenter().getLongitude() + 180) % (360 / Math
					.pow(2, coorBox.getLevelOfDetail())));

			int[] index = TileUtility.getXYTileIndex(coorBox.getCenter(),
					(int) coorBox.getLevelOfDetail());

			double n = Math.PI - (2.0 * Math.PI * index[1])
					/ Math.pow(2.0, coorBox.getLevelOfDetail());
			n = Math.toDegrees(Math.atan(Math.sinh(n)));

			double latDiff = Math.abs(coorBox.getCenter().getLatitude() - n);

			float xDiff = CoordinateUtility.convertDegreesToPixels(lonDiff,
					coorBox.getLevelOfDetail(),
					CoordinateUtility.DIRECTION_HORIZONTAL);

			float yDiff = CoordinateUtility.convertDegreesToPixels(latDiff,
					coorBox.getLevelOfDetail(),
					CoordinateUtility.DIRECTION_VERTICAL);

			yDiff = yDiff * pPerDiff;

			xDiff = coorBox.getPivot().x - Math.abs(xDiff);
			yDiff = coorBox.getPivot().y - Math.abs(yDiff);

			/*
			Log.d(TAG, String.format("TileOffset: x: %.8fdp y: %.8fdp\n"
					+ "TileOffset: lon: %.8f lat: %.8f\n" + "Center: %s\n"
					+ "LevelOfDetail: %.8f", xDiff, yDiff, lonDiff, latDiff,
					coorBox.getCenter(), coorBox.getLevelOfDetail()));
			 */
			
			synchronized(mapOffset) {
				mapOffset.setX(xDiff);
				mapOffset.setY(yDiff);
			}
		}
	}
	
	/**
	 * This Method computes the offset and index and clears the current variable
	 */
	public void computeParams() {
		this.computeTileOffset();
		this.indexXY = TileUtility.getXYTileIndex(coorBox.getCenter(),
				Math.round(this.coorBox.getLevelOfDetail()));
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

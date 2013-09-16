package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
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

		TwoFinger tf = new TwoFinger();
		tf.init();
		this.setOnTouchListener(tf);

	}
	
	public float x = 1;
	public float y = 1;
	public float px = 0;
	public float py = 0;

	@Override
	protected void onDraw(Canvas c) {
		for (int i = 0; i < tileT.size(); i++) {
			tileT.get(i).start();
		}
		tileT.clear();

		//c.scale(x, y);
		c.scale(x, y, px, py);
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
		this.currentTileWidth = CoordinateUtility
				.computeCurrentTileWidthInPixels(this.coorBox
						.getLevelOfDetail());

		this.pPerDiff = currentTileWidth / 334;

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

		this.computeAmountOfTiles();
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
			this.b = Bitmap.createScaledBitmap(b, Math.round(currentTileWidth),
					Math.round(currentTileWidth), false);
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

	public Matrix currentMatrix = new Matrix();

	public class TwoFinger implements OnTouchListener {

		// These matrices will be used to move and zoom image
		Matrix matrix = new Matrix();
		Matrix savedMatrix = new Matrix();

		// We can be in one of these 3 states
		static final int NONE = 0;
		static final int DRAG = 1;
		static final int ZOOM = 2;
		static final int DRAW = 3;
		int mode = NONE;

		// Remember some things for zooming
		PointF start = new PointF();
		PointF mid = new PointF();
		float oldDist = 1f;

		PointF dragOld = new PointF();

		// Limit zoomable/pannable image
		private float[] matrixValues = new float[9];
		private float maxZoom;
		private float minZoom;
		private float height;
		private float width;
		private RectF viewRect;

		// ///////************ touch events functions
		// **************////////////////////

		private void init() {
	    maxZoom = 4;
	    minZoom = 0.25f;
	    height = size.y;
	    width = size.x;
	    viewRect = new RectF(0, 0, size.x, size.y);
	}

		// ///////************touch events for image Moving, panning and zooming
		// ***********///
		public boolean onTouch(View v, MotionEvent event) {

			// Dump touch event to log
			// dumpEvent(event);
			// Handle touch events here...
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				Log.d(TAG, "mode=DRAG");
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				Log.d(TAG, "oldDist=" + oldDist);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
					Log.d(TAG, "mode=ZOOM");
				}
				break;
			case MotionEvent.ACTION_UP:
				dragOld = null;
				break;
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				Log.d(TAG, "mode=NONE");
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAW) {
					onTouchEvent(event);
				}
				if (mode == DRAG) {
					if(dragOld != null){
					PointF dragNew = new PointF(event.getX(),event.getY());
					float disX = dragOld.x - dragNew.x;
					float disy = dragOld.y - dragNew.y;
					dragOld = dragNew;
					BoundingBox.getInstance().shiftCenter( x, y);
					} else {
						dragOld = new PointF(event.getX(), event.getY());
					}
					// /code for draging..
				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					Log.d(TAG, "newDist=" + newDist);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.getValues(matrixValues);
						float currentScale = matrixValues[Matrix.MSCALE_X];
						// limit zoom
						if (scale * currentScale > maxZoom) {
							scale = maxZoom / currentScale;
						} else if (scale * currentScale < minZoom) {
							scale = minZoom / currentScale;
						}
						x = scale;
						y = scale;
						px = mid.x;
						py = mid.y;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
			}
			currentMatrix = matrix;
			setImageMatrix(matrix);
			return true; // indicate event was handled
		}

		// *******************Determine the space between the first two fingers
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}

		// ************* Calculate the mid point of the first two fingers
		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}
	}
}

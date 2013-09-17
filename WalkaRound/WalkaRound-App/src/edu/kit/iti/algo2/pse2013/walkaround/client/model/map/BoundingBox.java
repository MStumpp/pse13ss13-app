package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.activity.WalkaRound.MapListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.CurrentMapStyleModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

/**
 * 
 * This class represent a rectangle of two Coordinates and his center
 * Coordinate. This class holds the upper Left, bottom Right and center
 * Coordinate.
 * 
 * @author Ludwig Biermann
 * @version 4.1
 * 
 */
public class BoundingBox {

	// static --------------------------Variables-------------------------- //

	/**
	 * The Debug Tag of Bounding Box
	 */
	private static String TAG = BoundingBox.class.getSimpleName();

	/**
	 * Instance of BoundingBox
	 */
	private static BoundingBox coorBox;

	/**
	 * The default start Coordinate
	 */
	public static Coordinate defaultCoordinate = new Coordinate(49.0145, 8.419); // 211

	// class --------------------------Variables-------------------------- //

	/**
	 * Center Coordinate
	 */
	private Coordinate center;

	/**
	 * Upper Left Coordinate of the box
	 */
	//private Coordinate topLeft;

	/**
	 * Bottom Right Coordinate of the Box
	 */
	//private Coordinate bottomRight;

	/**
	 * The scaled Upper Left Coordinate of the box
	 */
	private Coordinate scaledTopLeft;

	/**
	 * The scaled Bottom Right Coordinate of the Box
	 */
	private Coordinate scaledBottomRight;

	/**
	 * Display Size
	 */
	private DoublePairing size;

	/**
	 * Display Size in Pixel
	 */
	private Point display;

	/**
	 * Current copy of LevelOf Detail
	 */
	private float levelOfDetail;

	/**
	 * The Scaling Level
	 */
	private float scale;

	/**
	 * The current Pivot Point
	 */
	private PointF pivot;

	/**
	 * The Level of Detail listener
	 */
	private Set<LevelOfDetailListener> lodListener;

	/**
	 * the Center Listener.
	 */
	private LinkedList<CenterListener> centerListener;

	/**
	 * the Scale Listener
	 */
	private LinkedList<ScaleListener> scaleListener;

	// --------------------------Constructor-------------------------- //

	public static void initialize(Point size) {
		Log.d(TAG, "initialisiere BoundingBox");
		coorBox = new BoundingBox(defaultCoordinate, size, CurrentMapStyleModel
				.getInstance().getCurrentMapStyle().getDefaultLevelOfDetail());
	}

	public static BoundingBox getInstance(Context context) {

		if (coorBox != null) {
			return coorBox;
		}

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		Point size = new Point(width, height);
		initialize(size);

		return coorBox;
	}

	/**
	 * !!!!Only possible if you called initialize or getInstance(Context)
	 * 
	 * @return BoundingBox
	 */
	public static BoundingBox getInstance() {
		return coorBox;
	}

	/**
	 * Constructs a new Bounding Box
	 * 
	 * @param center
	 *            center Coordinate
	 * @param size
	 *            Display size
	 * @param levelOfDetail
	 *            Level of Detail
	 */
	private BoundingBox(Coordinate center, Point size, float levelOfDetail) {
		Log.d(TAG, "initialize BoundingBox | Display: " + size.toString());
		this.scale = 1;
		this.lodListener = new HashSet<LevelOfDetailListener>();
		this.centerListener = new LinkedList<CenterListener>();
		this.scaleListener = new LinkedList<ScaleListener>();
		this.display = size;
		this.levelOfDetail = this.correctLevelOfDetail(levelOfDetail);
		this.computeSize();
		this.setCenter(center, levelOfDetail);
		this.notifyLODListener(this.levelOfDetail);
		this.pivot = new PointF(this.display.x / 2F, this.display.y / 2F);
	}

	// --------------------------Setter-------------------------- //

	/**
	 * Sets a new Center by a new center and a new Level of detail
	 * 
	 * @param center
	 *            the center Coordinate
	 * @param levelOfDetail
	 *            the Level of Detail
	 */
	public void setCenter(Coordinate center, float levelOfDetail) {
		if (this.levelOfDetail != levelOfDetail) {
			Log.d(TAG, "set a new Level of Detail: " + levelOfDetail);
			this.levelOfDetail = this.correctLevelOfDetail(levelOfDetail);
			this.computeSize();
		}
		this.setCenter(center);
		this.notifyLODListener(this.levelOfDetail);
	}

	/**
	 * Sets a new Center by a new center and the current Level of Detail
	 * 
	 * @param center
	 *            the center Coordinate
	 */
	public void setCenter(Coordinate center) {
		Log.d(TAG, "set a new Center: " + center.toString());
		this.center = center;
		//this.topLeft = this.computeTopLeft();
		//this.bottomRight = this.computeBottomRight();
		
		this.scaledBottomRight = this.computeScaledBottomRight();
		this.scaledTopLeft = this.computeScaledTopLeft();
		
		this.notifyCenterListener(center);
	}

	/**
	 * Set a new scale.
	 * 
	 * @param scale
	 *            the new Scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
		this.notifyScaleListener(scale);
	}

	/**
	 * Sets a new Center to the given DisplayCoordinate
	 * 
	 * @param dc
	 *            the given Display Coordinate
	 */
	public void setCenter(DisplayCoordinate dc) {

		double longitude = CoordinateUtility.convertPixelsToDegrees(dc.getX(),
				levelOfDetail, CoordinateUtility.DIRECTION_LONGITUDE);

		double latitude = CoordinateUtility.convertPixelsToDegrees(dc.getY(),
				levelOfDetail, CoordinateUtility.DIRECTION_LATITUDE);

		Coordinate center = new Coordinate(this.computeTopLeft(), latitude, longitude);

		this.setCenter(center);
	}

	/**
	 * Shifts the Center Coordinate bei a Pixel delta
	 * 
	 * @param x
	 *            pixel delta
	 * @param y
	 *            pixel delta
	 */
	public void shiftCenter(float x, float y) {

		double deltaLongitude = CoordinateUtility.convertPixelsToDegrees(x,
				levelOfDetail, CoordinateUtility.DIRECTION_LONGITUDE);
		double deltaLatitude = -1
				* CoordinateUtility.convertPixelsToDegrees(y, levelOfDetail,
						CoordinateUtility.DIRECTION_LATITUDE);

		Coordinate center = new Coordinate(this.center, deltaLatitude,
				deltaLongitude);

		this.setCenter(center);

	}

	/**
	 * Checks the correctness of level of Detail
	 * 
	 * @param levelOfDetail
	 *            the new level of detail;
	 * @return true of the level of detail is correct returns false if the level
	 *         of detail is out of bound
	 */
	public boolean checkLevelOfDetail(float levelOfDetail) {
		if (CurrentMapStyleModel.getInstance().getCurrentMapStyle()
				.getMaxLevelOfDetail() <= levelOfDetail) {
			return false;
		}

		if (CurrentMapStyleModel.getInstance().getCurrentMapStyle()
				.getMinLevelOfDetail() >= levelOfDetail) {
			return false;
		}
		return true;
	}

	/**
	 * Sets a new Level of detail by checking the Boundings
	 * 
	 * @param levelOfDetail
	 *            the new level of detail;
	 * @return the correct new LOD
	 */
	private float correctLevelOfDetail(float levelOfDetail) {
		if (CurrentMapStyleModel.getInstance().getCurrentMapStyle()
				.getMaxLevelOfDetail() <= levelOfDetail) {
			return CurrentMapStyleModel.getInstance().getCurrentMapStyle()
					.getMaxLevelOfDetail();
		}

		if (CurrentMapStyleModel.getInstance().getCurrentMapStyle()
				.getMinLevelOfDetail() >= levelOfDetail) {
			return CurrentMapStyleModel.getInstance().getCurrentMapStyle()
					.getMinLevelOfDetail();
		}
		return levelOfDetail;
	}

	/**
	 * Sets a new Level Of Detail by a delta
	 * 
	 * @param delta
	 *            the delta to the new Level Of Detail
	 * @returns false if the Level of details has to fitted into its bounds.
	 */
	public boolean setLevelOfDetailByADelta(float delta) {
		boolean check = this.checkLevelOfDetail(levelOfDetail + delta);
		this.setLevelOfDetail(levelOfDetail + delta);
		return check;
	}

	/**
	 * Sets a new Level of Detail
	 * 
	 * @param levelOfDetail
	 *            the new Level of Detail
	 */
	public void setLevelOfDetail(float levelOfDetail) {
		this.levelOfDetail = this.correctLevelOfDetail(levelOfDetail);
		this.computeSize();
		//this.topLeft = this.computeTopLeft();
		//this.bottomRight = this.computeBottomRight();

		this.scaledBottomRight = this.computeScaledBottomRight();
		this.scaledTopLeft = this.computeScaledTopLeft();
		
		this.notifyLODListener(this.levelOfDetail);
	}

	/**
	 * Sets a new Pivot Point.
	 * 
	 * @param pivot
	 *            the new Pivot Point
	 */
	public void setPivot(PointF pivot) {
		this.pivot = pivot;
	}

	// --------------------------Getter-------------------------- //

	/**
	 * Returns the current scaling Level
	 * 
	 * @return scaling levle
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Gives back the Coordinate of the upper left corner
	 * 
	 * @return top left
	 */
	public Coordinate getTopLeft() {
		return this.computeTopLeft();
	}

	/**
	 * Gives back the Coordinate of the upper left corner
	 * 
	 * @return top left
	 */
	public Coordinate getScaledTopLeft() {
		return this.scaledTopLeft;
	}

	/**
	 * Gives back the Coordinate of the upper right corner
	 * 
	 * @return top right
	 */
	public Coordinate getTopRight() {
		return this.computeTopRight();
	}

	/**
	 * Gives back the Coordinate of the upper right corner
	 * 
	 * @return bottom left
	 */
	public Coordinate getBottomLeft() {
		return this.computeBottomLeft();
	}

	/**
	 * Gives back the Coordinate of the upper right corner
	 * 
	 * @return bottom right
	 */
	public Coordinate getScaledBottomRight() {
		return this.scaledBottomRight;
	}

	/**
	 * Gives back the Coordinate of the upper right corner
	 * 
	 * @return bottom right
	 */
	public Coordinate getBottomRight() {
		return this.computeBottomRight();
	}

	/**
	 * Gives back the Center Coordinate of the box
	 * 
	 * @return the coordinate at the center of the screen
	 */
	public Coordinate getCenter() {
		return center;
	}

	/**
	 * Gives the width and the height of the current Display back
	 * 
	 * @return display size as Point
	 */
	public Point getDisplaySize() {
		return display;
	}

	/**
	 * Gives the current Level Of Detail back
	 * 
	 * @return Level of Detail as float
	 */
	public float getLevelOfDetail() {
		return levelOfDetail;
	}

	/**
	 * Gives the current PivotPoint back
	 * 
	 * @return the Pivot Point
	 */
	public PointF getPivot() {
		return pivot;
	}

	// --------------------------Computing-------------------------- //

	/**
	 * Compute the Size of the Display
	 * 
	 * @param levelOfDetail
	 */
	private void computeSize() {
		Log.d(TAG,
				"compute size of the display depending on the Level of Detail");

		Log.d("test", "1: " + (display != null) + " 2: " + (levelOfDetail > 0));

		this.size = new DoublePairing(
				CoordinateUtility.convertPixelsToDegrees(display.x,
						levelOfDetail, CoordinateUtility.DIRECTION_LONGITUDE),
				CoordinateUtility.convertPixelsToDegrees(display.y,
						levelOfDetail, CoordinateUtility.DIRECTION_LATITUDE));
	}

	/**
	 * Returns the scaled upperLeft Coordinate
	 * 
	 * @return the scaled top left geo-oordinate
	 */
	private Coordinate computeScaledTopLeft() {
		Log.d(TAG, "compute Scaled Top Left");
		return new Coordinate(center, size.height / 2f * MapListener.maxZoom, -size.width / 2f * MapListener.maxZoom);
	}

	/**
	 * Returns the upperLeft Coordinate
	 * 
	 * @return the top left geo-oordinate
	 */
	private Coordinate computeTopLeft() {
		Log.d(TAG, "compute Top Left");
		return new Coordinate(center, size.height / 2f, -size.width / 2f);
	}

	/**
	 * Returns the upper right Coordinate
	 * 
	 * @return the top right geo-oordinate
	 */
	private Coordinate computeTopRight() {
		Log.d(TAG, "compute Top Right");
		return new Coordinate(center, size.height / 2f, size.width / 2f);
	}

	/**
	 * Returns the bootom left Coordinate
	 * 
	 * @return the bottom left geo-oordinate
	 */
	private Coordinate computeBottomLeft() {
		Log.d(TAG, "Compute Bottom Left");
		return new Coordinate(center, -size.height / 2f, -size.width / 2f);
	}

	/**
	 * Returns the scaled bottom right Coordinate
	 * 
	 * @return the scaled bottom right geo-oordinate
	 */
	private Coordinate computeScaledBottomRight() {
		Log.d(TAG, "Compute BottomRight");
		return new Coordinate(center, -size.height / 2f * MapListener.maxZoom, size.width / 2f * MapListener.maxZoom);
	}

	/**
	 * Returns the bottom right Coordinate
	 * 
	 * @return the bottom right geo-oordinate
	 */
	private Coordinate computeBottomRight() {
		Log.d(TAG, "Compute BottomRight");
		return new Coordinate(center, -size.height / 2f, size.width / 2f);
	}

	@Override
	public String toString() {
		return "BoundingBox: TopLeft: " + scaledTopLeft.toString() + " , TopRight: "
				+ this.getTopRight().toString() + " , BottomLeft: "
				+ this.getBottomLeft().toString() + " , Bottom Right: "
				+ scaledBottomRight + " | Display Größe: " + display.toString()
				+ " | Abstand zwischen den Coordinaten: " + size.toString();
	}

	// --------------------------Helper Classes-------------------------- //

	/**
	 * A simple Helper Class to pair two doubles. In this Case the make
	 * relationship between width and height of the display in Coordinates
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class DoublePairing {

		/**
		 * The width of the display
		 */
		public double width;

		/**
		 * The height of the display
		 */
		public double height;

		/**
		 * Construct a new Double Paring
		 * 
		 * @param width
		 *            of the display
		 * @param height
		 *            of the display
		 */
		public DoublePairing(double width, double height) {
			this.width = width;
			this.height = height;
		}

		@Override
		public String toString() {
			return "Double Paring: width: " + width + ", height: " + height;
		}
	}

	// --------------------------Listener-------------------------- //

	/**
	 * A Interface to notify classes if the Level of Detail changes
	 * 
	 * @author Ludwig Biermann
	 * @version 1.0
	 * 
	 */
	public interface LevelOfDetailListener {

		/**
		 * If the level of Detail changes, this class will be called.
		 * 
		 * @param levelOfDetail
		 */
		public void onLevelOfDetailChange(float levelOfDetail);

	}

	/**
	 * This will register a new Level Of Detail Listener
	 * 
	 * @param listener
	 *            the new listener
	 * @return always true
	 */
	public float registerLevelOfDetailListener(LevelOfDetailListener listener) {
		Log.d(TAG, "register a new Center Listener: " + listener.toString());
		this.lodListener.add(listener);
		return this.levelOfDetail;
	}

	/**
	 * This method will notify all Level of Detail Listener
	 * 
	 * @param levelOfDetail
	 *            the new level of Detail
	 */
	private void notifyLODListener(float levelOfDetail) {
		Log.d(TAG, "notify LOD Listener new LOD: " + levelOfDetail);
		for (LevelOfDetailListener l : this.lodListener) {
			l.onLevelOfDetailChange(levelOfDetail);
		}
	}

	/**
	 * A Interface to notify classes if the Center Coordinate changes
	 * 
	 * @author Ludwig Biermann
	 * @version 1.0
	 */
	public interface CenterListener {

		/**
		 * If the Center Coordinate changes, this class will be called.
		 * 
		 * @param center
		 *            the new Coordinate that is currently at the center of the
		 *            screen
		 */
		public void onCenterChange(Coordinate center);

	}

	/**
	 * This will register a new Center Coordinate Listener
	 * 
	 * @param listener
	 *            the new Listener
	 * @return the current Center Coordinate
	 */
	public Coordinate registerCenterListener(CenterListener listener) {
		// Log.d(TAG, "register a new LOD Listener: " + listener.toString());
		this.centerListener.add(listener);
		return this.center;
	}

	/**
	 * This will notify all Center Listener
	 * 
	 * @param center
	 *            the new Center Coordiante
	 */
	private void notifyCenterListener(Coordinate center) {
		// Log.d(TAG, "notify Center Listener new Center: " +
		// center.toString());
		for (CenterListener l : this.centerListener) {
			l.onCenterChange(center);
		}
	}

	/**
	 * This will register a new Center Coordinate Listener
	 * 
	 * @param listener
	 *            the new Listener
	 * @return the current Center Coordinate
	 */
	public float registerScaleListener(ScaleListener listener) {
		// Log.d(TAG, "register a new LOD Listener: " + listener.toString());
		this.scaleListener.add(listener);
		return this.scale;
	}

	/**
	 * This will notify all Scale Listener
	 * 
	 * @param center
	 *            the new Center Coordiante
	 */
	private void notifyScaleListener(float scale) {
		// Log.d(TAG, "notify Center Listener new Center: " +
		// center.toString());
		for (ScaleListener l : this.scaleListener) {
			l.onScaleChange(scale);
		}
	}

	/**
	 * A Listener wich is called if the scale level is changed
	 * 
	 * @author Ludwig Biermann
	 * @version 1.0
	 * 
	 */
	public interface ScaleListener {

		/**
		 * Is called if the scale level is changed
		 * 
		 * @param scale
		 *            the new scale level
		 */
		public void onScaleChange(float scale);
	}
}

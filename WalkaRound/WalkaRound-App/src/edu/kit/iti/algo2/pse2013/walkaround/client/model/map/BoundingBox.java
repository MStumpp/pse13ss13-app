package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
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
	private Coordinate topLeft;

	/**
	 * Bottom Right Coordinate of the Box
	 */
	private Coordinate bottomRight;

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
	 * The Level of Detail listener
	 */
	LinkedList<LevelOfDetailListener> lodListener;

	/**
	 * the Center Listener.
	 */
	LinkedList<CenterListener> centerListener;


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
		this.lodListener = new LinkedList<LevelOfDetailListener>();
		this.centerListener = new LinkedList<CenterListener>();
		this.display = size;
		this.levelOfDetail = this.checkLevelOfDetail(levelOfDetail);
		this.computeSize();
		this.setCenter(center, levelOfDetail);
		this.notifyLODListener(this.levelOfDetail);
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
			this.levelOfDetail = this.checkLevelOfDetail(levelOfDetail);
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
		this.topLeft = this.computeTopLeft();
		Log.d(TAG, "Topleft is " + this.topLeft);
		this.bottomRight = this.computeBottomRight();
		this.notifyCenterListener(center);
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

		Coordinate center = new Coordinate(this.topLeft, latitude, longitude);

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
	 * Sets a new Level of detail by checking the Boundings
	 *
	 * @param levelOfDetail
	 *            the new level of detail;
	 * @return the correct new LOD
	 */
	private float checkLevelOfDetail(float levelOfDetail) {
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
	 */
	public void setLevelOfDetailByADelta(float delta) {
		this.setLevelOfDetail(levelOfDetail + delta);
	}

	/**
	 * Sets a new Level of Detail
	 *
	 * @param levelOfDetail
	 *            the new Level of Detail
	 */
	public void setLevelOfDetail(float levelOfDetail) {
		this.levelOfDetail = this.checkLevelOfDetail(levelOfDetail);
		this.computeSize();
		this.topLeft = this.computeTopLeft();
		this.bottomRight = this.computeBottomRight();
		this.notifyLODListener(this.levelOfDetail);
	}

	// --------------------------Getter-------------------------- //

	/**
	 * Gives back the Coordinate of the upper left corner
	 *
	 * @return top left
	 */
	public Coordinate getTopLeft() {
		return topLeft;
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
	public Coordinate getBottomRight() {
		return bottomRight;
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
		return "BoundingBox: TopLeft: " + topLeft.toString() + " , TopRight: "
				+ this.getTopRight().toString() + " , BottomLeft: "
				+ this.getBottomLeft().toString() + " , Bottom Right: "
				+ bottomRight + " | Display Größe: " + display.toString()
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
		 * @param center the new Coordinate that is currently at the center of the screen
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
		//Log.d(TAG, "register a new LOD Listener: " + listener.toString());
		this.centerListener.add(listener);
		return this.center;
	}

	/**
	 * This will notify all Listener
	 *
	 * @param center
	 *            the new Center Coordiante
	 */
	private void notifyCenterListener(Coordinate center) {
		//Log.d(TAG, "notify Center Listener new Center: " + center.toString());
		for (CenterListener l : this.centerListener) {
			l.onCenterChange(center);
		}
	}
}

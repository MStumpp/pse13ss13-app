package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

import android.graphics.Point;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * 
 * This class represent a rectangle of two Coordinates and his center
 * Coordinate. This class holds the upper Left, bottom Right and center
 * Coordinate.
 * 
 * @author Ludwig Biermann
 * 
 */
public class BoundingBox {

	/**
	 * The Debug Tag of Bounding Box
	 */
	private static String TAG = BoundingBox.class.getSimpleName();

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
	 * Constructs a new Bounding Box
	 * 
	 * @param center
	 *            center Coordinate
	 * @param size
	 *            Display size
	 * @param levelOfDetail
	 *            Level of Detail
	 */
	public BoundingBox(Coordinate center, Point size, float levelOfDetail) {
		Log.d(TAG, "initialize BoundingBox");
		this.display = size;
		this.levelOfDetail = levelOfDetail;
		this.computeSize();
		this.setCenter(center, levelOfDetail);
	}

	/**
	 * Compute the Size of the Display
	 * 
	 * @param levelOfDetail
	 */
	private void computeSize() {
		Log.d(TAG,
				"compute size of the display depending on the Level of Detail");
		this.size = new DoublePairing(
				CoordinateUtility.convertPixelsToDegrees(display.y,
						levelOfDetail, CoordinateUtility.DIRECTION_LATITUDE),
				CoordinateUtility.convertPixelsToDegrees(display.x,
						levelOfDetail, CoordinateUtility.DIRECTION_LONGITUDE));
	}

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
			this.levelOfDetail = levelOfDetail;
			this.computeSize();
		}
		this.setCenter(center);
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
		this.bottomRight = this.computeBottomRight();
	}

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
	 * @return
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

	// --------------------------Computing-------------------//

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
				+ bottomRight + " | Display Größe: " + display.toString() + " | Abstand zwischen den Coordinaten: "
				+ size.toString();
	}

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
}

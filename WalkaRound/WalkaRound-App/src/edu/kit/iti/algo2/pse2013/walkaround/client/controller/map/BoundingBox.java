package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

import android.graphics.Point;
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
	
	private Point displaySize;

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
	public BoundingBox(Coordinate center, Point displaySize, float levelOfDetail) {
		this.displaySize = displaySize;
		this.size = new DoublePairing(CoordinateUtility.convertPixelsToDegrees(
				displaySize.y, levelOfDetail, CoordinateUtility.DIRECTION_LATITUDE),
				CoordinateUtility.convertPixelsToDegrees(displaySize.x, levelOfDetail,
						CoordinateUtility.DIRECTION_LONGITUDE));
		this.setCenter(center, levelOfDetail);
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
		this.center = center;
		this.size = new DoublePairing(CoordinateUtility.convertPixelsToDegrees(
				this.displaySize.y, levelOfDetail, CoordinateUtility.DIRECTION_LATITUDE),
				CoordinateUtility.convertPixelsToDegrees(this.displaySize.x, levelOfDetail,
						CoordinateUtility.DIRECTION_LONGITUDE));
		this.topLeft = this.computeTopLeft(levelOfDetail);
		this.bottomRight = this.computeBottomRight(levelOfDetail);
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

	// --------------------------Computing-------------------//

	/**
	 * Returns the upperLeft Coordinate
	 * 
	 * @return the top left geo-oordinate
	 */
	private Coordinate computeTopLeft(float lod) {
		return new Coordinate(center, size.height / 2f, -size.width / 2f);
	}

	/**
	 * Returns the upper right Coordinate
	 * 
	 * @return the top right geo-oordinate
	 */
	@SuppressWarnings("unused")
	private Coordinate computeTopRight(float lod) {
		return new Coordinate(center, size.height / 2f, size.width / 2f);
	}

	/**
	 * Returns the bottom left Coordinate
	 * 
	 * @return the bottom left Coordinate
	 */
	private Coordinate computeTopRight() {
		return new Coordinate(topLeft.getLatitude(), bottomRight.getLongitude());
	}

	/**
	 * Returns the bootom left Coordinate
	 * 
	 * @return the bottom left geo-oordinate
	 */
	@SuppressWarnings("unused")
	private Coordinate computeBottomLeft(float lod) {
		return new Coordinate(center, -size.height / 2f, -size.width / 2f);
	}

	/**
	 * Returns the bottom left Coordinate
	 * 
	 * @return the bottom left Coordinate
	 */
	private Coordinate computeBottomLeft() {
		return new Coordinate(bottomRight.getLatitude(), topLeft.getLongitude());
	}

	/**
	 * Returns the bottom right Coordinate
	 * 
	 * @return the bottom right geo-oordinate
	 */
	private Coordinate computeBottomRight(float lod) {
		return new Coordinate(center, -size.height / 2f, size.width / 2f);
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
	}
}

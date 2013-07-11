package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an area.
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public class Area {

	/**
	 * IDs of the categories where the area belongs to.
	 */
	private int[] areaCategories;

	/**
	 * Coordinates of the area.
	 */
	private ArrayList<Coordinate> areaCoordinates;

	/**
	 * Constructs a new area.
	 *
	 * @param areaCategories
	 *            IDs of the categories where the area belongs to
	 * @param areaCoordinates
	 *            coordinates of the area
	 */
	public Area(int[] areaCategories, List<Coordinate> areaCoordinates) {
		if (areaCoordinates.size() <= 2)
			throw new IllegalArgumentException(
					"An area must have at least 3 Coordinates.");
		this.areaCategories = areaCategories;
		this.areaCoordinates = new ArrayList<Coordinate>(areaCoordinates);
	}

	/**
	 * Returns all IDs of the Categories where the area belongs to.
	 *
	 * @return all IDs of the categories where the area belongs to
	 */
	public int[] getAreaCategories() {
		return areaCategories;
	}

	/**
	 * Sets the area categories of this area.
	 *
	 * @param areaCategories
	 *            area categories to set
	 */
	public void setAreaCategories(int[] areaCategories) {
		this.areaCategories = areaCategories;
	}

	/**
	 * Returns the coordinates of the area.
	 *
	 * @return the coordinates of the area
	 */
	public ArrayList<Coordinate> getAreaCoordinates() {
		return areaCoordinates;
	}

}

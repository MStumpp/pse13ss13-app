package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents an area.
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public class Area implements Categorizable {

	/**
	 * Coordinates of the area.
	 */
	private ArrayList<Coordinate> areaCoordinates;
	private int[] categories;

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
		setCategories(areaCategories);
		this.areaCoordinates = new ArrayList<Coordinate>(areaCoordinates);
	}

	/**
	 * Returns the coordinates of the area.
	 *
	 * @return the coordinates of the area
	 */
	public ArrayList<Coordinate> getAreaCoordinates() {
		return areaCoordinates;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(getCategories());
		result = prime * result
				+ ((areaCoordinates == null) ? 0 : areaCoordinates.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Area)) {
			return false;
		}
		Area other = (Area) obj;
		if (!Arrays.equals(getCategories(), other.getCategories())) {
			return false;
		}
		if (areaCoordinates == null) {
			if (other.areaCoordinates != null) {
				return false;
			}
		} else if (!areaCoordinates.equals(other.areaCoordinates)) {
			return false;
		}
		return true;
	}

	@Override
	public int[] getCategories() {
		return categories;
	}

	@Override
	public void setCategories(int[] categories) {
		this.categories = categories;

	}

}

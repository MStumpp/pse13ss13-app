package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a category.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class Category {

	/**
	 * IDs of all existing categories for areas.
	 */
	private static int[] allAreaCategories;

	/**
	 * IDs of all existing categories for POIs.
	 */
	private static int[] allPOICategories;

	/**
	 * ID of the category.
	 */
	private int id;

	/**
	 * Constructs a new category.
	 * 
	 * @param id
	 *            id of the category
	 */
	public Category(int id) {
		this.id = id;
	}

	/**
	 * Returns the ID of the category.
	 * 
	 * @return ID of the category
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns all existing categories of areas.
	 * 
	 * @return all existing categories of areas.
	 */
	public static int[] getAllAreaCategories() {
		return allAreaCategories;
	}

	/**
	 * Returns all existing categories of POIs.
	 * 
	 * @return all existing categories of POIs
	 */
	public static int[] getAllPOICategories() {
		return allPOICategories;
	}

	/**
	 * Sets all existing categories of areas.
	 * 
	 * @param areaCategories
	 *            all existing categories of areas
	 */
	public static void setAllAreaCategories(int[] areaCategories) {
		allAreaCategories = areaCategories;
	}

	/**
	 * Sets all existing categories of POIs.
	 * 
	 * @param poiCategories
	 *            all existing categories of POIs
	 */
	public static void setAllPOICategories(int[] poiCategories) {
		allPOICategories = poiCategories;
	}
}
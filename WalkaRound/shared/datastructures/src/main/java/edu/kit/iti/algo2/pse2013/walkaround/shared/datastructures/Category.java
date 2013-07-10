package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a category.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class Category {

	//hard-coded
	
	public static Category FOOD;

	public static Category FAST_FOOD;

	public static Category BARS_AND_PUBS;

	public static Category CLUBS_AND_NIGHTCLUBS;

	public static Category SUPERMARKET;

	public static Category SHOP;

	public static Category PUBLIC_TRANSPORTATION;

	public static Category MUSEUM;

	public static Category THEATRE;

	public static Category CINEMA;

	public static Category SLEEPING_ACCOMODATIONS;

	/**
	 * IDs of all existing categories for POIs. (0 is default ID for empty category)
	 */
	private static final int[] ALL_POI_CATEGORIES = new int[] { 1, 2, 3, 4, 5,
			6, 7, 8, 9, 10, 11 };

	/**
	 * IDs of all existing categories for areas. (0 is default ID for empty category)
	 */
	private static final int[] ALL_AREA_CATEGORIES = new int[] { 1 };

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
		return ALL_AREA_CATEGORIES;
	}

	/**
	 * Returns all existing categories of POIs.
	 * 
	 * @return all existing categories of POIs
	 */
	public static int[] getAllPOICategories() {
		return ALL_POI_CATEGORIES;
	}
}
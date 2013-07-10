package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a category.
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public class Category {

	public static final Category FOOD = new Category(1);

	public static final Category FAST_FOOD = new Category(2);

	public static final Category BARS_AND_PUBS = new Category(3);

	public static final Category CLUBS_AND_NIGHTCLUBS = new Category(4);

	public static final Category SUPERMARKET = new Category(5);

	public static final Category SHOP = new Category(6);

	public static final Category PUBLIC_TRANSPORTATION = new Category(7);

	public static final Category MUSEUM = new Category(8);

	public static final Category THEATRE = new Category(9);

	public static final Category CINEMA = new Category(10);

	public static final Category SLEEPING_ACCOMODATIONS = new Category(11);

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
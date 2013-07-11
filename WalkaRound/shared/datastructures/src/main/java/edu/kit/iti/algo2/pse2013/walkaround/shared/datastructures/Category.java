package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a category.
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public class Category {

	public static final int BARS_AND_PUBS = 1;

	public static final int CINEMA = 2;

	public static final int CLUBS_AND_NIGHTCLUBS = 3;

	public static final int FAST_FOOD = 4;

	public static final int FOOD = 5;

	public static final int MUSEUM = 6;

	public static final int PUBLIC_TRANSPORTATION = 7;

	public static final int SHOP = 8;

	public static final int SLEEPING_ACCOMODATIONS = 9;

	public static final int SUPERMARKET = 10;

	public static final int THEATRE = 11;

	/**
	 * IDs of all existing categories for areas.
	 */
	private static int[] allAreaCategories;

	/**
	 * IDs of all existing categories for POIs.
	 */
	private static int[] allPOICategories = { FOOD, FAST_FOOD,
			BARS_AND_PUBS, CLUBS_AND_NIGHTCLUBS,
			SUPERMARKET, SHOP, PUBLIC_TRANSPORTATION,
			MUSEUM, THEATRE, CINEMA,
			SLEEPING_ACCOMODATIONS };

	/**
	 * Returns all existing categories of areas.
	 *
	 * @return all existing categories of areas.
	 */
	public static final int[] getAllAreaCategories() {
		return allAreaCategories;
	}

	/**
	 * Returns all existing categories of POIs.
	 *
	 * @return all existing categories of POIs
	 */
	public static final int[] getAllPOICategories() {
		return allPOICategories;
	}

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
	public final int getID() {
		return id;
	}
}
package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a category.
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public class Category {

	public static final int POI_BARS_AND_PUBS = 1;

	public static final int POI_CINEMA = 2;

	public static final int POI_CLUBS_AND_NIGHTCLUBS = 3;

	public static final int POI_FAST_FOOD = 4;

	public static final int POI_FOOD = 5;

	public static final int POI_MUSEUM = 6;

	public static final int POI_PUBLIC_TRANSPORTATION = 7;

	public static final int POI_SHOP = 8;

	public static final int POI_SLEEPING_ACCOMODATIONS = 9;

	public static final int POI_SUPERMARKET = 10;

	public static final int POI_THEATRE = 11;

	public static final int POI_CHURCHES_AND_MONUMENTS = 12;

	public static final int POI_CASTLES = 13;

	public static final int AREA_FOREST = 100;

	public static final int AREA_GARDEN = 101;

	/**
	 * IDs of all existing categories for areas.
	 */
	private static int[] allAreaCategories = {AREA_FOREST, AREA_GARDEN};

	/**
	 * IDs of all existing categories for POIs.
	 */
	private static int[] allPOICategories = { POI_FOOD, POI_FAST_FOOD,POI_BARS_AND_PUBS, POI_CLUBS_AND_NIGHTCLUBS,
			POI_SUPERMARKET, POI_SHOP, POI_PUBLIC_TRANSPORTATION, POI_MUSEUM, POI_THEATRE, POI_CINEMA,
			POI_SLEEPING_ACCOMODATIONS, POI_CHURCHES_AND_MONUMENTS, POI_CASTLES };

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
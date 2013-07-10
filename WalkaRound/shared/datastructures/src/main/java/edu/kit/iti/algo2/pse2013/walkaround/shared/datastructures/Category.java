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
	 * IDs of all existing categories for areas.
	 */
	private static int[] allAreaCategories;

	/**
	 * IDs of all existing categories for POIs.
	 */
	private static int[] allPOICategories = {FOOD.getID(), FAST_FOOD.getID(), BARS_AND_PUBS.getID(),
		CLUBS_AND_NIGHTCLUBS.getID(), SUPERMARKET.getID(), SHOP.getID(), PUBLIC_TRANSPORTATION.getID(),
		MUSEUM.getID(), THEATRE.getID(), CINEMA.getID(), SLEEPING_ACCOMODATIONS.getID()};

	/**
	 * ID of the category.
	 */
	private int id;

	/**
	 * Constructs a new category.
	 *
	 * @param id id of the category
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
}
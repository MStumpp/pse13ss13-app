package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a category.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class Category {

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
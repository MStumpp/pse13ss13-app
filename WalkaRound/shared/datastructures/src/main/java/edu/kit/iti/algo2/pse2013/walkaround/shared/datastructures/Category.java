package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

public class Category {

	private static int[] allAreaCategories;

	private static int[] allPOICategories;

	private int id;

	public Category(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public static int[] getAllAreaCategories() {
		return allAreaCategories;
	}

	public static int[] getAllPOICategories() {
		return allPOICategories;
	}

	public static void setAllAreaCategories(int[] areaCategories) {
		allAreaCategories = areaCategories;
	}

	public static void setAllPOICategories(int[] poiCategories) {
		allPOICategories = poiCategories;
	}
}
package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

public class Profile {

	public static Profile PROFILE_JOGGING;

	public static Profile PROFILE_SIGHTSEEING;

	public static Profile PROFILE_SHOPPING;

	public static Profile PROFILE_CLUBBING;

	public static Profile PROFILE_RENAMEME;

	private int id;

	private int[] areaCategories;

	private int[] poiCategories;

	public Profile(int id, int[] areaCategories, int[] poiCategories) {
		this.id = id;
		this.areaCategories = areaCategories;
		this.poiCategories = poiCategories;
	}

	public int getID() {
		return id;
	}

	public int[] getContainingAreaCategories() {
		return areaCategories;
	}

	public int[] getContainingPOICategories() {
		return poiCategories;
	}

	public static int[] getAllProfiles() {
		int[] allProfiles = new int[] { 1, 2, 3, 4, 5 };
		return allProfiles;
	}
}

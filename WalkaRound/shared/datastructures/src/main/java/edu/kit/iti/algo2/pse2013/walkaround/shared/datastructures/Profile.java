package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

public class Profile {

	public static final Profile PROFILE_JOGGING = new Profile(1);

	public static final Profile PROFILE_SIGHTSEEING = new Profile(2);;

	public static final Profile PROFILE_SHOPPING = new Profile(3);;

	public static final Profile PROFILE_CLUBBING = new Profile(4);;

	public static final Profile PROFILE_RENAMEME = new Profile(5);;

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

package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a profile.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class Profile {

	/**
	 * Jogging profile.
	 */
	public static Profile PROFILE_JOGGING;

	/**
	 * Sighseeing profile.
	 */
	public static Profile PROFILE_SIGHTSEEING;

	/**
	 * Shopping profile.
	 */
	public static Profile PROFILE_SHOPPING;

	/**
	 * Clubbing profile.
	 */
	public static Profile PROFILE_CLUBBING;

	// CHANGE ME!!!
	public static Profile PROFILE_RENAMEME;

	/**
	 * ID of the profile.
	 */
	private int id;

	/**
	 * All area categories IDs belonging to the profile.
	 */
	private int[] areaCategories;

	/**
	 * All POI categories IDs belonging to the profile.
	 */
	private int[] poiCategories;

	public Profile(int id, int[] areaCategories, int[] poiCategories) {
		this.id = id;
		this.areaCategories = areaCategories;
		this.poiCategories = poiCategories;
	}

	/**
	 * Returns the ID of the profile.
	 * 
	 * @return the ID of the profile
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns the IDs of area categories belonging to the profile.
	 * 
	 * @return the IDs of area categories belonging to the profile
	 */
	public int[] getContainingAreaCategories() {
		return areaCategories;
	}

	/**
	 * Returns the IDs of POI categories belonging to the profile.
	 * 
	 * @return the IDs of POI categories belonging to the profile
	 */
	public int[] getContainingPOICategories() {
		return poiCategories;
	}

	/**
	 * Returns the IDs of all existing Profiles.
	 * 
	 * @return the IDs of all existing Profiles
	 */
	public static int[] getAllProfiles() {
		int[] allProfiles = new int[] { 1, 2, 3, 4, 5 };
		return allProfiles;
	}

}

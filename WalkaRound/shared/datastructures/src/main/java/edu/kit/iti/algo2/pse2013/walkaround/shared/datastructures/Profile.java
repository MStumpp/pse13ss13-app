package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a profile.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class Profile {

	//hard-coded
	/**
	 * Jogging profile.
	 */
	public static Profile PROFILE_JOGGING = new Profile(1, new int[]{100, 101}, new int[]{});

	/**
	 * Sightseeing profile.
	 */
	public static Profile PROFILE_SIGHTSEEING = new Profile(2, new int[]{}, new int[]{12, 13});;

	/**
	 * Shopping profile.
	 */
	public static Profile PROFILE_SHOPPING = new Profile(3, new int[]{}, new int[]{8});

	/**
	 * Clubbing profile.
	 */
	public static Profile PROFILE_CLUBBING = new Profile(4, new int[]{}, new int[]{1, 3});

	/**
	 * IDs of all Profiles. (0 is default ID for empty Profile)
	 */
	public static final int[] ALL_PROFILE_IDS = new int[] { 1, 2, 3, 4 };

	// CHANGE ME!!!
	// public static Profile PROFILE_RENAMEME;

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
	 * Sets the poi categories of the profile.
	 * 
	 * @param poiCategories
	 *            poi categories to set
	 */
	public void setPoiCategories(int[] poiCategories) {
		this.poiCategories = poiCategories;
	}

	/**
	 * Sets the area categories of the profile.
	 * 
	 * @param areaCategories
	 *            area categories to set
	 */
	public void setAreaCategories(int[] areaCategories) {
		this.areaCategories = areaCategories;
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
	 * Returns the profile to the given ID.
	 * 
	 * @param id
	 *            id of the profile
	 * @return a profile
	 */
	public static Profile getByID(int id) {
		switch (id) {
		case 1:
			return PROFILE_JOGGING;

		case 2:
			return PROFILE_SIGHTSEEING;

		case 3:
			return PROFILE_SHOPPING;

		case 4:
			return PROFILE_CLUBBING;

			// case 5 : return PROFILE_RENAMEME;

		default:
			throw new IllegalArgumentException(
					"The profile for this ID is unknown.");
		}
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
	public static int[] getAllProfileIDs() {
		return ALL_PROFILE_IDS;
	}
	
}

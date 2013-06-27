package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

public class Profile {

	public static Profile getByID(int id) {
		return new Profile();
	}
	public int getID() {
		return 0;
	}
	public int[] getContainingPOICategories() {
		return new int[]{0};
	}
	public int[] getContainingAreaCategories() {
		return new int[]{0};
	}

}

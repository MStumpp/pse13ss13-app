package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

public interface Categorizable {
	/**
	 * Returns all IDs of the Categories where this object belongs to.
	 *
	 * @return all IDs of the categories where this object belongs to
	 */
	public int[] getCategories();

	/**
	 * Sets the categories of this object.
	 *
	 * @param categories	categories to set
	 */
	public void setCategories(int[] categories);
}
package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestCategory {

	@Test
	public void testPersistence() {
		int id = 42;
		Category c = new Category(id);
		assertEquals(id, c.getID());
	}

	@Test
	public void testPOICategories() {
		int[] cats = Category.getAllPOICategories();
		ArrayList<Integer> catList = new ArrayList<Integer>();
		for (int i = 0; i < cats.length; i++) {
			catList.add(cats[i]);
		}
		for(int i = 1; i <= 13; i++){
			assertTrue(catList.contains(i));
		}
	}
	@Test
	public void testAreaCategories() {
		int[] cats = Category.getAllAreaCategories();
		ArrayList<Integer> catList = new ArrayList<Integer>();
		for (int i = 0; i < cats.length; i++) {
			catList.add(cats[i]);
		}
		for(int i = 100; i <= 101; i++){
			assertTrue(catList.contains(i));
		}
	}

}

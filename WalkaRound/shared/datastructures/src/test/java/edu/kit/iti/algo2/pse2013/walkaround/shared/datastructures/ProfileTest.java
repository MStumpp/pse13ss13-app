package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

	private int id;
	private int[] poiCats;
	private int[] areaCats;
	private Profile p;
	private Profile x;

	@Before
	public void setUp() {
		id = 54;
		poiCats = new int[] { 1, 2, 3 };
		areaCats = new int[] { 5, 6, 7 };
		p = new Profile(id, areaCats, poiCats);
		x = new Profile(id, null, null);
		x.setPoiCategories(poiCats);
		x.setAreaCategories(areaCats);
	}

	@Test
	public void testPersistence() {
		assertArrayEquals(p.getContainingAreaCategories(),
				x.getContainingAreaCategories());
		assertArrayEquals(p.getContainingPOICategories(),
				x.getContainingPOICategories());
		assertArrayEquals(poiCats, p.getContainingPOICategories());
		assertArrayEquals(areaCats, p.getContainingAreaCategories());
		assertEquals(id, p.getID());
	}
}

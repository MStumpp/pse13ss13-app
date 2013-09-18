package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestPOI {

	private static final double DEFAULT_LAT = 48.12345;
	private static final double DEFAULT_LON = 8.54321;
	private static final String DEFAULT_NAME = "Test-Name";
	private static final String DEFAULT_TEXTINFO = "<p>Das ist der erste Absatz des Wikipedia-Artikels über den POI</p>";
	private static URL DEFAULT_URL;
	private static final int[] DEFAULT_POI_CATS = new int[]{1, 2};
	private static final Address DEFAULT_ADDRESS = new Address("Straße", "Hausnummer", "Karlsruhe", 12345);
	private static POI DEFAULT_POI;

	@BeforeClass
	public static void setUp() throws MalformedURLException {
		DEFAULT_URL = new URL("https://upload.wikimedia.org/wikipedia/commons/5/51/Karlsruher_Schloss_Front_Panorama.jpg");
		DEFAULT_POI = new POI(DEFAULT_LAT, DEFAULT_LON, DEFAULT_NAME, DEFAULT_TEXTINFO, DEFAULT_URL, DEFAULT_POI_CATS);
	}

	@Test
	public void testPersistence() throws MalformedURLException {
		//Assert
		assertEquals(DEFAULT_TEXTINFO, DEFAULT_POI.getTextInfo());
		assertEquals(DEFAULT_URL, DEFAULT_POI.getURL());
		assertArrayEquals(DEFAULT_POI_CATS, DEFAULT_POI.getCategories());

		POI p = (POI) DEFAULT_POI.clone();
		String testTextInfo = "TestText";
		URL testURL = new URL("http://www.kit.edu");
		p.setTextInfo(testTextInfo);
		p.setURL(testURL);
		assertEquals(testTextInfo, p.getTextInfo());
		assertEquals(testURL, p.getURL());
	}

	@Test
	public void testToString() {
		POI poi = new POI(DEFAULT_LAT, DEFAULT_LON, DEFAULT_NAME, DEFAULT_TEXTINFO, DEFAULT_URL, DEFAULT_POI_CATS, DEFAULT_ADDRESS);
		String poiCatString = "";
		for (int i : DEFAULT_POI_CATS) {
			poiCatString += " " + i;
		}
		assertTrue(poi.toString().contains(DEFAULT_TEXTINFO) && poi.toString().contains(DEFAULT_URL.toExternalForm()) && poi.toString().contains(poiCatString.trim()));
	}

	@Test
	public void testEquals() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		assertFalse(DEFAULT_POI.equals(null));
		assertFalse(DEFAULT_POI.equals("Hello world"));

		Location loc = new Location(DEFAULT_LAT, DEFAULT_LON, DEFAULT_NAME);
		POI poiA = new POI(loc, null, null, null);
		POI poiB = new POI(loc, null, null, null);
		assertFalse(poiA == poiB);
		assertTrue(poiA.equals(poiB));

		assertTrue(DEFAULT_POI.equals(DEFAULT_POI));
	}

	@Test
	public void testClone() {
		POI clone = (POI) DEFAULT_POI.clone();
		POI clone2 = (POI) clone.clone();
		assertEquals(DEFAULT_POI, clone);
		assertEquals(clone, clone2);
		assertEquals(clone2, DEFAULT_POI);

		assertTrue(DEFAULT_POI != clone);
		assertTrue(clone2 != clone);
		assertTrue(DEFAULT_POI != clone2);
	}

	@Test
	public void testHash() {
		int hashA = DEFAULT_POI.hashCode();
		int hashB = DEFAULT_POI.clone().hashCode();
		int hashC = new POI(DEFAULT_LAT + 1, DEFAULT_LON, DEFAULT_NAME, null, null, null).hashCode();
		assertEquals(hashA, hashB);
		assertNotEquals(hashB, hashC);
		assertNotEquals(hashA, hashC);
	}
}
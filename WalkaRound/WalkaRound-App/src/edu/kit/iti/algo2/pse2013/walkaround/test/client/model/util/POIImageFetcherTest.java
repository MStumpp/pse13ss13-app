package edu.kit.iti.algo2.pse2013.walkaround.test.client.model.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.POIImageFetcher;


public class POIImageFetcherTest extends AndroidTestCase {

	@Test
	public void testImageFetcher() throws MalformedURLException {
		Bitmap bitmap;
		bitmap = POIImageFetcher
				.fetchImage(new URL("http://commons.wikimedia.org/wiki/File:Gray_vacuum_cleaner.svg"));
		assertTrue(bitmap != null);

	}
}
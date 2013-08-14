package edu.kit.iti.algo2.pse2013.walkaround.test.client.model.util;

import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.POIImageFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.POIImageListener;


public class POIImageFetcherTest extends AndroidTestCase {
	private Bitmap bitmap;

	/**
	 * test den Image Fetcher
	 * 
	 * @throws MalformedURLException
	 */
	public void testImageFetcher() throws MalformedURLException {
		POIImageFetcher pf = new POIImageFetcher(new URL("http://commons.wikimedia.org/wiki/File:Gray_vacuum_cleaner.svg"), new POIImageListener() {
			@Override
			public void setImage(Bitmap b) {
				bitmap = b;
			}

		});
		assertTrue(bitmap != null);
	}
}
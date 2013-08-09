package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;
import android.graphics.Bitmap;

public class TestPOIImageFetcher extends TestCase {

	protected void setUp() {
	}

	public void testImageFetcher() throws MalformedURLException {
		Bitmap bitmap;
		bitmap = POIImageFetcher.fetchImage(new URL("http://commons.wikimedia.org/wiki/File:Gray_vacuum_cleaner.svg"));
		assertTrue(bitmap != null);

	}

	protected void tearDown() {
	}
}
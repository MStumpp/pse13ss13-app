package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;
import android.graphics.Bitmap;

public class TestPOIImageFetcher extends TestCase {
	private Bitmap bitmap;
	private POIImageListener listener = new POIImageListener() {
		@Override
		public void setImage(Bitmap b) {
			bitmap = b;
		}
	};

	protected void setUp() {
	}

	public void testImageFetcher() throws MalformedURLException {
		Thread t = new Thread(new POIImageFetcher(new URL("http://commons.wikimedia.org/wiki/File:Gray_vacuum_cleaner.svg"), listener));
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(bitmap != null);

	}

	protected void tearDown() {
	}
}
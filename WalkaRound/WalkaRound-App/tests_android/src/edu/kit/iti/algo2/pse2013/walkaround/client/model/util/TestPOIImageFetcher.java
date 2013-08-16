package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.graphics.Bitmap;

@RunWith(RobolectricTestRunner.class)
public class TestPOIImageFetcher {
	public static Bitmap bitmap;
	private POIImageListener listener = new POIImageListener() {
		public void setImage(Bitmap b) {
			bitmap = b;
		}
	};

	@Test
	public void testImageFetcher() throws MalformedURLException {
		Thread t = new Thread(new POIImageFetcher(new URL("http://upload.wikimedia.org/wikipedia/commons/5/51/Karlsruher_Schloss_Front_Panorama.jpg"), listener));
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(bitmap != null);

	}
}
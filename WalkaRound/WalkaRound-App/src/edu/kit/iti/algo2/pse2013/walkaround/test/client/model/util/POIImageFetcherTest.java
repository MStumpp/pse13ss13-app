package edu.kit.iti.algo2.pse2013.walkaround.test.client.model.util;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.POIImageFetcher;
import junit.framework.Assert;
import android.test.AndroidTestCase;

import java.io.IOException;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;


public class POIImageFetcherTest extends AndroidTestCase {

	public void testImageFetcher() {
		Bitmap bitmap;
		bitmap = POIImageFetcher
				.fetchImage("http://commons.wikimedia.org/wiki/File:Gray_vacuum_cleaner.svg");
		assertTrue(bitmap != null);

	}
}
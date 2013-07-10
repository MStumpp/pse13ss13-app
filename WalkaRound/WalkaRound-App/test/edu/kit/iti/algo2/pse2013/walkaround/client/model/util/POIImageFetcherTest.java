package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import android.graphics.Bitmap;

public class POIImageFetcherTest {

	@Test
	public void testImageFetcher() {
		Bitmap bitmap;
		try {
			bitmap = POIImageFetcher.fetchImage("http://commons.wikimedia.org/wiki/File:Gray_vacuum_cleaner.svg");
			assertTrue(bitmap != null);
		} catch (IOException e) {
			assertTrue(e == null);
		}
	}
}
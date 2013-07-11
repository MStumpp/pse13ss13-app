package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public final class POIImageFetcher {

	private static final String TAG_POIIMAGEFETCHER = POIImageFetcher.class.getSimpleName();

	private POIImageFetcher() {

	}

	public static Bitmap fetchImage(String url) {
		if (url.contains(".JPG")) {
			try {
				URL imageUrl = new URL(url);
				URLConnection connection = imageUrl.openConnection();
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				return bitmap;
			} catch (IOException e) {
				Log.e(TAG_POIIMAGEFETCHER, e.toString());
			}
		}
		return null;
	}
}

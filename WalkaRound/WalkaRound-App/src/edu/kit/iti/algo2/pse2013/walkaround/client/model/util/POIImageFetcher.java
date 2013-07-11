package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public final class POIImageFetcher {

	private POIImageFetcher() {

	}

	public static Bitmap fetchImage(String url) throws IOException {
		if(url.contains(".JPG")) {
		URL imageUrl = new URL(url);
		URLConnection connection = imageUrl
				.openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		return bitmap;
		} else if(url.contains(".SVG")) {
			return null;
		} else {
			return null;
		}
	}
}

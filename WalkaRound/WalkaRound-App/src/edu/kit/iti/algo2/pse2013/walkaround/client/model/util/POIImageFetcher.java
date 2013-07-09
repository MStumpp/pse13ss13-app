package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public final class POIImageFetcher {

	private POIImageFetcher() {

	}

	public static Bitmap fetchImage(String url) throws IOException {
		URL imageUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) imageUrl
				.openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		return bitmap;
	}
}

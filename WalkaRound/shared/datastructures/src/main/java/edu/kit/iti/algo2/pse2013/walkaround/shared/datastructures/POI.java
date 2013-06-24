package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class POI extends Location {

	private String textInfo;

	private String url;

	private int[] poiCategories;

	public POI(double longitude, double latitude, String name, int id,
			Address address, String textInfo, String url, int[] poiCategories) {
		super(latitude, latitude, url, id, address);
		this.textInfo = textInfo;
		this.url = url;
		this.poiCategories = poiCategories;
	}

	public String getTextInfo() {
		return textInfo;
	}

	// Return ist nun eine bitmap und kein bufferedImage mehr!
	/**
	 * xxx
	 * @return
	 * @throws IOException
	 */
	public Bitmap getImage() throws IOException {
		URL url = new URL(this.url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		return bitmap;
	}

	public int[] getPoiCategories() {
		return poiCategories;
	}

}

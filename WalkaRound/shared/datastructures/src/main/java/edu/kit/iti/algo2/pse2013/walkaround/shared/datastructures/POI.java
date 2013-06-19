package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class POI extends Location {

	private String textInfo;

	private String url;

	private ArrayList<Integer> poiCategories;

	public POI(double longitude, double latitude, String name, int id,
			Address address, String textInfo, String url) {
		super(latitude, latitude, url, id, address);
		this.textInfo = textInfo;
		this.url = url;
		poiCategories = new ArrayList<Integer>();
	}

	public String getTextInfo() {
		return textInfo;
	}

	// Return ist nun eine bitmap und kein bufferedImage mehr!
	public Bitmap getImage() {
		URL url = new URL(this.url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		return bitmap;
	}

	public ArrayList<Integer> getPoiCategories() {
		return poiCategories;
	}

}

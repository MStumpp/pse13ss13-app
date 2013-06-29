package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * This class represents a POI.
 * 
 * @author Matthias Stumpp
 * @version 1.0
 */
public class POI extends Location {

	/**
	 * text info of POI.
	 */
	private String textInfo;

	/**
	 * url of image of POI.
	 */
	private String url;

	/**
	 * poi categories of POI.
	 */
	private int[] poiCategories;

	/**
	 * Creates an instance of POI.
	 * 
	 * @param lon
	 *            Longitude of POI.
	 * @param lat
	 *            Latitude of POI.
	 * @param id
	 *            ID of POI.
	 * @param name
	 *            Name of POI.
	 * @param textInfo
	 *            Text info of POI.
	 * @param url
	 *            URL of an image of POI.
	 * @param poiCategories
	 *            Categories of POI.
	 */
	public POI(double lon, double lat, int id, String name, String textInfo,
			String url, int[] poiCategories) {
		this(lon, lat, id, name, textInfo, url, poiCategories, null);
	}

	/**
	 * Creates an instance of POI.
	 * 
	 * @param lon
	 *            Longitude of POI.
	 * @param lat
	 *            Latitude of POI.
	 * @param id
	 *            ID of POI.
	 * @param name
	 *            Name of POI.
	 * @param textInfo
	 *            Text info of POI.
	 * @param url
	 *            URL of an image of POI.
	 * @param poiCategories
	 *            Categories of POI.
	 * @param address
	 *            Address of POI.
	 */
	public POI(double lon, double lat, int id, String name, String textInfo,
			String url, int[] poiCategories, Address address) {
		super(lon, lat, id, name, address);
		this.textInfo = textInfo;
		this.url = url;
		this.poiCategories = poiCategories;
	}

	/**
	 * Returns text info of POI.
	 * 
	 * @return String.
	 */
	public String getTextInfo() {
		return textInfo;
	}

	/**
	 * Returns the wikipedia url of the POI.
	 * 
	 * @return the wikipedia url of the POI
	 */
	public String getWikipediaURL() {
		return url;
	}

	/**
	 * Sets the text information of the POI.
	 * 
	 * @param text
	 *            text information to set.
	 */
	public void setTextInfo(String text) {
		this.textInfo = text;
	}

	/**
	 * Returns an image of POI.
	 * 
	 * @return Bitmap.
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

	/**
	 * Returns all categories of POI.
	 * 
	 * @return int[].
	 */
	public int[] getPoiCategories() {
		return poiCategories;
	}

}

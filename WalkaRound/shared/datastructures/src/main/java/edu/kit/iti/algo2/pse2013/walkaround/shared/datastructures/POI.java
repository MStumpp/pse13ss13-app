package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

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

	public BufferedImage getImage() {
		return null;
	}

	public ArrayList<Integer> getPoiCategories() {
		return poiCategories;
	}

}

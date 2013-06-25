package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.io.File;
import java.util.ArrayList;

public class LocationDataIO {

	private ArrayList<POI> poiList;

	private ArrayList<Area> areaList;

	public ArrayList<POI> getPoiList() {
		return poiList;
	}

	public ArrayList<Area> getAreaList() {
		return areaList;
	}

	public void addPOI(POI poi) {
		poiList.add(poi);
	}

	public void addArea(Area area) {
		areaList.add(area);
	}

	public static LocationDataIO load(File f) {
		return null;
	}

	public static void save(LocationDataIO data, File f) {

	}
}

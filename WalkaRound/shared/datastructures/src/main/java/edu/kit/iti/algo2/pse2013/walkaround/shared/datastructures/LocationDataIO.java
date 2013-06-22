package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class LocationDataIO implements Serializable {
	/**
	 * Temporary Serial version ID as long as Java serialization is used
	 */
	private static final long serialVersionUID = 3394680623853287034L;

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

package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class LocationDataIO {

	private ArrayList<POI> poiList;

	private ArrayList<Area> areaList;

	public ArrayList<POI> getPOIList() {
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

	public static LocationDataIO load(File f) throws FileNotFoundException {
		if (!f.exists()) {
			throw new FileNotFoundException("The file doesn't exist.");
		}
		return null;
	}

	public static void save(LocationDataIO data, File f) {
		if (!f.exists()) {
			f.mkdir();
		}

	}
}

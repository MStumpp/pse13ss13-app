package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

	public static LocationDataIO load(File f) throws IOException,
			ClassNotFoundException {
		if (!f.exists()) {
			throw new FileNotFoundException("The given file doesn't exist.");
		}
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		LocationDataIO locationData = (LocationDataIO) ois.readObject();
		ois.close();
		return locationData;
	}

	public static void save(LocationDataIO data, File f) throws IOException {
		if (!f.exists()) {
			f.mkdirs();
			f.createNewFile();
		}
		ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(f)));
		oos.writeObject(data);
		oos.flush();
		oos.close();
	}
}

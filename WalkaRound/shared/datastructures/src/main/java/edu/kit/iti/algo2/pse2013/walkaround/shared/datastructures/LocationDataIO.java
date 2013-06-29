package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class contains some preprocessed data by OSMDataPreprocessor and
 * WikipediaPreprocessor.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class LocationDataIO implements Serializable {
	/**
	 * Temporary Serial version ID as long as Java serialization is used
	 */
	private static final long serialVersionUID = 3394680623853287034L;

	/**
	 * List of all POIs.
	 */
	private ArrayList<POI> poiList = new ArrayList<>();

	/**
	 * List of all areas.
	 */
	private ArrayList<Area> areaList = new ArrayList<>();

	/**
	 * Returns a list of all POIs.
	 * 
	 * @return a list of all POIs
	 */
	public ArrayList<POI> getPoiList() {
		return poiList;
	}

	/**
	 * Returns a list of all areas.
	 * 
	 * @return a list of all areas
	 */
	public ArrayList<Area> getAreaList() {
		return areaList;
	}

	/**
	 * Adds a POI to the list of all POIs.
	 * 
	 * @param poi
	 *            a POI to add
	 */
	public void addPOI(POI poi) {
		poiList.add(poi);
	}

	/**
	 * Adds an area to the list of all areas.
	 * 
	 * @param area
	 *            an area to add
	 */
	public void addArea(Area area) {
		areaList.add(area);
	}

	/**
	 * Loads a LocationDataIO-object from the given {@code source}-file.
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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

	/**
	 * Saves the GraphDataIO-object, which is given as parameter, as the
	 * {@code destination}-file.
	 * 
	 * @param data
	 * @param f
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void save(LocationDataIO data, File f)
			throws FileNotFoundException, IOException {
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

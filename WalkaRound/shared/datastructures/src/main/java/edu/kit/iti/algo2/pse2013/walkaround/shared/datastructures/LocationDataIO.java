package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.ProtobufConverter;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos;


/**
 * This class contains some preprocessed data by OSMDataPreprocessor and
 * WikipediaPreprocessor.
 *
 * @author Matthias Stumpp
 * @author Thomas Kadow
 * @author Florian Sch&auml;fer
 *
 * @version 1.0
 */
public class LocationDataIO {

	/**
	 * Stores POIs.
	 */
	private List<POI> poiList;


	/**
	 * Stores Areas.
	 */
	private List<Area> areaList;


	/**
	 * Initializes LocationDataIO object.
	 */
	public LocationDataIO() {
		poiList = new ArrayList<POI>();
		areaList = new ArrayList<Area>();
	}


	/**
	 * Returns a list of all POIs.
	 *
	 * @return List<Area> List of all POIs.
	 */
	public List<POI> getPOIs() {
		return poiList;
	}


	/**
	 * Returns a list of all areas.
	 *
	 * @return List<Area> List of all areas.
	 */
	public List<Area> getAreas() {
		return areaList;
	}


	/**
	 * Adds a POI to the list of all POIs.
	 *
	 * @param poi
	 *            POI to be added.
	 */
	public void addPOI(POI poi) {
		poiList.add(poi);
	}


	/**
	 * Adds an area to the list of Areas.
	 *
	 * @param area
	 *            Area to be added.
	 */
	public void addArea(Area area) {
		areaList.add(area);
	}


    /**
     * Saves the LocationDataIO object to an external file.
     *
     * @param objectToSave LocationDataIO object to save.
     * @param destination Location of output file on file system.
     * @throws java.io.IOException
     */
	public static void save(LocationDataIO objectToSave, File destination) throws IOException {
		OutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
		ProtobufConverter.getLocationDataBuilder(objectToSave).build().writeTo(out);
		out.flush();
		out.close();
	}


    /**
     * Loads and returns a LocationDataIO object from a given file.
     *
     * @param source Location of source file in file system.
     * @return the read LocationDataIO-object
     * @throws java.io.IOException
     */
	public static LocationDataIO load(File source) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(source));
		LocationDataIO geom = ProtobufConverter.getLocationData(Protos.SaveLocationData.parseFrom(in));
		in.close();
		return geom;
	}
}
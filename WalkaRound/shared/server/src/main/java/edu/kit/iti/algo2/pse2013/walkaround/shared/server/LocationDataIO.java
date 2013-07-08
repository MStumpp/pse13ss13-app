package edu.kit.iti.algo2.pse2013.walkaround.shared.server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains some preprocessed data by OSMDataPreprocessor and
 * WikipediaPreprocessor.
 *
 * @author Matthias Stumpp
 *
 * @version 1.0
 */
public class LocationDataIO implements Serializable {

    /**
     * Temporary Serial version ID as long as Java serialization is used
     */
    private static final long serialVersionUID = 3394680623853287035L;


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
     * @param poi POI to be added.
     */
    public void addPOI(POI poi) {
        poiList.add(poi);
    }


    /**
     * Adds an area to the list of Areas.
     *
     * @param area Area to be added.
     */
    public void addArea(Area area) {
        areaList.add(area);
    }


    /**
     * Saves the LocationDataIO object to an external file.
     *
     * @param objectToSave LocationDataIO object to save.
     * @param destination Location of output file on file system.
     * @throws FileNotFoundException
     * @throws java.io.IOException
     */
    public static void save(LocationDataIO objectToSave, File destination) throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
        oos.writeObject(objectToSave);
        oos.flush();
        oos.close();
    }


    /**
     * Loads and returns a LocationDataIO object from a given file.
     *
     * @param source Location of source file in file system.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static LocationDataIO load(File source) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source)));
        LocationDataIO locationDataIO = (LocationDataIO) ois.readObject();
        ois.close();
        return locationDataIO;
    }

}
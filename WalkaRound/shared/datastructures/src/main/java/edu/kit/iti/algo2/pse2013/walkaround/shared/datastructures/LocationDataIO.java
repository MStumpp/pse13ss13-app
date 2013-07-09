package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.ProtobufIO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
public class LocationDataIO implements Serializable {

    /**
     * Temporary Serial version ID as long as Java serialization is used
     */
    private static final long serialVersionUID = 3384680623853287035L;


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
    public static void save(LocationDataIO objectToSave, File destination) throws IOException {
//        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
//        oos.writeObject(objectToSave);
//        oos.flush();
//        oos.close();
    	ProtobufIO.write(objectToSave, destination);
//    	LocationProtos.SaveLocationData.Builder locationData = LocationProtos.SaveLocationData.newBuilder();
//    	for (POI p : objectToSave.getPOIs()) {
//    		locationData.addPOI(p.getSavePOI());
//    	}
//    	BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(destination));
//    	locationData.build().writeTo(outStream);
//    	outStream.flush();
//    	outStream.close();
    }


    /**
     * Loads and returns a LocationDataIO object from a given file.
     *
     * @param source Location of source file in file system.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static LocationDataIO load(File source) throws IOException, ClassNotFoundException {
//        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source)));
//        LocationDataIO location = (LocationDataIO) ois.readObject();
//        ois.close();
//        return location;
    	return ProtobufIO.readLocationData(source);
//    	LocationDataIO newLocationData = new LocationDataIO();
//    	LocationProtos.SaveLocationData input = LocationProtos.SaveLocationData.parseFrom(new BufferedInputStream(new FileInputStream(source)));
//    	for (int i = 0; i < input.getPOICount(); i++) {
//    		newLocationData.addPOI(POI.getInstance(input.getPOI(i)));
//    	}
//    	return newLocationData;
    }

}

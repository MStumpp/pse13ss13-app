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
}

package edu.kit.iti.algo2.pse2013.walkaround.server.view.admin;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.server.model.RoundtripProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.server.model.ShortestPathProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;

/**
 * Initialization for Tomcat Server.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Initialization implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        // some initialization
        setUpProcessors();
    }


    public void contextDestroyed(ServletContextEvent event) {
        // Do stuff during webapp's shutdown.
    }


    /**
     * Set up the server
     *
     * The following processors have to be set up:
     * - Graph
     * - ShortestPathProcessor
     * - GeometryProcessor
     */
    private void setUpProcessors() {

        GraphDataIO graphDataIO = getGraphDataIO();

        try {
            ShortestPathProcessor.init(graphDataIO, 2);
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(getGraphDataIO(), getLocationDataIO());
        GeometryProcessor.init(geometryDataIO, 2);

        try {
            RoundtripProcessor.init(graphDataIO, GeometryProcessor.getInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


    /**
     * Loads GraphDataIO
     */
    private GraphDataIO getGraphDataIO() {
        File file = new File(getClass().getResource("/graphData.pbf").getFile());
        GraphDataIO graphDataIO = null;
        try {
            graphDataIO = GraphDataIO.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graphDataIO;
    }


    /**
     * Loads LocationDataIO
     */
    private LocationDataIO getLocationDataIO() {
        File file = new File(getClass().getResource("/locationData.pbf").getFile());
        LocationDataIO locationDataIO = null;
        try {
            locationDataIO = LocationDataIO.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationDataIO;
    }


    /**
     * Loads GeometryDataIO
     */
    private GeometryDataIO getGeometryDataIO() {
        File file = new File(getClass().getResource("/geometryData.pbf").getFile());
        GeometryDataIO geometryDataIO = null;
        try {
            geometryDataIO = GeometryDataIO.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return geometryDataIO;
    }

}

package edu.kit.iti.algo2.pse2013.walkaround.server.view.admin;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.server.model.ShortestPathProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
    private static void setUpProcessors() {

        GraphDataIO graphDataIO = getGraphDataIO();
        LocationDataIO locationDataIO = getLocationDataIO();

        try {
            Graph.init(graphDataIO.getEdges());
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
        }

        try {
            ShortestPathProcessor.init(Graph.getInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(graphDataIO, locationDataIO);
        GeometryProcessor.init(geometryDataIO);
    }


    private static GraphDataIO getGraphDataIO() {

        GraphDataIO graphDataIO = new GraphDataIO();
        Vertex vertex1 = new Vertex(1.d, 1.d);
        Vertex vertex2 = new Vertex(2.d, 2.d);
        Vertex vertex3 = new Vertex(3.d, 3.d);
        Vertex vertex4 = new Vertex(4.d, 4.d);
        Vertex vertex5 = new Vertex(5.d, 5.d);
        Vertex vertex6 = new Vertex(6.d, 6.d);

        Edge edge1 = new Edge(vertex1, vertex2);
        edge1.setLength(5.d);
        Edge edge2 = new Edge(vertex1, vertex3);
        edge2.setLength(1.d);
        Edge edge3 = new Edge(vertex2, vertex5);
        edge3.setLength(2.d);
        Edge edge4 = new Edge(vertex3, vertex2);
        edge4.setLength(2.d);
        Edge edge5 = new Edge(vertex3, vertex4);
        edge5.setLength(3.d);
        Edge edge6 = new Edge(vertex3, vertex6);
        edge6.setLength(8.d);
        Edge edge7 = new Edge(vertex4, vertex1);
        edge7.setLength(1.d);
        Edge edge8 = new Edge(vertex5, vertex3);
        edge8.setLength(1.d);
        Edge edge9 = new Edge(vertex5, vertex6);
        edge9.setLength(2.d);
        Edge edge10 = new Edge(vertex6, vertex4);
        edge10.setLength(2.d);

        graphDataIO.addEdge(edge1);
        graphDataIO.addEdge(edge2);
        graphDataIO.addEdge(edge3);
        graphDataIO.addEdge(edge4);
        graphDataIO.addEdge(edge5);
        graphDataIO.addEdge(edge6);
        graphDataIO.addEdge(edge7);
        graphDataIO.addEdge(edge8);
        graphDataIO.addEdge(edge9);
        graphDataIO.addEdge(edge10);
        return graphDataIO;
    }


    private static LocationDataIO getLocationDataIO() {
        LocationDataIO locationDataIO = new LocationDataIO();
        locationDataIO.addPOI(new POI(1.d, 2.d, "poi 1", "info 1", "url 1", new int[]{0, 1}));
        locationDataIO.addPOI(new POI(3.d, 4.d, "poi 2", "info 2", "url 2", new int[]{0, 1}));
        locationDataIO.addPOI(new POI(5.d, 7.d, "poi 3", "info 3", "url 3", new int[]{0, 1}));
        return locationDataIO;
    }

}

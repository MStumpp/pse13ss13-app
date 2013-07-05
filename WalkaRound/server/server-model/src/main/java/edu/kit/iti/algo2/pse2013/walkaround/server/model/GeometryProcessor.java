package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.server.Coordinate;

/**
 * GeometryProcessor provides some api to query nearest Edges and Vertices based on Coordinates.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryProcessor {

    /**
     * GeometryProcessor instance.
     */
    private static GeometryProcessor instance;


    /**
     * GeometryDataIO instance.
     */
    private GeometryDataIO geometryDataIO;


    /**
     * Creates an instance of GeometryProcessor.
     *
     * @param geometryDataIO GeometryDataIO containing relevant data.
     */
    private GeometryProcessor(GeometryDataIO geometryDataIO) {
        this.geometryDataIO = geometryDataIO;
    }


    /**
     * Instantiates and/or returns a singleton instance of GeometryProcessor.
     *
     * @param geometryDataIO GeometryDataIO containing relevant data.
     * @return GeometryProcessor.
     */
    // TODO: unschön, wenn man sich nur eine instance holen möchte, ohne die Graph instance zu kennen, muss getrennt werden
    public static GeometryProcessor getInstance(GeometryDataIO geometryDataIO) {
        if (geometryDataIO == null)
            throw new IllegalArgumentException("geometryProcessor must be provided");
        if (instance == null)
            instance = new GeometryProcessor(geometryDataIO);
        return instance;
    }


    /**
     * Computes the nearest Edge given a Coordinate.
     *
     * @param coordinate The Coordinate the resulting Edge has to be closest to.
     * @return int ID of Edge.
     */
    public int getNearestEdge(Coordinate coordinate) {
        return 1;
    }


    /**
     * Computes the nearest Vertex given a Coordinate.
     *
     * @param coordinate The Coordinate the resulting Vertex has to be closest to.
     * @return int ID of Vertex.
     */
    public int getNearestVertex(Coordinate coordinate) {
        return 1;
    }


    /**
     * Computes the distance between an Edge and the nearest POI of a certain Category.
     *
     * @param edge ID of the Edge.
     * @param category ID of the Category of the POI.
     * @return float Distance.
     */
    public float getDistanceToNearestPOIForEdge(int edge, int category) {
        return 1.f;
    }


    /**
     * Computes whether and Edge is contained in an Area of a certain Category..
     *
     * @param edge ID of the Edge.
     * @param category ID of the Category of the Area.
     * @return boolean True if contained, false otherwise.
     */
    public boolean isEdgeInArea(int edge, int category) {
        return false;
    }

}

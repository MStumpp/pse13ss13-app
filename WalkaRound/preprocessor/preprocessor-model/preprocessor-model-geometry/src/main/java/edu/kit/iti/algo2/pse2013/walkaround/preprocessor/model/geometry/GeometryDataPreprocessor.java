package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.server.LocationDataIO;

import java.util.*;

/**
 * This class takes some graph and location data and provides an api to query some
 * related info.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryDataPreprocessor {

    /**
     * Preprocesses some data structure to be used by GeometryProcessor.
     *
     * @param graphDataIO GraphDataIO object.
     * @param locationDataIO LocationDataIO object.
     * @return GeometryDataIO.
     * @throw IllegalArgumentException If graphDataIO or locationDataIO args invalid.
     */
    public static GeometryDataIO preprocessGeometryDataIO(GraphDataIO graphDataIO, LocationDataIO locationDataIO) {
        if (graphDataIO == null || locationDataIO == null)
            throw new IllegalArgumentException("graphDataIO and locationDataIO must be provided");

        // get all vertices
        List<Vertex> vertices = new ArrayList<Vertex>();
        for (Edge edge : graphDataIO.getEdges())
            for (Vertex vertex : edge.getVertices())
                vertices.add(vertex);

        Set<Vertex> sortedLatitude = new TreeSet<Vertex>(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                if (v1.getLatitude() >  v2.getLatitude()){
                    return 1;
                } else if (v1.getLatitude() < v2.getLatitude()){
                    return -1;
                } else
                    return 0;
            }
        });
        sortedLatitude.addAll(vertices);

        Set<Vertex> sortedLongitude = new TreeSet<Vertex>(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                if (v1.getLongtitude() >  v2.getLongtitude()){
                    return 1;
                } else if (v1.getLongtitude() < v2.getLongtitude()){
                    return -1;
                } else
                    return 0;
            }
        });
        sortedLongitude.addAll(vertices);

        Vertex[][] array = new Vertex[][]{(Vertex[]) sortedLatitude.toArray(), (Vertex[]) sortedLongitude.toArray()};
        GeometryNode node = buildtree(array);
        return new GeometryDataIO(node);
    }

    private static GeometryNode buildtree(Vertex[][] data) {
        if (data[0].length != data[1].length)
            throw new IllegalArgumentException("both lists must be of same size");
        if (data[0].length == 0)
            throw new IllegalArgumentException("list of vertices must be greater than 0");
        return buildtree(data, 0, 0, data[0].length-1);
    }




}

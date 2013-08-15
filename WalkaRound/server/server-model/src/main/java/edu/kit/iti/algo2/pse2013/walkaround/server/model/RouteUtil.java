package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

import java.util.*;

/**
 * Utility class provides a set of utility methods.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class RouteUtil {

    /**
     * Reverses a list of Vertices.
     *
     * @param list List of Vertices to be reversed.
     * @return List of Vertices.
     */
    public static List<Vertex> reverseList(List<Vertex> list) {
        List<Vertex> invertedList = new LinkedList<Vertex>();
        for (int i = list.size() - 1; i >= 0; i--) {
            invertedList.add(list.get(i));
        }
        return invertedList;
    }


    /**
     * Computes the total length of a list of Vertices.
     *
     * @param vertices Computes the total length of a list of Vertices.
     * @return Total length.
     */
    public static double totalLength(List<Vertex> vertices) {
        double totalLength = 0.d;
        Iterator<Vertex> iter = vertices.iterator();
        Vertex previous = iter.next(), next;
        while (iter.hasNext()) {
            next = iter.next();
            totalLength += computeLength(previous, next);
            previous = next;
        }
        return totalLength;
    }


    /**
     * Returns distance between tail and head in meters using Haversine formula.
     * Source: http://stackoverflow.com/questions/120283/working-with-latitude-longitude-values-in-java
     *
     * @return double.
     */
    public static double computeLength(Vertex vertex1, Vertex vertex2) {
        double earthRadius = 6371009;
        double dLat = Math.toRadians(vertex2.getLatitude() - vertex1.getLatitude());
        double dLng = Math.toRadians(vertex2.getLongitude() - vertex1.getLongitude());
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(vertex1.getLatitude())) * Math.cos(Math.toRadians(vertex2.getLatitude()));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }

}

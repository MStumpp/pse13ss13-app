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
            totalLength += computeDistance(previous.getLatitude(), previous.getLongitude(),
                    next.getLatitude(), next.getLongitude());
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
    public static double computeDistance(double p1X, double p1Y, double p2X, double p2Y) {
        double earthRadius = 6371009;
        double dLat = Math.toRadians(p2X - p1X);
        double dLng = Math.toRadians(p2Y - p1Y);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(p1X)) * Math.cos(Math.toRadians(p2X));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }


    /**
     * Returns the X and Y coordinates on a line given by two points.
     * Source: http://paulbourke.net/geometry/pointlineplane/DistancePoint.java
     *
     * @return double[].
     */
    public static double[] computePointOnLine(double pl1X, double pl1Y,
                                            double pl2X, double pl2Y, double pX, double pY) {
        final double xDelta = pl2X - pl1X;
        final double yDelta = pl2Y - pl1Y;

        if ((xDelta == 0) && (yDelta == 0))
            throw new IllegalArgumentException("line points must be different");

        final double u = ((pX - pl1X) * xDelta + (pY - pl1Y) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

        if (u < 0) {
            return new double[] { pl1X, pl1Y };
        } else if (u > 1) {
            return new double[] { pl2X, pl2Y };
        } else {
            return new double[] { pl1X + u * xDelta, pl1Y + u * yDelta };
        }
    }

}

package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint;

import edu.kit.iti.algo2.pse2013.walkaround.server.model.*;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Profile;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.*;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents the endpoint to be deployed to a JAX-WS
 * compatible servlet container.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
@Path("/processor")
public class Server {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Server.class);


    /**
     * Endpoint method for computation of a shortest path between any given two Coordinates.
     * The actual computation is done by an instance of ShortestPathProcessor.
     *
     * @param coordinates List of coordinates.
     * @return RouteInfoTransfer.
     */
    @POST
    @Path("/computeShortestPath")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public RouteInfoTransfer computeShortestPath(List<Coordinate> coordinates) {

        RouteInfoTransfer transfer = new RouteInfoTransfer();

        // check input
        if (coordinates == null || coordinates.size() != 2) {
            transfer.setError("coordinates must not be null and of size 2");
            return transfer;
        }

        // project coordinate
        Edge sourceEdge;
        Edge targetEdge;
        try {
            sourceEdge = (Edge) GeometryProcessorEdge.getInstance().getNearestGeometrizable(
                    new GeometrySearch(new double[]{coordinates.get(0).getLatitude(), coordinates.get(0).getLongitude()}));
            targetEdge = (Edge) GeometryProcessorEdge.getInstance().getNearestGeometrizable(
                    new GeometrySearch(new double[]{coordinates.get(1).getLatitude(), coordinates.get(1).getLongitude()}));
        } catch (GeometryProcessorException e) {
            transfer.setError(e.getMessage());
            return transfer;
        } catch (InstantiationException e) {
            transfer.setError(e.getMessage());
            return transfer;
        } catch (GeometryComputationNoSlotsException e) {
            transfer.setError(e.getMessage());
            return transfer;
        }

        logger.info("computeShortestPath: Source: " + sourceEdge.toString() + " Target: " + targetEdge.toString());

        if (sourceEdge == null || targetEdge == null) {
            String msg = "no projekted source and/or target edge for given coordinates found";
            logger.info(msg);
            transfer.setError(msg);
        }

        // get source point on source edge
        double[] sourcePoint = RouteUtil.computePointOnLine(sourceEdge.getTail().getLatitude(), sourceEdge.getTail().getLongitude(),
                sourceEdge.getHead().getLatitude(), sourceEdge.getHead().getLongitude(), coordinates.get(0).getLatitude(), coordinates.get(0).getLongitude());

        Vertex source;
        // determine which edge endpoint is closer to point
        if (RouteUtil.computeDistance(sourcePoint[0], sourcePoint[1], sourceEdge.getTail().getLatitude(), sourceEdge.getTail().getLongitude()) <
                RouteUtil.computeDistance(sourcePoint[0], sourcePoint[1], sourceEdge.getHead().getLatitude(), sourceEdge.getHead().getLongitude())) {
            source = sourceEdge.getTail();
        }  else {
            source = sourceEdge.getHead();
        }

        // get target point on target edge
        double[] targetPoint = RouteUtil.computePointOnLine(targetEdge.getTail().getLatitude(), targetEdge.getTail().getLongitude(),
                targetEdge.getHead().getLatitude(), targetEdge.getHead().getLongitude(), coordinates.get(1).getLatitude(), coordinates.get(1).getLongitude());

        Vertex target;
        // determine which edge endpoint is closer to point
        if (RouteUtil.computeDistance(targetPoint[0], targetPoint[1], targetEdge.getTail().getLatitude(), targetEdge.getTail().getLongitude()) <
                RouteUtil.computeDistance(targetPoint[0], targetPoint[1], targetEdge.getHead().getLatitude(), targetEdge.getHead().getLongitude())) {
            target = targetEdge.getTail();
        }  else {
            target = targetEdge.getHead();
        }

        if (source == null || target == null) {
            String msg = "no source and/or target found for given coordinates";
            logger.info(msg);
            transfer.setError(msg);
        }

        List<Vertex> route;
        try {
            route = ShortestPathProcessor.getInstance().computeShortestPath(source, target);
        } catch (ShortestPathComputeException e) {
            transfer.setError(e.getMessage());
            return transfer;
        } catch (NoShortestPathExistsException e) {
            transfer.setError(e.getMessage());
            return transfer;
        } catch (InstantiationException e) {
            transfer.setError(e.getMessage());
            return transfer;
        } catch (ShortestPathComputationNoSlotsException e) {
            transfer.setError(e.getMessage());
            return transfer;
        }

        if (route == null) {
            String msg = "no route computed for given source and target";
            logger.info(msg);
            transfer.setError(msg);
            return transfer;
        }

        for (Vertex vertex : route) {
            transfer.appendCoordinate(new Coordinate(vertex.getLatitude(),
                    vertex.getLongitude(),
                    computeCrossingInformation(vertex)));
        }

        // eventually add point as first coordinate of the route
        Coordinate tail = new Coordinate(sourcePoint[0], sourcePoint[1]);
        if (!tail.equals(transfer.getCoordinates().getFirst()))
            transfer.prependCoordinate(tail);

        // eventually add point as last coordinate of the route
        Coordinate head = new Coordinate(targetPoint[0], targetPoint[1]);
        if (!head.equals(transfer.getCoordinates().getLast()))
            transfer.appendCoordinate(head);

        transfer.setLength(RouteUtil.totalLength(route));

        return transfer;
    }


    /**
     * Endpoint method for computation a roundtrip based on a starting Coordinate, Profile id
     * and a roundtrip length. The actual computation is done by an instance of RoundtripProcessor.
     *
     * @param coordinate The starting Coordinate of the roundtrip to be computed.
     * @param profile The id of the Profile of the roundtrip to be computed.
     * @param length The length of the roundtrip to be computed.
     * @return RouteInfoTransfer.
     */
    @POST
    @Path("computeRoundtrip/profile/{profile}/length/{length}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public RouteInfoTransfer computeRoundtrip(Coordinate coordinate,
                                              @PathParam("profile") String profile,
                                              @PathParam("length") String length) {

        RouteInfoTransfer transfer = new RouteInfoTransfer();

        // check input
        if (coordinate == null || profile == null || length == null) {
            transfer.setError("coordinate, profile and length must not be null");
            return transfer;
        }

        int profileAsInt;
        int lengthAsInt;
        try {
            profileAsInt = Integer.parseInt(profile);
            lengthAsInt = Integer.parseInt(length);
        } catch (NumberFormatException e) {
            transfer.setError(e.getMessage());
            return transfer;
        }

        logger.info("computeRoundtrip: Source: " + coordinate + " Profile: " + profile + " Length: " + length);

        // project coordinate
        Edge edge;
        try {
            edge = (Edge) GeometryProcessorEdge.getInstance().getNearestGeometrizable(
                    new GeometrySearch(new double[]{coordinate.getLatitude(), coordinate.getLongitude()}));
        } catch (GeometryProcessorException e) {
            transfer.setError(e.getMessage());
            return transfer;
        } catch (InstantiationException e) {
            transfer.setError(e.getMessage());
            return transfer;
        } catch (GeometryComputationNoSlotsException e) {
            transfer.setError(e.getMessage());
            return transfer;
        }

        if (edge == null) {
            String msg = "no projekted edge for given coordinate found";
            logger.info(msg);
            transfer.setError(msg);
        }

        // get point on edge
        double[] point = RouteUtil.computePointOnLine(edge.getTail().getLatitude(), edge.getTail().getLongitude(),
                edge.getHead().getLatitude(), edge.getHead().getLongitude(), coordinate.getLatitude(), coordinate.getLongitude());

        Vertex source;
        // determine which edge endpoint is closer to point
        if (RouteUtil.computeDistance(point[0], point[1], edge.getTail().getLatitude(), edge.getTail().getLongitude()) <
                RouteUtil.computeDistance(point[0], point[1], edge.getHead().getLatitude(), edge.getHead().getLongitude())) {
            source = edge.getTail();
        }  else {
            source = edge.getHead();
        }

        if (source == null) {
            String msg = "no source found for given edge";
            logger.info(msg);
            transfer.setError(msg);
        }

        List<Vertex> route;
        try {
            route = RoundtripProcessor.getInstance().computeRoundtrip(source, Profile.getByID(profileAsInt).getContainingPOICategories(), lengthAsInt);
        } catch (InstantiationException e) {
            transfer.setError(e.getMessage());
            return transfer;
        } catch (RoundtripComputationNoSlotsException e) {
            transfer.setError(e.getMessage());
            return transfer;
        } catch (RoundtripComputeException e) {
            transfer.setError(e.getMessage());
            return transfer;
        } catch (IllegalArgumentException e) {
            transfer.setError(e.getMessage());
            return transfer;
        }

        if (route == null) {
            String msg = "no roundtrip computed for given source";
            logger.info(msg);
            transfer.setError(msg);
            return transfer;
        }

        for (Vertex vertex : route) {
            transfer.appendCoordinate(new Coordinate(vertex.getLatitude(),
                    vertex.getLongitude(),
                    computeCrossingInformation(vertex)));
        }

        // eventually add point as first/last coordinate of the route
        Coordinate begin = new Coordinate(point[0], point[1]);
        if (!begin.equals(transfer.getCoordinates().getFirst())) {
            transfer.prependCoordinate(begin);
            transfer.appendCoordinate(begin);
        }

        transfer.setLength(RouteUtil.totalLength(route));

        return transfer;
    }


    /**
     * Endpoint method for computation an optimized route based on a given route.
     * The actual computation is done by an instance of RoundtripProcessor.
     *
     * @param coordinates List of coordinates.
     * @return RouteInfoTransfer.
     */
    @POST
    @Path("computeOptimizedRoute")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public RouteInfoTransfer computeOptimizedRoute(List<Coordinate> coordinates) {

        RouteInfoTransfer transfer = new RouteInfoTransfer();

        // check input
        if (coordinates == null || coordinates.size() < 2) {
            transfer.setError("coordinates must not be null and size equal to or greater than 2");
            return transfer;
        }

        logger.info("computeOptimizedRoute");

        transfer.setError("computeOptimizedRoute not yet implemented");
        return transfer;
    }


    /**
     * Endpoint method for computing the projected Coordinate.
     * The actual computation is done by an instance of GeometryProcessor.
     *
     * @param coordinate Coordinate to be projected.
     * @return Coordinate.
     */
    @POST
    @Path("/getNearestVertex")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Coordinate getNearestVertex(Coordinate coordinate) {

        // check input
        if (coordinate == null)
            return null;

        // project coordinate
        Edge edge;
        try {
            edge = (Edge) GeometryProcessorEdge.getInstance().getNearestGeometrizable(
                    new GeometrySearch(new double[]{coordinate.getLatitude(), coordinate.getLongitude()}));
        } catch (InstantiationException e) {
            return null;
        } catch (GeometryProcessorException e) {
            return null;
        } catch (GeometryComputationNoSlotsException e) {
            return null;
        }

        if (edge == null) {
            return null;
        }

        double[] point = RouteUtil.computePointOnLine(edge.getTail().getLatitude(), edge.getTail().getLongitude(),
                edge.getHead().getLatitude(), edge.getHead().getLongitude(), coordinate.getLatitude(), coordinate.getLongitude());

        return new Coordinate(point[0], point[1], null);
    }


    /**
     * Computes the CrossingInformation for a given Vertex.
     *
     * @param vertex Vertex CrossingInformation to be computed for.
     * @return CrossingInformation for the given Vertex.
     */
    private CrossingInformation computeCrossingInformation(Vertex vertex) {

        List<Float> angles = new ArrayList<Float>();
        for (Edge edge : vertex.getOutgoingEdges()) {
            double angle1 = Math.atan2(vertex.getParent().getLongitude() - vertex.getLongitude(),
                    vertex.getParent().getLatitude() - vertex.getLatitude());
            double angle2 = Math.atan2(vertex.getLongitude() - edge.getHead().getLongitude(),
                    vertex.getLatitude() - edge.getHead().getLatitude());
            angles.add(new Float(angle1-angle2));
        }
        float[] floatArray = new float[angles.size()];
        for (int i = 0; i < angles.size(); i++) {
            Float f = angles.get(i);
            floatArray[i] = f;
        }
        Arrays.sort(floatArray);
        return new CrossingInformation(floatArray);
    }

}

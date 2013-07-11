package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint;

import edu.kit.iti.algo2.pse2013.walkaround.server.model.NoShortestPathExistsException;
import edu.kit.iti.algo2.pse2013.walkaround.server.model.RoundtripProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.server.model.ShortestPathComputeException;
import edu.kit.iti.algo2.pse2013.walkaround.server.model.ShortestPathProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryProcessorException;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Graph;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.NoVertexForIDExistsException;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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

        Graph graph = null;
        try {
            graph = Graph.getInstance();
        } catch (InstantiationException e) {
            transfer.setError("InstantiationException");
            return transfer;
        }

        // project temporary
        Vertex source = null;
        Vertex target = null;
        try {
            source = graph.getVertexByID(0);
            target = graph.getVertexByID(4);
        } catch (NoVertexForIDExistsException e) {
            transfer.setError("NoVertexForIDExistsException");
            return transfer;
        }

        // project coordinate
//        Vertex source = null;
//        Vertex target = null;
//        try {
//            source = (Vertex) GeometryProcessor.getInstance().getNearestVertex(coordinates.get(0));
//            target = (Vertex) GeometryProcessor.getInstance().getNearestVertex(coordinates.get(1));
//        } catch (GeometryProcessorException e) {
//            transfer.setError("GeometryProcessorException");
//            return transfer;
//        } catch (InstantiationException e) {
//            transfer.setError("InstantiationException");
//            return transfer;
//        }

        List<Vertex> route = null;
        try {
            route = ShortestPathProcessor.getInstance().computeShortestPath(source, target);
        } catch (ShortestPathComputeException e) {
            transfer.setError("ShortestPathComputeException");
            return transfer;
        } catch (NoShortestPathExistsException e) {
            transfer.setError("NoShortestPathExistsException");
            return transfer;
        } catch (InstantiationException e) {
            transfer.setError("InstantiationException");
            return transfer;
        }

        if (route == null) {
            transfer.setError("route is null");
            return transfer;
        }

        for (Vertex vertex : route)
            transfer.addCoordinates(new Coordinate(vertex.getLatitude(),
                    vertex.getLongitude(),
                    new CrossingInformation(new float[]{50.f, 100.f})));

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
            transfer.setError("NumberFormatException");
            return transfer;
        }

        Graph graph = null;
        try {
            graph = Graph.getInstance();
        } catch (InstantiationException e) {
            transfer.setError("InstantiationException");
            return transfer;
        }

        // project temporary
        Vertex source = null;
        try {
            source = graph.getVertexByID(0);
        } catch (NoVertexForIDExistsException e) {
            transfer.setError("NoVertexForIDExistsException");
            return transfer;
        }

        // project coordinate
//        Vertex source = null;
//        try {
//            source = (Vertex) GeometryProcessor.getInstance().getNearestVertex(coordinate);
//        } catch (GeometryProcessorException e) {
//            transfer.setError("GeometryProcessorException");
//            return transfer;
//        } catch (InstantiationException e) {
//            transfer.setError("InstantiationException");
//            return transfer;
//        }

        List<Vertex> route = null;
        try {
            route = RoundtripProcessor.getInstance().computeRoundtrip(source, profileAsInt, lengthAsInt);
        } catch (ShortestPathComputeException e) {
            transfer.setError("ShortestPathComputeException");
            return transfer;
        } catch (NoShortestPathExistsException e) {
            transfer.setError("NoShortestPathExistsException");
            return transfer;
        } catch (InstantiationException e) {
            transfer.setError("InstantiationException");
            return transfer;
        }

        if (route == null) {
            transfer.setError("route is null");
            return transfer;
        }

        for (Vertex vertex : route)
            transfer.addCoordinates(new Coordinate(vertex.getLatitude(),
                    vertex.getLongitude(),
                    new CrossingInformation(new float[]{50.f, 100.f})));

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

//      return OptimizeRouteProcessor.getInstance().computeOptimizedRoute(routeInfoTransfer);
        transfer.setError("computeOptimizedRoute not yet implemented");
        return transfer;
    }

}

package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint;

import edu.kit.iti.algo2.pse2013.walkaround.server.model.*;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.RouteInfoTransfer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
     * @param coordinate1 One end of the route to be computed.
     * @param coordinate2 One end of the route to be computed.
     * @return RouteInfoTransfer.
     */
    @POST
    @Path("computeShortestPath")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public RouteInfoTransfer computeShortestPath(Coordinate coordinate1,
                                                 Coordinate coordinate2) {

        // check input
        if (coordinate1 == null || coordinate2 == null)
            throw new IllegalArgumentException("coordinate1 and coordinate2 must not be null");

        // project coordinate
        Vertex sourceVertex = null;
        Vertex targetVertex = null;
        try {
            sourceVertex = (Vertex) GeometryProcessor.getInstance().getNearestVertex(coordinate1);
            targetVertex = (Vertex) GeometryProcessor.getInstance().getNearestVertex(coordinate2);
        } catch (GeometryProcessorException e) {
            e.printStackTrace();
        }

        try {
            return ShortestPathProcessor.getInstance().computeShortestPath(sourceVertex, targetVertex);
        } catch (NoShortestPathExistsException e) {
            e.printStackTrace();
        } catch (ShortestPathComputeException e) {
            e.printStackTrace();
        }

        return null;
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
        int profileAsInt = Integer.parseInt(profile);
        int lengthAsInt = Integer.parseInt(length);
        return RoundtripProcessor.getInstance().computeRoundtrip(coordinate, profileAsInt, lengthAsInt);
    }


    /**
     * Endpoint method for computation an optimized route based on a given route.
     * The actual computation is done by an instance of RoundtripProcessor.
     *
     * @param routeInfoTransfer The route to be optimized.
     * @return RouteInfoTransfer.
     */
    @POST
    @Path("computeOptimizedRoute")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public RouteInfoTransfer computeOptimizedRoute(RouteInfoTransfer routeInfoTransfer) {
        return OptimizeRouteProcessor.getInstance().computeOptimizedRoute(routeInfoTransfer);
    }

}

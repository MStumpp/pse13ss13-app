package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint;

import edu.kit.iti.algo2.pse2013.walkaround.server.model.NoShortestPathExistsException;
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
//     * @param coordinate1 One end of the route to be computed.
//     * @param coordinate2 One end of the route to be computed.
     * @return RouteInfoTransfer.
     */
    @POST
    @Path("/computeShortestPath")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public RouteInfoTransfer computeShortestPath(Coordinate coordinate1,
                                                 Coordinate coordinate2)
            throws InstantiationException {

        throw new IllegalArgumentException("blabla");
            /*
        // check input
        if (coordinate1 == null || coordinate2 == null)
            throw new IllegalArgumentException("coordinate1 and coordinate2 must not be null");

        Graph graph = null;
        try {
            graph = Graph.getInstance();
        } catch (InstantiationException e) {
            return null;
        }

        // project temporary
        Vertex source = null;
        Vertex target = null;
        try {
            source = graph.getVertexByID(0);
            target = graph.getVertexByID(4);
        } catch (NoVertexForIDExistsException e) {
            e.printStackTrace();
            return null;
        }

        // project coordinate
//        Vertex source = null;
//        Vertex target = null;
//        try {
//            source = (Vertex) GeometryProcessor.getInstance().getNearestVertex(new C);
//            target = (Vertex) GeometryProcessor.getInstance().getNearestVertex(coordinate2);
//        } catch (GeometryProcessorException e) {
//            e.printStackTrace();
//            return null;
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//            return null;
//        }

        List<Vertex> route = null;
        try {
            route = ShortestPathProcessor.getInstance().computeShortestPath(source, target);
        } catch (ShortestPathComputeException e) {
            e.printStackTrace();
            return null;
        } catch (NoShortestPathExistsException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        }

        RouteInfoTransfer transfer = new RouteInfoTransfer();
        for (Vertex vertex : route)
            transfer.addCoordinates(new Coordinate(vertex.getLatitude(),
                    vertex.getLongitude(),
                    new CrossingInformation(new float[]{50.f, 100.f})));

        return transfer;     */
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
//        int profileAsInt = Integer.parseInt(profile);
//        int lengthAsInt = Integer.parseInt(length);
//        return RoundtripProcessor.getInstance().computeRoundtrip(coordinate, profileAsInt, lengthAsInt);        RouteInfoTransfer transfer = new RouteInfoTransfer();
        RouteInfoTransfer transfer = new RouteInfoTransfer();
        return transfer;
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
//        return OptimizeRouteProcessor.getInstance().computeOptimizedRoute(routeInfoTransfer);
        RouteInfoTransfer transfer = new RouteInfoTransfer();
        return transfer;
    }

}

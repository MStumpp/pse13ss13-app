package edu.kit.iti.algo2.pse2013.walkaround.shared.route;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;

/**
 * This class provides a set of delegation methods for computing a shortest path,
 * roundtrip and optimized route. The actual computation is done by an endpoint.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class RouteProcessing {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RouteProcessing.class);


    /**
     * TAG for android debugging.
     */
    private static String TAG_ROUTE_PROCESSING = RouteProcessing.class.getSimpleName();


    /**
     * URL for shortest path computation.
     */
    private static String URL_COMPUTESHORTESTPATH =
            "http://127.0.0.1:8080/walkaround/api/processor/computeShortestPath";


    /**
     * URL for roundtrip computation.
     */
    private static String URL_ROUNDTRIP =
            "http://127.0.0.1:8080/walkaround/api/processor/computeRoundtrip";


    /**
     * RouteProcessing instance.
     */
    private static RouteProcessing instance;


    /**
     * Creates a fresh instance of RouteProcessing.
     */
    private RouteProcessing() {
    }


    /**
     * Instantiates and/or returns a singleton instance of RouteProcessing.
     *
     * @return RouteProcessing.
     */
    public static RouteProcessing getInstance() {
        Log.d(TAG_ROUTE_PROCESSING, "getInstance()");
        if (instance == null)
            instance = new RouteProcessing();
        return instance;
    }


    /**
     * Delegation method for computing a shortest path between any two Coordinates.
     * The actual computation is done by an endpoint.
     *
     * @param coordinate1 One end of the route to be computed.
     * @param coordinate2 One end of the route to be computed.
     * @return RouteInfo.
     * @throws RouteProcessingException If something goes wrong.
     * @throws IllegalArgumentException if input parameter are null.
     */
    public RouteInfo computeShortestPath(Coordinate coordinate1,
                                         Coordinate coordinate2)
            throws RouteProcessingException {
        Log.d(TAG_ROUTE_PROCESSING, "computeShortestPath(Coordinate " + coordinate1 + ", Coordinate " + coordinate2 + ")");

        if (coordinate1 == null || coordinate2 == null)
            throw new IllegalArgumentException("coordinate 1 and coordinate 2 must be provided");

        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();

        InputStream is;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_COMPUTESHORTESTPATH);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");

            String requestAsJSON = gson.toJson(new Coordinate[]{coordinate1, coordinate2});
            logger.debug("requestAsJSON" + requestAsJSON);
            httpPost.setEntity(new StringEntity(requestAsJSON));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            throw new RouteProcessingException("UnsupportedEncodingException");
        } catch (ClientProtocolException e) {
            throw new RouteProcessingException("ClientProtocolException");
        } catch (IOException e) {
            throw new RouteProcessingException("IOException");
        }

        String responseAsJSON;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");
            is.close();
            responseAsJSON = sb.toString();
            logger.debug("responseAsJSON" + responseAsJSON);
        } catch (Exception e) {
            throw new RouteProcessingException("error converting result " + e.toString());
        }

        RouteInfoTransfer routeInfoTransfer = gson.fromJson(responseAsJSON, RouteInfoTransfer.class);

        if (routeInfoTransfer == null || routeInfoTransfer.getError() != null)
            throw new RouteProcessingException(routeInfoTransfer.getError());

        // replace first and last Coordinate with Waypoint
        routeInfoTransfer.postProcess();

        RouteInfo route = new Route(new LinkedList<Coordinate>(routeInfoTransfer.getCoordinates()));
        Log.d(TAG_ROUTE_PROCESSING, "computeShortestPath(Coordinate, Coordinate) returning Route: " + route);
        return route;
    }


    /**
     * Delegation method for computing a roundtrip based on a starting Coordinate, Profile id
     * and a roundtrip length. The actual computation is done by an endpoint.
     *
     * @param coordinate The starting Coordinate of the roundtrip to be computed.
     * @param profile The id of the Profile of the roundtrip to be computed.
     * @param length The length of the roundtrip to be computed.
     * @return RouteInfo.
     * @throws RouteProcessingException If something goes wrong.
     * @throws IllegalArgumentException if input parameter are null.
     */
    public RouteInfo computeRoundtrip(Coordinate coordinate, int profile, int length)
            throws RouteProcessingException {
        Log.d(TAG_ROUTE_PROCESSING, "computeRoundtrip(Coordinate coordinate, int profile, int length)");

        if (coordinate == null)
            throw new IllegalArgumentException("coordinate 1 and coordinate 2 must be provided");
        if (profile < 0)
            throw new IllegalArgumentException("profile id must be equal to or greater than 1");
        if (length < 100)
            throw new IllegalArgumentException("length must be at least 100 meter");

        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();

        InputStream is;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_ROUNDTRIP + "/profile/" + profile + "/length/" + length);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");

            String requestAsJSON = gson.toJson(coordinate);
            logger.debug("requestAsJSON" + requestAsJSON);
            httpPost.setEntity(new StringEntity(requestAsJSON));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            throw new RouteProcessingException("UnsupportedEncodingException");
        } catch (ClientProtocolException e) {
            throw new RouteProcessingException("ClientProtocolException");
        } catch (IOException e) {
            throw new RouteProcessingException("IOException");
        }

        String responseAsJSON;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");
            is.close();
            responseAsJSON = sb.toString();
            logger.debug("responseAsJSON" + responseAsJSON);
        } catch (Exception e) {
            throw new RouteProcessingException("error converting result " + e.toString());
        }

        RouteInfoTransfer routeInfoTransfer = gson.fromJson(responseAsJSON, RouteInfoTransfer.class);

        if (routeInfoTransfer == null || routeInfoTransfer.getError() != null)
            throw new RouteProcessingException(routeInfoTransfer.getError());

        // replace first and last Coordinate with Waypoint
        routeInfoTransfer.postProcess();

        RouteInfo routeInfo = new Route(new LinkedList<Coordinate>(routeInfoTransfer.getCoordinates()));
        Log.d(TAG_ROUTE_PROCESSING, "computeRoundtrip(Coordinate coordinate, int profile, int length) returning Route: " + routeInfo);
        return routeInfo;
    }


    /**
     * Delegation method for computing an optimized Route based on a given Route.
     * The actual computation is done by an endpoint.
     *
     * @param routeInfo The Route to be optimized.
     * @return RouteInfo.
     * @throws RouteProcessingException If something goes wrong.
     */
    public RouteInfo computeOptimizedRoute(RouteInfo routeInfo)
            throws RouteProcessingException {
        throw new RouteProcessingException("not yet implemented");
    }

}

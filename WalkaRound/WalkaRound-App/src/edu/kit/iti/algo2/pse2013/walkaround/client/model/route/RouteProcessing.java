package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.PreferenceUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * This class provides a set of delegation methods for computing a shortest
 * path, roundtrip and optimized route. The actual computation is done by an
 * endpoint.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class RouteProcessing {

	private static int timeout = 5000;

	/**
	 * TAG for android debugging.
	 */
	private static String TAG_ROUTE_PROCESSING = RouteProcessing.class.getSimpleName();

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

	private class JSONAnswerGetter implements Runnable {
		private Gson gson;
		private String json = "";
		private Object objectToSend;
		private HttpPost url;
		private Exception exception;

		public JSONAnswerGetter(Gson gson, Object objectToSend, HttpPost url) {
			this.gson = gson;
			this.objectToSend = objectToSend;
			this.url = url;
		}

		@Override
		public void run() {
			InputStream is;
			try {
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
				HttpConnectionParams.setSoTimeout(httpParameters, timeout);

				DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
				HttpPost httpPost = url;
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-Type", "application/json");


				String requestAsJSON = gson.toJson(objectToSend);

				Log.d(TAG_ROUTE_PROCESSING, "Built JSON: " + requestAsJSON);
				Log.d(TAG_ROUTE_PROCESSING, String.format("Send to URL %s", url.getURI().toURL().toExternalForm()));

				httpPost.setEntity(new StringEntity(requestAsJSON));

				HttpResponse httpResponse = httpClient.execute(httpPost);


				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

				Log.d(TAG_ROUTE_PROCESSING, "Sent JSON: " + requestAsJSON);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null)
					sb.append(line + "\n");
				is.close();
				json = sb.toString();

			} catch (UnsupportedEncodingException e) {
				exception = new RouteProcessingException(
						"UnsupportedEncodingException");
			} catch (ClientProtocolException e) {
				exception = new RouteProcessingException(
						"ClientProtocolException");
			} catch (IOException e) {
				exception = new RouteProcessingException("IOException");
			}
		}

		public Exception getException() {
			return exception;
		}

		public String getJSONAnswer() {
			return json;
		}
	}


	/**
	 * Delegation method for computing a shortest path between any two
	 * Coordinates. The actual computation is done by an endpoint.
	 *
	 * @param source
	 *            One end of the route to be computed.
	 * @param target
	 *            One end of the route to be computed.
	 * @return RouteInfo.
	 * @throws RouteProcessingException
	 *             If something goes wrong.
	 * @throws InterruptedException
	 * @throws IllegalArgumentException
	 *             if input parameter are null.
	 */
	public RouteInfo computeShortestPath(Coordinate source, Coordinate target) throws RouteProcessingException, InterruptedException {

		if (source == null || target == null)
			throw new IllegalArgumentException("source and target must be provided for RouteProcessing.computeShortestPath()");

		Log.d(TAG_ROUTE_PROCESSING, "computeShortestPath(Source " + source + ", Target " + target + ")");

		Gson gson = new GsonBuilder().create();

		RouteInfoTransfer routeInfoTransfer = null;

		JSONAnswerGetter jsonAnswerGetter = new JSONAnswerGetter(
			gson,
			new Coordinate[] {
					new Coordinate(source.getLatitude(), source.getLongitude()),
					new Coordinate(target.getLatitude(), target.getLongitude())
			},
			new HttpPost(PreferenceUtility.getInstance().getShortestPathServerUrl())
		);
		Thread thread = new Thread(jsonAnswerGetter);
		Log.d(TAG_ROUTE_PROCESSING, "computeShortestPath() - pre Thread " + thread.getId());
		thread.start();
		thread.join();
		Log.d(TAG_ROUTE_PROCESSING, "computeShortestPath() - post Thread " + thread.getId());

		if (jsonAnswerGetter.getException() != null) {
			Log.e(TAG_ROUTE_PROCESSING, "HTTP-Connection caused exception", jsonAnswerGetter.getException());
			throw new RouteProcessingException(jsonAnswerGetter.getException().toString());
		}
		Log.d(TAG_ROUTE_PROCESSING, "Answered JSON: " + jsonAnswerGetter.getJSONAnswer());
		try {
			routeInfoTransfer = gson.fromJson(jsonAnswerGetter.getJSONAnswer(), RouteInfoTransfer.class);
		} catch (JsonSyntaxException jse) {
			// EMPTY
		}

		if (routeInfoTransfer == null) {
			throw new RouteProcessingException("routeInfoTransfer is null");
		} else  if (routeInfoTransfer.getError() != null) {
 			throw new RouteProcessingException(routeInfoTransfer.getError());
 		}

		// replace first and last Coordinate with Waypoint
		routeInfoTransfer.postProcessShortestPath(source, target);

		RouteInfo route = new Route(new LinkedList<Coordinate>(
				routeInfoTransfer.getCoordinates()));
		Log.d(TAG_ROUTE_PROCESSING,
				"computeShortestPath(Coordinate, Coordinate) returning Route: "
						+ route);
		return route;
	}


	/**
	 * Delegation method for computing a roundtrip based on a starting
	 * Coordinate, Profile id and a roundtrip length. The actual computation is
	 * done by an endpoint.
	 *
	 * @param source
	 *            The starting Coordinate of the roundtrip to be computed.
	 * @param profile
	 *            The id of the Profile of the roundtrip to be computed.
	 * @param length
	 *            The length of the roundtrip to be computed.
	 * @return RouteInfo.
	 * @throws RouteProcessingException
	 *             If something goes wrong.
	 * @throws InterruptedException
	 * @throws IllegalArgumentException
	 *             if input parameter are null.
	 */
	public RouteInfo computeRoundtrip(Coordinate source, int profile, int length) throws RouteProcessingException, InterruptedException {
		Log.d(TAG_ROUTE_PROCESSING,
				"computeRoundtrip(Coordinate coordinate, int profile, int length)");

		if (source == null)
			throw new IllegalArgumentException("a source coordinate must be provided");
		if (profile < 0)
			throw new IllegalArgumentException("profile id must be at least 0");
		if (length < 100)
			throw new IllegalArgumentException("length must be at least 100 meter");

		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();

		RouteInfoTransfer routeInfoTransfer = null;

		String urlString =
			PreferenceUtility.getInstance().getRoundtripPathServerUrl()
			+ "/profile/" + profile + "/length/" + length;
		JSONAnswerGetter gsonAnswerer = new JSONAnswerGetter(
			gson,
			new Coordinate(
				source.getLatitude(),
				source.getLongitude()
			),
			new HttpPost(urlString)
		);
		Thread thread = new Thread(gsonAnswerer);
		thread.start();
		thread.join();

		if (gsonAnswerer.getException() != null) {
			Log.e(TAG_ROUTE_PROCESSING, "HTTP-Connection caused exception", gsonAnswerer.getException());
			throw new RouteProcessingException(gsonAnswerer.getException().toString());
		}
		Log.d(TAG_ROUTE_PROCESSING, "Answered JSON: " + gsonAnswerer.getJSONAnswer());
		try {
			routeInfoTransfer = gson.fromJson(gsonAnswerer.getJSONAnswer(), RouteInfoTransfer.class);
		} catch (JsonSyntaxException jse) {
			// EMPTY
		}
		if (routeInfoTransfer == null) {
			Log.e(TAG_ROUTE_PROCESSING, "Rundkurs konnte nicht berechnet werden");
			throw new RouteProcessingException("routeInfoTransfer is null");
		} else  if (routeInfoTransfer.getError() != null) {
 			throw new RouteProcessingException(routeInfoTransfer.getError());
 		}

		// replace first and last Coordinate with Waypoint
		routeInfoTransfer.postProcessRoundtrip();

		RouteInfo routeInfo = new Route(new LinkedList<Coordinate>(
				routeInfoTransfer.getCoordinates()));
		Log.d(TAG_ROUTE_PROCESSING,
				"computeRoundtrip(Coordinate coordinate, int profile, int length) returning Route: "
				+ routeInfo);
		return routeInfo;
	}


	/**
	 * Delegation method for computing an optimized Route based on a given
	 * Route. The actual computation is done by an endpoint.
	 *
	 * @param routeInfo
	 *            The Route to be optimized.
	 * @return RouteInfo.
	 * @throws RouteProcessingException
	 *             If something goes wrong.
	 */
	/*public RouteInfo computeOptimizedRoute(RouteInfo routeInfo)
			throws RouteProcessingException {
		throw new RouteProcessingException("not yet implemented");
	}*/

}

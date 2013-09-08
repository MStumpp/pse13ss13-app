package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * This class provides a method to normalize a coordinate to a coordinate on a
 * graph.
 *
 * @author Thomas Kadow
 * @author Matthias Stumpp
 * @version 1.1
 */
public final class CoordinateNormalizer {

	private static final int timeout = 2000;

	private static final String TAG = CoordinateNormalizer.class
			.getSimpleName();

	/**
	 * URL for roundtrip computation.
	 */
	//private static String URL_NEARESTVERTEX = "http://54.213.123.61:8080/walkaround/api/processor/getNearestVertex";

	/**
	 * This class is an utility class which is not instantiated.
	 */
	private CoordinateNormalizer() {
	}

	/**
	 * Normalizes a coordinate to a coordinate on a graph.
	 *
	 * @param coord
	 *            coordinate to normalize
	 * @param levelOfDetail
	 *            the "tile zoom level" for which the calculation should be made
	 * @return a normalized coordinate on a graph
	 * @throws CoordinateNormalizerException
	 *             If something goes wrong.
	 * @throws InterruptedException
	 */
	public static Coordinate normalizeCoordinate(Coordinate coordinate,
			float levelOfDetail) throws CoordinateNormalizerException,
			InterruptedException {
		Log.d(TAG, "normalizeCoordinate() METHOD START");

		if (coordinate == null)
			throw new IllegalArgumentException("coordinate must be provided");

		Log.d(TAG, "normalizeCoordinate(Coordinate " + coordinate + ", levelOfDetail " + levelOfDetail + ")");

		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();

		Coordinate normalizedCoordinate = null;

		JSONAnswerGetter gsonAnswerer = new JSONAnswerGetter(gson,
				new Coordinate(coordinate.getLatitude(),
						coordinate.getLongitude()), new HttpPost(
						PreferenceUtility.getInstance().getNextVertexServerUrl()));
		Thread thread = new Thread(gsonAnswerer);
		thread.start();
		thread.join();

		if (gsonAnswerer.getException() != null) {
			Log.e(TAG, "HTTP-Connection caused exception",
					gsonAnswerer.getException());
			throw new CoordinateNormalizerException(gsonAnswerer.getException()
					.toString());
		} else {
			Log.d(TAG, "Answered JSON: " + gsonAnswerer.getJSONAnswer());
			normalizedCoordinate = gson.fromJson(gsonAnswerer.getJSONAnswer(),
					Coordinate.class);
		}

		if (normalizedCoordinate == null)
			throw new CoordinateNormalizerException(
					"normalizedCoordinate is null");

		Log.d(TAG, "normalizeCoordinate() normalized Coordinate: " + normalizedCoordinate);
		Log.d(TAG, "normalizeCoordinate() METHOD END");
		return normalizedCoordinate;
	}

	private static class JSONAnswerGetter implements Runnable {
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

		public void run() {
			InputStream is;
			try {
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeout);
				HttpConnectionParams.setSoTimeout(httpParameters, timeout);

				DefaultHttpClient httpClient = new DefaultHttpClient(
						httpParameters);
				HttpPost httpPost = url;
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-Type", "application/json");

				String requestAsJSON = gson.toJson(objectToSend);

				Log.d(TAG, "Built JSON: " + requestAsJSON);

				httpPost.setEntity(new StringEntity(requestAsJSON));

				HttpResponse httpResponse = httpClient.execute(httpPost);

				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

				Log.d(TAG, "Sent JSON: " + requestAsJSON);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null)
					sb.append(line + "\n");
				is.close();
				json = sb.toString();

			} catch (UnsupportedEncodingException e) {
				exception = new CoordinateNormalizerException(
						"UnsupportedEncodingException");
			} catch (ClientProtocolException e) {
				exception = new CoordinateNormalizerException(
						"ClientProtocolException");
			} catch (IOException e) {
				exception = new CoordinateNormalizerException("IOException");
			}
		}

		public Exception getException() {
			return exception;
		}

		public String getJSONAnswer() {
			return json;
		}
	}
	
}

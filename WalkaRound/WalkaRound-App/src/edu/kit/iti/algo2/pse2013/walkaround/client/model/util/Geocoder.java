package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

public class Geocoder {
	private static final String TAG = Geocoder.class.getSimpleName();
	private int timeout = 5000;

	public void reverseGeocode(Context context, Location loc) {
		new Thread(new RunReverseGeocode(loc)).start();
		loc.setName(context.getString(R.string.unknown_location));
	}

	private class RunReverseGeocode implements Runnable {
		private Location loc;

		public RunReverseGeocode(Location loc) {
			this.loc = loc;
		}

		@Override
		public void run() {
			try {
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
				HttpConnectionParams.setSoTimeout(httpParameters, timeout);

				DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
				Log.d(TAG, String.format(Locale.UK, "http://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1",
						loc.getLatitude(),
						loc.getLongitude()));
				HttpGet httpGet = new HttpGet(
					String.format(Locale.UK, "http://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1",
					loc.getLatitude(),
					loc.getLongitude())
				);
				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-Type", "application/json");

				HttpResponse httpResponse = httpClient.execute(httpGet);

				HttpEntity httpEntity = httpResponse.getEntity();
				BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(httpEntity.getContent())));

				StringBuilder sb = new StringBuilder();
				String read = "";
				while ((read = br.readLine()) != null) {
					sb.append(read);
				}
				br.close();

				String name = "";
				JSONObject jsonObj = new JSONObject(sb.toString());
				if (jsonObj.has("address")) {
					JSONObject address = jsonObj.getJSONObject("address");
					if (address != null) {
						if (address.has("road")) {
							name = address.getString("road");
							if (address.has("house_number")) {
								name += " " + address.getString("house_number");
							}
						} else {
							if (address.has("postcode")) {
								name += address.getString("postcode") + " ";
							}
							if (address.has("city")) {
								name += address.getString("city");
							}
						}
					}
				}
				if (name != "") {
					loc.setName(name);
				}
				Log.d(TAG, "Location is " + name);
			} catch (IOException e) {
				Log.e(TAG, "Location not found!", e);
			} catch (JSONException e) {
				Log.e(TAG, "Location not found!", e);
			}
		}
	}
}

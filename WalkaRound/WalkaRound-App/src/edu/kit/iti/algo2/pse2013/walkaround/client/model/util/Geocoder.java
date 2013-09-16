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

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.activity.BootActivity;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

public class Geocoder {
	private static final String TAG = Geocoder.class.getSimpleName();
	private int timeout = 10000;

	public void reverseGeocode(Location loc) {
		new Thread(new RunReverseGeocode(loc)).start();
		loc.setName(BootActivity.getAppContext().getString(R.string.unknown_location, loc.getId()));
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

				String urlString = String.format(
					Locale.UK,
					"http://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1&email=%s",
					loc.getLatitude(),
					loc.getLongitude(),
					BootActivity.getAppContext().getString(R.string.email_address)
				);
				DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
				Log.d(TAG, urlString);
				HttpGet httpGet = new HttpGet(urlString);
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
						if (address.has("building")) {
							name = address.getString("building") + ", ";
						} else if (address.has("memorial")) {
							name = address.getString("memorial") + ", ";
						}
						if (address.has("pedestrian")) {
							name += address.getString("pedestrian");
						} else if (address.has("footway")) {
							name += address.getString("footway");
						} else if (address.has("cycleway")) {
							name += address.getString("cycleway");
						} else if (address.has("road")) {
							name += address.getString("road");
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
						if (name.endsWith(", ")) {
							name = name.substring(0, name.length() - 2);
						}
					}
				}
				if (name != "") {
					loc.setName(name);
					RouteController.getInstance().notifyAllRouteListeners();
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

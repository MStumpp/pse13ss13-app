package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.wikipedia;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * This class
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class WikipediaPreprocessor {

	public static void preprocessWikipediaInformation(
			LocationDataIO locationData) throws IOException {
		for (Iterator<POI> iter = locationData.getPoiList().iterator(); iter
				.hasNext();) {
			POI current = iter.next();
			/*
			 * URL url = new URL(current.getWikipeidaURL()); HttpURLConnection
			 * connection = (HttpURLConnection) url .openConnection();
			 * connection.connect(); InputStream input =
			 * connection.getInputStream(); // TODO: implement information
			 * fetching
			 */
			String textInfo = null;
			current.setTextInfo(textInfo);
		}
	}
}

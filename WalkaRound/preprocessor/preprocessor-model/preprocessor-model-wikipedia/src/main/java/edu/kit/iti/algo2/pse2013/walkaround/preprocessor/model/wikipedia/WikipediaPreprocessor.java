package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.wikipedia;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * This class
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class WikipediaPreprocessor {

	/**
	 * Enhances all POIs within the location data file with textual informations
	 * and a link to an image if available.
	 * 
	 * @param locationData
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public static void preprocessWikipediaInformation(
			LocationDataIO locationData) throws IOException, XMLStreamException {
		for (Iterator<POI> iter = locationData.getPOIs().iterator(); iter
				.hasNext();) {
			POI current = iter.next();

			/*Text information parsing.*/

			// Prepare wikipedia url
			String wikipediaURL = current.getWikipediaURL();
			String partA = wikipediaURL.substring(0,
					wikipediaURL.lastIndexOf("/"));
			String partB = wikipediaURL
					.substring(wikipediaURL.lastIndexOf("/"));
			wikipediaURL = partA + "/Spezial:Exportieren" + partB;
			URL url = new URL(wikipediaURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.connect();
			InputStream input = connection.getInputStream();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader parser = factory.createXMLStreamReader(input);

			// Parse
			String textInfo = "";
			while (parser.hasNext()) {
				if (parser.getEventType() == XMLStreamConstants.START_ELEMENT
						&& parser.getLocalName().equals("text")) {
					parser.next();
					while (parser.getEventType() == XMLStreamConstants.CHARACTERS
							&& !parser.getText().endsWith("==")) {
						// TODO: Einschränkung verbessern da möglicherweise zu viel unnötiger text mit genommen wird!
						textInfo += parser.getText();
						parser.next();
					}
					textInfo += parser.getText();
				}
				parser.next();
			}
			current.setTextInfo(textInfo);
		}
	}
}

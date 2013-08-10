package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.wikipedia;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * This class
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public class WikipediaPreprocessor {

	public static void main(String[] args) throws MalformedURLException {
		LocationDataIO locData = new LocationDataIO();
		locData.addPOI(new POI(new Location(5, 5, "LocName"), "Text-Info", new URL("http://de.wikipedia.org/w/index.php?title=Karlsruhe&printable=yes"), null));
		preprocessWikipediaInformation(locData);
	}
	/**
	 * Enhances all POIs within the location data file with textual informations
	 * and a link to an image if available.
	 *
	 * @param locationData
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public static void preprocessWikipediaInformation(LocationDataIO locationData) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setFeature("http://xml.org/sax/features/validation", false);
			SAXParser parser = factory.newSAXParser();

			Iterator<POI> iter = locationData.getPOIs().iterator();
			while (iter.hasNext()) {
				POI current = iter.next();
				if (current.getURL() != null) {
					try {
						URL url = current.getURL();
						URLConnection connection;
						connection = url.openConnection();
						connection.connect();
						InputSource input = new InputSource(connection.getInputStream());
						WikipediaHandler handler = new WikipediaHandler();

						parser.parse(input, handler);
						current.setTextInfo(handler.getFirstParagraphs());
						current.setURL(handler.getImageURL());
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXNotRecognizedException e) {
			e.printStackTrace();
		} catch (SAXNotSupportedException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
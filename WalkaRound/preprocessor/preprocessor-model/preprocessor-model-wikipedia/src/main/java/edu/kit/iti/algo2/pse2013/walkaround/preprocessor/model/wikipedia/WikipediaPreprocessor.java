package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.wikipedia;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * This class
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public class WikipediaPreprocessor {
	private static Logger logger = Logger.getLogger(WikipediaPreprocessor.class.getSimpleName());

	/**
	 * Enhances all POIs within the location data file with textual informations
	 * and a link to an image if available.
	 *
	 * @param locationData
	 */
	public static void preprocessWikipediaInformation(LocationDataIO locationData) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setFeature("http://xml.org/sax/features/validation", false);
			factory.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);
			factory.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);
			SAXParser parser = factory.newSAXParser();

			Iterator<POI> iter = locationData.getPOIs().iterator();
			while (iter.hasNext()) {
				POI current = iter.next();
				if (current.getURL() != null) {
					try {
						System.out.print(current.getName());
						URL url = current.getURL();
						URLConnection connection = url.openConnection();
						connection.connect();
						InputSource input = new InputSource(connection.getInputStream());
						String lang = current.getURL().toExternalForm();
						if (lang.length() > 9 && lang.substring(7, 9).matches("[a-zA-Z]{2}")) {
							lang = lang.substring(7, 9);
						} else {
							lang = "de";
						}
						WikipediaHandler handler = new WikipediaHandler(lang);

						parser.parse(input, handler);
						current.setTextInfo(handler.getFirstParagraphs());
						current.setURL(handler.getImageURL());
						System.out.print(" ✓\n");
					} catch (SAXException e) {
						logger.info(String.format("Schwerwiegender Syntaxfehler in %s! Wird ignoriert.", current.getURL().toExternalForm()));
					} catch (IOException e) {
						logger.info(current.getURL().toExternalForm() + " konnte nicht gelesen werden! Wird ignoriert.");
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
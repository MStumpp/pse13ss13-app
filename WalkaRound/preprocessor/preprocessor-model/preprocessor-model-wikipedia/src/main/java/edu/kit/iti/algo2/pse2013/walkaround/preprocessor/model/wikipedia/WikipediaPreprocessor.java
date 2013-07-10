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

			/* Text information parsing. */

			// Prepare wikipedia url

			// hier getURL() nicht url vom image sondern zun�chst wikipedia url
			// wird aber w�hrend dem preprocessing als url vom image
			// �berschrieben...
			// k�nnte eleganter gel�st werden.
			String wikipediaURL = current.getURL();
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
			StringBuilder sb = new StringBuilder();
			while (parser.hasNext()) {
				if (parser.getEventType() == XMLStreamConstants.START_ELEMENT
						&& parser.getLocalName().equals("text")) {
					parser.next();
					while (parser.getEventType() == XMLStreamConstants.CHARACTERS
							&& !parser.getText().endsWith("==")) {
						// TODO: Einschr�nkung verbessern da m�glicherweise zu
						// viel unn�tiger text mitgenommen wird!
						sb.append(parser.getText());
						parser.next();
					}
					sb.append(parser.getText());
				}
				parser.next();
			}
			sb = sb.delete(sb.indexOf("=="), sb.length());
			while (sb.indexOf("<ref>") != -1) {
				sb = sb.delete(sb.indexOf("<ref>"), sb.indexOf("</ref>") + 6);
			}
			while (sb.indexOf("{") != -1) {
				sb = sb.delete(sb.indexOf("{"), sb.indexOf("}") + 1);
			}
			/*
			 * while (sb.indexOf("[[Datei:") != -1) { sb =
			 * sb.delete(sb.indexOf("[[Datei:"), sb.indexOf("]]") + 2); }
			 */

			String textInfo = sb.toString();
			textInfo = textInfo.replaceAll("\\'", "");
			textInfo = textInfo.replaceAll("\n\n", " ");
			/*
			 * textInfo = textInfo.replaceAll("\\[", ""); textInfo =
			 * textInfo.replaceAll("\\]", ""); textInfo =
			 * textInfo.replaceAll("\\#", ""); textInfo =
			 * textInfo.replaceAll("\\|", ""); textInfo =
			 * textInfo.replaceAll("\\}", ""); textInfo =
			 * textInfo.replaceAll("\\{", "");
			 */
			current.setTextInfo(textInfo);

			//Seitenquelltext(HTML) durchgehen suchen nach erstem auftreten von upload.wikimedia.org/wikipedia/commons/ (in <body> suchen
			//Link: http://commons.wikimedia.org/wiki/File:    +      was nach Datei: steht dabei werden " " zu "_"
			//Link mit view-source: anreichern
		}
	}
}

package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.wikipedia;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Scanner;

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
			LocationDataIO locationData) {
		for (Iterator<POI> iter = locationData.getPOIs().iterator(); iter
				.hasNext();) {
			POI current = iter.next();

			/* Text information parsing. */

			// Prepare wikipedia url

			// hier getURL() nicht url vom image sondern zun�chst wikipedia url
			// wird aber w�hrend dem preprocessing als url vom image
			// �berschrieben...
			// k�nnte eleganter gel�st werden.
			if (!(current.getURL() == null)) {
				try {
					String wikipediaURL = current.getURL();
					String partA = wikipediaURL.substring(0,
							wikipediaURL.lastIndexOf("/"));
					String partB = wikipediaURL.substring(wikipediaURL
							.lastIndexOf("/"));
					wikipediaURL = partA + "/Spezial:Exportieren" + partB;
					wikipediaURL = wikipediaURL.replaceAll(" ", "_");
					URL url;
					url = new URL(wikipediaURL);
					URLConnection connection;
					connection = url.openConnection();
					connection.connect();
					InputStream input = connection.getInputStream();
					XMLInputFactory factory = XMLInputFactory.newInstance();
					XMLStreamReader parser;
					parser = factory.createXMLStreamReader(input);

					// Parse
					StringBuilder sb = new StringBuilder();
					while (parser.hasNext()) {
						if (parser.getEventType() == XMLStreamConstants.START_ELEMENT
								&& parser.getLocalName().equals("text")) {
							parser.next();
							while (parser.getEventType() == XMLStreamConstants.CHARACTERS
									&& !parser.getText().endsWith("==")) {
								// TODO: Einschr�nkung verbessern da
								// m�glicherweise
								// zu
								// viel unn�tiger text mitgenommen wird!
								sb.append(parser.getText());
								parser.next();
							}
							sb.append(parser.getText());
						}
						parser.next();
					}
					input.close();

					// set image url
					if (sb.indexOf("[[Datei:") != -1) {
						try {
							String imageUrl = sb
									.substring(sb.indexOf("[[Datei:") + 8,
											sb.indexOf("|"));
							imageUrl = imageUrl.replaceAll(" ", "_");
							String trueImageUrl = "http://commons.wikimedia.org/wiki/File:"
									+ imageUrl;
							URL javaImageUrl = new URL(trueImageUrl);
							Scanner scanner = new Scanner(
									javaImageUrl.openStream());
							StringBuilder imageSb = new StringBuilder();
							while (scanner.hasNextLine()) {
								imageSb.append(scanner.nextLine());
							}
							scanner.close();
							if (imageSb
									.indexOf("upload.wikimedia.org/wikipedia/commons/") != -1) {
								imageSb = imageSb
										.delete(0,
												imageSb.indexOf("upload.wikimedia.org/wikipedia/commons/"));
								trueImageUrl = imageSb.substring(
										0,
										imageSb.indexOf(imageUrl)
												+ imageUrl.length());
								current.setURL(trueImageUrl);
							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (sb.indexOf("[[Bild:") != -1) {
						try {
							String imageUrl = sb.substring(
									sb.indexOf("[[Bild:") + 7, sb.indexOf("|"));
							imageUrl = imageUrl.replaceAll(" ", "_");
							String trueImageUrl = "http://commons.wikimedia.org/wiki/File:"
									+ imageUrl;
							URL javaImageUrl = new URL(trueImageUrl);
							Scanner scanner = new Scanner(
									javaImageUrl.openStream());
							StringBuilder imageSb = new StringBuilder();
							while (scanner.hasNextLine()) {
								imageSb.append(scanner.nextLine());
							}
							scanner.close();
							if (imageSb
									.indexOf("upload.wikimedia.org/wikipedia/commons/") != -1) {
								imageSb = imageSb
										.delete(0,
												imageSb.indexOf("upload.wikimedia.org/wikipedia/commons/"));
								trueImageUrl = imageSb.substring(
										0,
										imageSb.indexOf(imageUrl)
												+ imageUrl.length());
								current.setURL(trueImageUrl);
							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					// continue parsing the text
					sb = sb.delete(sb.indexOf("=="), sb.length());

					while (sb.indexOf("<ref>") != -1) {
						sb = sb.delete(sb.indexOf("<ref>"),
								sb.indexOf("</ref>") + 6);
					}
					while (sb.indexOf("{{") != -1) {
						sb = sb.delete(sb.indexOf("{{"), sb.indexOf("}}") + 1);
					}
					while (sb.indexOf("[[Datei:") != -1) {
						int counter = 1;
						int beginChars = sb.indexOf("[[Datei:") + 8;
						int endChars = beginChars + 2;
						while (counter != 0) {
							if (sb.substring(beginChars, endChars).contains(
									"[[")) {
								counter += 1;
							}
							if (sb.substring(beginChars, endChars).equals("]]")) {
								counter -= 1;
								if (counter == 0) {
									sb.delete(sb.indexOf("[[Datei:"), endChars);
								}
							}
							beginChars += 1;
							endChars += 1;
						}
					}

					while (sb.indexOf("[[Bild:") != -1) {
						int counter = 1;
						int beginChars = sb.indexOf("[[Bild:") + 7;
						int endChars = beginChars + 2;
						while (counter != 0) {
							if (sb.substring(beginChars, endChars).contains(
									"[[")) {
								counter += 1;
							}
							if (sb.substring(beginChars, endChars).equals("]]")) {
								counter -= 1;
								if (counter == 0) {
									sb.delete(sb.indexOf("[Bild:"), endChars);
								}
							}
							beginChars += 1;
							endChars += 1;
						}
					}

					while (sb.indexOf("<!--") != -1) {
						sb.delete(sb.indexOf("!--"), sb.indexOf("-->"));
					}

					while (sb.indexOf("<br />") != -1) {
						sb.delete(sb.indexOf("<br />"), 6);
					}

					while (sb.indexOf("|") != -1) {
						int endChar = sb.indexOf("|") + 2;
						while (!sb.substring(endChar - 2, endChar).equals("]]")) {
							endChar += 1;
						}
						sb.delete(sb.indexOf("|"), endChar);
					}

					String textInfo = sb.toString();
					textInfo = textInfo.replaceAll("\\'", "");
					textInfo = textInfo.replaceAll("\n\n", " ");

					textInfo = textInfo.replaceAll("\\[", "");
					textInfo = textInfo.replaceAll("\\]", "");
					// textInfo = textInfo.replaceAll("\\#", "");
					// textInfo = textInfo.replaceAll("\\}", "");
					// textInfo = textInfo.replaceAll("\\{", "");

					textInfo = textInfo.trim();
					current.setTextInfo(textInfo);

					// Seitenquelltext(HTML) durchgehen suchen nach erstem
					// auftreten
					// von
					// upload.wikimedia.org/wikipedia/commons/ (in <body> suchen
					// Link: http://commons.wikimedia.org/wiki/File: + was nach
					// Datei:
					// steht dabei werden " " zu "_"
					// Link mit view-source: anreichern
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XMLStreamException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IndexOutOfBoundsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
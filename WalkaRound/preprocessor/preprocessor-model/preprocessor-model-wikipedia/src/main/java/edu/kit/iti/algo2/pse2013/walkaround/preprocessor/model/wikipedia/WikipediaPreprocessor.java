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
		for (Iterator<POI> iter = locationData.getPOIs().iterator();iter.hasNext();) {
			POI current = iter.next();

			/* Text information parsing. */

			// Prepare wikipedia url

			// hier getURL() nicht url vom image sondern zunächst wikipedia url
			// wird aber während dem preprocessing als url vom image
			// überschrieben...
			// könnte eleganter gelöst werden.
			if (!(current.getURL() == null)) {
				try {
					String wikipediaURL = current.getURL();
					wikipediaURL = wikipediaURL.replaceAll(" ", "_");
					String partA = wikipediaURL.substring(0,
							wikipediaURL.lastIndexOf("/"));
					String partB = wikipediaURL.substring(wikipediaURL
							.lastIndexOf("/"));

					// TODO: auf länderprüfen (iso-standard) Datei: und Bild:
					// auf jeweilige sprache übersetzen!!!
					if (partA.contains("/de.")) {
						wikipediaURL = partA + "/Spezial:Exportieren" + partB;
					} else if (partA.contains("/en.")) {
						wikipediaURL = partA + "/Special:Export" + partB;
					}
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
									&& !parser.getText().contains("==")) {
								// TODO: Einschränkung verbessern da
								// möglicherweise
								// zu
								// viel unnötiger text mitgenommen wird!
								sb.append(parser.getText());
								parser.next();
							}
							sb.append(parser.getText());
						}
						parser.next();
					}
					input.close();

					while (sb.indexOf("{{") != -1) {
						sb = sb.delete(sb.indexOf("{{"),
								sb.indexOf("}}") + 2);
					}

					// set image url
					if (partA.contains("/de.")) {
						sb = setImageUrl(sb, current, "german");
					} else if (partA.contains("/en.")) {
						sb = setImageUrl(sb, current, "english");
					} else {
						sb = setImageUrl(sb, current, "no language");
					}

					// continue parsing the text
					if (sb.indexOf("==") != -1) {
						sb = sb.delete(sb.indexOf("=="), sb.length());

						while (sb.indexOf("<ref>") != -1) {
							sb = sb.delete(sb.indexOf("<ref>"),
									sb.indexOf("</ref>") + 6);
						}

						while (sb.indexOf("<!--") != -1) {
							sb.delete(sb.indexOf("!--"), sb.indexOf("-->"));
						}

						while (sb.indexOf("<br />") != -1) {
							sb.delete(sb.indexOf("<br />"), 6);
						}

						while (sb.indexOf("|") != -1) {
							int endChar = sb.indexOf("|") + 2;
							while (!sb.substring(endChar - 2, endChar).equals(
									"]]")) {
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
						// upload.wikimedia.org/wikipedia/commons/ (in <body>
						// suchen
						// Link: http://commons.wikimedia.org/wiki/File: + was
						// nach
						// Datei:
						// steht dabei werden " " zu "_"
						// Link mit view-source: anreichern
					}
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

	private static StringBuilder setImageUrl(StringBuilder sb, POI current,
			String language) {
		String substringA = null;
		String substringB = null;
		if (language.equals("german")) {
			substringA = "[[Datei:";
			substringB = "[[Bild:";
		} else if (language.equals("english")) {
			substringA = "[[File:";
			substringB = "[[Bild:";
		}

		try {
			if (sb.indexOf(substringA) != -1) {
				String imageUrl = sb.substring(sb.indexOf(substringA)
						+ substringA.length(), sb.indexOf("|"));
				imageUrl = imageUrl.replaceAll(" ", "_");
				String trueImageUrl = "http://commons.wikimedia.org/wiki/File:"
						+ imageUrl;
				URL javaImageUrl = new URL(trueImageUrl);
				Scanner scanner = new Scanner(javaImageUrl.openStream());
				StringBuilder imageSb = new StringBuilder();
				while (scanner.hasNextLine()) {
					imageSb.append(scanner.nextLine());
				}
				scanner.close();
				if (imageSb.indexOf("upload.wikimedia.org/wikipedia/commons/") != -1) {
					imageSb = imageSb
							.delete(0,
									imageSb.indexOf("upload.wikimedia.org/wikipedia/commons/"));
					trueImageUrl = imageSb.substring(0,
							imageSb.indexOf(imageUrl) + imageUrl.length());
					current.setURL(trueImageUrl);
				}

			} else if (sb.indexOf(substringB) != -1) {
				String imageUrl = sb.substring(sb.indexOf(substringB)
						+ substringA.length(), sb.indexOf("|"));
				imageUrl = imageUrl.replaceAll(" ", "_");
				String trueImageUrl = "http://commons.wikimedia.org/wiki/File:"
						+ imageUrl;
				URL javaImageUrl = new URL(trueImageUrl);
				Scanner scanner = new Scanner(javaImageUrl.openStream());
				StringBuilder imageSb = new StringBuilder();
				while (scanner.hasNextLine()) {
					imageSb.append(scanner.nextLine());
				}
				scanner.close();
				if (imageSb.indexOf("upload.wikimedia.org/wikipedia/commons/") != -1) {
					imageSb = imageSb
							.delete(0,
									imageSb.indexOf("upload.wikimedia.org/wikipedia/commons/"));
					trueImageUrl = imageSb.substring(0,
							imageSb.indexOf(imageUrl) + imageUrl.length());
					current.setURL(trueImageUrl);
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// delete substringA in textInfo
		while (sb.indexOf(substringA) != -1) {
			int counter = 1;
			int beginChars = sb.indexOf(substringA) + substringA.length();
			int endChars = beginChars + 2;
			while (counter != 0) {
				if (sb.substring(beginChars, endChars).contains("[[")) {
					counter += 1;
				}
				if (sb.substring(beginChars, endChars).equals("]]")) {
					counter -= 1;
					if (counter == 0) {
						sb.delete(sb.indexOf(substringA), endChars);
					}
				}
				beginChars += 1;
				endChars += 1;
			}
		}

		// delete substringB in TextInfo
		while (sb.indexOf(substringB) != -1) {
			int counter = 1;
			int beginChars = sb.indexOf(substringB) + substringA.length();
			int endChars = beginChars + 2;
			while (counter != 0) {
				if (sb.substring(beginChars, endChars).contains("[[")) {
					counter += 1;
				}
				if (sb.substring(beginChars, endChars).equals("]]")) {
					counter -= 1;
					if (counter == 0) {
						sb.delete(sb.indexOf(substringB), endChars);
					}
				}
				beginChars += 1;
				endChars += 1;
			}
		}
		return sb;
	}
}
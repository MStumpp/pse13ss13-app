package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.wikipedia;

import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.HandlerBase;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WikipediaHandler extends DefaultHandler {
	private static final int STATE_HEADER = 0;
	private static final int STATE_BODY = 1;
	private static final int STATE_INSIDE_PARAGRAPH = 2;
	private static final int STATE_FINISHED = 3;

	private static final int NUM_PARAGRAPHS_TO_COLLECT = 2;

	private int alreadyCollectedParagraphs = 0;
	private int state = STATE_HEADER;
	private String firstParagraphs = "";
	private int depthInParagraph = -1;
	private URL imageURL;

	@Override
	public void characters(char[] chars, int start, int length) throws SAXException {
		if (state == STATE_BODY && depthInParagraph >= 0) {
			firstParagraphs += new String(chars).substring(start, start + length);
		}
	}

	@Override
	public void endDocument() throws SAXException {}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (depthInParagraph >= 0) {
			depthInParagraph--;
			firstParagraphs += "</" + qName + ">";
			if (depthInParagraph < 0) {
				alreadyCollectedParagraphs++;
			}
		}
	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {}

	@Override
	public void processingInstruction(String arg0, String arg1) throws SAXException {}

	@Override
	public void setDocumentLocator(Locator arg0) {}

	@Override
	public void skippedEntity(String arg0) throws SAXException {}

	@Override
	public void startDocument() throws SAXException {
		System.out.println("Begin document");
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if (state == STATE_HEADER) {
			if ("body".equals(qName)) {
				state = STATE_BODY;
				System.out.println("State: body");
			}
		} else if (state == STATE_BODY) {
			if (imageURL == null && "img".equals(qName) && "thumbimage".equals(atts.getValue("class"))) {
				try {
					imageURL = new URL("https:" + atts.getValue("src"));
				} catch (MalformedURLException e) {
					// Malformed URL, try next image
				}
			}
			if (depthInParagraph >= 0 || ("p".equals(qName) && alreadyCollectedParagraphs < NUM_PARAGRAPHS_TO_COLLECT)) {
				depthInParagraph++;
				firstParagraphs += "<" + qName;
				for (int i = 0; i < atts.getLength(); i++) {
					firstParagraphs += " " + atts.getQName(i) + "=\"" + atts.getValue(i) + '"';
				}
				firstParagraphs += ">";
			}
		}
	}

	@Override
	public void startPrefixMapping(String arg0, String arg1)throws SAXException {}

	public String getFirstParagraphs() {
		return firstParagraphs;
	}

	public URL getImageURL() {
		return imageURL;
	}
}
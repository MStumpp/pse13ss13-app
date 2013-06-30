package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.wikipedia;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

public class WikipediaPreprocessorTest {

	@Test
	public void testTextFetsching() throws IOException, XMLStreamException {
		LocationDataIO locData = new LocationDataIO();
		locData.addPOI(new POI(0, 0, 4, "Test", null, "http://de.wikipedia.org/wiki/Hardtwald_(Karlsruhe)", null));
		
		WikipediaPreprocessor.preprocessWikipediaInformation(locData);
		
		System.out.println(locData.getPOIs().get(0).getTextInfo());
	}
}

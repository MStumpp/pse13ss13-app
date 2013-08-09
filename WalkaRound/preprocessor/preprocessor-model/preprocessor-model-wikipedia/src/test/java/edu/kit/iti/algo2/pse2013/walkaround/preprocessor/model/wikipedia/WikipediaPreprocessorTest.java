package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.wikipedia;

import java.io.IOException;
import java.net.URL;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

public class WikipediaPreprocessorTest {

	@Test
	public void testTextFetsching() throws IOException, XMLStreamException {
		LocationDataIO locData = new LocationDataIO();
		locData.addPOI(new POI(0, 0, "Test", null,
				new URL("http://en.wikipedia.org/w/index.php?title=Badisches_Staatstheater_Karlsruhe&printable=yes"), null));

		WikipediaPreprocessor.preprocessWikipediaInformation(locData);

		System.out.println(locData.getPOIs().get(0).getTextInfo());
		System.out.println(locData.getPOIs().get(0).getURL());
	}
}

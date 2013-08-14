package edu.kit.iti.algo2.pse2013.walkaround.test.client.model.data;

import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import android.test.AndroidTestCase;

public class POIManagerTest extends AndroidTestCase{

	public void testPersistence() {
		assertTrue(POIManager.getInstance().isEmpty());
		POIManager.getInstance().addActivePOICategory(1);
		assertFalse(POIManager.getInstance().isEmpty());
		POIManager.getInstance().removeActivePOICategory(1);
		assertTrue(POIManager.getInstance().isEmpty());
	}
	
	public void testSearchByQuery() {
		List<POI> suggestions = POIManager.getInstance().searchPOIsByQuery("Schloss Karlsruhe");
	}
}

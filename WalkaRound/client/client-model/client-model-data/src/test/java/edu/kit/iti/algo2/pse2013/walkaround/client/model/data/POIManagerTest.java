package edu.kit.iti.algo2.pse2013.walkaround.client.model.data;

import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;

public class POIManagerTest {

	POIManager poiManager;

	@Test
	public void testLevensthein() {
		int i = POIManager.computeLevenstheinDistance("xxxs" ,"xxxx");
		System.out.println(i);
	}
}

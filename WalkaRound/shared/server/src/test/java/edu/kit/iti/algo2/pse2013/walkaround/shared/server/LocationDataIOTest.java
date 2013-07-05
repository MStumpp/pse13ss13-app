package edu.kit.iti.algo2.pse2013.walkaround.shared.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * LocationDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class LocationDataIOTest {

    private static final String fileLocaton = System.getProperty("java.io.tmpdir") + File.separator + "locationDataIO";

    @Test
    public void testSandAndLoad() {
        LocationDataIO writeLocationData = getLocationDataIO();
        int size = writeLocationData.getPOIs().size();

        try {
			LocationDataIO.save(writeLocationData, new File(fileLocaton));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        File f = new File(fileLocaton);
        Assert.assertTrue(f.exists());

        LocationDataIO readLocationData = null;
        try {
			readLocationData = LocationDataIO.load(new File(fileLocaton));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Check, if something was read
        Assert.assertNotNull(readLocationData);
        // Check, if the same number of POI was written and read
        Assert.assertEquals(size, readLocationData.getPOIs().size());

        // Check, if the POIs are really the same
        List<POI> oldPOIs = writeLocationData.getPOIs();
        List<POI> newPOIs = readLocationData.getPOIs();
        for (int i = 0; i < size; i++) {
        	Assert.assertEquals(oldPOIs.get(i), newPOIs.get(i));
        }
    }


    private LocationDataIO getLocationDataIO() {

        LocationDataIO locationDataIO = new LocationDataIO();
        ArrayList<Integer> categories1 = new ArrayList<Integer>();
        categories1.add(1);
        ArrayList<Integer> categories2 = new ArrayList<Integer>();
        categories2.add(1);
        categories2.add(2);
        POI poi1 = new POI(1.d, 2.d, 1, "poi 1", "info 1", "url 1", categories1);
        POI poi2 = new POI(3.d, 4.d, 1, "poi 2", "info 2", "url 2", categories2);

        locationDataIO.addPOI(poi1);
        locationDataIO.addPOI(poi2);

        return locationDataIO;
    }

}

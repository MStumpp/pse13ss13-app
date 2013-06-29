package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * LocationDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class LocationDataIOTest {

    private static String fileLocaton = System.getProperty("java.io.tmpdir") + File.separator + "locationDataIO";

    @Test
    public void testSandAndLoad() {
        LocationDataIO locationDataIO = getLocationDataIO();
        int size = locationDataIO.getPOIs().size();

        try {
            LocationDataIO.save(locationDataIO, new File(fileLocaton));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f = new File(fileLocaton);
        Assert.assertTrue(f.exists());

        locationDataIO = null;
        try {
            locationDataIO = LocationDataIO.load(new File(fileLocaton));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(locationDataIO);
        Assert.assertEquals(locationDataIO.getPOIs().size(), size);
    }


    private LocationDataIO getLocationDataIO() {

        LocationDataIO locationDataIO = new LocationDataIO();
        POI poi1 = new POI(1.d, 2.d, 1, "poi 1", "info 1", "url 1", new int[]{1});
        POI poi2 = new POI(3.d, 4.d, 1, "poi 2", "info 2", "url 2", new int[]{2});

        locationDataIO.addPOI(poi1);
        locationDataIO.addPOI(poi2);

        return locationDataIO;
    }

}

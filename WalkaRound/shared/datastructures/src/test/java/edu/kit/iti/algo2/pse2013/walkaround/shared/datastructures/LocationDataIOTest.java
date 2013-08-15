package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.kit.iti.algo2.pse2013.walkaround.shared.FileUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

/**
 * LocationDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class LocationDataIOTest {

	private static final File TMP_LOCATIONDATA_FILE = new File(System.getProperty("java.io.tmpdir") + File.separator + "locationData.pbf");
	private static final File REAL_LOCATIONDATA_FILE = FileUtil.getFile("locationData.pbf");

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field idCounter = Vertex.class.getDeclaredField("idCounter");
        idCounter.setAccessible(true);
        idCounter.setInt(null, 0);
    }


	@Test
    public void testSaveAndLoad() throws MalformedURLException {
		LocationDataIO writeLocationData = getLocationDataIO();
		int size = writeLocationData.getPOIs().size();
		try {
			LocationDataIO.save(writeLocationData, TMP_LOCATIONDATA_FILE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Assert.assertTrue(TMP_LOCATIONDATA_FILE.exists());

		LocationDataIO readLocationData = null;
		try {
			readLocationData = LocationDataIO.load(TMP_LOCATIONDATA_FILE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
			Assert.assertEquals(oldPOIs.get(i).getLatitude(), newPOIs.get(i).getLatitude(), 0.d);
			Assert.assertEquals(oldPOIs.get(i).getLongitude(), newPOIs.get(i).getLongitude(), 0.d);
		}
	}


    @Test
    public void testSaveAndLoadWithRealDataSet() throws IOException {
        Assert.assertNotNull(REAL_LOCATIONDATA_FILE);
        Assert.assertTrue(REAL_LOCATIONDATA_FILE.exists());

        LocationDataIO readLocationData = null;
        readLocationData = LocationDataIO.load(REAL_LOCATIONDATA_FILE);

        // Check, if something was read
        Assert.assertNotNull(readLocationData);

        Set<Integer> ids = new TreeSet<Integer>();
        for (POI p : readLocationData.getPOIs()) {
        	Assert.assertTrue(ids.add(p.getId()));
        }

        LocationDataIO.save(readLocationData, REAL_LOCATIONDATA_FILE);
    }


	private LocationDataIO getLocationDataIO() throws MalformedURLException {

		LocationDataIO locationDataIO = new LocationDataIO();

		POI poi1 = new POI(1.d, 2.d, "poi 1", "info 1", new URL("https://de.wikipedia.org/w/index.php?printable=yes&title=Wikipedia"), new int[] { 0, 1 });
		POI poi2 = new POI(3.d, 4.d, "poi 2", "info 2", new URL("https://de.wikipedia.org/w/index.php?printable=yes&title=KIT"), new int[] { 0, 1 });

		locationDataIO.addPOI(poi1);
		locationDataIO.addPOI(poi2);

		return locationDataIO;
	}

}

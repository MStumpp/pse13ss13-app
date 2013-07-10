package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Graph;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * LocationDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class LocationDataIOTest {

	private static final String fileLocation = System
			.getProperty("java.io.tmpdir") + File.separator + "locationDataIO";


    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = Graph.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        Field idCounter = Vertex.class.getDeclaredField("idCounter");
        idCounter.setAccessible(true);
        idCounter.setInt(null, 0);
    }


	@Test
    @Ignore
	public void testSandAndLoad() {
//		LocationDataIO writeLocationData = getLocationDataIO();
//		int size = writeLocationData.getPOIs().size();
//
//		try {
//			FileOutputStream fos = new FileOutputStream(fileLocation);
//			ProtobufConverter.getLocationDataBuilder(writeLocationData).build().writeTo(fos);
//			fos.flush();
//			fos.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		File f = new File(fileLocation);
//		Assert.assertTrue(f.exists());
//
//		LocationDataIO readLocationData = null;
//		try {
//			FileInputStream fis = new FileInputStream(fileLocation);
//			readLocationData = ProtobufConverter.getLocationData(SaveLocationData.parseFrom(fis));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		// Check, if something was read
//		Assert.assertNotNull(readLocationData);
//		// Check, if the same number of POI was written and read
//		Assert.assertEquals(size, readLocationData.getPOIs().size());
//
//		// Check, if the POIs are really the same
//		List<POI> oldPOIs = writeLocationData.getPOIs();
//		List<POI> newPOIs = readLocationData.getPOIs();
//		for (int i = 0; i < size; i++) {
//			Assert.assertEquals(oldPOIs.get(i).getLatitude(), newPOIs.get(i)
//					.getLatitude(), 0.d);
//			Assert.assertEquals(oldPOIs.get(i).getLongitude(), newPOIs.get(i)
//					.getLongitude(), 0.d);
//		}
	}

	private LocationDataIO getLocationDataIO() {

		LocationDataIO locationDataIO = new LocationDataIO();

		POI poi1 = new POI(1.d, 2.d, "poi 1", "info 1", "url 1", new int[] { 0, 1 });
		POI poi2 = new POI(3.d, 4.d, "poi 2", "info 2", "url 2", new int[] { 0, 1 });

		locationDataIO.addPOI(poi1);
		locationDataIO.addPOI(poi2);

		return locationDataIO;
	}

}

import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.POIImageFetcher;

public class POIImageFetcherTest {

	@Test
	public void testImageFetcher() {
		Bitmap bitmap = POIImageFetcher
				.fetchImage("http://commons.wikimedia.org/wiki/File:Gray_vacuum_cleaner.svg");
		if (bitmap == null) {
			System.out.println("FAIL");
		}
	}
}

package edu.kit.iti.algo2.pse2013.walkaround;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.graphics.Bitmap;
import edu.kit.iti.algo2.pse2013.walkaround.client.BootActivity;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.POIImageFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.POIImageListener;

@RunWith(RobolectricTestRunner.class)
public class Test {
	private Bitmap bitmap;

    @org.junit.Test
    public void shouldHaveHappySmiles() throws Exception {
        String hello = Robolectric.buildActivity(BootActivity.class).get().getResources().getString(R.string.address);
        assertThat(hello, equalTo("Address"));
    }

    @org.junit.Test
    public void test2() throws MalformedURLException, InterruptedException {
    	POIImageListener list = new POIImageListener() {

			@Override
			public void setImage(Bitmap b) {
				bitmap = b;

			}
		};
    	POIImageFetcher fetch = new POIImageFetcher(new URL("http://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Karlsruher_Schloss_Front_Panorama.jpg/220px-Karlsruher_Schloss_Front_Panorama.jpg"), list);
    	Thread t = new Thread(fetch);
    	t.start();
    	t.join();
    	Assert.assertTrue(bitmap != null);
    }
}

package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 
 * @author Thomas Kadow
 * @version 1.0
 *
 */
public final class POIImageFetcher implements Runnable {

	private static final String TAG_POIIMAGEFETCHER = POIImageFetcher.class.getSimpleName();
	private POIImageListener listener;
	private URL url;
	private Bitmap b;

	public POIImageFetcher(URL url, POIImageListener listener) {
		this.url = url;
		this.listener = listener;
	}

	public void run() {
		Thread t = new Thread() {
			public void run() {
				try {
					URLConnection connection = url.openConnection();
					connection.connect();
					InputStream input = connection.getInputStream();
					b = BitmapFactory.decodeStream(input);
				} catch (IOException e) {
					Log.e(TAG_POIIMAGEFETCHER, e.toString());
				}
			}
		};
		t.start();
		try {
			t.join();
			listener.setImage(b);
		} catch (InterruptedException e) {
			Log.e(TAG_POIIMAGEFETCHER, "Multithreading failed", e);
		}
	}
	
	public interface POIImageListener {
		public void setImage(Bitmap b);
	}

}

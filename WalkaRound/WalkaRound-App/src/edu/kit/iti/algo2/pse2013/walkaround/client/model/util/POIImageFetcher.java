package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 *
 * @author Thomas Kadow
 * @version 1.0
 *
 */
public final class POIImageFetcher implements Runnable {

	private static final String TAG_POIIMAGEFETCHER = POIImageFetcher.class.getSimpleName();
	private POIBitmapListener bmpListener;
	private URL url;
	private Bitmap bmp;

	public POIImageFetcher(URL url, POIBitmapListener listener) {
		this.url = url;
		this.bmpListener = listener;
	}

	public void run() {
		Thread t = new Thread() {
			public void run() {
				try {
					URLConnection connection = url.openConnection();
					connection.connect();
					InputStream input = connection.getInputStream();
					bmp = BitmapFactory.decodeStream(input);
				} catch (IOException e) {
					Log.e(TAG_POIIMAGEFETCHER, e.toString());
				}
			}
		};
		t.start();
		try {
			t.join();
			bmpListener.setBitmap(bmp);
		} catch (InterruptedException e) {
			Log.e(TAG_POIIMAGEFETCHER, "Multithreading failed", e);
		}
	}

	public interface POIBitmapListener {
		public void setBitmap(Bitmap b);
	}
	public static class Synchronous implements POIBitmapListener, Runnable {
		private URL url;
		private Bitmap bitmap;
		public Synchronous(URL url) {
			this.url = url;
		}
		public void run() {
			POIImageFetcher fetcher = new POIImageFetcher(url, this);
		}
		@Override
		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}
		public Bitmap getBitmap() throws InterruptedException {
			Thread t = new Thread(this);
			t.start();
			t.join();
			return bitmap;
		}
	}
}
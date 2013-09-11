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

	private static final String TAG = POIImageFetcher.class.getSimpleName();
	private POIBitmapListener bmpListener;
	private URL url;
	private Bitmap bmp;

	public POIImageFetcher(URL url, POIBitmapListener listener) {
		this.url = url;
		this.bmpListener = listener;
	}

	public void run() {
		try {
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream input = connection.getInputStream();
			bmp = BitmapFactory.decodeStream(input);
			Log.d(TAG, "Read POI-Bitmap from " + url.toExternalForm());
			Log.d(TAG, "Finished reading of " + bmp);
			if (bmpListener != null) {
				bmpListener.setBitmap(bmp);
			}
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}

	public Bitmap getBitmap() {
		return bmp;
	}

	public interface POIBitmapListener {
		public void setBitmap(Bitmap b);
	}
}
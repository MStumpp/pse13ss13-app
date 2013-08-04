package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * Testfälle kommen in {@code client-view}, da hier Android-Klassen verwendet werden.
 *
 * Diese Klasse implementiert einen Downloader für Karten-Kacheln, der eine Tile-Cache mit LRU-Ersetzungsstrategie
 * verwendet.
 * @author Florian Sch&auml;fer
 */
public class TileFetcher {
	private static final String TAG = TileFetcher.class.getSimpleName();
	private static final int MAX_CACHE_SIZE = 500;
	private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(MAX_CACHE_SIZE);
	private FetchingQueue currentRunnable = new FetchingQueue();
	private Thread currentThread = new Thread(currentRunnable);

	/**
	 * Downloads all tiles that are located inside the rectangular area which has the following parameters:
	 * <ul>
	 * <li>A geospatial coordinate {@code topLeft} from inside the upper- and leftmost tile in the requested rectangle</li>
	 * <li>{@code numTilesX} columns of tiles</li>
	 * <li>{@code numTilesY} rows of tiles</li>
	 * </ul>
	 *
	 * @param levelOfDetail the level of detail for all the downloaded tiles
	 * @param topLeft a coordinate, which lies inside the tile which marks the upper left corner of the requested rectangle
	 * @param numTilesX the number of tile-columns in the requested rectangle
	 * @param numTilesY the number of tile-rows in the requested rectangle
	 */
	public void requestTiles(final int levelOfDetail, final Coordinate topLeft, final int numTilesX, final int numTilesY, TileListener listener) {
		Log.d(TAG, String.format("TileFetcher.requestTiles(%d, [%.4f|%.4f], %d, %d, %s)", levelOfDetail, topLeft.getLatitude(), topLeft.getLongitude(), numTilesX, numTilesY, listener));

		Log.d(TAG, "Convert GeoCoordinates into Tile-Indices.");
		final int[] startTileIndex = TileUtility.getXYTileIndex(topLeft, levelOfDetail);
		Log.d(TAG, String.format("x: %d columns from %d on\ny: %d rows from %d on", numTilesX, startTileIndex[0], numTilesY, startTileIndex[1]));

		requestTiles(levelOfDetail, startTileIndex[0], startTileIndex[1], startTileIndex[0] + numTilesX, startTileIndex[1] + numTilesY, listener);
		requestTiles(levelOfDetail, startTileIndex[0] - 1, startTileIndex[1] - 1, startTileIndex[0] + numTilesX + 1, startTileIndex[1] + numTilesY + 1, null);
	}

	public void requestTiles(final int levelOfDetail, final int minX, final int minY, final int maxX, final int maxY, TileListener listener) {
		Log.d(TAG, String.format("TileFetcher.requestTiles(%d, %d, %d, %d, %d, %s)", levelOfDetail, minX, minY, maxX, maxY, listener));

		int minLevelOfDetail = CurrentMapStyleModel.getInstance().getCurrentMapStyle().getMinLevelOfDetail();
		int maxLevelOfDetail = CurrentMapStyleModel.getInstance().getCurrentMapStyle().getMaxLevelOfDetail();

		Log.d(TAG, String.format("Check for valid LoD (between %d and %d).", minLevelOfDetail, maxLevelOfDetail));
		if (maxLevelOfDetail < levelOfDetail || minLevelOfDetail > levelOfDetail) {
			Log.d(TAG, "LoD invalid! => Exiting => not sending back any tiles.");
			throw new IllegalArgumentException("Level of detail is invalid!");
		}
		Log.d(TAG, "LoD valid!");
		if (listener != null) {
			currentRunnable.clearQueue();
			currentRunnable.setListener(listener);
			Log.d(TAG, "Cleared fetching-queue");
		}


		int tileGridWith = (int) Math.pow(2, levelOfDetail);

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				x = x % tileGridWith;
				y = y % tileGridWith;

				final String urlString = String.format(CurrentMapStyleModel.getInstance().getCurrentMapStyle().getTileURL(), x, y, levelOfDetail);
				Bitmap bmpFromCache;

				if ((bmpFromCache = cache.get(urlString)) != null) {
					Log.d(TAG, String.format("Fetched tile from cache: %s (%s/%s/%s.png)", bmpFromCache, levelOfDetail, x, y));
					if (listener != null) {
						listener.receiveTile(bmpFromCache, x, y, levelOfDetail);
					}
				} else {
					currentRunnable.offerQueue(urlString, x, y, levelOfDetail);
					if (!currentThread.isAlive()) {
						currentThread = new Thread(currentRunnable);
						currentThread.start();
					}
				}
			}
		}
	}


	private class FetchingQueue implements Runnable {
		private Queue<String> urlList = new LinkedList<String>();
		private Queue<Integer> xList = new LinkedList<Integer>();
		private Queue<Integer> yList = new LinkedList<Integer>();
		private Queue<Integer> levelOfDetailList = new LinkedList<Integer>();
		private TileListener listener;

		public FetchingQueue() { }

		public void setListener(TileListener listener) {
			this.listener = listener;
		}

		public void offerQueue(String url, int x, int y, int levelOfDetail) {
			urlList.offer(url);
			xList.offer(x);
			yList.offer(y);
			levelOfDetailList.offer(levelOfDetail);
		}
		public void clearQueue() {
			xList.clear();
			yList.clear();
			urlList.clear();
			levelOfDetailList.clear();
		}

		@Override
		public void run() {
			while(xList.size() > 0) {
				int x = xList.poll();
				int y = yList.poll();
				String urlString = urlList.poll();
				int levelOfDetail = levelOfDetailList.poll();

				try {
					URLConnection conn = new URL(urlString).openConnection();
					conn.connect();
					InputStream inStream = conn.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(inStream);
					Bitmap result = BitmapFactory.decodeStream(bis);
					bis.close();
					inStream.close();
					Log.d(TAG, String.format("Send to TileListener: %s (%s/%s/%s.png)", result, levelOfDetail, x, y));
					listener.receiveTile(result, x, y, levelOfDetail);
					cache.put(urlString, result);
				} catch (MalformedURLException e) {
					Log.e(TAG, String.format("Could not fetch tile %d/%d/%d.png. The URL '%s' is malformed!", levelOfDetail, x, y, urlString));
					Log.e(TAG, e.getLocalizedMessage());
				} catch (IOException e) {
					Log.e(TAG, String.format("Could not fetch tile %d/%d/%d.png. IOException while reading from '%s'!", levelOfDetail, x, y, urlString));
					Log.e(TAG, e.getLocalizedMessage());
				} catch (OutOfMemoryError e) {
					Log.e(TAG, "Out of Memory! Clearing cache...");
					Log.e(TAG, e.toString());
					cache.evictAll();
					System.gc();
				} catch (NullPointerException e){
					Log.e(TAG, "Try to cache Null Pointer!");
					Log.e(TAG, e.toString());
				}
			}
		}
	}
}
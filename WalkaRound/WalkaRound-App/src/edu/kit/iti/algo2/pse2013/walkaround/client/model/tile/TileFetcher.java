package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	private static final int MAX_CACHE_SIZE = 250;
	private TreeMap<String, TimestampedBitmap> cache = new TreeMap<String, TimestampedBitmap>();
	private FetchingQueue currentRunnable = new FetchingQueue();
	private Thread currentThread = new Thread(currentRunnable);

	private TileListener listener;

	public void setTileListener(TileListener listener) {
		this.listener = listener;
	}

	/**
	 * Downloads all tiles that are located inside the rectangular area which has the following parameters:
	 * <ul>
	 * <li>A geospatial coordinate {@code topLeft} from inside the upper- and leftmost tile in the requested rectangle</li>
	 * <li>{@code numTilesX} columns of tiles</li>
	 * <li>{@code numTilesY} rows of tiles</li>
	 * </ul>
	 *
	 * @param levelOfDetail the level of detail for all the downloaded tiles
	 * @param topLeft a coordinate, which lies inside the tile which markks the upper left corner of the requested rectangle
	 * @param numTilesX the number of tile-columns in the requested rectangle
	 * @param numTilesY the number of tile-rows in the requested rectangle
	 */
	public boolean requestTiles(final int levelOfDetail, final Coordinate topLeft, final int numTilesX, final int numTilesY) {
		Log.d(TAG, String.format("TileFetcher.requestTiles(%d, [%.4f|%.4f], %d, %d)", levelOfDetail, topLeft.getLatitude(), topLeft.getLongitude(), numTilesX, numTilesY));

		int minLevelOfDetail = CurrentMapStyleModel.getInstance().getCurrentMapStyle().getMinLevelOfDetail();
		int maxLevelOfDetail = CurrentMapStyleModel.getInstance().getCurrentMapStyle().getMaxLevelOfDetail();

		Log.d(TAG, String.format("Check for valid LoD (between %d and %d).", minLevelOfDetail, maxLevelOfDetail));
		if (maxLevelOfDetail < levelOfDetail || minLevelOfDetail > levelOfDetail) {
			Log.d(TAG, "LoD invalid! => Exiting => not sending back any tiles.");
			return false;
		}
		Log.d(TAG, "LoD valid!");
		currentRunnable.clearQueue();

		Log.d(TAG, "Convert GeoCoordinates into Tile-Indices.");
		final int[] startTileIndex = TileUtility.getXYTileIndex(topLeft, levelOfDetail);
		Log.d(TAG, String.format("x: %d columns from %d on\ny: %d rows from %d on", numTilesX, startTileIndex[0], numTilesY, startTileIndex[1]));

		int tileGridWith = (int) Math.pow(2, levelOfDetail);

		for (int xOffset = 0; xOffset < numTilesX; xOffset++) {
			for (int yOffset = 0; yOffset < numTilesY; yOffset++) {
				final int x = (startTileIndex[0] + xOffset) % tileGridWith;
				final int y = (startTileIndex[1] + yOffset) % tileGridWith;

				final String urlString = String.format(CurrentMapStyleModel.getInstance().getCurrentMapStyle().getTileURL(), x, y, levelOfDetail);
				if (cache.containsKey(urlString)) {
					Log.d(TAG, String.format("Fetched tile from cache: %s (%s/%s/%s.png)", cache.get(urlString).getBitmap(), levelOfDetail, x, y));
					listener.receiveTile(cache.get(urlString).getBitmap(), x, y, levelOfDetail);
					cache.get(urlString).updateTimestamp();
				} else {
					currentRunnable.offerQueue(urlString, x, y, levelOfDetail);
					if (!currentThread.isAlive()) {
						currentThread = new Thread(currentRunnable);
						currentThread.start();
					}
				}
			}
		}
		return true;
	}

	private class FetchingQueue implements Runnable {
		private Queue<String> urlList = new LinkedList<String>();
		private Queue<Integer> xList = new LinkedList<Integer>();
		private Queue<Integer> yList = new LinkedList<Integer>();
		private Queue<Integer> levelOfDetailList = new LinkedList<Integer>();
		public FetchingQueue() { }

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
				String url = urlList.poll();
				int levelOfDetail = levelOfDetailList.poll();

				try {
					InputStream inStream = new URL(url).openStream();
					BufferedInputStream bis = new BufferedInputStream(inStream);
					Bitmap result = BitmapFactory.decodeStream(bis);
					bis.close();
					inStream.close();
					Log.d(TAG, String.format("Send to TileListener: %s (%s/%s/%s.png)", result, levelOfDetail, x, y));
					listener.receiveTile(result, x, y, levelOfDetail);
					cache.put(url, new TimestampedBitmap(System.currentTimeMillis(), result));
					if (cache.size() > MAX_CACHE_SIZE) {
						cache.pollFirstEntry();
						cache.pollFirstEntry();
						System.gc();
					}
				} catch (MalformedURLException e) {
					Log.e(TAG, String.format("Could not fetch tile %d/%d/%d.png. The URL '%s' is malformed!", levelOfDetail, x, y, url));
					Log.e(TAG, e.getLocalizedMessage());
				} catch (IOException e) {
					Log.e(TAG, String.format("Could not fetch tile %d/%d/%d.png. IOException while reading from '%s'!", levelOfDetail, x, y, url));
					Log.e(TAG, e.getLocalizedMessage());
				} catch (OutOfMemoryError e) {
					Log.e(TAG, "Out of Memory!");
					Log.e(TAG, e.toString());
					cache.clear();
					System.gc();
				}
			}
		}
	}
}
package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * Testfälle kommen in {@code client-view}, da hier Android-Klassen verwendet werden.
 * @author Florian Sch&auml;fer
 */
public class TileFetcher {
	private static final String TAG = TileFetcher.class.getSimpleName();
	private TreeMap<String, TimestampedBitmap> cache = new TreeMap<String, TimestampedBitmap>();

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
		Log.d(TAG, String.format("TileFetcher.requestTiles(%d, [%.4f|%.4f], %d, %d)", levelOfDetail, topLeft.getLatitude(), topLeft.getLongtitude(), numTilesX, numTilesY));

		int minLevelOfDetail = CurrentMapStyleModel.getInstance().getCurrentMapStyle().getMinLevelOfDetail();
		int maxLevelOfDetail = CurrentMapStyleModel.getInstance().getCurrentMapStyle().getMaxLevelOfDetail();

		Log.d(TAG, String.format("Check for valid LoD (between %d and %d).", minLevelOfDetail, maxLevelOfDetail));
		if (maxLevelOfDetail < levelOfDetail || minLevelOfDetail > levelOfDetail) {
			Log.d(TAG, "LoD invalid! => Exiting => not sending back any tiles.");
			return false;
		}
		Log.d(TAG, "LoD valid!");

		Log.d(TAG, "Convert GeoCoordinates into Tile-Indices.");
		final int[] startTileIndex = TileUtility.getXYTileIndex(topLeft, levelOfDetail);

		Log.d(TAG, String.format("x: %d columns from %d on\ny: %d rows from %d on", numTilesX, startTileIndex[0], numTilesY, startTileIndex[1]));
		int tileGridWith = (int) Math.pow(2, levelOfDetail);
		for (int x = 0; x < numTilesX; x++) {
			for (int y = 0; y < numTilesY; y++) {
				Thread t = new Thread(new SingleTileFetcher((startTileIndex[0] + x) % tileGridWith, (startTileIndex[1] + y) % tileGridWith, levelOfDetail));
				t.start();
			}
		}

		return true;
	}

	private class SingleTileFetcher implements Runnable {

		private static final int MAX_CACHE_SIZE = 500;
		private int x;
		private int y;
		private int levelOfDetail;

		/**
		 * Initializes the Fetcher, which downloads a single tile and sends it to the tilelistener of the surrounding class.
		 *
		 * @param x the column index of the tile
		 * @param y	the row index of the tile
		 * @param levelOfDetail	the level of detail of the tile (aka zoomlevel)
		 * @throws MalformedURLException if the fetcher tried to fetch the tile from the server with a broken URL
		 * @throws IOException if an error occured while reading
		 */
		public SingleTileFetcher(int x, int y, int levelOfDetail) {
			this.x = x;
			this.y = y;
			this.levelOfDetail = levelOfDetail;
		}

		@Override
		public void run() {
			final String urlString = String.format(CurrentMapStyleModel.getInstance().getCurrentMapStyle().getTileURL(), x, y, levelOfDetail);
			try {
				if (cache.containsKey(urlString)) {
					Log.d(TAG, String.format("Fetched tile from cache: %s (%s/%s/%s.png)", cache.get(urlString).getBitmap(), levelOfDetail, x, y));
					listener.receiveTile(cache.get(urlString).getBitmap(), x, y, levelOfDetail);
				} else {
					BufferedInputStream bis = new BufferedInputStream(new URL(urlString).openStream());
					Bitmap result = BitmapFactory.decodeStream(bis);
					bis.close();
					Log.d(TAG, String.format("Send to TileListener: %s (%s/%s/%s.png)", result, levelOfDetail, x, y));
					listener.receiveTile(result, x, y, levelOfDetail);
					cache.put(urlString, new TimestampedBitmap(System.currentTimeMillis(), result));
					if (cache.size() > MAX_CACHE_SIZE) {
						cache.pollFirstEntry();
					}
				}
			} catch (MalformedURLException e) {
				Log.e(TAG, String.format("Could not fetch tile %d/%d/%d.png. The URL '%s' is malformed!", levelOfDetail, x, y, urlString));
				Log.e(TAG, e.getLocalizedMessage());
			} catch (IOException e) {
				Log.e(TAG, String.format("Could not fetch tile %d/%d/%d.png. IOException while reading from '%s'!", levelOfDetail, x, y, urlString));
				Log.e(TAG, e.getLocalizedMessage());
			}
		}
	}
}
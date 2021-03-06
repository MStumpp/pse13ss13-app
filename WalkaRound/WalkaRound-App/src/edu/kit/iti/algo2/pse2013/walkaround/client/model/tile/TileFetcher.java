package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.PreferenceUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * Testfälle kommen in {@code client-view}, da hier Android-Klassen verwendet werden.
 *
 * Diese Klasse implementiert einen Downloader für Karten-Kacheln, der eine Tile-Cache mit LRU-Ersetzungsstrategie
 * verwendet.
 * @author Florian Schäfer
 * @author Ludwig Biermann
 * @version 3.3
 */
public class TileFetcher implements OnSharedPreferenceChangeListener{

	private static TileFetcher instance;
	private static final String TAG = TileFetcher.class.getSimpleName();
	private static final int MAX_CACHE_SIZE = 300;
	private static int DEFAULT_TILE_PATH = R.drawable.default_tile;
	private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(MAX_CACHE_SIZE);
	private ThreadPoolExecutor tpe = new ThreadPoolExecutor(2, 3, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	private static Bitmap defaultTile;

	/**
	 * Returns the current TileFetcher or null;
	 *
	 * @return TileFetcher or null
	 */
	public static TileFetcher getInstance() {
		if(instance == null){
			Log.e(TAG, "please call getInstance(context) first");
		}
		return instance;
	}

	/**
	 * Returns the current TileFetcher
	 * @param context
	 * @return TileFetcher
	 */
	public static TileFetcher getInstance(Context context) {
		if(instance == null){
			instance = new TileFetcher(context);
		}
		return instance;
	}

	private TileFetcher(Context context){
		PreferenceUtility.getInstance().registerOnSharedPreferenceChangeListener(this);
		defaultTile = BitmapFactory.decodeResource(context.getResources(), DEFAULT_TILE_PATH);
		float width = CoordinateUtility
		.computeCurrentTileWidthInPixels(BoundingBox.getInstance()
				.getLevelOfDetail());
		defaultTile = Bitmap.createScaledBitmap(defaultTile,(int) width,(int) width, false);

	}

	public void requestTiles(BoundingBox coorBox, TileListener listener){
		this.requestTiles((int) coorBox.getLevelOfDetail(), coorBox.getScaledTopLeft(), coorBox.getScaledBottomRight(), listener);
	}

	public void requestTiles(BoundingBox coorBox, int levelOfDetail){
		this.requestTiles(levelOfDetail, coorBox.getScaledTopLeft(), coorBox.getScaledBottomRight(), null);
	}

	public void requestTiles(BoundingBox coorBox, int levelOfDetail,  TileListener listener){
		this.requestTiles(levelOfDetail, coorBox.getScaledTopLeft(), coorBox.getScaledBottomRight(), listener);
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
	 * @param topLeft a coordinate, which lies inside the tile which marks the upper left corner of the requested rectangle
	 * @param bottomRight the bottom right coordinate (analog to topLeft)
	 * @param listener the TileListener listeneing for tiles
	 */
	private void requestTiles(final int levelOfDetail, final Coordinate topLeft, final Coordinate bottomRight, TileListener listener) {
		Log.d(TAG, String.format("TileFetcher.requestTiles(%d, %s, %s, %s)", levelOfDetail, topLeft, bottomRight, listener));

		Log.d(TAG, "Convert GeoCoordinates into Tile-Indices.");
		final int[] startTileIndex = TileUtility.getXYTileIndex(topLeft, levelOfDetail);
		final int[] endTileIndex = TileUtility.getXYTileIndex(bottomRight, levelOfDetail);

		requestTiles(levelOfDetail, startTileIndex[0], startTileIndex[1] - 1, endTileIndex[0], endTileIndex[1], listener);
		requestTiles(levelOfDetail, startTileIndex[0] - 1, startTileIndex[1] - 2, endTileIndex[0] + 1, endTileIndex[1] + 1, null);
	}
	public void clearCache(){
		Log.d(TAG, "Clearing cache...");
		cache.evictAll();
	}

	private void requestTiles(final int levelOfDetail, final int minX, final int minY, final int maxX, final int maxY, TileListener listener) {
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
			tpe.getQueue().clear();
			Log.d(TAG, "Cleared fetching-queue");
		}


		int tileGridWidth = (int) Math.pow(2, levelOfDetail);

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				x = x % tileGridWidth;
				y = y % tileGridWidth;

				final String urlString = String.format(CurrentMapStyleModel.getInstance().getCurrentMapStyle().getTileURL(), x, y, levelOfDetail);
				Bitmap bmpFromCache;

				if ((bmpFromCache = cache.get(urlString)) != null) {
					Log.d(TAG, String.format("Fetched tile from cache: %s (%s/%s/%s.png)", bmpFromCache, levelOfDetail, x, y));
					if (listener != null) {
						listener.receiveTile(bmpFromCache, x, y, levelOfDetail);
					}
				} else {
					if (listener != null) {
						listener.receiveTile(defaultTile, x, y, levelOfDetail);
					}
					tpe.execute(new SingleFetcher(urlString, x, y, levelOfDetail, listener));
				}
			}
		}
	}


	private class SingleFetcher implements Runnable {
		private String url;
		private int x;
		private int y;
		private int levelOfDetail;
		private TileListener listener;

		public SingleFetcher(String url, int x, int y, int levelOfDetail, TileListener listener) {
			this.url = url;
			this.x = x;
			this.y = y;
			this.levelOfDetail = levelOfDetail;
			this.listener = listener;
		}

		@Override
		public void run() {
			try {
				URLConnection conn = new URL(url).openConnection();
				conn.connect();
				InputStream inStream = conn.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(inStream);
				Bitmap result = BitmapFactory.decodeStream(bis);
				bis.close();
				inStream.close();
				Log.d(TAG, String.format("Send to TileListener: %s (%s/%s/%s.png)", result, levelOfDetail, x, y));
				if (listener != null) {
					listener.receiveTile(result, x, y, levelOfDetail);
				}
				cache.put(url, result);
				Log.d(TAG, String.format("Cachesize: %d", cache.size()));
			} catch (MalformedURLException e) {
				Log.e(TAG, String.format("Could not fetch tile %d/%d/%d.png. The URL '%s' is malformed!", levelOfDetail, x, y, url), e);
			} catch (IOException e) {
				Log.e(TAG, String.format("Could not fetch tile %d/%d/%d.png. IOException while reading from '%s'!", levelOfDetail, x, y, url), e);
			} catch (OutOfMemoryError e) {
				Log.e(TAG, "Out of Memory! Clearing cache...", e);
				cache.evictAll();
				System.gc();
			} catch (NullPointerException e){
				Log.e(TAG, "Try to catch Null Pointer!", e);
			}
		}
	}

	public interface TileListener {
		/**
		 * ATM the tile comes as BufferedImage. That will be changed in future versions.
		 * @param tile
		 * @param x
		 * @param y
		 * @param levelOfDetail
		 */
		public void receiveTile(final Bitmap tile, final int x, final int y, final int levelOfDetail);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sh, String key) {
		if(PreferenceUtility.KEY_MAP_TYP.equals(key)){
			CurrentMapStyleModel.getInstance().setCurrentMapStyle(PreferenceUtility.getInstance().getMapStyle());
			this.clearCache();
			BoundingBox.getInstance().setLevelOfDetail(BoundingBox.getInstance().getLevelOfDetail());
		}
	}
}
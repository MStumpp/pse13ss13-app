package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

//import javax.imageio.ImageIO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.CurrentMapStyleModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

public class TileFetcher {
	private TileListener listener;

	public void setTileListener(TileListener listener) {
		this.listener = listener;
	}

	/**
	 * Downloads all tiles that are located inside the rectangle which has the
	 * following two points as corners:
	 * <ul>
	 * <li>The geospatial coordinate {@code c1}</li>
	 * <li>The geospatial coordinate {@code c2}<</li>
	 * </ul>
	 * The lower longtitude marks the western edge, the higher one the eastern
	 * edge. The lower latitude marks the southern edge, the higher one the
	 * northern edge. <br>
	 * As a consequence, no rectangle can have the 180th longtitude running
	 * straight through itself. Also the northpole and southpole can only be at
	 * the northern or southern edge, never in between.
	 *
	 * @param lat1
	 *            the latitude of point 1
	 * @param lon1
	 *            the longtitude of point 1
	 * @param lat2
	 *            the latitude of point 2
	 * @param lon2
	 *            the longtitude of point 2
	 * @param zoom
	 *            the zoom level of all the downloaded tiles
	 * @param source
	 */
	public boolean requestTiles(final int levelOfDetail, Coordinate c1,
			Coordinate c2) {
		if (CurrentMapStyleModel.getInstance().getCurrentMapStyle().getMaxLevelOfDetail() < levelOfDetail
				|| CurrentMapStyleModel.getInstance().getCurrentMapStyle().getMinLevelOfDetail() > levelOfDetail) {
			return false;
		}
		final int[] c1XY = TileUtility.getXYTileIndex(c1, levelOfDetail);
		final int[] c2XY = TileUtility.getXYTileIndex(c2, levelOfDetail);

		final int minX = Math.min(c1XY[0], c2XY[0]);
		final int maxX = Math.max(c1XY[0], c2XY[0]);
		final int minY = Math.min(c1XY[1], c2XY[1]);
		final int maxY = Math.max(c1XY[1], c2XY[1]);

		// TODO: Make asynchronous from here on
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				try { // TODO: Find good solution for exception-handling
					requestTile(x, y, levelOfDetail);
				} catch (MalformedURLException mue) {
					mue.printStackTrace();
				} catch (IOException mue) {
					mue.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * Downloads the tile specified by the given parameters
	 *
	 * @param source the source, where the tiles come from
	 * @param x the column index of the tile
	 * @param y	the row index of the tile
	 * @param zoom	the zoom level of the tile
	 * @throws MalformedURLException
	 * 		if the method tried to fetch the tile from the server with a broken URL
	 * @throws IOException	if an error occured while reading
	 */
	public void requestTile(final int x, final int y, final int levelOfDetail) throws MalformedURLException, IOException {
		final String urlString = String.format(CurrentMapStyleModel.getInstance().getCurrentMapStyle().getTileURL(), x, y, levelOfDetail);
		
		BufferedInputStream bis = new BufferedInputStream(new URL(urlString).openStream());
		
		final Bitmap bmp = BitmapFactory.decodeStream(bis);
		
		listener.receiveTile(bmp, x, y, levelOfDetail);
	}
}
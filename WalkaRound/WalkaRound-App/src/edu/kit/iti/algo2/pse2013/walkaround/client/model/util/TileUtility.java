package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * This class provides methods which are needed by the map model and the tile
 * fetcher.
 *
 * @author the team from OpenStreetMap-Wiki
 * @see <a
 *      href="http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Java">Slippy
 *      Map tilenames</a>
 */
public final class TileUtility {

	/**
	 * This class is an utility class which is not instantiated.
	 */
	private TileUtility() {

	}

	/**
	 * Calculates the x- and y-Part of the tile URL for a given Coordinate
	 * anywhere on that tile
	 *
	 * @param c
	 *            a geographical coordinate which should lie on the tile with
	 *            the returned indices
	 * @param levelOfDetail
	 *            the "tile zoom level" for which the calculation should be made
	 * @return an array of exactly length 2 containing the x- and y-part of the
	 *         map-tile URL (in that order)
	 */
	public static int[] getXYTileIndex(Coordinate c, int levelOfDetail) {
		return new int[] {
				(int) Math.floor((c.getLongitude() + 180) / 360
						* (1 << levelOfDetail)),
				(int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(c
						.getLatitude()))
						+ 1
						/ Math.cos(Math.toRadians(c.getLatitude()))) / Math.PI)
						/ 2 * (1 << levelOfDetail)) };
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param levelOfDetail
	 * @return
	 */
	public static Coordinate getUpperLeftCoordinate(int x, int y,
			int levelOfDetail) {
		return null;
	}
}
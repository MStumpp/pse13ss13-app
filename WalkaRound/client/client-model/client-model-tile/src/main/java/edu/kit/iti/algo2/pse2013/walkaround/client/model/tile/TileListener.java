package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

import java.awt.image.BufferedImage;

public interface TileListener {
	/**
	 * ATM the tile comes as BufferedImage. That will be changed in future versions.
	 * @param tile
	 * @param x
	 * @param y
	 * @param levelOfDetail
	 */
	public void receiveTile(final BufferedImage tile, final int x, final int y, final int levelOfDetail);
}
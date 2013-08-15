package edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

/**
 * Thos Class draw and compute the Path between two Points.
 *
 * @author Ludwig Biermann
 *
 */
public class RouteGen implements Runnable {

	/**
	 * Debug Tag
	 */
	public static String Tag_RouteGen = RouteGen.class.getSimpleName();

	/**
	 * default Background Empty
	 */
	// TODO some static content is double pls merge
	public static int defaultBackgroundEmpty = Color.argb(0, 0, 0, 0);

	/**
	 * the route bitmap
	 */
	public Bitmap routeImg;

	/**
	 * The stroke of the bitmap
	 */
	private int strokeWidth = 8;

	/**
	 * the List of lines
	 */
	private float[] displayCoords;

	/**
	 * creats a new Route Gen
	 *
	 * @param coords
	 *            the needed List of Lines
	 */
	public RouteGen(List<DisplayCoordinate> coords, Bitmap routeImg) {
		if (coords == null) {
			throw new IllegalArgumentException("The coordinates to paint on the screen must not be null");
		}
		displayCoords = new float[0];
		if (coords.size() > 0) {
			this.displayCoords = new float[(coords.size() - 1) * 4];
			for (int i = 0; i < coords.size() - 1; i++) {
				displayCoords[4 * i] = coords.get(i).getX();
				displayCoords[4 * i + 1] = coords.get(i).getY();
				displayCoords[4 * i + 2] = coords.get(i + 1).getX();
				displayCoords[4 * i + 3] = coords.get(i + 1).getY();
			}
		}
		this.routeImg = routeImg;
	}

	/**
	 * Draw a Line between two points.
	 *
	 * @param fromX
	 *            from x
	 * @param fromY
	 *            from y
	 * @param toX
	 *            to x
	 * @param toY
	 *            to y
	 */
	private void drawRouteLines() {

		if (!routeImg.isRecycled()) {
			Canvas canvas = new Canvas(routeImg);

			Paint pinsel = new Paint();
			pinsel.setColor(Color.argb(100, 64, 64, 255));
			pinsel.setStrokeWidth(this.strokeWidth);

			if (displayCoords.length >= 4) {
				Log.d(Tag_RouteGen, "ZEICHNE Route!");
				canvas.drawLines(displayCoords, pinsel);
			}

			MapController.getInstance().onRouteOverlayImageChange(routeImg);
		}

	}

	public void run() {
		Log.d(Tag_RouteGen, "create Route Bitmap");
		this.routeImg.eraseColor(defaultBackgroundEmpty);
		this.routeImg.prepareToDraw();

		this.drawRouteLines();
	}
}

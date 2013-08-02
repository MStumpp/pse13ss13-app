package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

public class RouteGen implements Runnable{

	/**
	 * 
	 */
	public static String Tag_RouteGen = RouteGen.class.getSimpleName();

	/**
	 * 
	 */
	public static int defaultBackgroundEmpty = Color.argb(0, 0, 0, 0);

	/**
	 * 
	 */
	public Bitmap route;

	/**
	 * 
	 */
	private int strokeWidth = 8;
	
	/**
	 * 
	 */
	List<DisplayCoordinate> lines;
	
	/**
	 * 
	 * @param lines
	 */
	public RouteGen(List<DisplayCoordinate> lines, Bitmap route) {
		this.lines = lines;
		this.route = route;
	}

	/**
	 * Draws the Route Overlay between DisplayWaypoints
	 *
	 * @param lines
	 *            a list of Points
	 */
	private void drawDisplayCoordinates() {
		if (!lines.isEmpty()) {
			Log.d("CANVAS_DRAW_LINE", "Länge" + lines.size());
			for (int a = 0; a < (lines.size() - 1); a++) {
				Log.d("CANVAS_DRAW_LINE", "Von" + a + " nach " + (a + 1));
				if (a + 1 < lines.size() && lines.get(a) != null && lines.get(a + 1) != null) {
					this.drawRouteLine(lines.get(a).getX(), lines.get(a).getY(), lines.get(a + 1).getX(), lines.get(a + 1).getY());
				}
			}
		}
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
	private void drawRouteLine(final float fromX, final float fromY,
			final float toX, final float toY) {

		if (!route.isRecycled()) {
			Canvas canvas = new Canvas(route);

			Paint pinsel = new Paint();
			pinsel.setColor(Color.rgb(64, 64, 255));
			pinsel.setStrokeWidth(this.strokeWidth);

			// if (fromX > 0 || fromY > 0 || toX > 0 || toY > 0) {
			// if (fromX < size.x || fromY < size.y || toX < size.x
			// || toY < size.y) {
			Log.d(Tag_RouteGen, "ZEICHNE!");
			canvas.drawLine(fromX, fromY + 22, toX, toY + 22, pinsel);
			// }
			// }

			MapController.getInstance().onRouteOverlayImageChange(route);
		}

	}

	@Override
	public void run() {
		Log.d(Tag_RouteGen, "create Route Bitmap");
		this.route.eraseColor(defaultBackgroundEmpty);
		this.route.prepareToDraw();

		this.drawDisplayCoordinates();
	}

}

package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

/**
 * This View draw the Route
 * 
 * @author Ludwig Biermann
 * @version 1.6
 *
 */
public class RouteView extends View {

	/**
	 * The stroke of the bitmap ungearde Zahl aus genauikeitsgründen
	 */
	private int strokeWidth = 8;
	private static String TAG = RouteView.class.getSimpleName();
	private final int FRAME_RATE = 25;
	private BoundingBox coorBox;
	Paint pinsel;
	private Handler h;
	private Runnable r = new Runnable() {
		@Override
		public void run() {
			invalidate();
		}
	};

	/**
	 * This create a new POIview.
	 * 
	 * @param context the context of the app
	 * @param attrs the needed attributes
	 */
	public RouteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		h = new Handler();

		pinsel = new Paint();
		pinsel.setColor(Color.argb(100, 64, 64, 255));
		pinsel.setStrokeWidth(this.strokeWidth);
		this.coorBox = BoundingBox.getInstance(context);
	}

	@Override
	protected void onDraw(Canvas c) {
		List<DisplayCoordinate> lines;
		synchronized (RouteController.getInstance().getCurrentRoute()) {
			lines = CoordinateUtility.extractDisplayCoordinatesOutOfRouteInfo(
					RouteController.getInstance().getCurrentRoute(),
					coorBox.getCenter(), coorBox.getDisplaySize(),
					this.coorBox.getLevelOfDetail());
		}
		if (!lines.isEmpty()) {

			float[] displayCoords = new float[(lines.size() - 1) * 4];
			for (int i = 0; i < lines.size() - 1; i++) {
				displayCoords[4 * i] = lines.get(i).getX();
				displayCoords[4 * i + 1] = lines.get(i).getY();
				displayCoords[4 * i + 2] = lines.get(i + 1).getX();
				displayCoords[4 * i + 3] = lines.get(i + 1).getY();
			}

			if (displayCoords.length >= 4) {
				Log.d(TAG, "ZEICHNE Route!");
				c.drawLines(displayCoords, pinsel);
			}

		}

		h.postDelayed(r, FRAME_RATE);
	}

}

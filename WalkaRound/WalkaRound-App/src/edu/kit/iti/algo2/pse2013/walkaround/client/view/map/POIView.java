package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayPOI;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * The View draw and handle the POI Markers
 *
 * @author Ludwig Biermann
 * @version 1.2
 *
 */
public class POIView extends RelativeLayout {

	private static final String TAG = POIView.class.getSimpleName();
	private BoundingBox coorBox;
	private static int poiDrawable = R.drawable.poi;

	private Set<POI> poiList;
	private List<POIInfoListener> info = new LinkedList<POIInfoListener>();
	
	/**
	 * This create a new POIview.
	 *
	 * @param context the context of the app
	 * @param attrs the needed attributes
	 */
	public POIView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.coorBox = BoundingBox.getInstance(context);
	}

	/**
	 * updates the POI View
	 */
	public void updatePOIView() {
		poiList = POIManager.getInstance(getContext()).getPOIsWithinRectangle(
				coorBox.getScaledTopLeft(), coorBox.getScaledBottomRight(),
				coorBox.getLevelOfDetail());

		Log.d(TAG, "POI Anzahl" + poiList.size());

		LinkedList<DisplayPOI> poi = new LinkedList<DisplayPOI>();

		for (POI value : poiList) {

			double lon = -coorBox.getCenter().getLongitude()
					+ value.getLongitude();
			double lat = -value.getLatitude()
					+ coorBox.getCenter().getLatitude();

			float x = CoordinateUtility.convertDegreesToPixels(lon,
					coorBox.getLevelOfDetail(),
					CoordinateUtility.DIRECTION_LONGITUDE);

			float y = CoordinateUtility.convertDegreesToPixels(lat,
					coorBox.getLevelOfDetail(),
					CoordinateUtility.DIRECTION_LATITUDE) * .755f;

			Log.d("UserPos", " x: " + x + " y: " + y);

			float scale = BoundingBox.getInstance().getScale();
			
			x = coorBox.getDisplaySize().x / 2 + x * scale;
			y = coorBox.getDisplaySize().y / 2 + y * scale;

			Log.d("UserPos", " x: " + x + " y: " + y);

			poi.add(new DisplayPOI(x, y, value.getId()));

		}

		this.removeAllViews();
		for (DisplayPOI p : poi) {
			RelativeLayout.LayoutParams paramsOption = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			paramsOption.width = coorBox.getDisplaySize().x / 10;
			paramsOption.height = coorBox.getDisplaySize().x / 10;

			ImageView iv = new ImageView(this.getContext());

			iv.setY(p.getY() - coorBox.getDisplaySize().x / 10);
			iv.setX(p.getX() - coorBox.getDisplaySize().x / 10 / 2);
			iv.setId(p.getId());
			iv.setVisibility(View.VISIBLE);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			iv.setImageResource(poiDrawable);
			iv.setOnTouchListener(new POICall());

			this.addView(iv, paramsOption);
		}
	}

	/**
	 * A Helper class witch is called on a Touch event
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 */
	public class POICall implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			int action = event.getAction();

			if (action == MotionEvent.ACTION_DOWN) {
				int id = view.getId();
				for (POI poi : poiList) {
					if (id == poi.getId()) {
						notifyPOIInfoListener(poi);
					}
				}
				return true;
			}

			return false;
		}

	}

	/**
	 * Notify all POI Listener
	 *
	 * @param poi the pressed poi
	 */
	private void notifyPOIInfoListener(POI poi) {
		for (POIInfoListener l : info) {
			l.callPoiInfo(poi);
		}
	}

	/**
	 * Register a POI Info Listener
	 *
	 * @param listener the new listener
	 */
	public void registerPOIInfoListener(POIInfoListener listener) {
		info.add(listener);
	}

	/**
	 * A POI Info Listener interface
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	public interface POIInfoListener {
		public void callPoiInfo(POI poi);
	}
}

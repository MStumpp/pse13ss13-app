package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayPOI;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

public class POIView extends RelativeLayout {

	private static final String TAG = POIView.class.getSimpleName();
	private BoundingBox coorBox;
	private static int poiDrawable = R.drawable.poi;

	public POIView(Context context, AttributeSet attrs) {
		super(context, attrs);

		coorBox = BoundingBox.getInstance(context);
	}

	public void updatePOIView() {
		List<POI> poiList = POIManager.getInstance(getContext())
				.getPOIsWithinRectangle(coorBox.getTopLeft(),
						coorBox.getBottomRight(), coorBox.getLevelOfDetail());

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
					CoordinateUtility.DIRECTION_LATITUDE);

			Log.d("UserPos", " x: " + x + " y: " + y);

			x = coorBox.getDisplaySize().x / 2 + x;
			y = coorBox.getDisplaySize().y / 2 + y;

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
			iv.setTag(p.getId());
			iv.setVisibility(View.VISIBLE);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			iv.setImageResource(poiDrawable);
			//iv.setOnTouchListener(new WaypointTouchListener(iv, dw.getId()));

			this.addView(iv, paramsOption);
		}
	}
}

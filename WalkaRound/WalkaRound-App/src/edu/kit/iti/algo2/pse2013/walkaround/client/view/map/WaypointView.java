package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class WaypointView extends RelativeLayout {

	public static final int DEFAULT_FLAG = R.drawable.flag;
	public static final int DEFAULT_FLAG_ACTIVE = R.drawable.flag_activ;
	public static final int DEFAULT_FLAG_TARGET = R.drawable.flag_target;
	public static final int DEFAULT_FLAG_TARGET_ACTIVE = R.drawable.flag_target_activ;

	public static final int DEFAULT_WAYPOINT = R.drawable.waypoint;
	public static final int DEFAULT_WAYPOINT_ACTIVE = R.drawable.waypoint_activ;

	private RouteInfo route;

	
	private BoundingBox coorBox;

	public WaypointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		coorBox = BoundingBox.getInstance();
		this.route = RouteController.getInstance().getCurrentRoute();
	}

	public void updateWaypoint() {
			List<DisplayWaypoint> l = CoordinateUtility
					.extractDisplayWaypointsOutOfRouteInfo(route,
							coorBox.getCenter(), coorBox.getDisplaySize(),
							this.coorBox.getLevelOfDetail());

			this.removeAllViews();

			if (!l.isEmpty()) {
				Log.d("red", "" + l.size());

				for (DisplayWaypoint dw : l) {
					RelativeLayout.LayoutParams paramsOption = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					paramsOption.width = coorBox.getDisplaySize().x / 10;
					paramsOption.height = coorBox.getDisplaySize().x / 10;

					ImageView iv = new ImageView(this.getContext());

					iv.setY(dw.getY() - coorBox.getDisplaySize().x / 10);
					iv.setX(dw.getX() - coorBox.getDisplaySize().x / 10 / 2);
					iv.setTag(dw.getId());
					iv.setVisibility(View.VISIBLE);
					iv.setScaleType(ImageView.ScaleType.FIT_XY);
					iv.setImageDrawable(this.getResources().getDrawable(
							DEFAULT_WAYPOINT));

					this.addView(iv, paramsOption);

				}

				ImageView iv = (ImageView) this.getChildAt((this
						.getChildCount() - 1));
				iv.setImageDrawable(this.getResources().getDrawable(
						DEFAULT_FLAG_TARGET));

				iv = (ImageView) this.getChildAt(0);
				iv.setImageDrawable(this.getResources().getDrawable(
						DEFAULT_FLAG));

			}

			if (this.route.getActiveWaypoint() != null) {
				int id = this.route.getActiveWaypoint().getId();

				for (int a = 0; a < this.getChildCount(); a++) {
					int valueId = Integer.parseInt(this.getChildAt(a).getTag()
							.toString());
					if (valueId == id) {
						ImageView currentActive = (ImageView) this
								.getChildAt(a);

						if (currentActive.getDrawable().equals(
								this.getResources().getDrawable(DEFAULT_FLAG))) {
							currentActive.setImageDrawable(this.getResources()
									.getDrawable(DEFAULT_FLAG_ACTIVE));
						}

						if (currentActive.getDrawable().equals(
								this.getResources().getDrawable(
										DEFAULT_FLAG_TARGET))) {
							currentActive.setImageDrawable(this.getResources()
									.getDrawable(DEFAULT_FLAG_TARGET_ACTIVE));
						}

						if (currentActive.getDrawable().equals(
								this.getResources().getDrawable(
										DEFAULT_WAYPOINT))) {
							currentActive.setImageDrawable(this.getResources()
									.getDrawable(DEFAULT_WAYPOINT_ACTIVE));
						}
					}
				}

			}
	}
}

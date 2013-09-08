package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.CenterListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.LevelOfDetailListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

public class WaypointView extends RelativeLayout implements CenterListener, LevelOfDetailListener, RouteListener {

	public static final int DEFAULT_FLAG = R.drawable.flag;
	public static final int DEFAULT_FLAG_ACTIVE = R.drawable.flag_activ;
	public static final int DEFAULT_FLAG_TARGET = R.drawable.flag_target;
	public static final int DEFAULT_FLAG_TARGET_ACTIVE = R.drawable.flag_target_activ;

	public static final int DEFAULT_WAYPOINT = R.drawable.waypoint;
	public static final int DEFAULT_WAYPOINT_ACTIVE = R.drawable.waypoint_activ;
	private static final String TAG = WaypointView.class.getSimpleName();

	private RouteInfo route;
	private BoundingBox coorBox;
	private Drawable flag;
	// private Drawable flagActive;
	private Drawable flagTarget;
	// private Drawable flagTargetActive;
	private Drawable waypoint;
	private GestureDetector waypointGestureDetector;

	// private Drawable waypointActive;

	public WaypointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		coorBox = BoundingBox.getInstance();
		this.route = RouteController.getInstance().getCurrentRoute();

		// ---------------------------------------------
		Log.d(TAG, "Initialisiere Drawables.");
		flag = this.getResources().getDrawable(DEFAULT_FLAG);
		// flagActive = this.getResources().getDrawable(DEFAULT_FLAG_ACTIVE);
		flagTarget = this.getResources().getDrawable(DEFAULT_FLAG_TARGET);
		// flagTargetActive = this.getResources().getDrawable(
		// DEFAULT_FLAG_TARGET_ACTIVE);
		waypoint = this.getResources().getDrawable(DEFAULT_WAYPOINT);
		// waypointActive = this.getResources().getDrawable(
		// DEFAULT_WAYPOINT_ACTIVE);

		waypointGestureDetector = new GestureDetector(context,
				new WaypointGestureDetector());
		
		RouteController.getInstance().registerRouteListener(this);
		coorBox.registerCenterListener(this);
		coorBox.registerLevelOfDetailListener(this);
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
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				paramsOption.width = coorBox.getDisplaySize().x / 10;
				paramsOption.height = coorBox.getDisplaySize().x / 10;

				ImageView iv = new ImageView(this.getContext());

				iv.setY(dw.getY() - coorBox.getDisplaySize().x / 10);
				iv.setX(dw.getX() - coorBox.getDisplaySize().x / 10 / 2);
				iv.setTag(dw.getId());
				iv.setVisibility(View.VISIBLE);
				iv.setScaleType(ImageView.ScaleType.FIT_XY);
				iv.setImageDrawable(waypoint);
				iv.setOnTouchListener(new WaypointTouchListener(iv, dw.getId()));

				this.addView(iv, paramsOption);

			}

			ImageView iv = (ImageView) this
					.getChildAt((this.getChildCount() - 1));
			iv.setImageDrawable(flagTarget);

			iv = (ImageView) this.getChildAt(0);
			iv.setImageDrawable(flag);

		}

		if (this.route.getActiveWaypoint() != null) {
			int id = this.route.getActiveWaypoint().getId();

			for (int a = 0; a < this.getChildCount(); a++) {
				int valueId = Integer.parseInt(this.getChildAt(a).getTag()
						.toString());
				Log.d("active", "pase: " + valueId + " == " + id);
				if (valueId == id) {
					ImageView currentActive = (ImageView) this.getChildAt(a);

					if (currentActive.getDrawable().equals(flag)) {
						Log.d("active", "setzen");
						currentActive.setImageDrawable(this.getResources()
								.getDrawable(DEFAULT_FLAG_ACTIVE));
					}

					if (currentActive.getDrawable().equals(flagTarget)) {
						Log.d("active", "setzen");
						currentActive.setImageDrawable(this.getResources()
								.getDrawable(DEFAULT_FLAG_TARGET_ACTIVE));
					}

					if (currentActive.getDrawable().equals(waypoint)) {
						Log.d("active", "setzen");
						currentActive.setImageDrawable(this.getResources()
								.getDrawable(DEFAULT_WAYPOINT_ACTIVE));
					}
				}
			}

		}
	}

	/**
	 * This Class intercept the touch to waypoints
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class WaypointTouchListener implements OnTouchListener {

		private ImageView iv;
		private int id;

		/**
		 * Create a new Waypoint
		 * 
		 * @param iv
		 *            the new Imageview
		 */
		WaypointTouchListener(ImageView iv, int id) {
			this.iv = iv;
			this.id = id;
		}

		public boolean onTouch(View view, MotionEvent event) {
			if (view.equals(iv)) {
				Log.d(TAG, "UserTouch auf View ID:" + id);
				currentId = id;
				currentView = iv;

				int action = event.getAction();

				if (action == MotionEvent.ACTION_UP) {
					RouteController.getInstance()
							.moveActiveWaypointComputeOnly();
				}

				// if (waypointGestureDetector.onTouchEvent(event)) {
				// return true;
				// }

				// if(event.getAction() == MotionEvent.ACTION_UP){
				// mc.pushMovedWaypoint();
				// }
				// }

			}
			return waypointGestureDetector.onTouchEvent(event);
		}
	}

	private ImageView currentView;
	private int currentId;

	/**
	 * This is a Gesture Detector which listen to the Waypoint touches.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class WaypointGestureDetector implements OnGestureListener {

		DisplayCoordinate curentWP;

		public boolean onDown(MotionEvent event) {
			RouteController.getInstance().setActiveWaypoint(currentId);
			// mc.getActiveWaypointId();
			curentWP = new DisplayCoordinate(currentView.getX(),
					currentView.getY());
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			Log.d(TAG, "Fling! " + velocityY + " " + e2.getY() + " "
					+ currentId);

			float velocity = (float) Math.sqrt((double) Math.pow(
					Math.abs(velocityX), 2)
					+ (double) Math.pow(Math.abs(velocityY), 2));

			if (velocity > 400) {
				Log.d(TAG, "Delete Point " + currentId);
				RouteController.getInstance().deleteActiveWaypoint(currentId);
			}

			return false;
		}

		public void onLongPress(MotionEvent event) {
			Log.d(TAG, "Waypoint onLongPress " + currentId);
		}

		public boolean onScroll(MotionEvent event1, MotionEvent event2,
				float deltaX, float deltaY) {
			Log.d(TAG, "Waypoint onScroll " + currentId);

			curentWP.setX(curentWP.getX() - deltaX);
			curentWP.setY(curentWP.getY() - deltaY);

			Coordinate next = CoordinateUtility
					.convertDisplayCoordinateToCoordinate(
							new DisplayCoordinate(curentWP.getX(), curentWP
									.getY()), coorBox.getTopLeft(), coorBox
									.getLevelOfDetail());

			RouteController.getInstance().moveActiveWaypointMoveOnly(next);

			// routeList.removeView(currentView);
			// currentView.setX(currentView.getX() - deltaX);
			// currentView.setY(currentView.getY() - deltaY);
			// mc.onMovePoint(-deltaX, -deltaY, currentId);
			// routeList.addView(currentView);
			// mIsScrolling = true;

			return true;
		}

		public void onShowPress(MotionEvent arg0) {
			Log.d(TAG, "Waypoint onShowPress " + currentId);

		}

		public boolean onSingleTapUp(MotionEvent arg0) {
			Log.d(TAG, "Waypoint onSingleTapUp " + currentId);
			return false;
		}

	}

	@Override
	public void onRouteChange(RouteInfo currentRoute) {
		this.updateWaypoint();		
	}

	@Override
	public void onLevelOfDetailChange(float levelOfDetail) {
		this.updateWaypoint();
	}

	@Override
	public void onCenterChange(Coordinate center) {
		this.updateWaypoint();
	}
}

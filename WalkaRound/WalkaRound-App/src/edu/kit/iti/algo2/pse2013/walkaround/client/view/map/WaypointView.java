package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import java.util.List;

import android.content.Context;
import android.graphics.PointF;
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
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.CenterListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.LevelOfDetailListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.ScaleListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

/**
 * This view draw and handle the Waypoint Marker
 * 
 * @author Ludwig Biermann
 * @version 1.7
 * 
 */
public class WaypointView extends RelativeLayout implements CenterListener,
		LevelOfDetailListener, ScaleListener {

	public static final int DEFAULT_FLAG = R.drawable.flag;
	public static final int DEFAULT_FLAG_ACTIVE = R.drawable.flag_activ;
	public static final int DEFAULT_FLAG_TARGET = R.drawable.flag_target;
	public static final int DEFAULT_FLAG_TARGET_ACTIVE = R.drawable.flag_target_activ;
	public static final int DEFAULT_WAYPOINT = R.drawable.waypoint;
	public static final int DEFAULT_WAYPOINT_ACTIVE = R.drawable.waypoint_activ;
	private static final String TAG = WaypointView.class.getSimpleName();

	private boolean scroll = false;
	private RouteInfo route;
	private BoundingBox coorBox;
	private Drawable flag;
	private Drawable flagTarget;
	private Drawable waypoint;
	private GestureDetector waypointGestureDetector;
	private ImageView currentView;
	private int currentId;

	/**
	 * This create a new POIview.
	 * 
	 * @param context
	 *            the context of the app
	 * @param attrs
	 *            the needed attributes
	 */
	public WaypointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		coorBox = BoundingBox.getInstance();
		this.route = RouteController.getInstance().getCurrentRoute();

		// ---------------------------------------------
		Log.d(TAG, "Initialisiere Drawables.");
		flag = this.getResources().getDrawable(DEFAULT_FLAG);
		flagTarget = this.getResources().getDrawable(DEFAULT_FLAG_TARGET);
		waypoint = this.getResources().getDrawable(DEFAULT_WAYPOINT);
		waypointGestureDetector = new GestureDetector(context,
				new WaypointGestureDetector());

		coorBox.registerCenterListener(this);
		coorBox.registerLevelOfDetailListener(this);
		coorBox.registerScaleListener(this);
	}

	/**
	 * update the Waypoint
	 */
	public void updateWaypoint() {

		PointF p = BoundingBox.getInstance().getPivot();

		List<DisplayWaypoint> l = CoordinateUtility
				.extractDisplayWaypointsOutOfRouteInfo(route, coorBox
						.getCenter(), coorBox.getDisplaySize(), this.coorBox
						.getLevelOfDetail(), BoundingBox.getInstance()
						.getScale(), p);

		this.removeAllViews();

		if (!l.isEmpty()) {
			Log.d("red", "" + l.size());

			for (DisplayWaypoint dw : l) {
				RelativeLayout.LayoutParams paramsOption = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				paramsOption.width = coorBox.getDisplaySize().x / 10;
				paramsOption.height = coorBox.getDisplaySize().x / 10;

				ImageView iv = new ImageView(this.getContext());

				iv.setX(dw.getX() - coorBox.getDisplaySize().x / 10 / 2);
				iv.setY(dw.getY() - coorBox.getDisplaySize().x / 10);
				iv.setTag(dw.getId());
				iv.setVisibility(View.VISIBLE);
				iv.setScaleType(ImageView.ScaleType.FIT_XY);
				// iv.setScaleX(scale);
				// iv.setScaleY(scale);
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
	 * @version 1.0
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

				if (action == MotionEvent.ACTION_UP && scroll) {
					Log.d(TAG, "computeActive Waypoint");
					Thread t = new Thread(new FlingListener());
					t.start();
				}
			}
			return waypointGestureDetector.onTouchEvent(event);
		}
	}

	/********************************************** PATCH **********************************************/

	private boolean fling = false;

	private class FlingListener implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(500);
				if (!fling) {
					RouteController.getInstance()
							.moveActiveWaypointComputeOnly(currentId);
				}

			} catch (InterruptedException e) {
				RouteController.getInstance().moveActiveWaypointComputeOnly(
						currentId);
				e.printStackTrace();
			}
		}
	}

	/********************************************** PATCH **********************************************/

	/**
	 * This is a Gesture Detector which listen to the Waypoint touches.
	 * 
	 * @author Ludwig Biermann
	 * @version 1.0
	 * 
	 */
	private class WaypointGestureDetector implements OnGestureListener {

		private DisplayCoordinate curentWP;
		private static final int VELOCITY = 1000;

		public boolean onDown(MotionEvent event) {
			RouteController.getInstance().setActiveWaypoint(currentId);
			curentWP = new DisplayCoordinate(currentView.getX(),
					currentView.getY());
			scroll = false;
			fling = false;
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.d(TAG, "Fling! " + velocityY + " " + e2.getY() + " "
					+ currentId);

			float velocity = (float) Math.sqrt((double) Math.pow(
					Math.abs(velocityX), 2)
					+ (double) Math.pow(Math.abs(velocityY), 2));

			Log.d(TAG, "VELOCITY " + velocity);
			if (velocity >= VELOCITY) {
				fling = true;
				Log.d(TAG, "Delete Point " + currentId);
				RouteController.getInstance().deleteActiveWaypoint();
				return true;
			}

			return false;
		}

		public void onLongPress(MotionEvent event) {
			Log.d(TAG, "Waypoint onLongPress " + currentId);
		}

		public boolean onScroll(MotionEvent event1, MotionEvent event2,
				float deltaX, float deltaY) {
			scroll = true;
			Log.d(TAG, "Waypoint onScroll " + currentId);
			float scale = coorBox.getScale();
			
			curentWP.setX(curentWP.getX() - deltaX/scale);
			curentWP.setY(curentWP.getY() - deltaY/scale);

			Coordinate next = CoordinateUtility
					.convertDisplayCoordinateToCoordinate(
							new DisplayCoordinate(curentWP.getX(), curentWP
									.getY()), coorBox.getScaledTopLeft(),
							coorBox.getLevelOfDetail());

			next = new Coordinate(next,
					-coorBox.getDisplaySizeInCoordinates().y / 2F,
					coorBox.getDisplaySizeInCoordinates().x / 2F);

			RouteController.getInstance().moveActiveWaypointMoveOnly(next);

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
	public void onLevelOfDetailChange(float levelOfDetail) {
		this.updateWaypoint();
	}

	@Override
	public void onCenterChange(Coordinate center) {
		this.updateWaypoint();
	}

	@Override
	public void onScaleChange(float scale) {
		this.updateWaypoint();
	}
}

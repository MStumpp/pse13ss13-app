package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

// Java Library
import java.io.IOException;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayPOI;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.headup.HeadUpView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.PullUpView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
// Android Library
// Walkaround Library

/**
 * 
 * This class is the main activity of the Walkaround app. This class organize
 * the whole view and shows the drawing map and route.
 * 
 * @author Ludwig Biermann
 * 
 */
public class MapView extends Activity {

	/**
	 * Debug Information
	 */
	private static final String TAG_MAPVIEW = MapView.class.getSimpleName();
	private static final String TAG_MAPVIEW_ANIMATION = TAG_MAPVIEW + "_animation";
	private static final String TAG_MAPVIEW_GESTURE = TAG_MAPVIEW + "_gestures";
	private static final String TAG_MAPVIEW_TOUCH = TAG_MAPVIEW + "_touch";

	/**
	 * Fling defaults
	 */
	private static final int USER_X_DELTA = 15;
	private static final int USER_Y_DELTA = 19;

	/**
	 * Default Pictures
	 */
	public static final int USER_ARROW_IMAGE = R.drawable.user_arrow_30x38;
	public static final int DEFAULT_PATTERN = R.drawable.fog;

	public static final int DEFAULT_FLAG = R.drawable.flag;
	public static final int DEFAULT_FLAG_ACTIVE = R.drawable.flag_activ;
	public static final int DEFAULT_FLAG_TARGET = R.drawable.flag_target;
	public static final int DEFAULT_FLAG_TARGET_ACTIVE = R.drawable.flag_target_activ;

	public static final int DEFAULT_WAYPOINT = R.drawable.waypoint;
	public static final int DEFAULT_WAYPOINT_ACTIVE = R.drawable.waypoint_activ;

	public static final int DEFAULT_POI = R.drawable.poi;

	/**
	 * Default Drawables
	 */

	private Drawable flag;
	private Drawable flagActive;
	private Drawable flagTarget;
	private Drawable flagTargetActive;
	private Drawable waypoint;
	private Drawable waypointActive;
	private Drawable poi;
	// private Drawable poiActive;

	/**
	 * Views
	 */
	private ImageView map;
	private ImageView routeOverlay;
	private ImageView user;

	/**
	 * Controller
	 */
	private MapController mc;

	/**
	 * Animation
	 */
	private long startDelay = 0;

	/**
	 * Gestik
	 */
	private GestureDetector gestureDetector;
	private GestureOverlayView mapGest;

	/**
	 * Routen
	 */
	private ImageView currentActive;
	private RelativeLayout routeList;
	private RelativeLayout poiList;
	private float sizeOfPoints;
	private int sizeOfRoute = 6;

	/**
	 *
	 */
	private PullUpView pullUp;

	private Point size;

	private Canvas canvas;
	private Bitmap routeOverlayBitmap;

	private float fromX;
	private float fromY;

	/**
	 * User orientation
	 */
	private float userX;
	private float userY;
	private float delta;

	private LocationManager locationManager;

	private GestureDetector waypointGestureDetector;

	public Point getDisplaySize() {
		return size;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PositionManager.initialize(this);
		POIManager.initialize(this);

		Log.d(TAG_MAPVIEW, "Get Display size.");

		Display display = this.getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);

		routeOverlayBitmap = Bitmap.createBitmap(size.x, size.y,
				Bitmap.Config.ARGB_8888);
		routeOverlayBitmap.prepareToDraw();

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Initialisiere Layout.");
		this.setContentView(R.layout.map);

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Initialisiere Drawables.");
		flag = this.getResources().getDrawable(DEFAULT_FLAG);
		flagActive = this.getResources().getDrawable(DEFAULT_FLAG_ACTIVE);
		flagTarget = this.getResources().getDrawable(DEFAULT_FLAG_TARGET);
		flagTargetActive = this.getResources().getDrawable(
				DEFAULT_FLAG_TARGET_ACTIVE);
		waypoint = this.getResources().getDrawable(DEFAULT_WAYPOINT);
		waypointActive = this.getResources().getDrawable(
				DEFAULT_WAYPOINT_ACTIVE);
		poi = this.getResources().getDrawable(DEFAULT_POI);

		// ---------------------------------------------

		Log.d(TAG_MAPVIEW, "DisplayMaße: " + size.x + " * " + size.y);

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Initialisere Route und POI Ovetrlay.");
		routeList = (RelativeLayout) this.findViewById(R.id.routeList);
		poiList = (RelativeLayout) this.findViewById(R.id.poiList);
		sizeOfPoints = (size.y / 10);

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Karte wird erstellt.");
		map = (ImageView) this.findViewById(R.id.mapview_map);
		map.setMinimumWidth(size.x);
		map.setMinimumHeight(size.y);
		map.setOnTouchListener(new MapTouchEventListener());

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Initialisiere MapController.");
		mc = MapController.initialize(this);

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "User wird erstellt.");
		user = (ImageView) this.findViewById(R.id.mapview_user);
		user.setImageDrawable(this.getResources().getDrawable(USER_ARROW_IMAGE));
		user.getLayoutParams().width = USER_X_DELTA * 4;
		user.getLayoutParams().height = USER_Y_DELTA * 4;
		user.setOnTouchListener(new UserTouchEventListener());

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Fragmente werden eingebaut");
		FragmentTransaction ft = this.getFragmentManager().beginTransaction();

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Fragment PullUpMenue wird eingebaut");
		this.pullUp = new PullUpView();
		ft.add(R.id.pullUpMain, pullUp).commit();

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Fragment headUp wird eingebaut");
		Fragment headUp = new HeadUpView();
		ft.add(R.id.headupmain, headUp);

		// -----------------------TEST---------------------
		Log.d(TAG_MAPVIEW, "User wird in die Mitte gestellt.");
		this.setUserPositionOverlayImage(new DisplayCoordinate(
				(float) size.x / 2, (float) size.y / 2), 180);

		routeOverlayBitmap = Bitmap.createBitmap(size.x, size.y,
				Bitmap.Config.ARGB_8888);
		routeOverlayBitmap.prepareToDraw();

		gestureDetector = new GestureDetector(this, new MapGestureDetector());
		waypointGestureDetector = new GestureDetector(this,
				new WaypointGestureDetector());

		locationManager = (LocationManager) this.getApplicationContext()
				.getSystemService(LocationManager.KEY_LOCATION_CHANGED);
		Log.d(TAG_MAPVIEW, "locationManager is " + (locationManager != null));

	}

	/**
	 * gives the pullupview back
	 */
	public PullUpView getPullUpView() {
		return pullUp;
	}

	/**
	 * Updates the map
	 * 
	 * @param b
	 *            the new map bitmap
	 */
	public void updateMapImage(final Bitmap b) {

		runOnUiThread(new Runnable() {
			public void run() {
				synchronized (b) {
					if (!b.isRecycled()) {
						try {
							map.setImageBitmap(b);
							map.setVisibility(View.VISIBLE);
						} catch (IllegalArgumentException e) {
							Log.e(TAG_MAPVIEW, "" + e.toString());
						}
					}
				}
			}
		});
	}

	/**
	 * updates the route
	 * 
	 * @param b
	 *            the new route bitmap
	 */
	public void updateRouteOverlayImage(final Bitmap b) {

		if (routeOverlay == null) {
			routeOverlay = (ImageView) findViewById(R.id.mapview_overlay);
		}
		runOnUiThread(new Runnable() {
			public void run() {
				synchronized (b) {
					if (!b.isRecycled()) {
						try {
							routeOverlay.setImageBitmap(b);
							routeOverlay.setVisibility(View.VISIBLE);
						} catch (IllegalArgumentException e) {
							Log.e(TAG_MAPVIEW, "" + e.toString());
						}
					}
				}
			}
		});
	}

	/*
	 * 
	 * 
	 * @param dip
	 * 
	 * @return
	 * 
	 * public int dipToPixel(float dip) { return (int)
	 * (getResources().getDisplayMetrics().density * dip); }
	 * 
	 * public float pixelToDip(int p) { return (p /
	 * getResources().getDisplayMetrics().density); }
	 */

	/**
	 * Change the position and the orientation of the user
	 * 
	 * @param delta
	 *            degree of the neew rotation
	 */
	public void onPositionChange(final float x, final float y,
			final float deltaDegree) {
		runOnUiThread(new Runnable() {
			public void run() {
				delta = deltaDegree;
				userX = x;
				userY = y;
			}
		});
		this.setUserPositionOverlayImage(new DisplayCoordinate(userX, userY),
				this.delta);
	}

	/**
	 * Change the position of the user
	 * 
	 * @param x
	 *            coordinates
	 * @param y
	 *            coordinates
	 */
	public void onPositionChange(final float x, final float y) {
		runOnUiThread(new Runnable() {
			public void run() {
				userX = x;
				userY = y;
			}
		});
		this.setUserPositionOverlayImage(new DisplayCoordinate(userX, userY), 0);
	}

	/**
	 * Change the orientation of the user
	 * 
	 * @param delta
	 *            degree of the neew rotation
	 */
	public void onPositionChange(final float deltaDegree) {
		runOnUiThread(new Runnable() {
			public void run() {
				delta = deltaDegree;
				// .setUserPositionOverlayImage(new DisplayCoordinate(0, 0), 0);
			}
		});
		// this.setUserPositionOverlayImage(new DisplayCoordinate(0, 0), 0);
	}

	/**
	 * updates the DisplayWaypoints
	 * 
	 * @param displayPoints
	 *            the new DisplayWaypoints
	 */
	public void updateDisplayWaypoints(final List<DisplayWaypoint> displayPoints) {

		final Context context = this;

		if (displayPoints == null || displayPoints.size() == 0) {
			return;
		}

		runOnUiThread(new Runnable() {
			public void run() {
				currentActive = null;
				routeList.removeAllViews();

				fromX = displayPoints.get(0).getX();
				fromY = displayPoints.get(0).getY();

				for (DisplayWaypoint value : displayPoints) {
					ImageView iv = new ImageView(context);
					iv.setImageDrawable(waypoint);
					iv.setY(value.getY() - sizeOfPoints);
					iv.setX(value.getX() - sizeOfPoints / 2);
					iv.setVisibility(View.VISIBLE);
					iv.setLayoutParams(new LayoutParams((int) sizeOfPoints,
							(int) sizeOfPoints));
					iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
					iv.setTag(value.getId());
					iv.setOnTouchListener(new WaypointTouchListener(iv, value
							.getId()));
					routeList.addView(iv);
				}

				if (routeList.getChildCount() > 0) {
					ImageView iv = (ImageView) routeList.getChildAt((routeList
							.getChildCount() - 1));
					iv.setImageDrawable(flagTarget);
					iv.setX(iv.getX() - (sizeOfPoints / 2));

					iv = (ImageView) routeList.getChildAt(0);
					iv.setImageDrawable(flag);
					iv.setX(iv.getX() - (sizeOfPoints / 2));
				}
			}
		});
	}

	/**
	 * updates the POI´s on the Display
	 * 
	 * @param dp
	 *            List of pois
	 */
	public void updateDisplayCoordinate(final List<DisplayPOI> dp) {
		poiList.removeAllViews();
		final Context context = this;

		runOnUiThread(new Runnable() {
			public void run() {
				for (DisplayPOI value : dp) {
					ImageView iv = new ImageView(context);
					iv.setImageDrawable(poi);
					iv.setY(value.getY());
					iv.setX(value.getX());
					iv.setTag(value.getId());
					iv.setLayoutParams(new LayoutParams((int) sizeOfPoints,
							(int) sizeOfPoints));
					iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
					iv.setOnTouchListener(new POITouchListener(iv, value.getId()));
					poiList.addView(iv);
				}
			}
		});
	}

	/**
	 * Set an new Waypoint as active
	 * 
	 * @param id
	 *            the id of the waypoint
	 */
	public void setActive(int id) {

		if (currentActive != null) {
			if (currentActive.getDrawable().equals(flagActive)) {
				currentActive.setImageDrawable(flag);

			}

			if (currentActive.getDrawable().equals(flagTargetActive)) {
				currentActive.setImageDrawable(flagTarget);

			}

			if (currentActive.getDrawable().equals(waypointActive)) {
				currentActive.setImageDrawable(waypoint);

			}
		}

		boolean found = false;

		for (int a = 0; a < routeList.getChildCount() && !found; a++) {
			int valueId = Integer.parseInt(routeList.getChildAt(a).getTag()
					.toString());
			if (valueId == id) {
				currentActive = (ImageView) routeList.getChildAt(a);

				if (currentActive.getDrawable().equals(flag)) {
					currentActive.setImageDrawable(flagActive);
				}

				if (currentActive.getDrawable().equals(flagTarget)) {
					currentActive.setImageDrawable(flagTargetActive);
				}

				if (currentActive.getDrawable().equals(waypoint)) {
					currentActive.setImageDrawable(waypointActive);
				}
				found = true;
			}
		}
	}

	/**
	 * shift the User Position arrow to an new position
	 * 
	 * @param coor
	 *            target coordinates
	 * @param degree
	 *            rotation
	 */
	public void setUserPositionOverlayImage(final DisplayCoordinate coor) {
		runOnUiThread(new Runnable() {
			public void run() {
				user.setX(coor.getX());
				user.setY(coor.getY());
			}
		});
	}

	/**
	 * shift the User Position arrow to an new position
	 * 
	 * @param coor
	 *            target coordinates
	 * @param degree
	 *            rotation
	 */
	public void setUserPositionOverlayImage(final float degree) {
		runOnUiThread(new Runnable() {
			public void run() {
				user.setRotation(degree);
			}
		});
	}

	/**
	 * shift the User Position arrow to an new position
	 * 
	 * @param coor
	 *            target coordinates
	 * @param degree
	 *            rotation
	 */
	private void setUserPositionOverlayImage(DisplayCoordinate coor,
			float degree) {
		// TODO duration überdenken
		AnimatorSet set = new AnimatorSet();
		this.userX = coor.getX();
		this.userY = coor.getY();

		Log.d(TAG_MAPVIEW, "Thread Animator Set UP");

		ObjectAnimator transX = ObjectAnimator.ofFloat(user, "x", coor.getX()
				- USER_X_DELTA);
		ObjectAnimator transY = ObjectAnimator.ofFloat(user, "y", coor.getY()
				- USER_Y_DELTA);
		ObjectAnimator rotate = ObjectAnimator
				.ofFloat(user, "rotation", degree);

		transX.setDuration(1000);
		transY.setDuration(1000);
		rotate.setDuration(1000);

		set.play(transX).with(transY);
		set.play(transY).with(rotate);
		set.play(transX).with(rotate);

		set.addListener(new UserAnimationListener(coor, degree));

		set.setStartDelay(startDelay);
		startDelay += 1000;
		set.start();

	}

	// ----------------Animation Listener ---------------------

	/**
	 * A Listener who listen to the user position animation
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class UserAnimationListener implements AnimatorListener {

		private float degree;
		private DisplayCoordinate coor;

		public UserAnimationListener(DisplayCoordinate coor, float degree) {
			this.degree = degree;
			this.coor = coor;
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			Log.d(TAG_MAPVIEW_ANIMATION, "Animation Cancel");
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			user.clearAnimation();
			startDelay -= 1000;

			user.setX(coor.getX() - USER_X_DELTA);
			user.setY(coor.getY() - USER_Y_DELTA);

			user.setRotation(degree);
			Log.d(TAG_MAPVIEW_ANIMATION, "Drehe den User um " + degree
					+ " Grad");
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			Log.d(TAG_MAPVIEW_ANIMATION, "Animation Repeat");

		}

		@Override
		public void onAnimationStart(Animator animation) {
			Log.d(TAG_MAPVIEW_ANIMATION, "Animation Start");
		}

	}

	// ----------------Touch Listener ---------------------

	/**
	 * This is a Gesture Detector which listen to the map touches.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class MapGestureDetector implements OnGestureListener {

		@SuppressWarnings("unused")
		float oldX;
		@SuppressWarnings("unused")
		float oldY;
		@SuppressWarnings("unused")
		float gesamt;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.d(TAG_MAPVIEW_GESTURE, "MapTouch Fling");
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			Log.d(TAG_MAPVIEW_GESTURE, "SCROLL EVENT");

			if (e2.getPointerCount() >= 2) {

				float x = Math.abs(e2.getX(0) - e2.getX(1));
				float y = Math.abs(e2.getY(0) - e2.getY(1));

				if (e2.getX(0) < e2.getX(1)) {
					x += e2.getX(0);
				} else {
					x += e2.getX(1);
				}

				if (e2.getY(0) < e2.getY(1)) {
					x += e2.getY(0);
				} else {
					x += e2.getY(1);
				}

				float z = ((Math.abs(distanceY) + Math.abs(distanceX)) / 10);

				this.gesamt += z;

				this.oldX = e2.getX();
				this.oldY = e2.getY();

				mc.onZoom(z, new DisplayCoordinate(x, y));
			} else {

				Log.d(TAG_MAPVIEW_GESTURE, "MapTouch Scroll");
				// distanceY *= -1;
				if (e1.getY() > e2.getY()) {
					Log.d(TAG_MAPVIEW_GESTURE, "Up " + distanceY);
				} else {
					Log.d(TAG_MAPVIEW_GESTURE, "Down " + -distanceY);
				}

				if (e1.getX() > e2.getX()) {
					Log.d(TAG_MAPVIEW_GESTURE, "Left " + distanceX);
				} else {
					Log.d(TAG_MAPVIEW_GESTURE, "Right " + -distanceX);
				}

				mc.onShift(distanceX, distanceY);
			}
			return true;
		}

		@Override
		public void onLongPress(MotionEvent event) {
			Log.d(TAG_MAPVIEW_GESTURE, "MapTouch Long Touch");
			mc.onCreatePoint(new DisplayCoordinate(event.getX(), event.getY()));
		}

		@Override
		public boolean onDown(MotionEvent e) {
			Log.d(TAG_MAPVIEW_GESTURE, "MapTouch Down");
			this.oldX = e.getX();
			this.oldY = e.getY();
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			Log.d(TAG_MAPVIEW_GESTURE, "MapTouch Show Press");
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			Log.d(TAG_MAPVIEW_GESTURE, "MapTouch TapUp");
			return false;
		}

	}

	/**
	 * This class forwards the touch events to the gesture detector
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class MapTouchEventListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (map.equals(view)) {
				Log.d(TAG_MAPVIEW_TOUCH, "TOUCH!" + view.toString());
				gestureDetector.onTouchEvent(event);
				return true;
			}

			return false;
		}

	}

	/**
	 * This class intercept the touch events on the user arrow
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class UserTouchEventListener implements OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			if (arg0.equals(user)) {
				Log.d(TAG_MAPVIEW_TOUCH, "UserTouch on User Arrow");
				return true;
			}
			return false;
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

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (view.equals(iv)) {
				Log.d(TAG_MAPVIEW_TOUCH, "UserTouch auf View ID:" + id);
				currentId = id;
				return waypointGestureDetector.onTouchEvent(event)	;
			}
			return false;
		}
	}
	int currentId;
	private POI currentPOI;

	/**
	 * This is a Gesture Detector which listen to the Waypoint touches.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class WaypointGestureDetector implements OnGestureListener {
		
		

		@Override
		public boolean onDown(MotionEvent event) {
			Log.d(TAG_MAPVIEW_GESTURE, "Waypoint onDown " + currentId);
			mc.setActive(currentId);
			//mc.getActiveWaypointId();
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			Log.d(TAG_MAPVIEW_GESTURE, "Fling! " + velocityY + " " + e2.getY() + " " + currentId);
			if (Math.abs(velocityY) > 100 || Math.abs(velocityY) > 100) {
				Log.d(TAG_MAPVIEW_GESTURE, "Delete Point" + currentId);
				mc.onDeletePoint(currentId);
			}
			
			return false;
		}

		@Override
		public void onLongPress(MotionEvent event) {
			Log.d(TAG_MAPVIEW_GESTURE, "Waypoint onLongPress " + currentId);
		}

		@Override
		public boolean onScroll(MotionEvent event1, MotionEvent event2, float deltaX,
				float deltaY) {
			Log.d(TAG_MAPVIEW_GESTURE, "Waypoint onScroll " + currentId);
			
			mc.onMovePoint(deltaX, deltaY, currentId);
			
			return true;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			Log.d(TAG_MAPVIEW_GESTURE, "Waypoint onShowPress " + currentId);

		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			Log.d(TAG_MAPVIEW_GESTURE, "Waypoint onSingleTapUp " + currentId);
			return false;
		}

	}
	/**
	 * This Class intercept the touch to waypoints
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class POITouchListener implements OnTouchListener {

		private ImageView iv;
		private int id;

		/**
		 * Create a new POITouchListener
		 * 
		 * @param iv
		 *            the new Imageview
		 */
		POITouchListener(ImageView iv, int id) {
			this.iv = iv;
			this.id = id;
		}

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (view.equals(iv)) {
				Log.d(TAG_MAPVIEW_TOUCH, "UserTouch auf POI ID:" + id);
				currentPOI = mc.getPOIById(id);
				pullUp.changeView(PullUpView.CONTENT_INFO);
				pullUp.setFullSizeHeight();
			}
			return false;
		}
	}
	
	@Override
	public void onLowMemory() {
        super.onLowMemory();
        System.gc();
	}

	public POI getCurrentPOI() {
		return currentPOI;
	}
}
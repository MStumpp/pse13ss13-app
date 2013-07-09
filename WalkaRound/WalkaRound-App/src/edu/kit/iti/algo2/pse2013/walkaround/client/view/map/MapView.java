package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayPOI;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.headup.HeadUpView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.PullUpView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

public class MapView extends Activity {

	private static final String TAG_MAPVIEW = "MAP_VIEW";

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
	// public static final int DEFAULT_POI_ACTIVE = R.drawable.poi_active;

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
	MapController mc;

	/**
	 * Animation
	 */
	long startDelay = 0;

	/**
	 * Gestik
	 */
	GestureDetector gestureDetector;
	GestureOverlayView mapGest;

	/**
	 * Routen
	 */
	ImageView currentActive;
	RelativeLayout routeList;
	RelativeLayout poiList;
	float sizeOfPoints;
	int sizeOfRoute = 6;

	/**
	 * 
	 */

	PullUpView pullUp;

	Point size;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG_MAPVIEW, "Rufe Display ab.");

		Display display = this.getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);

		routeOverlayBitmap = Bitmap.createBitmap(size.x, size.y,
				Bitmap.Config.ARGB_8888);
		routeOverlayBitmap.prepareToDraw();

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "RouteOverlay wird erstellt.");
		// routeOverlay.setOnTouchListener(new
		// RouteOverlayTouchEventListener());

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
		// poiActive = this.getResources().getDrawable(DEFAULT_POI_ACTIVE);

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
		// map.setImageBitmap(this.getDefaultFogScreen());

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
		/*
		 * Log.d(TAG_MAPVIEW,
		 * "ein paar DisplayCoordinaten werden hinzugef�gt");
		 * DisplayWaypoint[] list = new DisplayWaypoint[4]; list[0] = new
		 * DisplayWaypoint(-50, 550, 1); list[1] = new DisplayWaypoint(250, 700,
		 * 2); list[2] = new DisplayWaypoint(500, 800, 3); list[3] = new
		 * DisplayWaypoint(300, 900, 4);
		 * 
		 * this.updateDisplayCoordinate(list);
		 */
		/*
		 * updateDisplayCoordinate(list);
		 * 
		 * /* Log.d(TAG_MAPVIEW,
		 * "ein paar DisplayCoordinaten werden hinzugef�gt"); DisplayPOI[]
		 * list2 = new DisplayPOI[3]; list2[0] = new DisplayPOI(250, 350, 4);
		 * list2[1] = new DisplayPOI(450, 400, 5); list2[2] = new
		 * DisplayPOI(700, 500, 6);
		 * 
		 * 
		 * updateDisplayCoordinate(list2);
		 */

		Log.d(TAG_MAPVIEW, "Ein Punkt wird aktiv gesetzt");
		this.setActive(3);

		gestureDetector = new GestureDetector(this, new MyGestureDetector());

		// this.updateRouteOverlayImage();

		// this.drawRoute(0, 0, 500, 500);
	}

	/**
	 * 
	 */
	public PullUpView getPullUpView() {
		return pullUp;
	}

	/**
	 * Updatet die Karte
	 * 
	 * @param b
	 */
	public void updateMapImage(final Bitmap b) {

		runOnUiThread(new Runnable() {
			public void run() {

				if (!b.isRecycled()) {
					map.setImageBitmap(b);
					map.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	Canvas canvas;
	Bitmap routeOverlayBitmap;

	float fromX;
	float fromY;

	/**
	 * 
	 * @param dip
	 * @return
	 */
	public int dipToPixel(float dip) {
		return (int) (getResources().getDisplayMetrics().density * dip);
	}

	public float pixelToDip(int p) {
		return (p / getResources().getDisplayMetrics().density);
	}

	// DisplayWaypoint[] dw;

	public void updateDisplayCoordinate(final DisplayWaypoint[] dw) {

		// fromX = dw[0].getX();
		updateDisplayWaypoint(dw);
		/*
		 * DisplayWaypoint[] dw = new DisplayWaypoint[4]; dw[0] = new
		 * DisplayWaypoint(-50, 550, 1); dw[1] = new DisplayWaypoint(250, 700,
		 * 2); dw[2] = new DisplayWaypoint(500, 800, 3); dw[3] = new
		 * DisplayWaypoint(300, 900, 4); updateDisplayWaypoint(dw); /*
		 * runOnUiThread(new Runnable() { public void run() { dw = dw2;
		 * updateDisplayWaypoint(dw); } });
		 */

	}

	/**
	 * 
	 * 
	 * @param dw
	 */
	private void updateDisplayWaypoint(final DisplayWaypoint[] dw) {

		final Context context = this;

		runOnUiThread(new Runnable() {
			public void run() {
				routeList.removeAllViews();
				currentActive = null;

				fromX = dw[0].getX();
				fromY = dw[0].getY();
				Log.d(TAG_MAPVIEW + "_DRAW", "tt " + dw[0].getX());
				Log.d(TAG_MAPVIEW + "_DRAW", "tt " + dw[0].getY());

				for (DisplayWaypoint value : dw) {
					ImageView iv = new ImageView(context);
					iv.setImageDrawable(waypoint);
					iv.setY(value.getY() - sizeOfPoints);
					iv.setX(value.getX() - sizeOfPoints / 2);
					iv.setTag(value.getId());
					iv.setVisibility(View.VISIBLE);
					iv.setLayoutParams(new LayoutParams((int) sizeOfPoints,
							(int) sizeOfPoints));
					// iv.setBackgroundColor(Color.rgb(0, 0, 0));
					// iv.setX(sizeOfPoints);
					// iv.setY(sizeOfPoints);

					// iv.getLayoutParams().width = sizeOfPoints;
					// iv.getLayoutParams().height = sizeOfPoints;
					iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
					routeList.addView(iv);
					Log.d(TAG_MAPVIEW + "_DRAW", " " + fromX);
					Log.d(TAG_MAPVIEW + "_DRAW", " " + fromY);
					Log.d(TAG_MAPVIEW + "_DRAW", " " + value.getX() + " : "
							+ iv.getX());
					Log.d(TAG_MAPVIEW + "_DRAW", " " + value.getY() + " : "
							+ iv.getY());

					// drawRoute(fromX, fromY, value.getX(), value.getY());

					// fromX = value.getX();
					// fromY = value.getY();
				}

				if (routeList.getChildCount() > 0) {
					ImageView iv = (ImageView) routeList.getChildAt(0);
					iv.setImageDrawable(flag);
					iv.setX(iv.getX() - (sizeOfPoints / 2));

					iv = (ImageView) routeList.getChildAt((routeList
							.getChildCount() - 1));
					iv.setImageDrawable(flagTarget);
					iv.setX(iv.getX() - (sizeOfPoints / 2));
				}
			}
		});
	}

	/**
	 * 
	 * @param dp
	 */
	public void updateDisplayCoordinate(DisplayPOI[] dp) {
		poiList.removeAllViews();
		currentActive = null;

		for (DisplayPOI value : dp) {
			ImageView iv = new ImageView(this);
			iv.setImageDrawable(poi);
			iv.setY(value.getY());
			iv.setX(value.getX());
			iv.setTag(value.getId());
			iv.setLayoutParams(new LayoutParams((int) sizeOfPoints,
					(int) sizeOfPoints));
			iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			poiList.addView(iv);
		}
	}

	/**
	 * Setzt einen neuen Punkt aktive
	 * 
	 * @param id
	 */
	public void setActive(int id) {
		// TODO is equal von Location pr�fen

		if (currentActive != null) {
			if (currentActive.getDrawable().equals(flagActive)) {
				currentActive.setImageDrawable(flag);
				return;
			}
			if (currentActive.getDrawable().equals(flagTargetActive)) {
				currentActive.setImageDrawable(flagTarget);
				return;
			}
			if (currentActive.getDrawable().equals(waypointActive)) {
				currentActive.setImageDrawable(waypoint);
				return;
			}
		}

		for (int a = 0; a < routeList.getChildCount(); a++) {
			if (routeList.getChildAt(a).getTag().equals(id)) {
				currentActive = (ImageView) routeList.getChildAt(a);
				if (currentActive.getDrawable().equals(flag)) {
					currentActive.setImageDrawable(flagActive);
					return;
				}
				if (currentActive.getDrawable().equals(flagTarget)) {
					currentActive.setImageDrawable(flagTargetActive);
					return;
				}
				if (currentActive.getDrawable().equals(waypoint)) {
					currentActive.setImageDrawable(waypointActive);
					return;

				}
			}
		}
	}

	/**
	 * verschiebt die User Pfeil zu der Koordinate innerhalb einer Sekunde
	 * 
	 * @param coor
	 *            Zielkoordinate
	 * @param degree
	 *            Rotation
	 * @return
	 */
	public void setUserPositionOverlayImage(DisplayCoordinate coor, float degree) {

		AnimatorSet set = new AnimatorSet();

		Log.d("MAP_THREAD", "Thread Animator Set UP");

		ObjectAnimator transX = ObjectAnimator.ofFloat(user, "x", coor.getX()
				- USER_X_DELTA);
		ObjectAnimator transY = ObjectAnimator.ofFloat(user, "y", coor.getY()
				- USER_Y_DELTA);
		ObjectAnimator rotate = ObjectAnimator
				.ofFloat(user, "rotation", degree);

		// duration relativieren!
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

	private class UserAnimationListener implements AnimatorListener {

		private float degree;
		private DisplayCoordinate coor;

		public UserAnimationListener(DisplayCoordinate coor, float degree) {
			this.degree = degree;
			this.coor = coor;
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			Log.d("MAP_ANIMATION", "Animation Cancel");
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			user.clearAnimation();
			startDelay -= 1000;

			Log.d("MAP_ANIMATION", "Setze User an die richtige Position. x: "
					+ (coor.getX() - USER_X_DELTA) + " y: "
					+ (coor.getY() - USER_Y_DELTA));

			user.setX(coor.getX() - USER_X_DELTA);
			user.setY(coor.getY() - USER_Y_DELTA);

			Log.d("MAP_ANIMATION", "User hat die Position " + user.getX()
					+ " * " + user.getY());
			Log.d("MAP_ANIMATION", "User Rotation: " + user.getRotation());

			user.setRotation(degree);
			Log.d("MAP_ANIMATION", "Drehe den User um " + degree + " Grad");
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			Log.d("MAP_ANIMATION", "Animation Repeat");

		}

		@Override
		public void onAnimationStart(Animator animation) {
			Log.d("MAP_ANIMATION", "Animation Start");
		}

	}

	// ----------------Touch Listener ---------------------

	/**
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class MyGestureDetector implements OnGestureListener {

		float oldX;
		float oldY;
		float gesamt;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.d("MAP_TOUCH", "MapTouch Fling");
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			Log.d("MAP_TOUCH_ZOOM", "Counter1: " + e1.getPointerCount());

			Log.d("MAP_TOUCH_ZOOM", "Counter2: " + e2.getPointerCount());

			if (e2.getPointerCount() >= 2) {
				Log.d("MAP_TOUCH_ZOOM",
						"event 1 " + e1.getX(0) + " " + e1.getY(0));
				Log.d("MAP_TOUCH_ZOOM", "Distanc " + distanceX + " "
						+ distanceY);
				Log.d("MAP_TOUCH_ZOOM", "event 2 POINTER 1" + e2.getX(0) + " "
						+ e2.getY(0));
				Log.d("MAP_TOUCH_ZOOM", "event 2 POINTER 2" + e2.getX(1) + " "
						+ e2.getY(1));

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

				Log.d("MAP_TOUCH_ZOOM", "event Coor" + x + " " + y);
				float z = ((Math.abs(distanceY) + Math.abs(distanceX)) / 10);
				Log.d("MAP_TOUCH_ZOOM", "Zoom Faktor: " + z);
				// float z = 1;

				gesamt += z;
				Log.d("MAP_TOUCH_ZOOM", "Gesamt Zoom Faktor: " + z);

				this.oldX = e2.getX();
				this.oldY = e2.getY();

				mc.onZoom(z, new DisplayCoordinate(x, y));
			} else {

				Log.d("MAP_TOUCH", "MapTouch Scroll");
				// distanceY *= -1;
				if (e1.getY() > e2.getY()) {
					Log.d("MAP_TOUCH_SCROLL", "Rauf " + distanceY);
				} else {
					Log.d("MAP_TOUCH_SCROLL", "Runter " + -distanceY);
				}

				if (e1.getX() > e2.getX()) {
					Log.d("MAP_TOUCH_SCROLL", "Links " + distanceX);
				} else {
					Log.d("MAP_TOUCH_SCROLL", "Rechts " + -distanceX);
				}

				mc.onShift(distanceX, distanceY);
			}
			return true;
		}

		@Override
		public void onLongPress(MotionEvent event) {
			Log.d("MAP_TOUCH", "MapTouch Long Touch");
			mc.onCreatePoint(new DisplayCoordinate(event.getX(), event.getY()));
		}

		@Override
		public boolean onDown(MotionEvent e) {
			Log.d("MAP_TOUCH", "MapTouch Down");
			this.oldX = e.getX();
			this.oldY = e.getY();
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			Log.d("MAP_TOUCH", "MapTouch Show Press");

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			Log.d("MAP_TOUCH", "MapTouch TapUp");
			return false;
		}

	}

	/**
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class MapTouchEventListener implements OnTouchListener {

		float startX;
		float startY;

		float deltaX;
		float deltaY;

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (map.equals(view)) {
				Log.d("MAP_TOUCH", "MapTouch " + view.toString());
				gestureDetector.onTouchEvent(event);
				return true;
				// ziehen touch
				// zoom
				/*
				 * if (event.getAction() == MotionEvent.ACTION_DOWN) {
				 * Log.d("MAP_TOUCH", "MapTouch Down"); startX = event.getX();
				 * startY = event.getY(); return
				 * gestureDetector.onTouchEvent(event); }
				 * 
				 * if (event.getAction() == MotionEvent.ACTION_MOVE &&(
				 * (Math.abs(startX-event.getX())) > 100 ||
				 * (Math.abs(startY-event.getY())) > 100) &&
				 * !gestureDetector.isLongpressEnabled()) { Log.d("MAP_TOUCH",
				 * "MapTouch Move"); mc.onShift(new
				 * DisplayCoordinate(event.getX()-deltaX, event.getY()-deltaY));
				 * deltaX = event.getX(); deltaY = event.getY(); return true; }
				 * return false;
				 */
			}

			return false;
		}

	}

	/**
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class RouteOverlayTouchEventListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			Log.d("MAP_TOUCH", "RouteOverlayTouch");
			// TODO Auto-generated method stub
			//

			return false;
		}

	}

	/**
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class UserTouchEventListener implements OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			Log.d("MAP_TOUCH", "UserTouch");
			// TODO Auto-generated method stub
			//
			return false;
		}

	}

	private class WaypointTouchlistener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			Log.d("MAP_TOUCH", "UserTouch auf View ID:");
			// TODO Auto-generated method stub
			//
			return false;
		}
	}

	/**
	 * 
	 * @param b
	 */
	public void updateRouteOverlayImage(final Bitmap b) {

		if (routeOverlay == null) {
			routeOverlay = (ImageView) findViewById(R.id.mapview_overlay);
		}

		runOnUiThread(new Runnable() {
			public void run() {
				if (!b.isRecycled()) {
					routeOverlay.setImageBitmap(b);
					routeOverlay.setVisibility(View.VISIBLE);
				}
			}
		});
	}
}
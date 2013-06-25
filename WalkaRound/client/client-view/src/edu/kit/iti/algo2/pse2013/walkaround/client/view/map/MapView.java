package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import java.util.LinkedList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayPOI;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;

import edu.kit.iti.algo2.pse2013.walkaround.client.view.headup.HeadUpView;
//import android.widget.RelativeLayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.PullUpView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

//import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayCoordinate;

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
	//public static final int DEFAULT_POI_ACTIVE = R.drawable.poi_active;

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
	//private Drawable poiActive;

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
	 * Routen
	 */
	ImageView currentActive;
	RelativeLayout routeList;
	RelativeLayout poiList;
	int sizeOfPoints;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Initialisiere MapController.");
		mc = MapController.initialize(this);

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
		//poiActive = this.getResources().getDrawable(DEFAULT_POI_ACTIVE);

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Rufe Display ab.");

		Display display = this.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Initialisere Route und POI Ovetrlay.");
		routeList = (RelativeLayout) this.findViewById(R.id.routeList);
		poiList = (RelativeLayout) this.findViewById(R.id.poiList);
		sizeOfPoints = (int) (size.y/10);
		
		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Karte wird erstellt.");
		map = (ImageView) this.findViewById(R.id.mapview_map);
		map.setOnTouchListener(new MapTouchEventListener());
		// map.setImageBitmap(this.getDefaultFogScreen());

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "RouteOverlay wird erstellt.");
		routeOverlay = (ImageView) this.findViewById(R.id.mapview_overlay);
		routeOverlay.setOnTouchListener(new RouteOverlayTouchEventListener());

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
		Log.d(TAG_MAPVIEW, "Fragment PullUpMenü wird eingebaut");
		Fragment pullUp = new PullUpView();
		ft.add(R.id.pullUpMain, pullUp).commit();

		// ---------------------------------------------
		Log.d(TAG_MAPVIEW, "Fragment headUp wird eingebaut");
		Fragment headUp = new HeadUpView();
		ft.add(R.id.headupmain, headUp);

		// -----------------------TEST---------------------
		Log.d(TAG_MAPVIEW, "User wird in die Mitte gestellt.");
		this.setUserPositionOverlayImage(new DisplayCoordinate(
				(float) size.x / 2, (float) size.y / 2), 180);
		

		Log.d(TAG_MAPVIEW, "ein paar DisplayCoordinaten werden hinzugefügt");
		DisplayWaypoint[] list = new DisplayWaypoint[3];
		list[0] = new DisplayWaypoint(50,150,1);
		list[1] = new DisplayWaypoint(250,200,2);
		list[2] = new DisplayWaypoint(500,300,3);
		
		updateDisplayCoordinate(list);

		Log.d(TAG_MAPVIEW, "ein paar DisplayCoordinaten werden hinzugefügt");
		DisplayPOI[] list2 = new DisplayPOI[3];
		list2[0] = new DisplayPOI(250,350,4);
		list2[1] = new DisplayPOI(450,400,5);
		list2[2] = new DisplayPOI(700,500,6);
		
		updateDisplayCoordinate(list2);
		

		Log.d(TAG_MAPVIEW, "Ein Punkt wird aktiv gesetzt");
		this.setActive(3);
	}

	/**
	 * Updatet die Karte
	 * 
	 * @param b
	 */
	public void updateMapImage(Bitmap b) {
		this.map.setImageBitmap(b);
	}

	/**
	 * Updatet das Routen Overlay
	 * 
	 * @param b
	 */
	public void updateRouteOverlayImage(Bitmap b) {
		this.routeOverlay.setImageBitmap(b);
	}

	/**
	 * 
	 * 
	 * @param dw
	 */
	public void updateDisplayCoordinate(DisplayWaypoint[] dw) {

		routeList.removeAllViews();
		currentActive = null;

		for (DisplayWaypoint value : dw) {
			ImageView iv = new ImageView(this);
			iv.setImageDrawable(waypoint);
			iv.setY(value.getY());
			iv.setX(value.getX());
			iv.setTag(value.getId());
			iv.setVisibility(View.VISIBLE);
			iv.setLayoutParams(new LayoutParams(sizeOfPoints,sizeOfPoints));
			//iv.getLayoutParams().width = sizeOfPoints;
			//iv.getLayoutParams().height = sizeOfPoints;
			iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			routeList.addView(iv);
		}

		if (routeList.getChildCount() > 0) {
			ImageView iv = (ImageView) routeList.getChildAt(0);
			iv.setImageDrawable(flag);

			iv = (ImageView) routeList
					.getChildAt((routeList.getChildCount() - 1));
			iv.setImageDrawable(flagTarget);
		}
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
			iv.setLayoutParams(new LayoutParams(sizeOfPoints,sizeOfPoints));
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
		// TODO is equal von Location prüfen

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
	private class MapTouchEventListener implements OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			Log.d("MAP_TOUCH", "MapTouch");
			// TODO Auto-generated method stub
			// ziehen touch
			// zoom
			//
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
		public boolean onTouch(View arg0, MotionEvent arg1) {
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

	private class WaypointTouchlistener implements OnTouchListener{

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			Log.d("MAP_TOUCH", "UserTouch auf View ID:");
			// TODO Auto-generated method stub
			//
			return false;
		}
	}
	
	/*
	 * Erstellt ein Muster aus einer Bitmap
	 * 
	 * Nicht mehr notwendig - möglicherweise für Mapmodel interressant
	 * 
	 * @return
	 * 
	 * private Bitmap getDefaultFogScreen() {
	 * 
	 * Log.d("MAP_VIEW", "Rufe Display ab."); Display display =
	 * getWindowManager().getDefaultDisplay(); Point size = new Point();
	 * display.getSize(size);
	 * 
	 * Log.d("MAP_VIEW", "Display wurde abgerufen. Breite: " + size.x +
	 * " Höhe: " + size.y);
	 * 
	 * Bitmap fog = BitmapFactory.decodeResource(getResources(),
	 * DEFAULT_PATTERN);
	 * 
	 * int fogWidth = fog.getWidth();
	 * 
	 * int width = ((int) Math.ceil((double) size.x) / fogWidth) * fogWidth; int
	 * height = ((int) Math.ceil((double) size.y) / fogWidth) * fogWidth;
	 * 
	 * Log.d("MAP_VIEW", "Höhen wurden erstellt:" + width + " * " + height +
	 * " wurde abgerufen.");
	 * 
	 * Bitmap result = Bitmap.createBitmap(width, height,
	 * Bitmap.Config.ARGB_8888);
	 * 
	 * Canvas canvas = new Canvas(result); for (int x = width / fogWidth; x >=
	 * 0; x--) { for (int y = height / fogWidth; y >= 0; y--) {
	 * canvas.drawBitmap(fog, (x * fogWidth), (y * fogWidth), null); } }
	 * Log.d("MAP_VIEW", "Fog wurde erstellt.");
	 * 
	 * return Bitmap.createScaledBitmap(result, size.x, size.y, false);
	 * 
	 * }
	 */
}

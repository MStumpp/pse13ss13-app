package edu.kit.iti.algo2.pse2013.walkaround.client.controller.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager.UpdateFavorites;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.CenterListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.LevelOfDetailListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.ScaleListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.CompassManager.CompassListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager.PositionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TextToSpeechUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.POIView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.POIView.POIInfoListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.RouteView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.WaypointView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.HeadUpView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.HeadUpView.HeadUpViewListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.PullUpView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.POILayout.POIChangeListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Roundtrip.ComputeRoundtripListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Search.UpdateMapListener;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

/**
 * This is the main Activity of WalkaRound. This class works like a controller.
 * 
 * @author Ludwig Biermann
 * @version 8.0
 * 
 */
public class WalkaRound extends Activity implements HeadUpViewListener,
		PositionListener, CompassListener, RouteListener, UpdateFavorites,
		UpdateMapListener, ComputeRoundtripListener, POIChangeListener,
		POIInfoListener, CenterListener, LevelOfDetailListener,
		ProgressListener, ScaleListener {

	private MapView mapView;
	private GestureDetector gestureDetector;
	private BoundingBox coorBox;
	private HeadUpView headUpView;
	private static String TAG = WalkaRound.class.getSimpleName();
	private boolean userLock = true;
	private ImageView user;
	private int userDiff;
	private WaypointView waypointView;
	private PullUpView pullUpView;
	private POIView poiView;
	private RouteView routeView;
	private float gesamt;
	private boolean isZomm = false;
	@SuppressWarnings("unused")
	private float targetX;
	@SuppressWarnings("unused")
	private float targetY;
	private Coordinate userCoordinate = BoundingBox.defaultCoordinate;
	private RelativeLayout progress;
	private static int ROUNDTRIP_TIME = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		coorBox = BoundingBox.getInstance(this);
		coorBox.registerCenterListener(this);
		coorBox.registerLevelOfDetailListener(this);
		Log.d(TAG, "initialisiere BoundingBox "
				+ coorBox.getDisplaySize().toString());
		PositionManager.initialize(this);

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			gpsAlert();
		}

		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (!mWifi.isConnected()) {
			wifiAlert();
		}

		// MapView

		mapView = (MapView) findViewById(R.id.mapView);
		gestureDetector = new GestureDetector(this.getApplicationContext(),
				new MapGestureListener());

		// HeadUpView
		headUpView = (HeadUpView) findViewById(R.id.headUpView);
		headUpView.registerListener(this);

		// poiView
		poiView = (POIView) findViewById(R.id.poiView);
		routeView = (RouteView) this.findViewById(R.id.routeView);

		RelativeLayout.LayoutParams paramUser = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramUser.width = coorBox.getDisplaySize().x / 10;
		paramUser.height = coorBox.getDisplaySize().x / 10;
		userDiff = coorBox.getDisplaySize().x / 10 / 2;

		RelativeLayout rl = (RelativeLayout) this
				.findViewById(R.id.mapviewmain);

		user = new ImageView(this);
		user.setImageDrawable(this.getResources().getDrawable(
				R.drawable.user_arrow));
		user.setX(coorBox.getDisplaySize().x / 2 - userDiff);
		user.setY(coorBox.getDisplaySize().y / 2 - userDiff);
		rl.addView(user, paramUser);

		PositionManager.getInstance().registerPositionListener(this);
		PositionManager.getInstance().getCompassManager()
				.registerCompassListener(this);

		waypointView = (WaypointView) this.findViewById(R.id.waypointView);

		mapView.computeParams();
		TileFetcher.getInstance().requestTiles(coorBox, mapView);
		pullUpView = (PullUpView) this.findViewById(R.id.pullUpView);
		pullUpView.bringToFront();

		this.findViewById(R.id.mapviewmain).requestLayout();
		this.findViewById(R.id.mapviewmain).invalidate();

		RouteController.getInstance().registerRouteListener(this);
		FavoriteManager.getInstance(this).registerListener(this);
		pullUpView.registerComputeRoundtripListener(this);
		pullUpView.registerPOIChangeListener(this);
		poiView.registerPOIInfoListener(this);
		pullUpView.registerUpdateMapListener(this);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		mapView.setOnTouchListener(new MapListener());

		this.findViewById(R.id.progressBar1).setOnTouchListener(
				new ProgressTouchListener());
		this.findViewById(R.id.progressBar1).bringToFront();
		progress = (RelativeLayout) this.findViewById(R.id.progressBarMain);
		progress.bringToFront();
		progress.setOnTouchListener(new ProgressTouchListener());
		this.findViewById(R.id.imageView1).setOnTouchListener(
				new ProgressTouchListener());

		this.findViewById(R.id.mapviewmain).requestLayout();
		this.findViewById(R.id.mapviewmain).invalidate();

		progress.setVisibility(View.GONE);

		POIManager.getInstance(this).registerProgressListener(this);

		BoundingBox.getInstance().registerScaleListener(this);
	}

	class ProgressTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			return true;
		}

	}

	private class MapGestureListener implements OnGestureListener {

		@Override
		public boolean onDown(MotionEvent event) {
			Log.d(TAG, "MapTouch Down");
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
				float velocityX, float velocityY) {
			Log.d(TAG, "MapTouch Fling");
			/*
			if (mode != MapListener.ZOOM) {
				if(RouteController.getInstance().getCurrentRoute().getActiveWaypoint() != null){
					RouteController.getInstance().deleteActiveWaypoint();
				}
			}
			*/
			return false;
		}

		@Override
		public void onLongPress(MotionEvent event) {
			if (mode != MapListener.ZOOM) {
				Log.d(TAG, "MapTouch Long Touch");
			}

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (mode != MapListener.ZOOM) {
				Log.d(TAG, "MapTouch SCroll");
				coorBox.shiftCenter(distanceX, distanceY);
				userLock = false;
				headUpView.setUserPositionLock(isRestricted());
				updateUser();
				poiView.updatePOIView();
			}
			return true;
		}

		@Override
		public void onShowPress(MotionEvent event) {

			if (mode != MapListener.ZOOM) {
				Log.d(TAG, "MapTouch Press");
				Coordinate next = CoordinateUtility
						.convertDisplayCoordinateToCoordinate(
								new DisplayCoordinate(event.getX(), event
										.getY()), coorBox.getTopLeft(), coorBox
										.getLevelOfDetail());
				addWaypointAlert(next);
			}
		}

		@Override
		public boolean onSingleTapUp(MotionEvent event) {
			if (mode != MapListener.ZOOM) {
				Log.d(TAG, "MapTouch Single Tap");
			}
			return false;
		}

	}

	/**
	 * Helper Method to create Alert
	 * 
	 * @param next
	 */
	public void addWaypointAlert(final Coordinate next) {
		Log.d(TAG, "Add waypoint!");
		Waypoint wp = new Waypoint(next.getLatitude(), next.getLongitude(),
				getString(R.string.unknown_location));
		RouteController.getInstance().addWaypoint(wp);
	}

	boolean mode = MapListener.NONE;

	/**
	 * 
	 * @author Ludwig Biermann
	 * @version 1.0
	 * 
	 */
	public class MapListener implements OnTouchListener {

		static final boolean NONE = false;
		static final boolean ZOOM = true;
		static final float MIN_DIST = 10f;

		PointF start = new PointF();
		PointF mid = new PointF();
		float oldDist = 1f;

		float saveDist = -1;

		PointF dragOld = new PointF();

		private float maxZoom;
		private float minZoom;

		/**
		 * 
		 */
		public MapListener() {
			maxZoom = 2;
			minZoom = 0.5f;
		}
		
		static final int ZOOM_PER_PX = 12500;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				start.set(event.getX(), event.getY());
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				Log.d(TAG, "oldDist=" + oldDist);
				if (oldDist > MIN_DIST) {
					midPoint(mid, event);
					mode = ZOOM;
					Log.d(TAG, "mode=ZOOM");
				}
				break;
			case MotionEvent.ACTION_UP:
				dragOld = null;
				saveDist = oldDist;
				break;
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				Log.d(TAG, "mode=NONE");
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == ZOOM) {
					float newDist = spacing(event);
					Log.d(TAG, "newDist=" + newDist);
					if (newDist > MIN_DIST) {
						//float newScale = newDist / oldDist;
						float newScale = BoundingBox.getInstance().getScale() + ((newDist - oldDist) /ZOOM_PER_PX)
								;
						Log.d("Scale", "Level A: " + newScale);
						if (newScale >= maxZoom) {
							BoundingBox.getInstance().setLevelOfDetailByADelta(
									1);
							newScale = 1;
						} else if (newScale <= minZoom) {
							BoundingBox.getInstance().setLevelOfDetailByADelta(
									-1);
							newScale = maxZoom;
						}
						Log.d("Scale", "Level S: " + newScale);

						BoundingBox.getInstance().setScale(newScale);
						// TODO allow scaling to another Point as the center
						// Point
						// BoundingBox.getInstance().setPivot(mid);
					}
				}
				break;
			}

			return gestureDetector.onTouchEvent(event);
		}

		/**
		 * 
		 * @param event
		 * @return
		 */
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return (float) Math.sqrt(x * x + y * y);
		}

		/**
		 * 
		 * @param point
		 * @param event
		 */
		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}

	}

	@Override
	public void callPoiInfo(POI poi) {
		pullUpView.updateInfoView(poi);
	}

	/**
	 * Helper Method to create Alert
	 */
	public void gpsAlert() {
		Log.d(TAG, "ALERT");

		AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle("GPS Signal");
		alert.setMessage(getString(R.string.dialog_text_gps_inactive));
		alert.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(R.string.option_yes),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				});

		alert.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(R.string.option_no),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.cancel();
					}
				});

		alert.show();
	}

	@Override
	public void onBackPressed() {
		this.moveTaskToBack(true);
	}

	@Override
	public void onCenterChange(Coordinate center) {
		this.updateUser();
	}

	@Override
	public void onCompassChange(float direction) {
		user.setRotation(direction);
	}

	@Override
	public void onComputeRoundtrip(int profile, int length) {
		if (RouteController.getInstance().getCurrentRoute().getWaypoints()
				.isEmpty()) {

			Coordinate next = CoordinateUtility
					.convertDisplayCoordinateToCoordinate(
							new DisplayCoordinate(user.getX(), user.getY()),
							coorBox.getTopLeft(), coorBox.getLevelOfDetail());

			RouteController.getInstance().addWaypoint(
					new Waypoint(next.getLatitude(), next.getLongitude(), ""));
			try {
				Thread.sleep(ROUNDTRIP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (RouteController.getInstance().getCurrentRoute().getActiveWaypoint() == null
				&& !RouteController.getInstance().getCurrentRoute()
						.getWaypoints().isEmpty()) {
			RouteController.getInstance().setActiveWaypoint(
					RouteController.getInstance().getCurrentRoute()
							.getWaypoints().getLast());
		}

		if (RouteController.getInstance().getCurrentRoute().getActiveWaypoint() != null) {
			RouteController.getInstance().addRoundtrip(profile, length);
		} else {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(getString(R.string.dialog_header_add,
					getString(R.string.term_boomerang)));
			alertDialog.setMessage(getString(R.string.dialog_text_boomerang));
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Do nothing
						}
					});
			alertDialog.show();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.arrow_game, menu);
		return true;
	}

	public void serverConnectionAlert() {
		Log.d(TAG, "ServerConnectionAlert!");
		AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle(getString(R.string.dialog_header_no_server_connection));
		alert.setMessage(getString(R.string.dialog_text_no_server_connection));

		alert.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(R.string.option_ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.cancel();
					}
				});

		alert.show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		TextToSpeechUtility.shutdown();
		Log.d(TAG, "Destroy WalkAround");
	}

	@Override
	public void onLevelOfDetailChange(float levelOfDetail) {
		this.updateUser();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	@Override
	public void onOptionPressed() {
		Intent intent = new Intent(this, Option.class);
		this.startActivity(intent);
	}

	@Override
	public void onPOIChange() {
		poiView.updatePOIView();
	}

	@Override
	public void onPositionChange(Location androidLocation) {

		userCoordinate = new Coordinate(androidLocation.getLatitude(),
				androidLocation.getLongitude());

		updateUser();

		if (userLock) {
			coorBox.setCenter(userCoordinate);
			user.setX(coorBox.getDisplaySize().x / 2 - userDiff);
			user.setY(coorBox.getDisplaySize().y / 2 - userDiff);
		}
	}

	@Override
	public void onRouteChange(RouteInfo currentRoute) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				waypointView.updateWaypoint();
				pullUpView.updateRoute();
			}
		});
	}

	@Override
	public void onUserLock() {
		if (userLock) {
			userLock = false;
		} else {
			userLock = true;
			coorBox.setCenter(userCoordinate);
			// mapView.computeParams();
			// tileFetcher.requestTiles(coorBox, mapView);
			user.setX(coorBox.getDisplaySize().x / 2 - userDiff);
			user.setY(coorBox.getDisplaySize().y / 2 - userDiff);
			// waypointView.updateWaypoint();
		}
		headUpView.setUserPositionLock(userLock);
	}

	@Override
	public void onZoom(float delta) {
		this.coorBox.setLevelOfDetailByADelta(delta);
		poiView.updatePOIView();
		// this.mapView.computeParams();
		// this.tileFetcher.requestTiles(coorBox, mapView);
		// waypointView.updateWaypoint();
	}

	@Override
	public String toString() {
		return WalkaRound.class.getSimpleName();
	}

	@Override
	public void updateFacorites() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pullUpView.updateFavorite();
			}
		});
	}

	@Override
	public void updateMap() {
		// mapView.computeParams();
		// tileFetcher.requestTiles(coorBox, mapView);
		// this.updateUser();
		// waypointView.updateWaypoint();

	}

	private void updateUser() {

		float scale = BoundingBox.getInstance().getScale();

		double lon = -coorBox.getCenter().getLongitude()
				+ userCoordinate.getLongitude();
		double lat = -userCoordinate.getLatitude()
				+ coorBox.getCenter().getLatitude();

		float x = CoordinateUtility.convertDegreesToPixels(lon,
				coorBox.getLevelOfDetail(),
				CoordinateUtility.DIRECTION_LONGITUDE) * scale;

		float y = CoordinateUtility.convertDegreesToPixels(lat,
				coorBox.getLevelOfDetail(),
				CoordinateUtility.DIRECTION_LATITUDE) * 0.75f * scale;

		Log.d("UserPos", " x: " + x + " y: " + y);

		x += coorBox.getDisplaySize().x / 2;
		y += coorBox.getDisplaySize().y / 2;

		Log.d("UserPos", " x: " + x + " y: " + y);

		user.setX(x - userDiff);
		user.setY(y - userDiff);

	}

	/**
	 * Helüer Alert to create Wifi Alert
	 */
	public void wifiAlert() {
		Log.d(TAG, "ALERT");

		AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle(getString(R.string.dialog_header_wifi));
		alert.setMessage(getString(R.string.dialog_text_wifi));
		alert.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(R.string.option_yes),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(
								android.provider.Settings.ACTION_WIFI_SETTINGS));
					}
				});

		alert.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(R.string.option_no),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.cancel();
					}
				});

		alert.show();
	}

	@Override
	public void showProgress() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "show progress");
				progress.setVisibility(View.VISIBLE);
				progress.bringToFront();
			}
		});
	}

	@Override
	public void hideProgress() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "hide progress");
				progress.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onScaleChange(float scale) {
		this.updateUser();
	}
}
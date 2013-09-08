package edu.kit.iti.algo2.pse2013.walkaround.client.controller.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import android.widget.RelativeLayout.LayoutParams;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager.UpdateFavorites;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.CenterListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox.LevelOfDetailListener;
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

public class WalkaRound extends Activity implements HeadUpViewListener,
		PositionListener, CompassListener, RouteListener, UpdateFavorites,
		UpdateMapListener, ComputeRoundtripListener, POIChangeListener,
		POIInfoListener, CenterListener, LevelOfDetailListener {

	private MapView mapView;

	GestureDetector gestureDetector;

	BoundingBox coorBox;

	TileFetcher tileFetcher;

	private HeadUpView headUpView;

	private static String TAG = WalkaRound.class.getSimpleName();

	private boolean userLock = true;

	private ImageView user;
	private int userDiff;

	private WaypointView waypointView;

	private PullUpView pullUpView;

	private POIView poiView;

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
		
	    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER ) ) {
			gpsAlert();
		}
	    
	    ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

	    if (!mWifi.isConnected()) {
			wifiAlert();
	    }

		
		// BoundingBox & tiles

		this.tileFetcher = TileFetcher.getInstance();

		// MapView

		mapView = (MapView) findViewById(R.id.mapView);
		gestureDetector = new GestureDetector(this.getApplicationContext(),
				new MapGestureListener());
		this.tileFetcher.requestTiles(coorBox, mapView);

		// HeadUpView
		headUpView = (HeadUpView) findViewById(R.id.headUpView);
		headUpView.registerListener(this);

		// poiView
		poiView = (POIView) findViewById(R.id.poiView);

		RelativeLayout.LayoutParams paramUser = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
		tileFetcher.requestTiles(coorBox, mapView);
		pullUpView = (PullUpView) this.findViewById(R.id.pullUpView);
		pullUpView.bringToFront();

		RouteController.getInstance().registerRouteListener(this);
		FavoriteManager.getInstance(this).registerListener(this);
		pullUpView.registerComputeRoundtripListener(this);
		pullUpView.registerPOIChangeListener(this);
		poiView.registerPOIInfoListener(this);
		pullUpView.registerUpdateMapListener(this);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		mapView.setOnTouchListener(new MapListener());
		
	}

	public class MapListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			int action = event.getAction();
			if (event.getPointerCount() >= 2 && action == MotionEvent.ACTION_DOWN && !isZomm) {
				float deltaX = Math.abs(event.getX(0) - event.getX(1));
				float deltaY = Math.abs(event.getY(0) - event.getY(1));


				gesamt = (float) Math.sqrt(Math.pow(
						(double) Math.abs(event.getX(0) - event.getX(1)), 2)
						* Math.pow((double) Math.abs(event.getY(1) - event.getY(1)),
								2));
				
				if (event.getX(0) < event.getX(1)) {
					targetX += (event.getX(0));
				} else {
					targetX += (event.getX(1));
				}
				targetX += deltaX;

				if (event.getY(0) < event.getY(1)) {
					targetY += (event.getY(0));
				} else {
					targetY += (event.getY(1));
				}
				targetY += deltaY;
				isZomm = true;
			}
			if (action == MotionEvent.ACTION_UP) {
				isZomm = false;
			}

			return gestureDetector.onTouchEvent(event);
		}

	}
	float gesamt;
	boolean isZomm = false;
	float targetX;
	float targetY;

	private class MapGestureListener implements OnGestureListener {

		int twoFingerZoomOffset = 1;

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			if (e2.getPointerCount() >= 2) {
				//Log.d(TAG, "Finger1: " + e2.getX(0)+ ":" + e2.getY(0));
				//Log.d(TAG, "Finger2: " + e2.getX(1)+ ":" + e2.getY(1));


				float fingerDiff = (float) Math.sqrt(Math.pow(
						(double) Math.abs(e2.getX(0) - e2.getX(1)), 2)
						* Math.pow((double) Math.abs(e2.getY(1) - e2.getY(1)),
								2));
				
				
				float diff = (float) Math.sqrt(Math.abs(Math.pow(distanceX, 2)
						* Math.pow(distanceY, 2)));
				
				diff /= twoFingerZoomOffset;
				
				diff = (int) diff;
				Log.d(TAG, "new Zooming Offset: " + diff);

				if(fingerDiff < gesamt){
					coorBox.setLevelOfDetailByADelta(-diff);
					Log.d(TAG, "new Zooming Offset: -" + diff);
				} else {
					coorBox.setLevelOfDetailByADelta(diff);
					Log.d(TAG, "new Zooming Offset: " + diff);
				}
				gesamt = fingerDiff;
				
				
				//TODO new Zooming Point
				//coorBox.setCenter(new DisplayCoordinate(targetX, targetY));
			} else {
				Log.d(TAG, "MapTouch SCroll");
				coorBox.shiftCenter(distanceX, distanceY);
			}
			userLock = false;
			headUpView.setUserPositionLock(isRestricted());
			//mapView.computeParams();
			//tileFetcher.requestTiles(coorBox, mapView);
			updateUser();
			poiView.updatePOIView();
			//waypointView.updateWaypoint();
			return true;
		}

		public void onLongPress(MotionEvent event) {
			Log.d(TAG, "MapTouch Long Touch");

		}

		@Override
		public boolean onDown(MotionEvent event) {
			Log.d(TAG, "MapTouch Down");
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			Log.d(TAG, "MapTouch Fling");
			return false;
		}

		@Override
		public void onShowPress(MotionEvent event) {
			Log.d(TAG, "MapTouch Press");
			Coordinate next = CoordinateUtility
					.convertDisplayCoordinateToCoordinate(
							new DisplayCoordinate(event.getX(), event.getY()),
							coorBox.getTopLeft(), coorBox.getLevelOfDetail());

			RouteController.getInstance().addWaypoint(
					new Waypoint(next.getLatitude(), next.getLongitude(),
							"PLACEHOLDER"));

		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			Log.d(TAG, "MapTouch Single Tap");
			// TODO Auto-generated method stub
			return false;
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.arrow_game, menu);
		return true;
	}

	@Override
	public void onZoom(float delta) {
		this.coorBox.setLevelOfDetailByADelta(delta);
		//this.mapView.computeParams();
		//this.tileFetcher.requestTiles(coorBox, mapView);
		//waypointView.updateWaypoint();
	}

	@Override
	public void onOptionPressed() {
		Intent intent = new Intent(this, Option.class);
		this.startActivity(intent);
	}

	@Override
	public void onUserLock() {
		if (userLock) {
			userLock = false;
		} else {
			userLock = true;
			coorBox.setCenter(userCoordinate);
			//mapView.computeParams();
			//tileFetcher.requestTiles(coorBox, mapView);
			user.setX(coorBox.getDisplaySize().x / 2 - userDiff);
			user.setY(coorBox.getDisplaySize().y / 2 - userDiff);
			//waypointView.updateWaypoint();
		}
		headUpView.setUserPositionLock(userLock);
	}

	Coordinate userCoordinate = BoundingBox.defaultCoordinate;

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

	private void updateUser() {
		double lon = -coorBox.getCenter().getLongitude()
				+ userCoordinate.getLongitude();
		double lat = -userCoordinate.getLatitude()
				+ coorBox.getCenter().getLatitude();

		float x = CoordinateUtility.convertDegreesToPixels(lon,
				coorBox.getLevelOfDetail(),
				CoordinateUtility.DIRECTION_LONGITUDE);

		float y = CoordinateUtility.convertDegreesToPixels(lat,
				coorBox.getLevelOfDetail(),
				CoordinateUtility.DIRECTION_LATITUDE) * 0.75f;

		Log.d("UserPos", " x: " + x + " y: " + y);

		x += coorBox.getDisplaySize().x / 2;
		y += coorBox.getDisplaySize().y / 2;

		Log.d("UserPos", " x: " + x + " y: " + y);

		user.setX(x - userDiff);
		user.setY(y - userDiff);

	}

	@Override
	public void onCompassChange(float direction) {
		user.setRotation(direction);
	}

	@Override
	public void onRouteChange(RouteInfo currentRoute) {
		runOnUiThread(new Runnable() {
			public void run() {
				waypointView.updateWaypoint();
				pullUpView.updateRoute();
			}
		});
	}

	@Override
	public void updateFacorites() {
		runOnUiThread(new Runnable() {
			public void run() {
				pullUpView.updateFavorite();
			}
		});
	}

	private static int ROUNDTRIP_TIME = 3000;

	@Override
	public void onComputeRoundtrip(int profile, int length) {
		if (RouteController.getInstance().getCurrentRoute().getWaypoints()
				.isEmpty()) {

			Coordinate next = CoordinateUtility
					.convertDisplayCoordinateToCoordinate(
							new DisplayCoordinate(user.getX(), user.getY()),
							coorBox.getTopLeft(), coorBox.getLevelOfDetail());

			RouteController.getInstance().addWaypoint(
					new Waypoint(next.getLatitude(), next.getLongitude(),
							"PLACEHOLDER"));
			try {
				Thread.sleep(ROUNDTRIP_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
			alertDialog.setTitle("Fehlender AusgangsPunkt");
			alertDialog
					.setMessage("Bitte setzten Sie zuerst einen Ausgangspunkt auf der Karte.");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			alertDialog.show();

		}

	}

	@Override
	public void onPOIChange() {
		poiView.updatePOIView();
	}

	@Override
	public void callPoiInfo(POI poi) {
		pullUpView.updateInfoView(poi);

	}

	@Override
	public void updateMap() {
		//mapView.computeParams();
		//tileFetcher.requestTiles(coorBox, mapView);
		//this.updateUser();
		//waypointView.updateWaypoint();

	}

	public void gpsAlert() {
		Log.d(TAG, "ALERT");
		
		AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle("GPS Signal");
		alert.setMessage("Ihr GPS ist nicht aktiviert. Soll es aktiviert werden?");
		alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				});

		alert.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                 dialog.cancel();
            }
        });
		
		alert.show();
	}


	public void wifiAlert() {
		Log.d(TAG, "ALERT");
		
		AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle("Wifi Signal");
		alert.setMessage("Es konnte keine Verbindung zum Internet festgestellt werden.");
		alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
					}
				});

		alert.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                 dialog.cancel();
            }
        });
		
		alert.show();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		TextToSpeechUtility.getInstance().shutdown();
		Log.d(TAG, "Destroy WalkAround");
	}

	@Override
	public void onBackPressed() {
        this.moveTaskToBack(true);
	}

	@Override
	public void onLevelOfDetailChange(float levelOfDetail) {
		this.updateUser();
	}

	@Override
	public void onCenterChange(Coordinate center) {
		this.updateUser();		
	}
	
	@Override
	public String toString(){
		return WalkaRound.class.getSimpleName();
	}
}

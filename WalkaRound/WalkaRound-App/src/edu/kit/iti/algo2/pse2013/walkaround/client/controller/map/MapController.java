package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpViewListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager.UpdateFavorites;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.CompassListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.headup.HeadUpView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.POIView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.POIView.POIInfoListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.RouteView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.WaypointView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.PullUpView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.views.POILayout.POIChangeListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.views.Roundtrip.ComputeRoundtripListener;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class MapController extends Activity implements HeadUpViewListener,
		PositionListener, CompassListener, RouteListener, UpdateFavorites, ComputeRoundtripListener, POIChangeListener, POIInfoListener {

	private MapView mapView;

	GestureDetector gestureDetector;

	BoundingBox coorBox;

	TileFetcher tileFetcher;

	private HeadUpView headUpView;

	private static String TAG = MapController.class.getSimpleName();

	private boolean userLock = true;

	private RouteView routeView;
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
		Log.d(TAG, "initialisiere BoundingBox "
				+ coorBox.getDisplaySize().toString());
		PositionManager.initialize(this);

		// BoundingBox & tiles

		this.tileFetcher = new TileFetcher();

		// MapView

		mapView = (MapView) findViewById(R.id.mapView);
		gestureDetector = new GestureDetector(this, gestureListener);

		this.tileFetcher.requestTiles(coorBox, mapView);

		// HeadUpView
		headUpView = (HeadUpView) findViewById(R.id.headUpView);
		headUpView.registerListener(this);

		// RouteView
		routeView = (RouteView) findViewById(R.id.routeView);

		//poiView
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
		PositionManager.getInstance().getCompassManager().registerCompassListener(this);

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
		
		
		getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			coorBox.shiftCenter(distanceX, distanceY);
			userLock = false;
			headUpView.setUserPositionLock(isRestricted());
			mapView.computeParams();
			tileFetcher.requestTiles(coorBox, mapView);
			updateUser();
			poiView.updatePOIView();
			waypointView.updateWaypoint();
			return true;
		}

		public void onLongPress(MotionEvent event) {
			Log.d(TAG, "MapTouch Long Touch");

			Coordinate next = CoordinateUtility
					.convertDisplayCoordinateToCoordinate(
							new DisplayCoordinate(event.getX(), event.getY()),
							coorBox.getTopLeft(), coorBox.getLevelOfDetail());

			RouteController.getInstance().addWaypoint(
					new Waypoint(next.getLatitude(), next.getLongitude(),
							"PLACEHOLDER"));
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
		this.mapView.computeParams();
		this.tileFetcher.requestTiles(coorBox, mapView);
		waypointView.updateWaypoint();
	}

	@Override
	public void onOptionPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserLock() {
		if (userLock) {
			userLock = false;
		} else {
			userLock = true;
			coorBox.setCenter(userCoordinate);
			mapView.computeParams();
			tileFetcher.requestTiles(coorBox, mapView);
			user.setX(coorBox.getDisplaySize().x / 2 - userDiff);
			user.setY(coorBox.getDisplaySize().y / 2 - userDiff);
			waypointView.updateWaypoint();
		}
		headUpView.setUserPositionLock(userLock);
	}

	Coordinate userCoordinate = BoundingBox.defaultCoordinate;

	@Override
	public void onPositionChange(Location androidLocation) {
		userCoordinate = new Coordinate(androidLocation.getLatitude(),
				androidLocation.getLongitude());
		updateUser();
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

		if (userLock) {
			coorBox.setCenter(userCoordinate);
			mapView.computeParams();
			tileFetcher.requestTiles(coorBox, mapView);
			user.setX(coorBox.getDisplaySize().x / 2 - userDiff);
			user.setY(coorBox.getDisplaySize().y / 2 - userDiff);
		}
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
		if(RouteController.getInstance().getCurrentRoute().getWaypoints().isEmpty()){

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
		
		if(RouteController.getInstance().getCurrentRoute().getActiveWaypoint() == null && !RouteController.getInstance().getCurrentRoute().getWaypoints().isEmpty()){
			RouteController.getInstance().setActiveWaypoint(RouteController.getInstance().getCurrentRoute().getWaypoints().getLast());
		}
		
		if(RouteController.getInstance().getCurrentRoute().getActiveWaypoint() != null){
			RouteController.getInstance().addRoundtrip(profile, length);
		} else {
				AlertDialog alertDialog = new AlertDialog.Builder(this)
						.create();
				alertDialog.setTitle("Fehlender AusgangsPunkt");
				alertDialog.setMessage("Bitte setzten Sie zuerst einen Ausgangspunkt auf der Karte.");
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

}

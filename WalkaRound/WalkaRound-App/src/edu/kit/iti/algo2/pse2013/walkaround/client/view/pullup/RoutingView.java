package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import java.util.Iterator;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.FavoriteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class RoutingView extends Fragment implements RouteListener {

	private final String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupRoutingSwitcher;

	private FavoriteMenuController favController;
	private RouteController routeController;

	private RouteInfo lastKnownRoute;

	private Button reset;
	private ImageView invert;
	private ImageView tsp;
	private ImageView load;
	private ImageView save;
	private Button addFavorite;
	private Button goToMap;
	private LinearLayout layout;

	private static boolean isListener = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create RoutingView");

		favController = FavoriteMenuController.getInstance();
		routeController = RouteController.getInstance();

		Log.d(TAG_PULLUP_CONTENT, "Register on route controller");
		if (!isListener) {
			routeController.registerRouteListener(this);
			isListener = true;
		}

		reset = (Button) this.getActivity().findViewById(R.id.reset);
		invert = (ImageView) this.getActivity().findViewById(R.id.invert);
		tsp = (ImageView) this.getActivity().findViewById(R.id.tsp);
		load = (ImageView) this.getActivity().findViewById(R.id.load);
		save = (ImageView) this.getActivity().findViewById(R.id.save);
		addFavorite = (Button) this.getActivity().findViewById(
				R.id.add_favorite);
		goToMap = (Button) this.getActivity().findViewById(R.id.go_to_map);
		layout = (LinearLayout) getActivity().findViewById(R.id.waylist);

		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		Log.d(TAG_PULLUP_CONTENT, "Einstellen der Größenverhältnisse");
		reset.setX(size.x / 5 * 0);
		reset.getLayoutParams().width = size.x / 5;
		reset.getLayoutParams().height = size.y / 8;
		invert.setX(size.x / 5 * 1);
		invert.getLayoutParams().width = size.x / 5;
		invert.getLayoutParams().height = size.y / 8;
		tsp.setX(size.x / 5 * 2);
		tsp.getLayoutParams().width = size.x / 5;
		tsp.getLayoutParams().height = size.y / 8;
		load.setX(size.x / 5 * 3);
		load.getLayoutParams().width = size.x / 5;
		load.getLayoutParams().height = size.y / 8;
		save.setX(size.x / 5 * 4);
		save.getLayoutParams().width = size.x / 5;
		save.getLayoutParams().height = size.y / 8;
		addFavorite.setX(size.x * 0);
		addFavorite.getLayoutParams().width = size.x / 2;
		goToMap.setX(size.x / 2);
		goToMap.getLayoutParams().width = size.x / 2;

		Log.d(TAG_PULLUP_CONTENT, "Zuweisung der Listener");
		reset.setOnTouchListener(new resetListener());
		invert.setOnTouchListener(new invertListener());
		tsp.setOnTouchListener(new tspListener());
		load.setOnTouchListener(new loadListener());
		save.setOnTouchListener(new saveListener());
		addFavorite.setOnTouchListener(new favoriteListener());
		goToMap.setOnTouchListener(new backToMapListener());

		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG_PULLUP_CONTENT, "Destroy RoutingView");
		routeController.unregisterRouteListener(this);
		this.getActivity().findViewById(switcher).setVisibility(View.GONE);
	}

	public boolean equals(Fragment f) {
		if (f.toString().equals(PullUpView.CONTENT_ROUTING)) {
			return true;
		}
		return false;
	}

	private class resetListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(reset) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "reset wurde gedr�ckt");
				routeController.resetRoute();
			}
			// TODO: refresh activity?
			return false;
		}

	}

	private class invertListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(invert)
					&& event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "invert wurde gedr�ckt");
				routeController.revertRoute();
			}
			// TODO: refresh activity?
			return false;
		}

	}

	private class tspListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(tsp) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "tsp button wurde gedr�ckt");
				routeController.optimizeRoute();
			}
			return false;
		}

	}

	private class loadListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(load) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "load wurde gedr�ckt");
				// TODO: ansicht wechselt in die liste der favorisierten routen
			}
			return false;
		}

	}

	private class saveListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(save) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "save wurde gedr�ckt");
				// TODO : favoritenansicht öffnet sich
			}
			return false;
		}

	}

	private class favoriteListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(addFavorite)
					&& event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "add favorite wurde gedr�ckt");
				// TODO: ansicht wechselt in die liste der favorisierten orte
			}
			return false;
		}

	}

	private class backToMapListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(goToMap)
					&& event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "go to map wurde gedr�ckt");
				// TODO:pullup muss sich schlie�en
			}
			return false;
		}

	}

	@Override
	public void onRouteChange(final RouteInfo currentRoute) {

		/*
		 * getActivity().runOnUiThread(new Runnable() { public void run() { if
		 * (currentRoute != null) { lastKnownRoute = currentRoute;
		 * 
		 * for (Iterator<Waypoint> iter = lastKnownRoute
		 * .getWaypoints().iterator(); iter.hasNext();) { Waypoint current =
		 * iter.next(); TextView waypoint = new TextView(getActivity()
		 * .getApplicationContext()); waypoint.setText("TEST " +
		 * current.getName());
		 * 
		 * Log.d("wtf", ""+ (layout == null)); Log.d("wtf", ""+ (waypoint ==
		 * null)); layout.addView(waypoint); } } } });
		 */

	}
}

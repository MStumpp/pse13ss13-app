package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import java.util.List;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.FavoriteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator.MapGen;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class RoutingView extends Fragment {

	private final String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupRoutingSwitcher;

	private FavoriteMenuController favController;

	private RouteInfo lastKnownRoute;

	private Button reset;
	private ImageView invert;
	private ImageView tsp;
	// private ImageView load;
	private ImageView save;
	private Button addFavorite;
	private Button goToMap;
	private LinearLayout layout;
	private EditText favoriteName;

	private static boolean isListener = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create RoutingView");

		favController = FavoriteMenuController.getInstance();
		// RouteController.getInstance().registerRouteListener(this);

		reset = (Button) this.getActivity().findViewById(R.id.reset);
		invert = (ImageView) this.getActivity().findViewById(R.id.invert);
		tsp = (ImageView) this.getActivity().findViewById(R.id.tsp);
		// load = (ImageView) this.getActivity().findViewById(R.id.load);
		save = (ImageView) this.getActivity().findViewById(R.id.save);
		addFavorite = (Button) this.getActivity().findViewById(
				R.id.add_favorite);
		goToMap = (Button) this.getActivity().findViewById(R.id.go_to_map);
		layout = (LinearLayout) getActivity().findViewById(R.id.waylist);

		favoriteName = (EditText) this.getActivity().findViewById(
				R.id.favoritename);

		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		Log.d(TAG_PULLUP_CONTENT, "Einstellen der sizes");
		// reset.setX(size.x / 5 * 0);
		reset.getLayoutParams().width = size.x / 4;
		reset.getLayoutParams().height = size.y / 8;

		// invert.setX(size.x / 5 * 1);
		invert.getLayoutParams().width = size.x / 4;
		invert.getLayoutParams().height = size.y / 8;

		// tsp.setX(size.x / 5 * 2);
		tsp.getLayoutParams().width = size.x / 4;
		tsp.getLayoutParams().height = size.y / 8;

		// load.setX(size.x / 5 * 3);
		// load.getLayoutParams().width = size.x / 5;
		// load.getLayoutParams().height = size.y / 8;

		// save.setX(size.x / 5 * 4);
		save.getLayoutParams().width = size.x / 4;
		save.getLayoutParams().height = size.y / 8;

		// addFavorite.setX(size.x * 0);
		addFavorite.getLayoutParams().width = size.x / 2;

		// goToMap.setX(size.x / 2);
		goToMap.getLayoutParams().width = size.x / 2;

		Log.d(TAG_PULLUP_CONTENT, "Zuweisung der Listener");
		reset.setOnTouchListener(new resetListener());
		invert.setOnTouchListener(new invertListener());
		tsp.setOnTouchListener(new tspListener());
		// load.setOnTouchListener(new loadListener());
		save.setOnTouchListener(new saveListener());
		addFavorite.setOnTouchListener(new favoriteListener());
		goToMap.setOnTouchListener(new backToMapListener());
		favoriteName
				.setOnEditorActionListener(new FavoriteNameActionListener());

		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
	}

	public void onDestroyView() {
		super.onDestroyView();
		// routeController.unregisterRouteListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Log.d(TAG_PULLUP_CONTENT, "Destroy RoutingView");
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
				RouteController.getInstance().resetRoute();
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
				Log.d(TAG_PULLUP_CONTENT, "invert was pressed");
				RouteController.getInstance().revertRoute();
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
				RouteController.getInstance().optimizeRoute();
			}
			return false;
		}

	}

	/*
	 * private class loadListener implements OnTouchListener {
	 * 
	 * @Override public boolean onTouch(View v, MotionEvent event) { if
	 * (v.equals(load) && event.getAction() == MotionEvent.ACTION_DOWN) {
	 * Log.d(TAG_PULLUP_CONTENT, "load wurde gedr�ckt"); // TODO: ansicht
	 * wechselt in die liste der !!!!favorisierten // routen!!!!
	 * MapController.getInstance().getPullUpView()
	 * .changeView(PullUpView.CONTENT_FAVORITE); } return false; }
	 * 
	 * }
	 */

	private class saveListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(save) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "save wurde gedr�ckt");
				if (layout.getChildCount() != 0) {
					favoriteName.setVisibility(View.VISIBLE);
				}
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
				// TODO: ansicht wechselt in die liste der !!!favorisierten
				// orte!!!!
				MapController.getInstance().getPullUpView()
						.changeView(PullUpView.CONTENT_FAVORITE);
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
				MapController.getInstance().getPullUpView().setNullSizeHeight();
			}
			return false;
		}

	}

	public void onRouteChange(final RouteInfo currentRoute, Context context) {

		if (currentRoute != null) {
			if (layout != null) {
				// lastKnownRoute = currentRoute;
				layout.removeAllViews();

				for (Waypoint value : currentRoute.getWaypoints()) {
					final TextView waypoint = new TextView(context);
					Log.d("routingView: ",
							" " + value.getName() + " " + value.getId());
					waypoint.setText(value.getName() + " " + value.getId());
					// TODO TextSize relativieren
					waypoint.setTextSize(30);
					LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					myParams.setMargins(0, 10, 0, 0);
					waypoint.setLayoutParams(myParams);
					waypoint.setBackgroundColor(MapGen.defaultBackground);
					waypoint.setOnTouchListener(new OnTouchListener() {

						GestureDetector gestDect = new GestureDetector(
								new WaypointTextGestureListener(waypoint));

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							gestDect.onTouchEvent(event);
							return false;
						}
					});

					layout.addView(waypoint);
				}
			} else {
				Log.d(TAG_PULLUP_CONTENT, "save wurde gedr�ckt");
			}
		}
	}

	private class FavoriteNameActionListener implements OnEditorActionListener {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (v.equals(favoriteName)) {
				Log.d(TAG_PULLUP_CONTENT, "Ein name wurde eingegeben");
				// TODO: ansicht wechselt in die liste der !!!!favorisierten
				// routen!!!!
				String checkString = v.getText().toString()
						.replaceAll(" ", null);
				if (!checkString.isEmpty()) {
					RouteController.getInstance().addRouteToFavorites(
							v.getText().toString());
					favoriteName.setVisibility(View.GONE);
					MapController.getInstance().getPullUpView()
							.changeView(PullUpView.CONTENT_FAVORITE);
				}
			}
			return false;
		}
	}

	private class WaypointTextGestureListener implements OnGestureListener {

		TextView tv;

		public WaypointTextGestureListener(TextView tv) {
			this.tv = tv;
		}

		@Override
		public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {

			float velocity = (float) Math.sqrt((double) Math.pow(
					Math.abs(arg2), 2) + (double) Math.pow(Math.abs(arg3), 2));

			if (velocity > 400) {
				Log.d(TAG_PULLUP_CONTENT, "GESTURE!!!!");
				RouteController.getInstance().resetRoute();
				layout.removeView(tv);
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}

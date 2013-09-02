package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothClass.Device.Major;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapControllerOld;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.FavoriteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator.MapGen;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class RoutingView extends Fragment {

	private final String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupRoutingSwitcher;

	private FavoriteMenuController favController;

	private RouteInfo lastKnownRoute;

	private Button reset;
	private ImageView invert;
	// private ImageView tsp;
	// private ImageView load;
	private ImageView save;
	private Button addFavorite;
	private Button goToMap;
	private LinearLayout layout;
	private EditText favoriteName;

	private Point size;

	private static boolean isListener = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create RoutingView");

		favController = FavoriteMenuController.getInstance();
		// RouteController.getInstance().registerRouteListener(this);

		reset = (Button) this.getActivity().findViewById(R.id.reset);
		invert = (ImageView) this.getActivity().findViewById(R.id.invert);
		// tsp = (ImageView) this.getActivity().findViewById(R.id.tsp);
		// load = (ImageView) this.getActivity().findViewById(R.id.load);
		save = (ImageView) this.getActivity().findViewById(R.id.save);
		addFavorite = (Button) this.getActivity().findViewById(
				R.id.add_favorite);
		goToMap = (Button) this.getActivity().findViewById(R.id.go_to_map);
		layout = (LinearLayout) getActivity().findViewById(R.id.waylist);

		favoriteName = (EditText) this.getActivity().findViewById(
				R.id.favorite_name);
		
		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		size = new Point();
		display.getSize(size);

		Log.d(TAG_PULLUP_CONTENT, "Einstellen der sizes");
		// reset.setX(size.x / 5 * 0);
		reset.getLayoutParams().width = size.x / 3;
		reset.getLayoutParams().height = size.y / 8;

		// invert.setX(size.x / 5 * 1);
		invert.getLayoutParams().width = size.x / 3;
		invert.getLayoutParams().height = size.y / 8;

		// tsp.setX(size.x / 5 * 2);
		// tsp.getLayoutParams().width = size.x / 4;
		// tsp.getLayoutParams().height = size.y / 8;

		// load.setX(size.x / 5 * 3);
		// load.getLayoutParams().width = size.x / 5;
		// load.getLayoutParams().height = size.y / 8;

		// save.setX(size.x / 5 * 4);
		save.getLayoutParams().width = size.x / 3;
		save.getLayoutParams().height = size.y / 8;

		// addFavorite.setX(size.x * 0);
		addFavorite.getLayoutParams().width = size.x / 2;

		// goToMap.setX(size.x / 2);
		goToMap.getLayoutParams().width = size.x / 2;

		Log.d(TAG_PULLUP_CONTENT, "Zuweisung der Listener");
		reset.setOnTouchListener(new resetListener());
		invert.setOnTouchListener(new invertListener());
		// tsp.setOnTouchListener(new tspListener());
		// load.setOnTouchListener(new loadListener());
		save.setOnTouchListener(new saveListener());
		addFavorite.setOnTouchListener(new favoriteListener());
		goToMap.setOnTouchListener(new backToMapListener());

		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
		this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
		if (f.toString().equals(PullUpViewOld.CONTENT_ROUTING)) {
			return true;
		}
		return false;
	}

	private class resetListener implements OnTouchListener {

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

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(invert)
					&& event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "invert was pressed");
				RouteController.getInstance().invertRoute();
			}
			// TODO: refresh activity?
			return false;
		}

	}

	/*
	 * private class tspListener implements OnTouchListener {
	 * 
	 * @Override public boolean onTouch(View v, MotionEvent event) { if
	 * (v.equals(tsp) && event.getAction() == MotionEvent.ACTION_DOWN) {
	 * Log.d(TAG_PULLUP_CONTENT, "tsp button wurde gedr�ckt");
	 * RouteController.getInstance().optimizeRoute(); } return false; }
	 * 
	 * }
	 */

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

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(save) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "save wurde gedr�ckt");
				if (layout.getChildCount() != 0) {
					favoriteName
							.setOnEditorActionListener(new FavoriteNameActionListener());
					favoriteName.setVisibility(View.VISIBLE);
				}
			}
			return false;
		}
	}

	private class favoriteListener implements OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(addFavorite)
					&& event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "add favorite wurde gedr�ckt");
				// TODO: ansicht wechselt in die liste der !!!favorisierten
				// orte!!!!
				MapControllerOld.getInstance().getPullUpView()
						.changeView(PullUpViewOld.CONTENT_FAVORITE);
			}
			return false;
		}

	}

	private class backToMapListener implements OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(goToMap)
					&& event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "go to map wurde gedr�ckt");
				MapControllerOld.getInstance().getPullUpView().setNullSizeHeight();
			}
			return false;
		}

	}

	public void onRouteChange(final RouteInfo currentRoute, Context context) {
		
		Log.d(TAG_PULLUP_CONTENT, "onRouteChange() METHOD START");

		if (currentRoute != null) {
			Log.d(TAG_PULLUP_CONTENT, "onRouteChange() übergebene Route Anzahl Wegpunkte: " + currentRoute.getWaypoints().size());
			if (layout != null) {
				// lastKnownRoute = currentRoute;
				layout.removeAllViews();

				// set layout margins for Text
				LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(
						3 * size.x / 5, LinearLayout.LayoutParams.WRAP_CONTENT);
				myParams.setMargins(0, 10, 0, 0);

				// set layout margins for delete bu tton
				// TODO: größe des delete buttons generisch an text größe
				// anpassen
				LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
						size.x / 5, 135);
				deleteParams.setMargins(0, 10, 0, 0);

				// set layout margins for delete bu tton
				// TODO: größe des delete buttons generisch an text größe
				// anpassen
				LinearLayout.LayoutParams saveFavParams = new LinearLayout.LayoutParams(
						size.x / 5, 135);
				saveFavParams.setMargins(0, 10, 0, 0);

				for (Waypoint value : currentRoute.getWaypoints()) {
					LinearLayout waypoint = new LinearLayout(context.getApplicationContext());
					waypoint.setOrientation(LinearLayout.HORIZONTAL);
					TextView waypointText = new TextView(context);
					ImageButton delete = new ImageButton(context.getApplicationContext());
					delete.setImageResource(R.drawable.delete);
					ImageButton saveFav = new ImageButton(context.getApplicationContext());
					saveFav.setImageResource(R.drawable.favorite);
					Log.d("routingView: ",
							" " + value.getName() + " " + value.getId());
					waypointText.setText(value.getName() + " " + value.getId());
					// TODO TextSize relativieren
					waypointText.setTextSize(30);
					delete.setLayoutParams(deleteParams);
					waypointText.setLayoutParams(myParams);
					waypointText.setBackgroundColor(MapGen.defaultBackground);
					delete.setOnTouchListener(new WaypointDeleteTouch(value,
							delete));
					saveFav.setOnTouchListener(new WaypointSaveTouch(value,
							saveFav));
					waypoint.addView(waypointText);
					waypoint.addView(delete);
					waypoint.addView(saveFav);
					layout.addView(waypoint);
				}
			} else {
				Log.d(TAG_PULLUP_CONTENT, "route ist null");
			}
		}
	}

	private class FavoriteNameActionListener implements OnEditorActionListener {

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
					MapControllerOld.getInstance().getPullUpView()
							.changeView(PullUpViewOld.CONTENT_FAVORITE);
				}
			}
			return false;
		}
	}

	private class WaypointSaveTouch implements OnTouchListener {

		private Waypoint value;
		private View view;

		public WaypointSaveTouch(Waypoint value, View view) {
			this.value = value;
			this.view = view;
		}

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "save waypoint gedrückt");
				favoriteName
						.setOnEditorActionListener(new FavoriteNameForWaypointsActionListener(
								value));
				favoriteName.setVisibility(View.VISIBLE);
			}
			return false;
		}
	}

	private class FavoriteNameForWaypointsActionListener implements
			OnEditorActionListener {

		Waypoint value;

		public FavoriteNameForWaypointsActionListener(Waypoint value) {
			this.value = value;
		}

		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (v.equals(favoriteName)) {
				Log.d(TAG_PULLUP_CONTENT, "Ein name wurde eingegeben");
				// TODO: ansicht wechselt in die liste der !!!!favorisierten
				// pois!!!!
				// RouteController.getInstance().addLocationToFavorites(value,
				// name);
				String checkString = v.getText().toString()
						.replaceAll(" ", null);
				if (!checkString.isEmpty()) {
					RouteController.getInstance().addLocationToFavorites(
							new Location(value.getLatitude(),
									value.getLongitude(), v.getText()
											.toString()),
							v.getText().toString());
					favoriteName.setVisibility(View.GONE);
					MapControllerOld.getInstance().getPullUpView()
							.changeView(PullUpViewOld.CONTENT_FAVORITE);
				}
			}
			return false;
		}
	}

	private class WaypointDeleteTouch implements OnTouchListener {

		private Waypoint value;
		private View view;

		public WaypointDeleteTouch(Waypoint value, View view) {
			this.value = value;
			this.view = view;
		}

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "delete waypoint gedrückt");

				// dialog erstellen
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						getActivity());
				alertDialog.setTitle(R.string.delete_dialog_header);
				alertDialog.setMessage(R.string.delete_dialog);
				alertDialog.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								RouteController.getInstance()
										.setActiveWaypoint(value.getId());
								RouteController.getInstance()
										.deleteActiveWaypoint();
							}
						});
				alertDialog.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				alertDialog.show();
			}
			return false;
		}
	}
}

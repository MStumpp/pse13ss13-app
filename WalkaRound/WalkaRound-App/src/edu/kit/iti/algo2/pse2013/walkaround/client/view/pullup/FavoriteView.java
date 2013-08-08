package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import java.util.Iterator;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.FavoriteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator.MapGen;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

public class FavoriteView extends Fragment {

	private final String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupFavoriteSwitcher;

	private TabHost tabHost;

	private TextView favorite;
	private LinearLayout favPois;
	private LinearLayout favRoutes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create FavoriteView");

		Log.d(TAG_PULLUP_CONTENT, "Create Tabs");
		tabHost = (TabHost) this.getActivity().findViewById(R.id.tabhost_favs);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("route_favorites_tab");
		spec1.setContent(R.id.route_favorites);
		spec1.setIndicator("Routes");
		TabSpec spec2 = tabHost.newTabSpec("poi_favorites_tab");
		spec2.setContent(R.id.poi_favorites);
		spec2.setIndicator("POIs");
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);

		favorite = (TextView) this.getActivity().findViewById(R.id.favorite);
		favPois = (LinearLayout) this.getActivity().findViewById(
				R.id.poi_favorites);
		favRoutes = (LinearLayout) this.getActivity().findViewById(
				R.id.route_favorites);

		tabHost.setVisibility(View.VISIBLE);

		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		Log.d(TAG_PULLUP_CONTENT, "Einstellen der Größenverhältnisse");
		// favorite.setX((float) (size.x / 2.5));
		// favorite.getLayoutParams().width = size.x;
		// tabHost.setX(size.x * 0);
		// tabHost.getLayoutParams().width = size.x;
		// tabHost.getLayoutParams().height = size.y / 5;

		Log.d(TAG_PULLUP_CONTENT, "Favoriten laden");
		updateFavorties();

		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG_PULLUP_CONTENT, "Destroy FavoriteView");
		tabHost.getTabWidget().removeAllViews();
		favPois.removeAllViews();
		favRoutes.removeAllViews();
		this.getActivity().findViewById(switcher).setVisibility(View.GONE);

	}

	public boolean equals(Fragment f) {
		if (f.toString().equals(PullUpView.CONTENT_FAVORITE)) {
			return true;
		}
		return false;
	}

	private boolean updateFavorties() {
		// set layout margins
		LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		myParams.setMargins(0, 10, 0, 0);

		// locations
		for (Iterator<String> iter = FavoriteMenuController.getInstance()
				.getNamesOfFavoriteLocations().iterator(); iter.hasNext();) {
			String current = iter.next();
			TextView tv = new TextView(getActivity());
			tv.setText(current);
			// TODO TextSize relativieren
			tv.setOnTouchListener(new favLocationTouch(current, tv));
			tv.setTextSize(30);
			tv.setLayoutParams(myParams);
			tv.setBackgroundColor(MapGen.defaultBackground);
			favPois.addView(tv);
		}

		// routes
		for (Iterator<String> iter = FavoriteMenuController.getInstance()
				.getNamesOfFavoriteRoutes().iterator(); iter.hasNext();) {
			String current = iter.next();
			TextView tv = new TextView(getActivity());
			tv.setText(current);
			// TODO TextSize relativieren
			tv.setOnTouchListener(new favRouteTouch(current, tv));
			tv.setTextSize(30);
			tv.setLayoutParams(myParams);
			tv.setBackgroundColor(MapGen.defaultBackground);
			favRoutes.addView(tv);
		}
		return false;
	}

	private class favRouteTouch implements OnTouchListener {

		private String name;
		private View view;

		public favRouteTouch(String name, View view) {
			this.name = name;
			this.view = view;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view)) {
				FavoriteMenuController.getInstance()
						.appendFavoriteRouteToRoute(name);
				MapController.getInstance().getPullUpView().setNullSizeHeight();
			}
			return false;
		}
	}

	private class favLocationTouch implements OnTouchListener {

		private String name;
		private View view;

		public favLocationTouch(String name, View view) {
			this.name = name;
			this.view = view;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view)) {
				FavoriteMenuController.getInstance()
						.appendFavoriteLocationToRoute(name);
				MapController.getInstance().getPullUpView().setNullSizeHeight();
			}
			return false;
		}
	}
}

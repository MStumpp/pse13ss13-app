package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import java.util.Iterator;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.FavoriteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator.MapGen;

public class FavoriteView extends Fragment {

	private final String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupFavoriteSwitcher;

	private String TAG = FavoriteView.class.getSimpleName();

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
		spec1.setContent(R.id.tab_route);
		spec1.setIndicator("Routes");
		TabSpec spec2 = tabHost.newTabSpec("poi_favorites_tab");
		spec2.setContent(R.id.tab_poi);
		spec2.setIndicator("POIs");
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);

		favorite = (TextView) this.getActivity().findViewById(R.id.favorite);
		favPois = (LinearLayout) this.getActivity().findViewById(R.id.poifavs);
		favRoutes = (LinearLayout) this.getActivity().findViewById(
				R.id.routefavs);
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

		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		// set layout margins for Text
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				4 * size.x / 5, LinearLayout.LayoutParams.WRAP_CONTENT);
		textParams.setMargins(0, 10, 0, 0);

		// set layout margins for delete bu tton
		// TODO: größe des delete buttons generisch an text größe anpassen
		LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
				size.x / 5, 135);
		deleteParams.setMargins(0, 10, 0, 0);

		favPois.removeAllViews();
		favRoutes.removeAllViews();

		// locations
		for (Iterator<String> iter = FavoriteMenuController.getInstance()
				.getNamesOfFavoriteLocations().iterator(); iter.hasNext();) {
			String current = iter.next();
			LinearLayout poi = new LinearLayout(getActivity());
			poi.setOrientation(LinearLayout.HORIZONTAL);
			TextView tv = new TextView(getActivity());
			ImageButton delete = new ImageButton(getActivity());
			delete.setImageResource(R.drawable.delete);
			tv.setText(current);
			// TODO TextSize relativieren
			tv.setOnTouchListener(new favLocationTouch(current, tv));
			delete.setOnTouchListener(new favLocationDeleteTouch(current,
					delete));
			tv.setTextSize(30);
			delete.setLayoutParams(deleteParams);
			tv.setLayoutParams(textParams);
			tv.setBackgroundColor(MapGen.defaultBackground);
			poi.addView(tv);
			poi.addView(delete);
			favPois.addView(poi);
		}

		// routes
		for (Iterator<String> iter = FavoriteMenuController.getInstance()
				.getNamesOfFavoriteRoutes().iterator(); iter.hasNext();) {
			String current = iter.next();
			LinearLayout poi = new LinearLayout(getActivity());
			poi.setOrientation(LinearLayout.HORIZONTAL);
			TextView tv = new TextView(getActivity());
			ImageButton delete = new ImageButton(getActivity());
			delete.setImageResource(R.drawable.delete);
			tv.setText(current);
			// TODO TextSize relativieren
			tv.setOnTouchListener(new favRouteTouch(current, tv));
			delete.setOnTouchListener(new favRouteDeleteTouch(current, delete));
			tv.setTextSize(30);
			delete.setLayoutParams(deleteParams);
			tv.setLayoutParams(textParams);
			tv.setBackgroundColor(MapGen.defaultBackground);
			poi.addView(tv);
			poi.addView(delete);
			favRoutes.addView(poi);
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

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG,
						String.format("Add route '%s' to current route", name));
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

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG, String.format("Add location '%s' to current route",
						name));
				FavoriteMenuController.getInstance()
						.appendFavoriteLocationToRoute(name);
				MapController.getInstance().getPullUpView().setNullSizeHeight();
			}
			return false;
		}
	}

	private class favLocationDeleteTouch implements OnTouchListener {

		private String name;
		private View view;

		public favLocationDeleteTouch(String name, View view) {
			this.name = name;
			this.view = view;
		}

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view) && event.getAction() == MotionEvent.ACTION_DOWN) {

				// dialog erstellen
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						getActivity());
				alertDialog.setTitle(R.string.delete_dialog_header);
				alertDialog
						.setMessage(R.string.delete_dialog);
				alertDialog.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Log.d(TAG, String.format("delete fav location"));
								FavoriteMenuController.getInstance()
										.deleteLocation(name);
								updateFavorties();
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

	private class favRouteDeleteTouch implements OnTouchListener {

		private String name;
		private View view;

		public favRouteDeleteTouch(String name, View view) {
			this.name = name;
			this.view = view;
		}

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG, String.format("delete fav location"));
				FavoriteMenuController.getInstance().deleteRoute(name);
				updateFavorties();
			}
			return false;
		}
	}
}
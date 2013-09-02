package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.views;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapControllerOld;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.generator.MapGen;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.views.Routing.GoToMapListener;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class Search extends RelativeLayout {

	private static final String TAG = Search.class.getSimpleName();

	private LinearLayout poiSide;
	private LinearLayout addressSide;
	private LinearLayout main;
	private LinearLayout tabHost;
	private Button waypointButton;
	private Button routeButton;
	private boolean selected = true;
	private EditText zipEdit;
	private EditText cityEdit;
	private EditText streetEdit;
	private EditText numberEdit;
	private EditText freeText;

	private LinearLayout result;
	private int width;

	public Search(Context context, AttributeSet attrs) {
		super(context, attrs);

		Point size = BoundingBox.getInstance(context).getDisplaySize();

		width = size.x;

		main = new LinearLayout(context, attrs);
		main.setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		this.addView(main, mainParams);

		tabHost = new LinearLayout(context, attrs);
		tabHost.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams tabHostParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tabHostParams.width = size.x;

		main.addView(tabHost, tabHostParams);

		waypointButton = new Button(context, attrs);
		waypointButton.setText("Adressen");
		waypointButton.setOnTouchListener(new AddressSideTabListener());

		routeButton = new Button(context, attrs);
		routeButton.setText("POI");
		routeButton.setOnTouchListener(new POISideTabListener());

		LinearLayout.LayoutParams waypointButtontParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		waypointButtontParams.height = size.y / 10;
		waypointButtontParams.width = size.x / 2;
		waypointButtontParams.leftMargin = 0;
		waypointButtontParams.rightMargin = 0;

		LinearLayout.LayoutParams routeButtontParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		routeButtontParams.height = size.y / 10;
		routeButtontParams.width = size.x / 2;
		waypointButtontParams.leftMargin = 0;
		waypointButtontParams.rightMargin = 0;

		tabHost.addView(waypointButton, waypointButtontParams);
		tabHost.addView(routeButton, routeButtontParams);

		poiSide = new LinearLayout(context, attrs);
		poiSide.setOrientation(LinearLayout.VERTICAL);
		poiSide.setVisibility(GONE);

		addressSide = new LinearLayout(context, attrs);
		addressSide.setOrientation(LinearLayout.VERTICAL);
		addressSide.setVisibility(VISIBLE);
		waypointButton.setTextColor(Color.RED);

		LinearLayout.LayoutParams routeSiedeParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams waypointSiedeParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		main.addView(poiSide, routeSiedeParam);
		main.addView(addressSide, waypointSiedeParam);

		LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textParam.width = size.x / 2;
		textParam.height = size.y / 15;

		LinearLayout.LayoutParams editParam = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		editParam.width = size.x / 2;
		textParam.height = size.y / 15;

		// Address

		// Zip
		LinearLayout zipLayout = new LinearLayout(context);
		zipLayout.setOrientation(LinearLayout.HORIZONTAL);

		TextView zip = new TextView(context);
		zip.setText("ZIP");
		zip.setTextSize(size.x / 21);
		zip.setGravity(Gravity.CENTER);

		zipEdit = new EditText(context);
		zipEdit.setGravity(Gravity.CENTER);

		zipLayout.addView(zip, textParam);
		zipLayout.addView(zipEdit, editParam);

		addressSide.addView(zipLayout, layoutParam);

		// City
		LinearLayout cityLayout = new LinearLayout(context);
		cityLayout.setOrientation(LinearLayout.HORIZONTAL);

		TextView city = new TextView(context);
		city.setText("City");
		city.setTextSize(size.x / 21);
		city.setGravity(Gravity.CENTER);

		cityEdit = new EditText(context);
		cityEdit.setGravity(Gravity.CENTER);

		cityLayout.addView(city, textParam);
		cityLayout.addView(cityEdit, editParam);

		addressSide.addView(cityLayout, layoutParam);

		// Street
		LinearLayout streetLayout = new LinearLayout(context);
		streetLayout.setOrientation(LinearLayout.HORIZONTAL);

		TextView street = new TextView(context);
		street.setText("Street");
		street.setTextSize(size.x / 21);
		street.setGravity(Gravity.CENTER);

		streetEdit = new EditText(context);
		streetEdit.setGravity(Gravity.CENTER);

		streetLayout.addView(street, textParam);
		streetLayout.addView(streetEdit, editParam);

		addressSide.addView(streetLayout, layoutParam);

		// Street
		LinearLayout numberLayout = new LinearLayout(context);
		numberLayout.setOrientation(LinearLayout.HORIZONTAL);

		TextView number = new TextView(context);
		number.setText("Number");
		number.setTextSize(size.x / 21);
		number.setGravity(Gravity.CENTER);

		numberEdit = new EditText(context);
		numberEdit.setGravity(Gravity.CENTER);

		numberLayout.addView(number, textParam);
		numberLayout.addView(numberEdit, editParam);

		addressSide.addView(numberLayout, layoutParam);

		// Go

		LinearLayout.LayoutParams goParam = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		goParam.width = size.x;
		goParam.height = size.y / 15;
		goParam.topMargin = 10;

		Button go = new Button(context, attrs);
		go.setGravity(Gravity.CENTER);
		go.setText("Go");

		go.setOnTouchListener(new GoAdressListener());

		addressSide.addView(go, goParam);

		// POI

		LinearLayout.LayoutParams freeParam = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		freeParam.height = size.y / 15;
		freeParam.width = width;

		freeText = new EditText(context);
		freeText.setGravity(Gravity.CENTER);

		poiSide.addView(freeText, freeParam);

		// Go

		LinearLayout.LayoutParams goFreeParam = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		goFreeParam.width = size.x;
		goFreeParam.height = size.y / 15;
		goFreeParam.topMargin = 10;

		Button goFree = new Button(context, attrs);
		goFree.setGravity(Gravity.CENTER);
		goFree.setText("Go");

		goFree.setOnTouchListener(new GoQueryListener());

		poiSide.addView(goFree, goFreeParam);

		// Result
		result = new LinearLayout(context, attrs);
		result.setVisibility(GONE);

		main.addView(result);

		zipEdit.setText("76185");
		cityEdit.setText("Karlsruhe");
		streetEdit.setText("Yorckstr.");
		numberEdit.setText("48");
		freeText.setText("FreeText");
	}

	private class POISideTabListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_UP) {
				setTab(true);
				isResult = false;
				toogleResult();
			}
			return false;
		}

	}

	private class AddressSideTabListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_UP) {
				setTab(false);
				isResult = false;
				toogleResult();
			}
			return false;
		}

	}

	private void setTab(boolean b) {
		selected = b;
		if (selected) {
			waypointButton.setSelected(false);
			waypointButton.setTextColor(Color.BLACK);
			routeButton.setSelected(true);
			routeButton.setTextColor(Color.RED);
			poiSide.setVisibility(VISIBLE);
			addressSide.setVisibility(GONE);
		} else {
			waypointButton.setSelected(true);
			waypointButton.setTextColor(Color.RED);
			routeButton.setSelected(false);
			routeButton.setTextColor(Color.BLACK);
			poiSide.setVisibility(GONE);
			addressSide.setVisibility(VISIBLE);
		}
	}

	boolean isResult = false;

	private void toogleResult() {
		if (isResult) {
			poiSide.setVisibility(GONE);
			addressSide.setVisibility(GONE);
			result.setVisibility(View.VISIBLE);
		} else {
			result.setVisibility(View.GONE);
			result.removeAllViews();
		}
	}

	private class GoAdressListener implements OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				List<Location> locations;
				Log.d(TAG, "Go wurde gedrückt");
				Log.d(TAG, "" + zipEdit.getText().toString());

				String zip = zipEdit.getText().toString().replaceAll(" ", null);
				String city = cityEdit.getText().toString()
						.replaceAll(" ", null);
				String street = streetEdit.getText().toString()
						.replaceAll(" ", null);
				String number = numberEdit.getText().toString()
						.replaceAll(" ", null);

				if (zip.isEmpty()) {
					Address addr = new Address(street, number, city, 0);
					locations = POIManager.getInstance(getContext())
							.searchPOIsByAddress(getContext(), addr);
				} else {
					Address addr = new Address(street, number, city,
							(Integer.parseInt(zip)));
					locations = POIManager.getInstance(getContext())
							.searchPOIsByAddress(getContext(), addr);
				}

				if (!locations.isEmpty()) {

					// Resultat anzeigen
					isResult = true;
					toogleResult();

					for (Location value : locations) {
						Button location = new Button(getContext());
						Log.d("routingView: ", " " + value.getName() + " "
								+ value.getId());
						location.setText(value.getAddress().toString());
						location.setOnTouchListener(new locationTouch(value,
								location));
						// TODO TextSize relativieren
						location.setTextSize(30);
						LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						myParams.topMargin = 10;
						myParams.width = width;
						result.addView(location, myParams);
					}
				} else {
					alertResult(zip + " " + city + " " + street + " " + number);
				}
			}
			return false;
		}
	}

	private class GoQueryListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			Log.d(TAG, "Eine query wurde eingegeben");

			int action = event.getAction();

			if (action == MotionEvent.ACTION_UP) {
				String text = freeText.getText().toString()
						.replaceAll(" ", "");
				if (!text.isEmpty()) {

					List<POI> poiS = POIManager.getInstance(getContext())
							.searchPOIsByQuery(freeText.getText().toString());

					if (!poiS.isEmpty()) {

						isResult = true;
						toogleResult();

						for (POI value : poiS) {
							Button poi = new Button(getContext());
							Log.d("routingView: ", " " + value.getName() + " "
									+ value.getId());
							poi.setText(value.getName());
							poi.setOnTouchListener(new poiTouch(value, poi));
							// TODO TextSize relativieren
							poi.setTextSize(30);
							LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT);
							myParams.topMargin = 10;
							myParams.width = width;
							result.addView(poi, myParams);
						}
					} else {
						alertResult(text);
					}
				}
			}
			return false;
		}

	}

	public void alertResult(String text) {
		AlertDialog alertDialog = new AlertDialog.Builder(getContext())
				.create();
		alertDialog.setTitle("Sorry..");
		alertDialog
				.setMessage("Es wurden keine mit Ihrer Suchanfrage: \n \n" + text + "\n \n übereinstimmenden Orte gefunde!");
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alertDialog.show();
	}

	private class poiTouch implements OnTouchListener {

		private POI poi;
		private View view;

		public poiTouch(POI p, View view) {
			this.poi = p;
			this.view = view;
		}

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view)) {
				RouteController.getInstance().addWaypoint(
						new Waypoint(poi.getLatitude(), poi.getLongitude(), poi
								.getName()));
				BoundingBox.getInstance(getContext()).setCenter(
						new Coordinate(poi.getLatitude(), poi.getLongitude()));
				notifyGoToMapListener();
			}
			return false;
		}

	}

	private class locationTouch implements OnTouchListener {

		private Location location;
		private View view;

		public locationTouch(Location p, View view) {
			this.location = p;
			this.view = view;
		}

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view)) {
				RouteController.getInstance().addWaypoint(
						new Waypoint(location.getLatitude(), location
								.getLongitude(), location.getName()));

				BoundingBox.getInstance(getContext()).setCenter(
						new Coordinate(location.getLatitude(), location
								.getLongitude()));
				notifyGoToMapListener();

			}
			return false;
		}
	}

	LinkedList<GoToMapListener> rl = new LinkedList<GoToMapListener>();

	private void notifyGoToMapListener() {
		for (GoToMapListener l : rl) {
			l.onGoToMap();
		}
	}

	public void registerGoToMapListener(GoToMapListener listener) {
		rl.add(listener);
	}

}
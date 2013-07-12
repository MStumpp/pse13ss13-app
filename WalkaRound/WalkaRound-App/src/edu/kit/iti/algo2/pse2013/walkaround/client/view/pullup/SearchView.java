package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import java.util.List;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.SearchMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.MapModel;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class SearchView extends Fragment {

	private final String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupSearchSwitcher;

	private SearchMenuController searchMenuController;

	private TabHost tabHost;

	private TextView postalCode;
	private TextView city;
	private TextView street;
	private TextView number;
	private EditText postalCodeSearch;
	private EditText citySearch;
	private EditText streetSearch;
	private EditText numberSearch;
	private EditText query;
	private Button goButton;
	LinearLayout result;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create SearchView");

		searchMenuController = SearchMenuController.getInstance();

		Log.d(TAG_PULLUP_CONTENT, "Create Tabs");
		tabHost = (TabHost) this.getActivity()
				.findViewById(R.id.tabhost_search);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("address_tab");
		spec1.setContent(R.id.address_search);
		spec1.setIndicator("Address");
		TabSpec spec2 = tabHost.newTabSpec("poi_tab");
		spec2.setContent(R.id.poi_search);
		spec2.setIndicator("POI");
		
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);

		postalCode = (TextView) this.getActivity().findViewById(
				R.id.postal_code);
		city = (TextView) this.getActivity().findViewById(R.id.city);
		street = (TextView) this.getActivity().findViewById(R.id.street);
		number = (TextView) this.getActivity().findViewById(R.id.number);
		postalCodeSearch = (EditText) this.getActivity().findViewById(
				R.id.postal_code_search);
		citySearch = (EditText) this.getActivity().findViewById(
				R.id.city_search);
		streetSearch = (EditText) this.getActivity().findViewById(
				R.id.street_search);
		numberSearch = (EditText) this.getActivity().findViewById(
				R.id.number_search);
		query = (EditText) this.getActivity().findViewById(R.id.query);
		goButton = (Button) this.getActivity().findViewById(R.id.go_button);

		result = (LinearLayout) this.getActivity().findViewById(R.id.search_result);
		result.setVisibility(View.GONE);
		tabHost.setVisibility(View.VISIBLE);
		
		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		Log.d(TAG_PULLUP_CONTENT, "Einstellen der Größenverhältnisse");
		/*city.setX(size.x / 18);
		city.getLayoutParams().width = size.x / 12;
		street.setX(size.x / 18);
		street.getLayoutParams().width = size.x / 12;
		number.setX(size.x / 18);
		number.getLayoutParams().width = size.x / 12;
		postalCode.setX(size.x / 18);
		postalCode.getLayoutParams().width = size.x / 12;
		postalCodeSearch.setX(size.x / 4);
		postalCodeSearch.getLayoutParams().width = size.x / 3 * 2;
		citySearch.setX(size.x / 4);
		citySearch.getLayoutParams().width = size.x / 3 * 2;
		streetSearch.setX(size.x / 4);
		streetSearch.getLayoutParams().width = size.x / 3 * 2;
		numberSearch.setX(size.x / 4);
		numberSearch.getLayoutParams().width = size.x / 3 * 2;
		query.setX(0);
		query.getLayoutParams().width = size.x;
		goButton.setX(size.x / 4);
		goButton.getLayoutParams().width = size.x / 2;*/

		Log.d(TAG_PULLUP_CONTENT, "Listener werden hinzugef�gt");

		goButton.setOnTouchListener(new GoListener());
		query.setOnEditorActionListener(new QueryActionListener());

		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG_PULLUP_CONTENT, "Destroy SearchView");
		tabHost.getTabWidget().removeAllViews();
		this.getActivity().findViewById(switcher).setVisibility(View.GONE);
	}

	public boolean equals(Fragment f) {
		if (f.toString().equals(PullUpView.CONTENT_SEARCH)) {
			return true;
		}
		return false;
	}

	private class GoListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (v.equals(goButton)
					&& event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "Go wurde gedr�ckt");
				Log.d(TAG_PULLUP_CONTENT, "" + postalCodeSearch.getText().toString());
				if (postalCodeSearch.getText().toString().trim().equals("")) {
					searchMenuController.requestSuggestionsByAddress(0,
							citySearch.getText().toString(), streetSearch
									.getText().toString(), numberSearch
									.getText().toString());
				} else {
					searchMenuController.requestSuggestionsByAddress(Integer
							.parseInt(postalCodeSearch.getText().toString()),
							citySearch.getText().toString(), streetSearch
									.getText().toString(), numberSearch
									.getText().toString());
				}
				// TODO: zur vorschl�gen gehen
			}
			return false;
		}
	}

	private class QueryActionListener implements OnEditorActionListener {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (v.equals(query)) {
				Log.d(TAG_PULLUP_CONTENT, "Eine query wurde eingegeben");

				List<POI> poiS = searchMenuController.requestSuggestionsByText(v.getText()
						.toString());

				result.setVisibility(View.VISIBLE);
				tabHost.setVisibility(View.GONE);

				for (POI value : poiS) {
					TextView waypoint = new TextView(getActivity());
					Log.d("routingView: ", " " + value.getName() + " " + value.getId());
					waypoint.setText("TEST " + value.getName() + " " + value.getId());
					//TODO TextSize relativieren
					waypoint.setTextSize(40);
					waypoint.setPadding(10, 20, 10, 20);
					waypoint.setBackgroundColor(MapModel.defaultBackground);
					result.addView(waypoint);
				}
				
				
				// TODO: zur karte zur�ckkehren
			}
			return false;
		}

	}
}

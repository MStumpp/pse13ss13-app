package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.SearchMenuController;

public class SearchView extends Fragment {

	public String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupSearchSwitcher;
	
	SearchMenuController searchMenuController;
	
	TabHost tabHost;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create SearchView");
		
		searchMenuController = SearchMenuController.getInstance();
		
		Log.d(TAG_PULLUP_CONTENT, "Create Tabs");
		tabHost = (TabHost) this.getActivity().findViewById(R.id.tabhost_search);
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
		postalCodeSearch = (EditText) this.getActivity().findViewById(R.id.postal_code_search);
		citySearch = (EditText) this.getActivity().findViewById(R.id.city_search);
		streetSearch = (EditText) this.getActivity().findViewById(R.id.street_search);
		numberSearch = (EditText) this.getActivity().findViewById(R.id.number_search);
		query = (EditText) this.getActivity().findViewById(R.id.query);
		goButton = (Button) this.getActivity().findViewById(R.id.go_button);
		
		Log.d(TAG_PULLUP_CONTENT, "Listener werden hinzugefï¿½gt");
		
		goButton.setOnTouchListener(new GoListener());
		query.setOnEditorActionListener(new QueryActionListener());
		
		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG_PULLUP_CONTENT, "Destroy SearchView");
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
			
			if (v.equals(goButton)) {
				Log.d(TAG_PULLUP_CONTENT, "Go wurde gedrückt");
				//searchMenuController.requestSuggestionsByAddress(postalCodeSearch.getText(), citySearch.getText(), streetSearch.getText(), numberSearch.getText(), );
			}
			return false;
		}
		
	}
	
	private class QueryActionListener implements OnEditorActionListener {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if(v.equals(query)) {
				searchMenuController.requestSuggestionsByText(v.getText().toString());
			}
			return false;
		}
		
	}
}

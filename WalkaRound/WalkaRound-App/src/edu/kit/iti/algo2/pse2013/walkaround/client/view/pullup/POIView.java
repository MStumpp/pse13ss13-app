package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;

public class POIView extends Fragment {

	public String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupPOISwitcher;

	private TextView pois;
	private TextView category1;
	private TextView category2;
	private TextView category3;
	private TextView category4;
	private TextView category5;
	private TextView category6;
	private TextView category7;
	private TextView category8;
	private TextView category9;
	private TextView category10;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create POIView");

		pois = (TextView) this.getActivity().findViewById(R.id.pois);
		category1 = (TextView) this.getActivity().findViewById(R.id.category_1);
		category2 = (TextView) this.getActivity().findViewById(R.id.category_2);
		category3 = (TextView) this.getActivity().findViewById(R.id.category_3);
		category4 = (TextView) this.getActivity().findViewById(R.id.category_4);
		category5 = (TextView) this.getActivity().findViewById(R.id.category_5);
		category6 = (TextView) this.getActivity().findViewById(R.id.category_6);
		category7 = (TextView) this.getActivity().findViewById(R.id.category_7);
		category8 = (TextView) this.getActivity().findViewById(R.id.category_8);
		category9 = (TextView) this.getActivity().findViewById(R.id.category_9);
		category10 = (TextView) this.getActivity().findViewById(R.id.category_10);
		
		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		Log.d(TAG_PULLUP_CONTENT, "Einstellen der Größenverhältnisse");
		pois.setX(size.x / 2.3f);
		pois.getLayoutParams().width = size.x;
		category1.setX(size.x / 6);
		category1.getLayoutParams().width = size.x / 2;
		category2.setX((size.x / 6));
		category2.getLayoutParams().width = size.x / 2;
		category3.setX(size.x / 6);
		category3.getLayoutParams().width = size.x / 2;
		category4.setX(size.x / 6);
		category4.getLayoutParams().width = size.x / 2;
		category5.setX(size.x / 6);
		category5.getLayoutParams().width = size.x / 2;
		category6.setX((size.x / 6));
		category6.getLayoutParams().width = size.x / 2;
		category7.setX(size.x / 6);
		category7.getLayoutParams().width = size.x / 2;
		category8.setX(size.x / 6);
		category8.getLayoutParams().width = size.x / 2;
		category9.setX(size.x / 6);
		category9.getLayoutParams().width = size.x / 2;
		category10.setX((size.x / 6));
		category10.getLayoutParams().width = size.x / 2;

		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG_PULLUP_CONTENT, "Destroy POIView");
		this.getActivity().findViewById(switcher).setVisibility(View.GONE);
	}

	public boolean equals(Fragment f) {
		if (f.toString().equals(PullUpView.CONTENT_POI)) {
			return true;
		}
		return false;
	}
}

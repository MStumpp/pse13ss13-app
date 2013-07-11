package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.POIMenuController;

public class POIView extends Fragment {

	private final String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

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
	private TextView category11;
	
	POIMenuController poiController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create POIView");
		
		poiController = POIMenuController.getInstance();

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
		category10 = (TextView) this.getActivity().findViewById(
				R.id.category_10);
		category11 = (TextView) this.getActivity().findViewById(
				R.id.category_11);

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
		category11.setX((size.x / 6));
		category11.getLayoutParams().width = size.x / 2;

		Log.d(TAG_PULLUP_CONTENT, "Listener werden hinzugef�gt");
		category1.setOnTouchListener(new onCategoryTouch());
		category2.setOnTouchListener(new onCategoryTouch());
		category3.setOnTouchListener(new onCategoryTouch());
		category4.setOnTouchListener(new onCategoryTouch());
		category5.setOnTouchListener(new onCategoryTouch());
		category6.setOnTouchListener(new onCategoryTouch());
		category7.setOnTouchListener(new onCategoryTouch());
		category8.setOnTouchListener(new onCategoryTouch());
		category9.setOnTouchListener(new onCategoryTouch());
		category10.setOnTouchListener(new onCategoryTouch());
		category11.setOnTouchListener(new onCategoryTouch());

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

	private class onCategoryTouch implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(category1) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!category1.isSelected()) {
					setSelected(category1);
				} else {
					setUnselected(category1);
				}
			} else if (v.equals(category2) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!category2.isSelected()) {
					setSelected(category2);
				} else {
					setUnselected(category2);
				}
			} else if (v.equals(category3) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!category3.isSelected()) {
					setSelected(category3);
				} else {
					setUnselected(category3);
				}
			} else if (v.equals(category4) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!category4.isSelected()) {
					setSelected(category4);
				} else {
					setUnselected(category4);
				}
			} else if (v.equals(category5) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!category5.isSelected()) {
					setSelected(category5);
				} else {
					setUnselected(category5);
				}
			} else if (v.equals(category6) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!category6.isSelected()) {
					setSelected(category6);
				} else {
					setUnselected(category6);
				}
			} else if (v.equals(category7) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!category7.isSelected()) {
					setSelected(category7);
				} else {
					setUnselected(category7);
				}
			} else if (v.equals(category8) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!category8.isSelected()) {
					setSelected(category8);
				} else {
					setUnselected(category8);
				}
			} else if (v.equals(category9) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!category9.isSelected()) {
					setSelected(category9);
				} else {
					setUnselected(category9);
				}
			} else if (v.equals(category10) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!category10.isSelected()) {
					setSelected(category10);
				} else {
					setUnselected(category10);
				}
			} else if (v.equals(category11) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!category11.isSelected()) {
					setSelected(category11);
				} else {
					setUnselected(category11);
				}
			}
			return false;
		}

		private void setUnselected(TextView v) {
			v.setSelected(false);
			v.setTextColor(Color.WHITE);
			poiController.removeActiveCategory(Integer.parseInt(v.getTag().toString()));
		}

		private void setSelected(TextView v) {
			v.setSelected(true);
			v.setTextColor(Color.YELLOW);
			poiController.addActiveCategory(Integer.parseInt(v.getTag().toString()));
		}
	}
}

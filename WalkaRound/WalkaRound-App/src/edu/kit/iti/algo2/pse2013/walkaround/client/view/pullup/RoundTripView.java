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
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;

public class RoundTripView extends Fragment {

	private final String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private final int MAXIMUM_LENGTH_ROUNDTRIP = 5000;

	private final int MINIMUN_LENGTH_ROUNDTRIP = 100;

	private final int NUMBER_OF_STEPS = MAXIMUM_LENGTH_ROUNDTRIP / 100;

	private final int ON_LONG_CLICK_UPDATE_INTERVALL_MS = 100;

	private int switcher = R.id.pullupRoundtripSwitcher;

	private RouteController routeController;

	private NumberPicker np;

	private TextView meter;
	private TextView length;
	private TextView profiles;
	private TextView jogging;
	private TextView sightseeing;
	private TextView shopping;
	private TextView clubbing;
	private Button computeRoundtrip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create RoundtripView");

		routeController = RouteController.getInstance();

		Log.d(TAG_PULLUP_CONTENT, "Create NumberPicker");
		np = (NumberPicker) this.getActivity().findViewById(R.id.length_picker);
		String[] nums = new String[NUMBER_OF_STEPS];
		for (int i = 0; i < nums.length; i++)
			nums[i] = Integer.toString(100 + i * 100);
		np.setMinValue(MINIMUN_LENGTH_ROUNDTRIP / 100);
		np.setMaxValue(MAXIMUM_LENGTH_ROUNDTRIP / 100);
		np.setWrapSelectorWheel(false);
		np.setDisplayedValues(nums);
		np.setOnLongPressUpdateInterval(ON_LONG_CLICK_UPDATE_INTERVALL_MS);

		meter = (TextView) this.getActivity().findViewById(R.id.meter);
		length = (TextView) this.getActivity().findViewById(R.id.length_text);
		profiles = (TextView) this.getActivity().findViewById(R.id.profiles);
		jogging = (TextView) this.getActivity().findViewById(
				R.id.profile_jogging);
		sightseeing = (TextView) this.getActivity().findViewById(
				R.id.profile_sightseeing);
		shopping = (TextView) this.getActivity().findViewById(
				R.id.profile_shopping);
		clubbing = (TextView) this.getActivity().findViewById(
				R.id.profile_clubbing);
		computeRoundtrip = (Button) this.getActivity().findViewById(
				R.id.roundtrip_compute);

		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		Log.d(TAG_PULLUP_CONTENT, "Einstellen der Größenverhältnisse");
		meter.setX(size.x / 3 * 2);
		meter.getLayoutParams().width = size.x;
		np.setX(size.x / 3);
		np.getLayoutParams().width = size.x / 3;
		length.setX(size.x / 10);
		length.getLayoutParams().width = size.x / 5;
		profiles.setX((size.x / 2.6f));
		profiles.getLayoutParams().width = size.x / 2;
		jogging.setX(size.x / 6);
		jogging.getLayoutParams().width = size.x / 2;
		sightseeing.setX((size.x / 6));
		sightseeing.getLayoutParams().width = size.x / 2;
		shopping.setX(size.x / 6);
		shopping.getLayoutParams().width = size.x / 2;
		clubbing.setX(size.x / 6);
		clubbing.getLayoutParams().width = size.x / 2;
		computeRoundtrip.setX(size.x / 4);
		computeRoundtrip.getLayoutParams().width = size.x / 2;

		Log.d(TAG_PULLUP_CONTENT, "Listener werden hinzugef�gt");
		computeRoundtrip.setOnTouchListener(new RoundtripComputeListener());
		jogging.setOnTouchListener(new OnProfileTouch());
		sightseeing.setOnTouchListener(new OnProfileTouch());
		shopping.setOnTouchListener(new OnProfileTouch());
		clubbing.setOnTouchListener(new OnProfileTouch());

		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG_PULLUP_CONTENT, "Destroy RoundtripView");
		this.getActivity().findViewById(switcher).setVisibility(View.GONE);
	}

	public boolean equals(Fragment f) {
		if (f.toString().equals(PullUpView.CONTENT_ROUNDTRIP)) {
			return true;
		}
		return false;
	}

	private class RoundtripComputeListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(computeRoundtrip)) {
				if (jogging.isSelected()) {
					routeController.addRoundtrip(
							Integer.parseInt(jogging.getTag().toString()),
							np.getValue() * 100);
				} else if (shopping.isSelected()) {
					routeController.addRoundtrip(
							Integer.parseInt(shopping.getTag().toString()),
							np.getValue() * 100);
				} else if (sightseeing.isSelected()) {
					routeController.addRoundtrip(
							Integer.parseInt(sightseeing.getTag().toString()),
							np.getValue() * 100);
				} else if (clubbing.isSelected()) {
					routeController.addRoundtrip(
							Integer.parseInt(clubbing.getTag().toString()),
							np.getValue() * 100);
				} else {
					routeController.addRoundtrip(0, np.getValue() * 100);
				}
			}
			return false;
		}

	}

	private class OnProfileTouch implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(jogging)) {
				if (!jogging.isSelected()) {
					setSelected(jogging);
					setUnselected(sightseeing);
					setUnselected(shopping);
					setUnselected(clubbing);

				} else {
					setUnselected(jogging);
				}
			} else if (v.equals(shopping)) {
				if (!shopping.isSelected()) {
					setSelected(shopping);
					setUnselected(sightseeing);
					setUnselected(jogging);
					setUnselected(clubbing);
				} else {
					setUnselected(shopping);
				}
			} else if (v.equals(sightseeing)) {
				if (!sightseeing.isSelected()) {
					setSelected(sightseeing);
					setUnselected(jogging);
					setUnselected(shopping);
					setUnselected(clubbing);
				} else {
					setUnselected(sightseeing);
				}
			} else if (v.equals(clubbing)) {
				if (!clubbing.isSelected()) {
					setSelected(clubbing);
					setUnselected(sightseeing);
					setUnselected(shopping);
					setUnselected(jogging);
				} else {
					setUnselected(clubbing);
				}
			}
			return false;
		}

		private void setUnselected(TextView v) {
			v.setSelected(false);
			v.setTextColor(Color.WHITE);
		}

		private void setSelected(TextView v) {
			v.setSelected(true);
			v.setTextColor(Color.YELLOW);
		}

	}
}

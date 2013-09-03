package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.views;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.views.Routing.GoToMapListener;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class Roundtrip extends LinearLayout {

	private static int PROFILE1 = R.string.sightseeing;
	private static int PROFILE2 = R.string.shopping;
	private static int PROFILE3 = R.string.clubbing;
	private final int ON_LONG_CLICK_UPDATE_INTERVALL_MS = 100;

	LinkedList<Integer> profilesStrings = new LinkedList<Integer>();
	LinkedList<TextView> profiles = new LinkedList<TextView>();

	private static int MIN_VALUE = 1000;
	private static int MAX_VALUE = 50000;
	private final int NUMBER_OF_STEPS = (MAX_VALUE - MIN_VALUE) / 100;

	private NumberPicker number;

	public Roundtrip(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.VERTICAL);

		Point size = BoundingBox.getInstance(context).getDisplaySize();

		profilesStrings.add(PROFILE1);
		profilesStrings.add(PROFILE2);
		profilesStrings.add(PROFILE3);

		// Zuweisung

		TextView title = new TextView(context, attrs);
		title.setText("Profiles");
		title.setTextSize(size.y / 28);
		title.setGravity(Gravity.CENTER);
		Button compute = new Button(context, attrs);
		compute.setText("compute Roundtrip");
		compute.setGravity(Gravity.CENTER);
		compute.setOnTouchListener(new OnComputeTouch());
		number = new NumberPicker(context, attrs);
		number.setMinValue(MIN_VALUE);
		number.setMaxValue(MAX_VALUE);
		number.setWrapSelectorWheel(false);

		LinearLayout.LayoutParams numberParam = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		numberParam.height = size.y / 5;
		numberParam.width = size.x / 2;
		numberParam.topMargin = 10;

		LinearLayout.LayoutParams computeParam = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		computeParam.height = size.y / 15;
		computeParam.width = size.x / 2;
		numberParam.topMargin = 10;

		String[] nums = new String[NUMBER_OF_STEPS];
		for (int i = 0; i < nums.length; i++)
			nums[i] = Integer.toString(MIN_VALUE + i * 100);

		number.setDisplayedValues(nums);
		number.setOnLongPressUpdateInterval(ON_LONG_CLICK_UPDATE_INTERVALL_MS);

		int counter = 2;
		for (Integer i : profilesStrings) {
			TextView text = new TextView(context, attrs);
			text.setText(context.getResources().getString(i));
			text.setTag(counter);
			text.setId(counter);
			text.setTextSize(size.y / 35);
			text.setGravity(Gravity.CENTER);
			text.setTextColor(Color.GRAY);
			text.setOnTouchListener(new OnProfileTouch());
			profiles.add(text);
			counter++;
		}

		LinearLayout line1 = new LinearLayout(context, attrs);
		line1.setOrientation(LinearLayout.HORIZONTAL);

		line1.addView(number, numberParam);
		line1.addView(compute, computeParam);
		line1.setGravity(Gravity.CENTER);

		this.addView(line1);
		this.addView(title);
		for (TextView i : profiles) {
			this.addView(i);
		}

	}

	public class OnProfileTouch implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			//int action = event.getAction();

			TextView t = (TextView) view;
			for (TextView v : profiles) {
				if (v.getId() == t.getId()) {
					if (v.isSelected()) {
						t.setSelected(false);
						t.setTextColor(Color.GRAY);
					} else {
						t.setSelected(true);
						t.setTextColor(Color.RED);
					}
				}  else {
					v.setSelected(false);
					v.setTextColor(Color.GRAY);
				}
			}

			return false;
		}

	}

	public class OnComputeTouch implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			int action = event.getAction();

			if (action == MotionEvent.ACTION_UP) {
				int id = -1;
				for (TextView v : profiles) {
					if (v.isSelected()) {
						id = Integer.parseInt(v.getTag().toString());
					}
				}
				if (id == -1) {
					this.alert();
					return false;
				}

				notifyComputeRoundtripListener(id, number.getValue());
				notifyGoToMapListener();
			}

			return false;
		}

		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle("Fehlendes Profil");
			alertDialog.setMessage("Bitte wählen Sie ein Profil.");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alertDialog.show();
		}
	}

	LinkedList<ComputeRoundtripListener> roundtripListener = new LinkedList<ComputeRoundtripListener>();

	private void notifyComputeRoundtripListener(int profile, int length) {
		for (ComputeRoundtripListener l : roundtripListener) {
			l.onComputeRoundtrip(profile, length);
		}
	}

	public void registerComputeRoundtripListener(
			ComputeRoundtripListener listener) {
		roundtripListener.add(listener);
	}

	public interface ComputeRoundtripListener {
		public void onComputeRoundtrip(int profile, int length);
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

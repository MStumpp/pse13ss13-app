package edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Routing.GoToMapListener;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Profile;

/**
 * This View shows the Roundtrip menu
 *
 * @author Ludwig Biermann
 * @version 1.1
 *
 */
public class Roundtrip extends LinearLayout {

	private static int PROFILE1 = R.string.sightseeing;
	private static int PROFILE2 = R.string.shopping;
	private static int PROFILE3 = R.string.clubbing;
	private final int ON_LONG_CLICK_UPDATE_INTERVALL_MS = 100;

	private List<Integer> profilesStrings = new LinkedList<Integer>();
	private List<TextView> profiles = new LinkedList<TextView>();
	private List<ComputeRoundtripListener> roundtripListener = new LinkedList<ComputeRoundtripListener>();
	private List<GoToMapListener> rl = new LinkedList<GoToMapListener>();

	private static int MIN_VALUE = 1000;
	private static int MAX_VALUE = 20000;
	private final int STEPSIZE = 100;

	private NumberPicker lengthPicker;
	private CheckBox checkbox;

	/**
	 * This create a new POIview.
	 *
	 * @param context the context of the app
	 * @param attrs the needed attributes
	 */
	public Roundtrip(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.VERTICAL);

		Point size = BoundingBox.getInstance(context).getDisplaySize();

		profilesStrings.add(PROFILE1);
		profilesStrings.add(PROFILE2);
		profilesStrings.add(PROFILE3);

		// Zuweisung

		TextView title = new TextView(context, attrs);
		title.setText(R.string.term_profiles);
		title.setTextSize(30);
		title.setGravity(Gravity.CENTER);
		Button compute = new Button(context, attrs);
		compute.setText(context.getString(R.string.compute, context.getString(R.string.term_roundtrip)));
		compute.setGravity(Gravity.CENTER);
		compute.setOnTouchListener(new OnComputeTouch());
		lengthPicker = new NumberPicker(context, attrs);
		lengthPicker.setMinValue(MIN_VALUE);
		lengthPicker.setMaxValue(MAX_VALUE);
		lengthPicker.setWrapSelectorWheel(false);
		lengthPicker.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				lengthPicker.setValue((int) (Math.signum(newVal - oldVal) * STEPSIZE + oldVal));
			}
		});
		// führt dazu das man in den picker nichts mehr schreiben kann (nur noch über buttons verstellbar)
		lengthPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

		LinearLayout.LayoutParams numberParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		numberParam.height = size.y / 5;
		numberParam.width = size.x / 3;
		//numberParam.topMargin = 10;
		numberParam.gravity = Gravity.CENTER;

		LinearLayout.LayoutParams computeParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		computeParam.height = size.y / 10;
		computeParam.width = size.x * 2 / 3;
		computeParam.topMargin = 10;
		computeParam.gravity = Gravity.CENTER;

		//String[] nums = new String[NUMBER_OF_STEPS];
		//for (int i = 0; i < nums.length; i++)
		//	nums[i] = Integer.toString(MIN_VALUE + i * 100);

		//number.setDisplayedValues(nums);
		lengthPicker.setOnLongPressUpdateInterval(ON_LONG_CLICK_UPDATE_INTERVALL_MS);

		int counter = 2;
		for (Integer i : profilesStrings) {
			TextView text = new TextView(context, attrs);
			text.setText(context.getResources().getString(i));
			text.setTag(counter);
			text.setId(counter);
			text.setTextSize(25);
			text.setGravity(Gravity.CENTER);
			text.setTextColor(Color.WHITE);
			text.setOnTouchListener(new OnProfileTouch());
			profiles.add(text);
			counter++;
		}
		
		// Checkbox für das Anzeigen der PoiCats des rundkurses
		
		LinearLayout.LayoutParams checkboxParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		checkboxParam.topMargin = 10;
		checkboxParam.gravity = Gravity.CENTER;
		
		checkbox = new CheckBox(context);
		checkbox.setText(R.string.poi_checkbox);
		checkbox.setTextSize(30);
		checkbox.setTextColor(Color.WHITE);
		checkbox.setChecked(true);

		LinearLayout line1 = new LinearLayout(context, attrs);
		line1.setOrientation(LinearLayout.HORIZONTAL);

		TextView length = new TextView(getContext());
		length.setText(R.string.length);
		length.setTextSize(30);
		length.setGravity(Gravity.CENTER);

		TextView unit = new TextView(getContext());
		unit.setText(R.string.meter);
		unit.setTextSize(30);
		unit.setGravity(Gravity.CENTER);

		line1.addView(length);
		line1.addView(lengthPicker, numberParam);
		line1.addView(unit);
		//line1.addView(compute, computeParam);
		line1.setGravity(Gravity.CENTER);

		this.addView(line1);
		this.addView(title);
		for (TextView i : profiles) {
			this.addView(i);
		}
		this.addView(checkbox, checkboxParam);
		this.addView(compute, computeParam);
	}

	/**
	 * Listen to a Profile touch event
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	public class OnProfileTouch implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			TextView t = (TextView) view;
			for (TextView v : profiles) {
				if (v.getId() == t.getId()) {
					if (v.isSelected()) {
						t.setSelected(false);
						t.setTextColor(Color.WHITE);
					} else {
						t.setSelected(true);
						t.setTextColor(Color.RED);
					}
				}  else {
					v.setSelected(false);
					v.setTextColor(Color.WHITE);
				}
			}

			return false;
		}

	}

	/**
	 *
	 * Listen to a the Compute touch event
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
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
				
				if (checkbox.isChecked() && id != -1) {
					for(int cat : Profile.getByID(id).getContainingPOICategories()) {
						POIManager.getInstance(getContext()).togglePOICategory(cat);
					}
				}

				notifyComputeRoundtripListener(id, lengthPicker.getValue());
				notifyGoToMapListener();
			}

			return false;
		}

		/**
		 * makes a alert
		 */
		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle("Fehlendes Profil");
			alertDialog.setMessage("Bitte wählen Sie ein Profil.");
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Do nothing
						}
					});
			alertDialog.show();
		}
	}


	/**
	 * natify all compute Roundtrip Listener
	 *
	 * @param profile new profile
	 * @param length the length
	 */
	private void notifyComputeRoundtripListener(int profile, int length) {
		for (ComputeRoundtripListener l : roundtripListener) {
			l.onComputeRoundtrip(profile, length);
		}
	}

	/**
	 * register a new ComputeRoundtrip Listener
	 * @param listener new listener
	 */
	public void registerComputeRoundtripListener(
			ComputeRoundtripListener listener) {
		roundtripListener.add(listener);
	}

	/**
	 * Interface to make a call by computing Roundtrips
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	public interface ComputeRoundtripListener {

		/**
		 * is called if the compute button is pressed
		 *
		 * @param profile the profile
		 * @param length the length
		 */
		public void onComputeRoundtrip(int profile, int length);
	}

	/**
	 * notify all listener
	 */
	private void notifyGoToMapListener() {
		for (GoToMapListener l : rl) {
			l.onGoToMap();
		}
	}

	/**
	 * register a gotomap listener
	 * @param listener the new listener
	 */
	public void registerGoToMapListener(GoToMapListener listener) {
		rl.add(listener);
	}
}

package edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.PreferenceUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TextToSpeechUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * This View shows the Information View for POI´s.
 * 
 * @author Ludwig Biermann
 * @version 1.2
 *
 */
public class Info extends LinearLayout {

	private TextView title;
	private TextView text;
	private ImageButton sound;
	private ImageButton save;
	private ImageView textImage;
	private static final String TAG = Info.class.getSimpleName();

	private boolean speak = false;

	/**
	 * This create a new POIview.
	 * 
	 * @param context the context of the app
	 * @param attrs the needed attributes
	 */
	public Info(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(LinearLayout.VERTICAL);
		TextToSpeechUtility.initialize(getContext(), true);

		Point size = BoundingBox.getInstance(context).getDisplaySize();

		title = new TextView(context, attrs);
		title.setTextSize(size.y / 35);
		title.setGravity(Gravity.CENTER);

		text = new TextView(context, attrs);
		text.setTextSize(size.y / 50);

		textImage = new ImageView(context, attrs);
		textImage.setImageResource(R.drawable.play);
		textImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

		sound = new ImageButton(context, attrs);
		sound.setImageResource(R.drawable.play);

		LinearLayout.LayoutParams soundParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		soundParams.height = size.x / 5;
		soundParams.width = size.x / 5;

		save = new ImageButton(context, attrs);
		save.setImageResource(R.drawable.favorite);

		LinearLayout.LayoutParams saveParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		saveParams.height = size.x / 5;
		saveParams.width = size.x / 5;

		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleParams.height = size.x / 5;
		titleParams.width = size.x * 3 / 5;

		LinearLayout.LayoutParams textImageParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textImageParams.height = size.x / 2;
		textImageParams.width = size.x / 2;

		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textParams.width = size.x;

		LinearLayout line1 = new LinearLayout(context, attrs);
		line1.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout line2 = new LinearLayout(context, attrs);
		line1.setOrientation(LinearLayout.HORIZONTAL);

		ScrollView scroll = new ScrollView(context, attrs);

		title.setVisibility(GONE);
		text.setVisibility(GONE);
		textImage.setVisibility(GONE);
		sound.setVisibility(GONE);

		line1.addView(title, titleParams);
		line1.addView(sound, soundParams);
		line1.addView(save, saveParams);

		this.addView(line1);
		this.addView(scroll);

		scroll.addView(line2);

		line2.setGravity(Gravity.CENTER);

		line2.addView(textImage, textImageParams);
		line2.addView(text, textParams);

	}

	public void update(POI poi) {

		this.title.setVisibility(GONE);
		this.text.setVisibility(GONE);
		this.textImage.setVisibility(GONE);
		sound.setVisibility(GONE);
		save.setOnTouchListener(new saveListener(poi));

		if (poi.getName() != null) {
			this.title.setText(poi.getName());
			this.title.setVisibility(VISIBLE);

			if (PreferenceUtility.getInstance().isPOITitleSoundOn()) {
				TextToSpeechUtility.getInstance().speak(poi.getName());
				this.speak = true;
				this.sound.setImageResource(R.drawable.pause);
			}
		}
		
		if (null != null && PreferenceUtility.getInstance().isPOIImageOn()) {
			this.textImage.setImageBitmap(null);
			this.textImage.setVisibility(VISIBLE);
		}
		
		if (poi.getTextInfo() != null) {
			speak = true;
			sound.setOnTouchListener(new playListener(poi.getTextInfo()));
			this.text.setText(poi.getTextInfo());
			this.text.setVisibility(VISIBLE);
			sound.setVisibility(VISIBLE);

			if (PreferenceUtility.getInstance().isPOITextSoundOn()) {
				TextToSpeechUtility.getInstance().speak(poi.getTextInfo());
				this.speak = true;
				this.sound.setImageResource(R.drawable.pause);
			}

		}
	}

	public void stopSpeaking() {
		TextToSpeechUtility.getInstance().stopSpeaking();
		toogleSpeaking();
	}

	private void toogleSpeaking() {
		if (this.speak) {
			this.speak = false;
			this.sound.setImageResource(R.drawable.play);

		} else {
			this.speak = true;
			this.sound.setImageResource(R.drawable.pause);

		}
	}

	private class playListener implements OnTouchListener {

		String text;

		public playListener(String text) {
			this.text = text;
		}

		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(sound) && event.getAction() == MotionEvent.ACTION_DOWN) {
				if (speak) {
					Log.d(TAG, "play wurde gedrückt");
					toogleSpeaking();
					TextToSpeechUtility.getInstance().stopSpeaking();
				} else {
					toogleSpeaking();
					TextToSpeechUtility.getInstance().speak(text);
				}
			}
			return false;
		}
	}

	private class saveListener implements OnTouchListener {

		private POI poi;
		private EditText edit;

		public saveListener(POI poi) {
			this.poi = poi;
		}

		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				Log.d(TAG, "save wurde gedrückt");
				this.alert();
			}
			return false;
		}

		public void alert() {
			edit = new EditText(getContext());
			edit.setText(poi.getName());
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle("Favorite hinzufügen");
			alertDialog.setMessage("Bitte geben Sie einen Namen an.");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							FavoriteManager.getInstance(getContext())
									.addLocationToFavorites(
											new Location(poi.getLatitude(), poi
													.getLongitude(), poi
													.getName()),
											edit.getText().toString());

						}
					});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alertDialog.setView(edit);
			alertDialog.show();
		}
	}
}

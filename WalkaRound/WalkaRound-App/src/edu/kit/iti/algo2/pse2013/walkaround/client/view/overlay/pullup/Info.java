package edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.POIImageFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.PreferenceUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TextToSpeechUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

/**
 * This View shows the Information View for POI´s.
 *
 * @author Ludwig Biermann
 * @version 1.2
 *
 */
public class Info extends LinearLayout {

	private Button title;
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

		Point size = BoundingBox.getInstance(context).getDisplaySize();

		title = new Button(context, attrs);
		title.setTextSize(25);
		title.setGravity(Gravity.CENTER);

		text = new TextView(context, attrs);
		text.setTextSize(15);

		textImage = new ImageView(context, attrs);
		textImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

		sound = new ImageButton(context, attrs);
		sound.setImageResource(R.drawable.play);

		LinearLayout.LayoutParams soundParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		soundParams.height = size.x / 5;
		soundParams.width = size.x / 5;

		save = new ImageButton(context, attrs);
		save.setImageResource(R.drawable.favorite);

		LinearLayout.LayoutParams saveParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		saveParams.height = size.x / 5;
		saveParams.width = size.x / 5;

		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		titleParams.height = size.x / 5;
		titleParams.width = size.x * 3 / 5;

		LinearLayout.LayoutParams textImageParams = new LinearLayout.LayoutParams(size.x, size.x / 2);

		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

		line2.setOrientation(VERTICAL);

		line2.addView(textImage, textImageParams);
		line2.addView(text, textParams);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Catch all Touch events
		return true;
	}

	/**
	 * update a poi
	 * @param poi the poi to update
	 */
	public void update(final POI poi) {

		this.title.setVisibility(GONE);
		this.text.setVisibility(GONE);
		this.textImage.setVisibility(GONE);
		sound.setVisibility(GONE);
		save.setOnTouchListener(new saveListener(poi));

		if (poi.getName() != null) {
			this.title.setText(poi.getName());
			this.title.setVisibility(VISIBLE);
			title.setOnTouchListener(new AddPOIListener(poi));
			sound.setVisibility(VISIBLE);
			sound.setOnTouchListener(new PlayListener(poi.getName()));

			if (PreferenceUtility.getInstance().isPOITitleSoundOn()) {
				TextToSpeechUtility.speak(poi.getName());
				this.speak = true;
				this.sound.setImageResource(R.drawable.pause);
			}
		}

		if (poi.getTextInfo() != null) {
			speak = true;
			Spanned htmlizedText = Html.fromHtml(poi.getTextInfo());
			if(poi.getName() == null){
				sound.setOnTouchListener(new PlayListener(htmlizedText.toString()));
			} else {
				sound.setOnTouchListener(new PlayListener(poi.getName() + " " + htmlizedText.toString()));
			}
			text.setText(htmlizedText);
			text.setMovementMethod(LinkMovementMethod.getInstance());
			text.setVisibility(VISIBLE);
			sound.setVisibility(VISIBLE);

			if (PreferenceUtility.getInstance().isPOITextSoundOn()) {
				TextToSpeechUtility.speak(htmlizedText.toString());
				this.speak = true;
				this.sound.setImageResource(R.drawable.pause);
			}
		}

		if (poi.getURL() != null && PreferenceUtility.getInstance().isPOIImageOn()) {
			textImage.post(new Runnable() {
				@Override
				public void run() {
					try {
						POIImageFetcher fetcher = new POIImageFetcher(poi.getURL(), null);
						Thread t = new Thread(fetcher);
						t.start();
						t.join();
						Bitmap bmp = fetcher.getBitmap();
						Log.d(TAG, "Fetched POI-Bitmap " + bmp);
						textImage.setImageBitmap(bmp);
						textImage.setVisibility(VISIBLE);
					} catch (InterruptedException e) {
						Log.e(TAG, "Could not fetch POI-image", e);
					}
				}
			});
		}
	}

	/**
	 * stops speaking
	 */
	public void stopSpeaking() {
		TextToSpeechUtility.stopSpeaking();
		toogleSpeaking();
	}

	/**
	 * toogle speaking
	 */
	private void toogleSpeaking() {
		if (this.speak) {
			this.speak = false;
			this.sound.setImageResource(R.drawable.play);

		} else {
			this.speak = true;
			this.sound.setImageResource(R.drawable.pause);

		}
	}

	/**
	 * Listen for a Play event
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class PlayListener implements OnTouchListener {

		private String text;

		/**
		 * construct a new play listener
		 *
		 * @param text the text to speak
		 */
		public PlayListener(String text) {
			this.text = text;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(sound) && event.getAction() == MotionEvent.ACTION_UP) {
				if (speak) {
					Log.d(TAG, "play wurde gedrückt");
					toogleSpeaking();
					TextToSpeechUtility.stopSpeaking();
				} else {
					toogleSpeaking();
					TextToSpeechUtility.speak(text);
				}
			}
			return false;
		}
	}

	/**
	 * Listen for a save event
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 */
	private class saveListener implements OnTouchListener {

		private POI poi;
		private EditText edit;

		/**
		 * construkt a save lsitener
		 * @param poi the poi to listen
		 */
		public saveListener(POI poi) {
			this.poi = poi;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				Log.d(TAG, "save wurde gedrückt");
				this.alert();
			}
			return false;
		}

		/**
		 * makes a alert
		 */
		public void alert() {
			edit = new EditText(getContext());
			edit.setText(poi.getName());
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle("Favorite hinzufügen");
			alertDialog.setMessage("Bitte geben Sie einen Namen an.");
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							FavoriteManager.getInstance(getContext())
									.addLocationToFavorites(
											new Location(poi.getLatitude(), poi
													.getLongitude(), poi
													.getName()),
											edit.getText().toString());

						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Do nothing
						}
					});
			alertDialog.setView(edit);
			alertDialog.show();
		}
	}
	
	private class AddPOIListener implements OnTouchListener {

		private POI poi;

		/**
		 * construct a new play listener
		 *
		 * @param text the text to speak
		 */
		public AddPOIListener(POI poi) {
			this.poi = poi;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(title) && event.getAction() == MotionEvent.ACTION_UP) {
				this.alert();
			}
			return false;
		}
		
		/**
		 * makes a alert
		 */
		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle(getContext().getString(R.string.dialog_header_add_poi_to_route,
					getContext().getString(R.string.term_poi)));
			alertDialog.setMessage(getContext().getString(
					R.string.dialog_text_add_poi_to_route,
					poi.getName()));
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					getContext().getString(R.string.option_add),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							RouteController.getInstance().addWaypoint(new Waypoint(poi.getLatitude(),
									poi.getLongitude(), poi.getName()));
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					getContext().getString(R.string.option_cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// EMPTY
						}
					});
			alertDialog.show();
		}

	}		
}

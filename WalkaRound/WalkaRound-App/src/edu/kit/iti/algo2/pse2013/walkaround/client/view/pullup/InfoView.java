package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.POIImageFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TextToSpeechUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

public class InfoView extends Fragment {

	private final String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupInfoViewSwitcher;
	private TextView title;
	private ImageView iv;
	private TextView category;
	private TextView text;
	private ImageView save;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create InfoView");
		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);

		this.title = (TextView) this.getActivity().findViewById(
				R.id.poiinfoview_title);
		Log.d("wtf", "title: " + (this.title == null));
		this.iv = (ImageView) this.getActivity().findViewById(
				R.id.poiinfoview_image);
		this.category = (TextView) this.getActivity().findViewById(
				R.id.poiinfoview_category);
		this.text = (TextView) this.getActivity().findViewById(
				R.id.poiinfoview_text);
		save = (ImageView) (this.getActivity().findViewById(R.id.savepoi));

		POI poi = MapController.getInstance().getPOI();
		save.setOnTouchListener(new saveListener(poi, save));

		if (poi != null) {
			Log.d("wtf", "" + (poi.getName() == null) + (title == null));

			if (poi.getName() != null) {
				title.setText(poi.getName());
				title.setVisibility(View.VISIBLE);
			}

			Bitmap b = POIImageFetcher.fetchImage(poi.getURL());
			if (b != null) {
				iv.setImageBitmap(b);
				iv.setVisibility(View.VISIBLE);
			}

			if (poi.getPOICategories().length != 0) {
				category.setText("Category: ");
				for (int i = 0; i < poi.getPOICategories().length; i++) {
					if (i == 0) {
						category.setText(category.getText()
								+ getCategoryName(poi.getPOICategories()[i]));
					} else {
						category.setText(category.getText() + ", "
								+ getCategoryName(poi.getPOICategories()[i]));
					}
				}
				category.setVisibility(View.VISIBLE);
			}

			if (poi.getTextInfo() != null) {
				text.setText(poi.getTextInfo());
				text.setVisibility(View.VISIBLE);
				TextToSpeechUtility.getInstance().speak(poi.getTextInfo());
			}
		}

	}

	private String getCategoryName(int id) {
		TextView tv;
		switch (id) {
		case 1:
			tv = (TextView) getActivity().findViewById(R.id.category_1);
			return tv.getText().toString();
		case 2:
			tv = (TextView) getActivity().findViewById(R.id.category_2);
			return tv.getText().toString();
		case 3:
			tv = (TextView) getActivity().findViewById(R.id.category_3);
			return tv.getText().toString();
		case 4:
			tv = (TextView) getActivity().findViewById(R.id.category_4);
			return tv.getText().toString();
		case 5:
			tv = (TextView) getActivity().findViewById(R.id.category_5);
			return tv.getText().toString();
		case 6:
			tv = (TextView) getActivity().findViewById(R.id.category_6);
			return tv.getText().toString();
		case 7:
			tv = (TextView) getActivity().findViewById(R.id.category_7);
			return tv.getText().toString();
		case 8:
			tv = (TextView) getActivity().findViewById(R.id.category_8);
			return tv.getText().toString();
		case 9:
			tv = (TextView) getActivity().findViewById(R.id.category_9);
			return tv.getText().toString();
		case 10:
			tv = (TextView) getActivity().findViewById(R.id.category_10);
			return tv.getText().toString();
		case 11:
			tv = (TextView) getActivity().findViewById(R.id.category_11);
			return tv.getText().toString();
		default:
			return "";
		}
	}

	@Override
	public void onDestroy() {
		Log.d(TAG_PULLUP_CONTENT, "Destroy InfoView");
		this.getActivity().findViewById(switcher).setVisibility(View.GONE);
		super.onDestroy();
	}

	public boolean equals(Fragment f) {
		if (f.toString().equals(PullUpView.CONTENT_INFO)) {
			return true;
		}
		return false;
	}

	private class saveListener implements OnTouchListener {

		private POI poi;
		private View view;

		public saveListener(POI poi, View view) {
			this.poi = poi;
			this.view = view;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(save) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_CONTENT, "save wurde gedr�ckt");
				RouteController.getInstance().addLocationToFavorites(
						new Location(poi.getLatitude(), poi.getLongitude(),
								poi.getName()), poi.getName());
				MapController.getInstance().getPullUpView()
				.changeView(PullUpView.CONTENT_FAVORITE);
			}
			return false;
		}
	}
}

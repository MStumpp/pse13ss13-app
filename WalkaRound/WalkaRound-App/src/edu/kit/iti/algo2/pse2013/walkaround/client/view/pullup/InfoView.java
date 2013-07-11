package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.POIImageFetcher;

public class InfoView extends Fragment {

	private final String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupInfoViewSwitcher;
	TextView title;
	ImageView iv;
	TextView text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create InfoView");
		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);

		title = (TextView) this.getActivity().findViewById(
				R.id.poiinfoview_title);
		iv = (ImageView) this.getActivity()
				.findViewById(R.id.poiinfoview_image);
		text = (TextView) this.getActivity()
				.findViewById(R.id.poiinfoview_text);
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

	public void showPOIInfo(String name, String url, String textInfo) {

		Log.d("wtf", "" + (name == null) + (title == null));

		if (name != null) {
			title.setText(name);
			title.setVisibility(View.VISIBLE);
		}

		Bitmap b = POIImageFetcher.fetchImage(url);
		if (b != null) {
			iv.setImageBitmap(b);
			iv.setVisibility(View.VISIBLE);
		}

		if (textInfo != null) {
			text.setText(textInfo);
			text.setVisibility(View.VISIBLE);
		}
	}
}

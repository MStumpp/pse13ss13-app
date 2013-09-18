package edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Roundtrip.POICatsChangeListener;

/**
 * This View shows the POI Menu.
 *
 * @author Ludwig Biermann
 * @version 1.1
 *
 */
public class POILayout extends RelativeLayout implements POICatsChangeListener{

	private ScrollView scrollView;
	private String[] categories;
	private LinkedList<POIChangeListener> poiChangeListener = new LinkedList<POIChangeListener>();
	private LinearLayout content;

	/**
	 * This create a new POIview.
	 *
	 * @param context
	 *            the context of the app
	 * @param attrs
	 *            the needed attributes
	 */
	public POILayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		categories = context.getResources().getStringArray(R.array.POICat);

		Point size = BoundingBox.getInstance(context).getDisplaySize();

		scrollView = new ScrollView(context, attrs);
		content = new LinearLayout(context, attrs);
		content.setOrientation(LinearLayout.VERTICAL);

		// Content
		LayoutParams contentParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		// ScrollView
		RelativeLayout.LayoutParams scrollViewParam = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		// Category
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		param.topMargin = 5;
		param.height = size.y / 20;
		scrollView.addView(content, contentParam);

		for (int i = 0; i < categories.length; i++) {
			TextView b = new TextView(context, attrs);
			b.setText(categories[i]);
			b.setTag(i);
			b.setOnTouchListener(new OnCategoryTouch());
			// b.setBackgroundColor(Color.rgb(50, 50, 50));
			b.setTextColor(Color.WHITE);
			b.setHighlightColor(Color.BLUE);
			b.setTextSize(25);
			b.setSelected(false);
			b.setGravity(Gravity.CENTER);

			content.addView(b);
		}

		this.addView(scrollView, scrollViewParam);
	}

	/**
	 * Listen for a Category change
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class OnCategoryTouch implements OnTouchListener {

		@Override
		public boolean onTouch(final View v, MotionEvent event) {

			TextView b = (TextView) v;

			final int id = Integer.parseInt(v.getTag().toString());
			if (b.isSelected()) {
				b.setSelected(false);
				b.setTextColor(Color.WHITE);
				b.setBackgroundColor(Color.TRANSPARENT);
			} else {
				b.setSelected(true);
				b.setTextColor(Color.RED);
				b.setBackgroundColor(Color.TRANSPARENT);
			}
			POIManager.getInstance(getContext()).togglePOICategory(id);
			notifyComputeRoundtripListener();
			return false;
		}
	}

	/**
	 * notify all Listener
	 */
	private void notifyComputeRoundtripListener() {
		for (POIChangeListener l : poiChangeListener) {
			l.onPOIChange();
		}
	}

	/**
	 * register a new POIChangeListener
	 *
	 * @param listener
	 *            the new Listener
	 */
	public void registerPOIChangeListener(POIChangeListener listener) {
		poiChangeListener.add(listener);
	}

	/**
	 * A Interface for the POI´s
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	public interface POIChangeListener {

		/**
		 * is called if a POI is changed
		 */
		public void onPOIChange();
	}

	@Override
	public void onCatsChange(int[] cats) {
		
		for(int cat : cats) {
			for(int i = 0; i < content.getChildCount(); i++) {
				if(content.getChildAt(i).getTag().toString().equals(String.valueOf(cat))) {
					TextView b = (TextView) content.getChildAt(i);
					
					if (!b.isSelected()) {
						b.setSelected(true);
						b.setTextColor(Color.RED);
						b.setBackgroundColor(Color.TRANSPARENT);
						POIManager.getInstance(getContext()).togglePOICategory(cat);
					}
					notifyComputeRoundtripListener();
				}
			}
		}
	}
}

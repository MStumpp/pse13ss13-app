package edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;

/**
 * This View shows the POI Menu.
 * 
 * @author Ludwig Biermann
 * @version 1.1
 *
 */
public class POILayout extends RelativeLayout {

	private ScrollView scrollView;
	private static int category1 = R.string.bars_and_pubs;
	private static int category2 = R.string.cinema;
	private static int category3 = R.string.clubs_and_nightclubs;
	private static int category4 = R.string.fast_food;
	private static int category5 = R.string.food;
	private static int category6 = R.string.museum;
	private static int category7 = R.string.public_transportation;
	private static int category8 = R.string.shop;
	private static int category9 = R.string.sleeping_accomodation;
	private static int category10 = R.string.supermarket;
	private static int category11 = R.string.theatre;
	private static int category12 = R.string.monument;
	private static int category13 = R.string.castle;

	private LinkedList<Integer> category = new LinkedList<Integer>();
	private LinearLayout content;

	/**
	 * This create a new POIview.
	 * 
	 * @param context the context of the app
	 * @param attrs the needed attributes
	 */
	public POILayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		Point size = BoundingBox.getInstance(context).getDisplaySize();

		category.add(category1);
		category.add(category2);
		category.add(category3);
		category.add(category4);
		category.add(category5);
		category.add(category6);
		category.add(category7);
		category.add(category8);
		category.add(category9);
		category.add(category10);
		category.add(category11);
		category.add(category12);
		category.add(category13);

		scrollView = new ScrollView(context, attrs);
		content = new LinearLayout(context, attrs);
		content.setOrientation(LinearLayout.VERTICAL);

		// Content
		LayoutParams contentParam = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		// ScrollView
		RelativeLayout.LayoutParams scrollViewParam = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		// Category
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		param.topMargin = 5;
		param.height = size.y / 20;
		scrollView.addView(content, contentParam);

		for (Integer res : category) {
			TextView b = new TextView(context, attrs);
			b.setText(context.getResources().getString(res));
			b.setTag(res);
			b.setOnTouchListener(new onCategoryTouch());
			b.setBackgroundColor(Color.GRAY);
			b.setTextColor(Color.BLACK);
			b.setSelected(false);
			b.setGravity(Gravity.CENTER);

			content.addView(b, param);
		}

		this.addView(scrollView, scrollViewParam);
	}

	private class onCategoryTouch implements OnTouchListener {

		public boolean onTouch(final View v, MotionEvent event) {

			TextView b = (TextView) v;

			int id = Integer.parseInt(v.getTag().toString());
			if (b.isSelected()) {
				b.setSelected(false);
				b.setTextColor(Color.BLACK);
				POIManager.getInstance(getContext()).removeActivePOICategory(
						getCategoryID(id));
			} else {
				b.setSelected(true);
				b.setTextColor(Color.RED);
				POIManager.getInstance(getContext()).addActivePOICategory(
						getCategoryID(id));

			}
			notifyComputeRoundtripListener();
			return false;
		}

		public int getCategoryID(int id) {
			for (int i = 0; i < category.size(); i++) {
				if (category.get(i) == id) {
					return (i + 1);
				}
			}
			return 0;

		}

	}

	LinkedList<POIChangeListener> poiChangeListener = new LinkedList<POIChangeListener>();

	private void notifyComputeRoundtripListener() {
		for (POIChangeListener l : poiChangeListener) {
			l.onPOIChange();
		}
	}

	public void registerPOIChangeListener(POIChangeListener listener) {
		poiChangeListener.add(listener);
	}

	public interface POIChangeListener {
		public void onPOIChange();
	}
}

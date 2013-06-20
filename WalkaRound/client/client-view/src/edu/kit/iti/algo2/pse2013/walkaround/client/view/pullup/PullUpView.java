package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;

public class PullUpView extends Activity {

	private static final String TAG_PULLUP = "PULL_UP";

	private float height;

	private RelativeLayout main;

	private RelativeLayout menu;
	private ImageView routing;
	private ImageView favorite;
	private ImageView roundtrip;
	private ImageView poi;
	private ImageView search;

	private ImageView regulator;

	private PullUpMenuListener pullListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.pullup);

		Log.d(TAG_PULLUP, "Zuweisung des layouts");

		main = (RelativeLayout) this.findViewById(R.id.pullUpMain);

		menu = (RelativeLayout) this.findViewById(R.id.staticMenu);
		routing = (ImageView) this.findViewById(R.id.routingMenuButton);
		favorite = (ImageView) this.findViewById(R.id.favoriteMenuButton);
		roundtrip = (ImageView) this.findViewById(R.id.roundtripMenuButton);
		poi = (ImageView) this.findViewById(R.id.poiMenuButton);
		search = (ImageView) this.findViewById(R.id.searchMenuButton);

		regulator = (ImageView) this.findViewById(R.id.pullupOpenClose);

		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		Log.d(TAG_PULLUP, "Einstellen der Größenverhältnisse");

		// Menü
		// menu.setLayoutParams(new LayoutParams(size.x, size.y));
		menu.getLayoutParams().width = size.x;
		menu.getLayoutParams().height = size.y / 10;
		Log.d(TAG_PULLUP,
				"Das static Menü hat die Breite: "
						+ menu.getLayoutParams().width);

		routing.setX(size.x / 5 * 0);
		routing.getLayoutParams().width = size.x / 5;

		favorite.setX(size.x / 5 * 1);
		favorite.getLayoutParams().width = size.x / 5;

		roundtrip.setX(size.x / 5 * 2);
		roundtrip.getLayoutParams().width = size.x / 5;

		poi.setX(size.x / 5 * 3);
		poi.getLayoutParams().width = size.x / 5;

		search.setX(size.x / 5 * 4);
		search.getLayoutParams().width = size.x / 5;

		Log.d(TAG_PULLUP, "Die Position des Routing Button: " + routing.getX());
		Log.d(TAG_PULLUP, "Die Position des Search Button: " + search.getX());

		// main
		// main.getLayoutParams().height = size.y;
		Log.d(TAG_PULLUP, "Die Höhe von main ist: "
				+ main.getLayoutParams().height);

		// Pull Up Controll Button

		regulator.getLayoutParams().height = size.y / 20;

		Log.d(TAG_PULLUP, "Zuweisung der Listener");
		pullListener = new PullUpMenuListener();
		routing.setOnTouchListener(new RoutingListener());
		favorite.setOnTouchListener(new FavoriteListener());
		roundtrip.setOnTouchListener(new RoundtripListener());
		poi.setOnTouchListener(new POIListener());
		search.setOnTouchListener(new SearchListener());
		regulator.setOnLongClickListener(new PullUpMenuListener());

	}

	public void setHeight(int height) {
		main.getLayoutParams().height = height;
	}

	public float getHeight() {
		return main.getLayoutParams().height;
	}

	public void changeView(int v) {

	}

	private class RoutingListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d(TAG_PULLUP, "routing wurde aufgerufen");
			return true;
		}
	}

	private class FavoriteListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d(TAG_PULLUP, "Favorite wurde aufgerufen");
			return true;
		}
	}

	private class RoundtripListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d(TAG_PULLUP, "roundtrip wurde aufgerufen");
			return true;
		}
	}

	private class POIListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d(TAG_PULLUP, "poi wurde aufgerufen");
			return true;
		}
	}

	private class SearchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d(TAG_PULLUP, "search wurde aufgerufen");
			return true;
		}
	}

	private class PullUpMenuListener implements OnLongClickListener {

		
		@Override
		public boolean onLongClick(View arg0) {

			Log.d(TAG_PULLUP, "regulator wurde aufgerufen");
			main.setY(500);
			Log.d(TAG_PULLUP,
					"Die Höhe von main ist: " + main.getLayoutParams().height);
			return true;
		}

	}
}

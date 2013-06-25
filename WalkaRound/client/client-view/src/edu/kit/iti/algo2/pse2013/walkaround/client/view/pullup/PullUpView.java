package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;

/**
 * 
 * Diese Klasse implementiert ein sogenanntes PullUp Menü. Dieses Menü ist speziell für Walkaround entwickelt worden und 
 * bietet ein statisches menü zum wechseln der Nutzerinteraktionsmöglichkeiten. Sowie speziellen animationen um das
 * Menü so Nutzer freundlich wie Möglich zu machen.
 * 
 * 
 * @author Ludwig Biermann
 *
 */
public class PullUpView extends Fragment {

	/*
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	*/
	private static final String TAG_PULLUP = "PULL_UP";

	// private float height;

	public static final int CONTENT_ROUTING = 0;
	public static final int CONTENT_FAVORITE = 1;
	public static final int CONTENT_ROUNDTRIP = 2;
	public static final int CONTENT_POI = 3;
	public static final int CONTENT_SEARCH = 4;
	
	private RelativeLayout main;

	private RelativeLayout menu;
	private ImageView routing;
	private ImageView favorite;
	private ImageView roundtrip;
	private ImageView poi;
	private ImageView search;

	private ImageView regulator;

	private float minHeight;
	private float minBorderHeight;
	private float halfHeight;
	private float maxBorderHeight;
	private float maxHeight;

	private GestureDetector gestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.pullup);

		Log.d(TAG_PULLUP, "Zuweisung des layouts");

		main = (RelativeLayout) this.getActivity()
				.findViewById(R.id.pullUpMain);

		menu = (RelativeLayout) this.getActivity()
				.findViewById(R.id.staticMenu);
		routing = (ImageView) this.getActivity().findViewById(
				R.id.routingMenuButton);
		favorite = (ImageView) this.getActivity().findViewById(
				R.id.favoriteMenuButton);
		roundtrip = (ImageView) this.getActivity().findViewById(
				R.id.roundtripMenuButton);
		poi = (ImageView) this.getActivity().findViewById(R.id.poiMenuButton);
		search = (ImageView) this.getActivity().findViewById(
				R.id.searchMenuButton);

		regulator = (ImageView) this.getActivity().findViewById(
				R.id.pullupOpenClose);

		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		minHeight = 0;
		maxHeight = size.y - size.y / 10 - size.y / 40;
		halfHeight = (maxHeight / 2);
		minBorderHeight = halfHeight / 2;
		maxBorderHeight = halfHeight / 2 + halfHeight;

		Log.d(TAG_PULLUP, "Max = " + maxHeight);
		Log.d(TAG_PULLUP, "Min = " + minHeight);

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
		routing.setOnTouchListener(new RoutingListener());
		favorite.setOnTouchListener(new FavoriteListener());
		roundtrip.setOnTouchListener(new RoundtripListener());
		poi.setOnTouchListener(new POIListener());
		search.setOnTouchListener(new SearchListener());

		// regulator.setTag("Schieberegler");
		regulator.setOnTouchListener(new RegulatorListener());
		gestureDetector = new GestureDetector(getActivity(),
				new MyGestureDetector());
		// regulator.setOnTouchListener(new RegulatorListener());
		// regulator.setOnDragListener(new DragListener());

		// regulator.setOnTouchListener(new PullUpMenuListener());
		main.setY(maxHeight);

	}

	/**
	 *  Setzt die Höhe des Menüs auf FullSize
	 */
	private void setFullSizeHeight() {
		this.setHeight(main.getY() * -1, 1000);
		// this.duration = 0;
	}

	/**
	 *   Setzt die Höhe des Menüs auf HalfSize
	 */
	private void setHalfSizeHeight() {
		this.setHeight(halfHeight - main.getY(), 1000);
		// this.duration = 0;
	}

	/**
	 *   Setzt die Höhe des Menüs auf Minimal
	 */
	private void setNullSizeHeight() {
		this.setHeight(maxHeight - main.getY(), 1000);
		// this.duration = 0;
	}

	/**
	 *  Ändert die Höhe des Menüs um ein Delta
	 *   
	 * @param delta Differenz zur neuen Höhe
	 */
	private void setHeight(float delta) {
		setHeight(delta, 1);
		//TODO  1ms ist bissel wenig
	}

	/**
	 * Ändert die Höhe des Menüs um ein Delta in einer bestimmten Zeit
	 * 
	 * @param delta  Differenz zur neuen Höhe
	 * @param duration dauer der Animation
	 */
	private void setHeight(float delta, long duration) {
		TranslateAnimation anim = new TranslateAnimation(0, 0, 0, delta);
		// anim.setFillAfter(true);
		anim.setDuration(duration);
		// this.duration += duration;
		anim.setAnimationListener(new RegulatorAnimationListener(delta
				+ main.getY()));
		main.startAnimation(anim);
	}

	/**
	 * Gibt die derzeitige Höhe zurück
	 * 
	 * @return Höhe des PullUpMenüs
	 */
	public float getHeight() {
		return main.getY();
	}

	/**
	 * Ändert den Content des Menüs
	 * 
	 * @param id des Contents
	 */
	public void changeView(int id) {
		Log.d(TAG_PULLUP, "Content wird geändert");
		
		switch(id){ 
        case PullUpView.CONTENT_ROUTING: 
			Log.d(TAG_PULLUP, "routing wird gestartet");
            break; 
        case PullUpView.CONTENT_FAVORITE: 
			Log.d(TAG_PULLUP, "favorite wird gestartet");
            break; 
        case PullUpView.CONTENT_ROUNDTRIP:
			Log.d(TAG_PULLUP, "roundtrip wird gestartet"); 
            break; 
        case PullUpView.CONTENT_POI: 
			Log.d(TAG_PULLUP, "poi wird gestartet");
            break; 
        case PullUpView.CONTENT_SEARCH: 
			Log.d(TAG_PULLUP, "search wird gestartet");
            break; 
        default: 
        	
        } 

	}

	/**
	 * Listener zur Änderung des Content des Menüs.
	 * 
	 * @author Ludwig Biermann
	 *
	 */
	private class RoutingListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(!v.equals(routing)) {
				return false;
			}
			Log.d(TAG_PULLUP, "routing wurde aufgerufen");
			changeView(PullUpView.CONTENT_ROUTING);
			setFullSizeHeight();
			return true;
		}
	}

	/**
	 * Listener zur Änderung des Content des Menüs.
	 * 
	 * @author Ludwig Biermann
	 *
	 */
	private class FavoriteListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(!v.equals(favorite)) {
				return false;
			}
			
			Log.d(TAG_PULLUP, "Favorite wurde aufgerufen");
			changeView(PullUpView.CONTENT_FAVORITE);
			setFullSizeHeight();
			return true;
		}
	}

	/**
	 * Listener zur Änderung des Content des Menüs.
	 * 
	 * @author Ludwig Biermann
	 *
	 */
	private class RoundtripListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if(!v.equals(roundtrip)) {
				return false;
			}
			
			Log.d(TAG_PULLUP, "roundtrip wurde aufgerufen");
			changeView(PullUpView.CONTENT_ROUNDTRIP);
			setFullSizeHeight();
			return true;
		}
	}

	/**
	 * Listener zur Änderung des Content des Menüs.
	 * 
	 * @author Ludwig Biermann
	 *
	 */
	private class POIListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if(!v.equals(poi)) {
				return false;
			}
			
			Log.d(TAG_PULLUP, "poi wurde aufgerufen");
			changeView(PullUpView.CONTENT_POI);
			setHalfSizeHeight();
			return true;
		}
	}

	/**
	 * Listener zur Änderung des Content des Menüs.
	 * 
	 * @author Ludwig Biermann
	 *
	 */
	private class SearchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if(!v.equals(search)) {
				return false;
			}
			
			Log.d(TAG_PULLUP, "search wurde aufgerufen");
			changeView(PullUpView.CONTENT_SEARCH);
			setHalfSizeHeight();
			return true;
		}
	}

	private boolean animationRun = false;

	private class RegulatorListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			int action = MotionEventCompat.getActionMasked(event);
			switch (action) {
			case (MotionEvent.ACTION_DOWN):
				Log.d(TAG_PULLUP, "Action was DOWN");
				return true;
			case (MotionEvent.ACTION_MOVE):
				Log.d(TAG_PULLUP, "Action was MOVE");
				if (!animationRun) {
					animationRun = true;
					float delta = event.getY() - regulator.getY();
					setHeight(delta);
					Log.d(TAG_PULLUP, "los gehts endlich2");
				}
				return gestureDetector.onTouchEvent(event);
			case (MotionEvent.ACTION_UP):
				// Log.d(TAG_PULLUP, "Animations Ende " + duration);
				Log.d(TAG_PULLUP, "Action was UP");
				return gestureDetector.onTouchEvent(event);

			case (MotionEvent.ACTION_CANCEL):
				Log.d(TAG_PULLUP, "Action was CANCEL");
				return gestureDetector.onTouchEvent(event);
			case (MotionEvent.ACTION_OUTSIDE):
				Log.d(TAG_PULLUP, "Movement occurred outside bounds "
						+ "of current screen element");
				return gestureDetector.onTouchEvent(event);
			default:
				return gestureDetector.onTouchEvent(event);
			}
		}
	}

	// private long duration = 0;

	private class RegulatorAnimationListener implements AnimationListener {

		float height;

		public RegulatorAnimationListener(float height) {
			this.height = height;
		}

		@Override
		public void onAnimationEnd(Animation anim) {

			main.setY(height);
			animationRun = false;
			// duration -= anim.getDuration();
			// if(duration<0) {
			// duration = 0;
			// }
			main.clearAnimation();
			// Log.d(TAG_PULLUP, " " + duration);
			// if (duration == 0) {
			
			if (main.getY() != halfHeight && main.getY() <= maxBorderHeight
					&& main.getY() >= minBorderHeight) {
				float delta = (main.getY() - halfHeight) * -1;
				Log.d(TAG_PULLUP, "Korregieren auf halfSize");
				setHeight(delta, 1000);
			}
			
			if (main.getY() > maxBorderHeight && main.getY() != maxHeight) {

				float delta = (main.getY() - maxHeight) * -1;
				Log.d(TAG_PULLUP, "Korregieren Out of Bound Max delta: "
						+ delta);
				setHeight(delta, 1000);

			} else if (main.getY() < minBorderHeight && main.getY() != 0.0F) {
				float delta = main.getY() * -1;
				Log.d(TAG_PULLUP, "Korregieren Out of Bound Min delta" + delta);
				setHeight(delta, 1000);
			}

		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub

		}

	}

	private class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			Log.d(TAG_PULLUP, "Fling! " + velocityY + " " + e2.getY());
			if (Math.abs(velocityY) > 1000) {
				if (e2.getY() < 0) {
					setFullSizeHeight();
				} else {
					setNullSizeHeight();
				}
			}

			/*
			 * try { if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
			 * return false;
			 * 
			 * if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MIN_DISTANCE) { if
			 * (e1.getY() > e2.getY()) { setNullSizeHeight();
			 * 
			 * } else { setFullSizeHeight(); } } } catch (Exception e) {
			 * 
			 * }
			 * 
			 * 
			 * if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) return
			 * false; // right to left swipe if (e1.getX() - e2.getX() >
			 * SWIPE_MIN_DISTANCE && Math.abs(velocityX) >
			 * SWIPE_THRESHOLD_VELOCITY) { Log.d(TAG_PULLUP, "Links Swift"); }
			 * else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&
			 * Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			 * Log.d(TAG_PULLUP, "Right Swift"); }
			 */

			return false;
		}

	}

	/*
	 * 
	 * @Override public boolean onTouch(View view, MotionEvent motionEvent) { if
	 * (motionEvent.getAction() == MotionEvent.ACTION_DOWN) { ClipData data =
	 * ClipData.newPlainText("", ""); DragShadowBuilder shadowBuilder = new
	 * View.DragShadowBuilder(main); main.startDrag(data, shadowBuilder, view,
	 * 0); main.setVisibility(View.INVISIBLE); return true; } else { return
	 * false; } }
	 * 
	 * }
	 * 
	 * private class DragListener implements OnDragListener {
	 * 
	 * @Override public boolean onDrag(View v, DragEvent event) {
	 * Log.d(TAG_PULLUP, "drag event"); 
	 * int action = event.getAction();
	 * 
	 * return true; } }
	 */
}

package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayCoordinate;

public class PullUpView extends Activity {

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
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

	private float maxHeight;
	private float minHeight;

	private GestureDetector gestureDetector;

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

		minHeight = 0;
		maxHeight = size.y - size.y / 10 - size.y / 40;
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
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		// regulator.setOnTouchListener(new RegulatorListener());
		// regulator.setOnDragListener(new DragListener());

		// regulator.setOnTouchListener(new PullUpMenuListener());
		main.setY(maxHeight);

	}

	public void setFullSizeHeight() {
		this.setHeight(main.getY() * -1, 1000);
	}

	public void setHalfSizeHeight() {
		this.setHeight((maxHeight / 2) - main.getY(), 1000);
	}

	public void setNullSizeHeight() {
		this.setHeight(maxHeight - main.getY(), 1000);
	}

	public void setHeight(float delta) {
		setHeight(delta, 1);
	}

	public void setHeight(float delta, long duration) {
		TranslateAnimation anim = new TranslateAnimation(0, 0, 0, delta);
		// anim.setFillAfter(true);
		anim.setDuration(duration);
		anim.setAnimationListener(new RegulatorAnimationListener(delta
				+ main.getY()));
		main.startAnimation(anim);
	}

	public float getHeight() {
		return main.getY();
	}

	public void changeView(int v) {

	}

	private class RoutingListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d(TAG_PULLUP, "routing wurde aufgerufen");
			changeView(0);
			setFullSizeHeight();
			return true;
		}
	}

	private class FavoriteListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d(TAG_PULLUP, "Favorite wurde aufgerufen");
			changeView(0);
			setFullSizeHeight();
			return true;
		}
	}

	private class RoundtripListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d(TAG_PULLUP, "roundtrip wurde aufgerufen");
			changeView(0);
			setFullSizeHeight();
			return true;
		}
	}

	private class POIListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d(TAG_PULLUP, "poi wurde aufgerufen");
			changeView(0);
			setHalfSizeHeight();
			return true;
		}
	}

	private class SearchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d(TAG_PULLUP, "search wurde aufgerufen");
			changeView(0);
			setHalfSizeHeight();
			return true;
		}
	}

	boolean animationRun = false;

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

	private class RegulatorAnimationListener implements AnimationListener {

		float height;

		public RegulatorAnimationListener(float height) {
			this.height = height;
		}

		@Override
		public void onAnimationEnd(Animation arg0) {
			main.setY(height);
			animationRun = false;
			main.clearAnimation();

			if (main.getY() > maxHeight - 100) {

				float delta = (main.getY() - maxHeight) * -1;
				Log.d(TAG_PULLUP, "Korregieren Out of Bound Max delta: "
						+ delta);
				setHeight(delta);
			}

			if (main.getY() < minHeight) {
				float delta = main.getY() * -1;
				Log.d(TAG_PULLUP, "Korregieren Out of Bound Min" + delta);
				setHeight(delta);
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
			if (Math.abs(velocityY) > 1000){
				if(e2.getY() < 0){
					setFullSizeHeight();
				} else {
					setNullSizeHeight();
				}
			}

			/*try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
				
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MIN_DISTANCE) {
					if (e1.getY() > e2.getY()) {
						setNullSizeHeight();

					} else {
						setFullSizeHeight();
					}
				}
			} catch (Exception e) {

			}

			
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
	 * Log.d(TAG_PULLUP, "drag event"); // TODO Auto-generated method stub final
	 * int action = event.getAction();
	 * 
	 * return true; } }
	 */
}

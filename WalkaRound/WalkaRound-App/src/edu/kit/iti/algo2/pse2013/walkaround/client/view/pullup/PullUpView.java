package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

// Android Library
import android.app.Fragment;
import android.app.FragmentTransaction;
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

// Walkaround Library
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.option.OptionView;

/**
 * 
 * This class creates a variant of a pull down menu. Instead of pulling down you
 * can pull this menu up. This Class enables us to integrate a different
 * fragments to display different menus.
 * 
 * @author Ludwig Biermann
 * 
 */
public class PullUpView extends Fragment {

	/**
	 * Debug Information
	 */
	private static final String TAG_PULLUP = PullUpView.class.getSimpleName();
	private static final String TAG_PULLUP_ANIMATIOn = PullUpView.class
			.getSimpleName() + "_animate";
	private static final String TAG_PULLUP_TOUCH = PullUpView.class
			.getSimpleName() + "_touch";

	/**
	 * Content ID
	 */
	public static final int CONTENT_ROUTING = 0;
	public static final int CONTENT_FAVORITE = 1;
	public static final int CONTENT_ROUNDTRIP = 2;
	public static final int CONTENT_POI = 3;
	public static final int CONTENT_SEARCH = 4;
	public static final int CONTENT_INFO = 5;
	public static final int CONTENT_OPTION = 6;

	/**
	 * Views
	 */
	private RelativeLayout main;

	private RelativeLayout menu;
	private ImageView routing;
	private ImageView favorite;
	private ImageView roundtrip;
	private ImageView poi;
	private ImageView search;

	private ImageView regulator;

	/**
	 * Permanent values
	 */
	private float minHeight;
	private float minBorderHeight;
	private float halfHeight;
	private float maxBorderHeight;
	private float maxHeight;
	private boolean animationRun = false;

	/**
	 * Gestik
	 */
	private GestureDetector gestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG_PULLUP, "allocate views");
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

		Log.d("COORDINATE_UTILITY", "get display size");
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

		Log.d(TAG_PULLUP, "Resize views");
		menu.getLayoutParams().width = size.x;
		menu.getLayoutParams().height = size.y / 10;

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

		regulator.getLayoutParams().height = size.y / 20;

		Log.d(TAG_PULLUP, "allocate Listener");
		routing.setOnTouchListener(new RoutingListener());
		favorite.setOnTouchListener(new FavoriteListener());
		roundtrip.setOnTouchListener(new RoundtripListener());
		poi.setOnTouchListener(new POIListener());
		search.setOnTouchListener(new SearchListener());
		regulator.setOnTouchListener(new RegulatorListener());
		gestureDetector = new GestureDetector(getActivity(),
				new FlingDetector());
		main.setY(maxHeight);

		Log.d(TAG_PULLUP, "allocate fragments");
		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
		pullUpContent = new InfoView();
		ft.add(R.id.pullupContent, pullUpContent).commit();
		main.setOnTouchListener(new MainListener());

	}

	/**
	 * Set the height to FullSize
	 */
	public void setFullSizeHeight() {
		this.setHeight(main.getY() * -1, 1000);
		// this.duration = 0;
	}

	/**
	 * Set the height to HalfSize
	 */
	public void setHalfSizeHeight() {
		this.setHeight(halfHeight - main.getY(), 1000);
		// this.duration = 0;
	}

	/**
	 * Set the height to minimum
	 */
	public void setNullSizeHeight() {
		this.setHeight(maxHeight - main.getY(), 1000);
	}

	/**
	 * change the height
	 * 
	 * @param delta
	 *            to the new height
	 */
	private void setHeight(float delta) {
		setHeight(delta, 1);
	}

	/**
	 * change the height in a time
	 * 
	 * @param delta
	 *            to the new height
	 * @param duration
	 *            of the animation
	 */
	private void setHeight(float delta, long duration) {
		TranslateAnimation anim = new TranslateAnimation(0, 0, 0, delta);
		anim.setDuration(duration);
		anim.setAnimationListener(new RegulatorAnimationListener(delta
				+ main.getY()));
		main.startAnimation(anim);
	}

	/**
	 * Gives the height back
	 * 
	 * @return height of the PullUpMenüs
	 */
	public float getHeight() {
		return main.getY();
	}

	Fragment pullUpContent;

	/**
	 * change the content of the pullup
	 * 
	 * @param id
	 *            of content
	 */
	public void changeView(int id) {
		Log.d(TAG_PULLUP, "Content Change");

		switch (id) {
		case PullUpView.CONTENT_ROUTING:

			if (!this.pullUpContent.equals(CONTENT_ROUTING)) {
				FragmentTransaction ft = this.getFragmentManager()
						.beginTransaction();
				Log.d(TAG_PULLUP, "routing starts");
				ft.remove(pullUpContent);
				pullUpContent = new RoutingView();
				ft.add(R.id.pullupContent, pullUpContent).commit();
			}

			break;
		case PullUpView.CONTENT_FAVORITE:

			if (!this.pullUpContent.equals(CONTENT_FAVORITE)) {
				FragmentTransaction ft = this.getFragmentManager()
						.beginTransaction();
				Log.d(TAG_PULLUP, "favorite starts");
				ft.remove(pullUpContent);
				pullUpContent = new FavoriteView();
				ft.add(R.id.pullupContent, pullUpContent).commit();
			}

			break;
		case PullUpView.CONTENT_ROUNDTRIP:

			if (!this.pullUpContent.equals(CONTENT_ROUNDTRIP)) {
				FragmentTransaction ft = this.getFragmentManager()
						.beginTransaction();
				Log.d(TAG_PULLUP, "roundtrip starts");
				ft.remove(pullUpContent);
				pullUpContent = new RoundTripView();
				ft.add(R.id.pullupContent, pullUpContent).commit();
			}

			break;
		case PullUpView.CONTENT_POI:

			if (!this.pullUpContent.equals(CONTENT_POI)) {
				FragmentTransaction ft = this.getFragmentManager()
						.beginTransaction();
				Log.d(TAG_PULLUP, "poi starts");
				ft.remove(pullUpContent);
				pullUpContent = new POIView();
				ft.add(R.id.pullupContent, pullUpContent).commit();
			}

			break;
		case PullUpView.CONTENT_SEARCH:

			if (!this.pullUpContent.equals(CONTENT_SEARCH)) {
				FragmentTransaction ft = this.getFragmentManager()
						.beginTransaction();
				Log.d(TAG_PULLUP, "search starts");
				ft.remove(pullUpContent);
				pullUpContent = new SearchView();
				ft.add(R.id.pullupContent, pullUpContent).commit();
			}

			break;
		case PullUpView.CONTENT_OPTION:

			if (!this.pullUpContent.equals(CONTENT_OPTION)) {
				FragmentTransaction ft = this.getFragmentManager()
						.beginTransaction();
				Log.d(TAG_PULLUP, "optionen starts");
				ft.remove(pullUpContent);
				pullUpContent = new OptionView();
				ft.add(R.id.pullupContent, pullUpContent).commit();
			}

			break;
		default:

			if (!this.pullUpContent.equals(CONTENT_INFO)) {
				FragmentTransaction ft = this.getFragmentManager()
						.beginTransaction();
				Log.d(TAG_PULLUP, "InfoView starts");
				ft.remove(pullUpContent);
				pullUpContent = new InfoView();
				ft.add(R.id.pullupContent, pullUpContent).commit();
			}

			break;
		}

	}

	/**
	 * Routing button Listener
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class RoutingListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(routing) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_TOUCH, "routing starts");
				changeView(PullUpView.CONTENT_ROUTING);
				setFullSizeHeight();
				return false;
			}
			return true;
		}
	}

	/**
	 * Favorite button listener.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class FavoriteListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(favorite) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_TOUCH, "Favorite starts");
				changeView(PullUpView.CONTENT_FAVORITE);
				setFullSizeHeight();
				return false;
			}
			return true;
		}
	}

	/**
	 * roundtrip button listener
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class RoundtripListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (v.equals(roundtrip) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_TOUCH, "roundtrip starts");
				changeView(PullUpView.CONTENT_ROUNDTRIP);
				setFullSizeHeight();
				return false;
			}
			return true;
		}
	}

	/**
	 * poi button listener
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class POIListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (v.equals(poi) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_TOUCH, "poi starts");
				changeView(PullUpView.CONTENT_POI);
				setHalfSizeHeight();
				return false;
			}
			return true;
		}
	}

	/**
	 * Listener to get general touch events
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class MainListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (!v.equals(main) && event.getAction() == MotionEvent.ACTION_DOWN) {
				return false;
			}

			Log.d(TAG_PULLUP_TOUCH, "main starts");
			return true;
		}
	}

	/**
	 * Listener zur änderung des Content des Menüs.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class SearchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (v.equals(search) && event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG_PULLUP_TOUCH, "search starts");
				changeView(PullUpView.CONTENT_SEARCH);
				setFullSizeHeight();
				return false;
			}
			return true;
		}
	}

	/**
	 * Implements the listener of height regulator button
	 * 
	 * @author ludwig Biermann
	 * 
	 */
	private class RegulatorListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			int action = MotionEventCompat.getActionMasked(event);
			switch (action) {
			case (MotionEvent.ACTION_DOWN):
				Log.d(TAG_PULLUP_TOUCH, "Action was DOWN");
				return true;
			case (MotionEvent.ACTION_MOVE):
				Log.d(TAG_PULLUP_TOUCH, "Action was MOVE");
				if (!animationRun) {
					animationRun = true;
					float delta = event.getY() - regulator.getY();
					setHeight(delta);
				}
				return gestureDetector.onTouchEvent(event);
			case (MotionEvent.ACTION_UP):
				Log.d(TAG_PULLUP_TOUCH, "Action was UP");
				return gestureDetector.onTouchEvent(event);

			case (MotionEvent.ACTION_CANCEL):
				Log.d(TAG_PULLUP_TOUCH, "Action was CANCEL");
				return gestureDetector.onTouchEvent(event);
			case (MotionEvent.ACTION_OUTSIDE):
				Log.d(TAG_PULLUP_TOUCH, "Movement occurred outside bounds "
						+ "of current screen element");
				return gestureDetector.onTouchEvent(event);
			default:
				return gestureDetector.onTouchEvent(event);
			}
		}
	}

	/**
	 * Implements the Animation listener of the transaction of the pullupview
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class RegulatorAnimationListener implements AnimationListener {

		float height;

		public RegulatorAnimationListener(float height) {
			this.height = height;
		}

		@Override
		public void onAnimationEnd(Animation anim) {

			main.setY(height);
			animationRun = false;
			main.clearAnimation();

			if (main.getY() != halfHeight && main.getY() <= maxBorderHeight
					&& main.getY() >= minBorderHeight) {
				float delta = (main.getY() - halfHeight) * -1;
				Log.d(TAG_PULLUP_ANIMATIOn, "Correct to halfSize");
				setHeight(delta, 1000);
			}

			if (main.getY() > maxBorderHeight && main.getY() != maxHeight) {

				float delta = (main.getY() - maxHeight) * -1;
				Log.d(TAG_PULLUP_ANIMATIOn,
						"Correct to Out of Bound Max delta: " + delta);
				setHeight(delta, 1000);

			} else if (main.getY() < minBorderHeight && main.getY() != 0.0F) {
				float delta = main.getY() * -1;
				Log.d(TAG_PULLUP_ANIMATIOn, "Correct to Out of Bound Min delta"
						+ delta);
				setHeight(delta, 1000);
			}

		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			Log.d(TAG_PULLUP_ANIMATIOn, "Repeat Animation");

		}

		@Override
		public void onAnimationStart(Animation arg0) {
			Log.d(TAG_PULLUP_ANIMATIOn, "Repeat Animation");
		}

	}

	/**
	 * Detect fling events on the pull up menu
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class FlingDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			Log.d(TAG_PULLUP_TOUCH, "Fling! " + velocityY + " " + e2.getY());
			if (Math.abs(velocityY) > 1000) {
				if (e2.getY() < 0) {
					setFullSizeHeight();
				} else {
					setNullSizeHeight();
				}
			}
			return false;
		}
	}

}

package edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Favorite;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Info;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.POILayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.POILayout.POIChangeListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Roundtrip;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Roundtrip.ComputeRoundtripListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Routing;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Routing.GoToFavoriteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Routing.GoToMapListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Search;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Search.UpdateMapListener;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * This class hold and manage all pullup views
 * 
 * @author Ludwig Biermann
 * @version 10.2
 * 
 */
public class PullUpView extends RelativeLayout implements GoToMapListener,
		GoToFavoriteListener {

	Point size;
	private ImageView routing;
	private ImageView star;
	private ImageView roundtrip;
	private ImageView poi;
	private ImageView search;
	private static String TAG = PullUpView.class.getSimpleName();

	public static final int CONTENT_ROUTING = 0;
	public static final int CONTENT_FAVORITE = 1;
	public static final int CONTENT_ROUNDTRIP = 2;
	public static final int CONTENT_POI = 3;
	public static final int CONTENT_SEARCH = 4;
	public static final int CONTENT_INFO = 5;
	public static final int CONTENT_OPTION = 6;
	private static final long ANIMATION_DURATION = 500;

	private int nullSize;
	private ImageView regulator;
	private Routing routingMenu;
	private POILayout poiMenu;
	private Favorite favoriteMenu;
	private Roundtrip roundtripMenu;
	private Search searchMenu;
	private Info infoMenu;
	private FrameLayout optionMenu;
	private int pullUpContent = -1;

	/**
	 * This create a new POIview.
	 * 
	 * @param context
	 *            the context of the app
	 * @param attrs
	 *            the needed attributes
	 */
	public PullUpView(Context context, AttributeSet attrs) {
		super(context, attrs);
		size = BoundingBox.getInstance(context).getDisplaySize();

		RelativeLayout.LayoutParams main = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		main.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		main.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		this.getRootView().setBackgroundColor(Color.GRAY);

		main.height = size.y;
		main.width = size.x;

		this.setLayoutParams(main);
		this.nullSize = size.y - size.x / 4 + 2;
		this.setY(size.y - size.x / 4 + 2);

		// statische Menü

		routing = new ImageView(context);
		star = new ImageView(context);
		roundtrip = new ImageView(context);
		poi = new ImageView(context);
		search = new ImageView(context);

		routing.setImageDrawable(context.getResources().getDrawable(
				R.drawable.list));
		star.setImageDrawable(context.getResources().getDrawable(
				R.drawable.favorite));
		roundtrip.setImageDrawable(context.getResources().getDrawable(
				R.drawable.rundkurs));
		poi.setImageDrawable(context.getResources()
				.getDrawable(R.drawable.flag));
		search.setImageDrawable(context.getResources().getDrawable(
				R.drawable.loupe));

		routing.setTag(CONTENT_ROUTING);
		star.setTag(CONTENT_FAVORITE);
		roundtrip.setTag(CONTENT_ROUNDTRIP);
		poi.setTag(CONTENT_POI);
		search.setTag(CONTENT_SEARCH);

		routing.setScaleType(ImageView.ScaleType.FIT_XY);
		star.setScaleType(ImageView.ScaleType.FIT_XY);
		roundtrip.setScaleType(ImageView.ScaleType.FIT_XY);
		poi.setScaleType(ImageView.ScaleType.FIT_XY);
		search.setScaleType(ImageView.ScaleType.FIT_XY);

		routing.setId(1);
		star.setId(2);
		roundtrip.setId(3);
		poi.setId(4);

		LinearLayout staticL = new LinearLayout(context, attrs);
		LinearLayout.LayoutParams staticLParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		staticL.setOrientation(LinearLayout.VERTICAL);
		staticLParams.width = size.x;
		staticLParams.height = size.y;
		
		this.addView(staticL, staticLParams);

		LinearLayout lineOne = new LinearLayout(context, attrs);
		LinearLayout.LayoutParams lineOneLParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lineOne.setOrientation(LinearLayout.HORIZONTAL);
		lineOne.setGravity(Gravity.CENTER);
		
		staticL.addView(lineOne, lineOneLParams);
		
		LinearLayout.LayoutParams paramsRouting = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//paramsRouting.addRule(RelativeLayout.ALIGN_PARENT_TOP,
		//		RelativeLayout.TRUE);
		paramsRouting.width = size.x / 5;
		paramsRouting.height = size.x / 5;
		//paramsRouting.leftMargin = Math.abs(size.x / 6 - size.x / 5) / 2;


		LinearLayout.LayoutParams paramsStar = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//paramsStar
		//		.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		//paramsStar.addRule(RelativeLayout.RIGHT_OF, 1);
		paramsStar.width = size.x / 5;
		paramsStar.height = size.x / 5;
		//paramsStar.leftMargin = Math.abs(size.x / 6 - size.x / 5) / 2;


		LinearLayout.LayoutParams paramsRoundtrip = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//paramsRoundtrip.addRule(RelativeLayout.ALIGN_PARENT_TOP,
		//		RelativeLayout.TRUE);
		//paramsRoundtrip.addRule(RelativeLayout.RIGHT_OF, 2);
		paramsRoundtrip.width = size.x / 5;
		paramsRoundtrip.height = size.x / 5;
		//paramsRoundtrip.leftMargin = Math.abs(size.x / 6 - size.x / 5) / 2;


		LinearLayout.LayoutParams paramsPOI = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//paramsPOI.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		//paramsPOI.addRule(RelativeLayout.RIGHT_OF, 3);
		paramsPOI.width = size.x / 5;
		paramsPOI.height = size.x / 5;
		//paramsPOI.leftMargin = Math.abs(size.x / 6 - size.x / 5) / 2;


		LinearLayout.LayoutParams paramsSearch = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//paramsSearch.addRule(RelativeLayout.ALIGN_PARENT_TOP,
		//		RelativeLayout.TRUE);
		//paramsSearch.addRule(RelativeLayout.RIGHT_OF, 4);
		paramsSearch.width = size.x / 5;
		paramsSearch.height = size.x / 5;
		//paramsSearch.leftMargin = Math.abs(size.x / 6 - size.x / 5) / 2;


		regulator = new ImageView(context, attrs);
		regulator.setImageDrawable(context.getResources().getDrawable(
				R.drawable.pikto_rechts));
		regulator.setRotation(90);
		regulator.setTag(-1);
		regulator.setId(5);
		search.setScaleType(ImageView.ScaleType.FIT_XY);

		LinearLayout.LayoutParams paramsRegulator = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//paramsRegulator.addRule(RelativeLayout.BELOW, 1);
		paramsRegulator.gravity = Gravity.CENTER_HORIZONTAL;

		lineOne.addView(routing, paramsRouting);
		lineOne.addView(star, paramsStar);
		lineOne.addView(roundtrip, paramsRoundtrip);
		lineOne.addView(poi, paramsPOI);
		lineOne.addView(search, paramsSearch);
		
		staticL.addView(regulator, paramsRegulator);
			

		// content = new RelativeLayout(context, attrs);
		LayoutParams paramsContent = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		// content.getRootView().setBackgroundColor(Color.BLACK);

		routingMenu = new Routing(context, attrs);
		poiMenu = new POILayout(context, attrs);
		favoriteMenu = new Favorite(context, attrs);
		roundtripMenu = new Roundtrip(context, attrs);
		searchMenu = new Search(context, attrs);
		infoMenu = new Info(context, attrs);
		optionMenu = new FrameLayout(context, attrs);

		routingMenu.getRootView().setBackgroundColor(Color.BLACK);
		poiMenu.getRootView().setBackgroundColor(Color.BLACK);
		favoriteMenu.getRootView().setBackgroundColor(Color.BLACK);
		roundtripMenu.getRootView().setBackgroundColor(Color.BLACK);
		searchMenu.getRootView().setBackgroundColor(Color.BLACK);
		infoMenu.getRootView().setBackgroundColor(Color.BLACK);
		optionMenu.getRootView().setBackgroundColor(Color.BLACK);

		staticL.addView(routingMenu, paramsContent);
		staticL.addView(favoriteMenu, paramsContent);
		staticL.addView(roundtripMenu, paramsContent);
		staticL.addView(searchMenu, paramsContent);
		staticL.addView(poiMenu, paramsContent);
		staticL.addView(infoMenu, paramsContent);
		staticL.addView(optionMenu, paramsContent);

		routing.setOnTouchListener(new StaticTouchListener());
		star.setOnTouchListener(new StaticTouchListener());
		roundtrip.setOnTouchListener(new StaticTouchListener());
		search.setOnTouchListener(new StaticTouchListener());
		poi.setOnTouchListener(new StaticTouchListener());

		regulator.setOnTouchListener(new RegulatorTouchListener());

		routingMenu.registerGoToMapListener(this);
		routingMenu.registerFavoriteListener(this);
		searchMenu.registerGoToMapListener(this);
		roundtripMenu.registerGoToMapListener(this);
	}

	/**
	 * change the content of the pullup
	 * 
	 * @param id
	 *            of content
	 */
	public void changeView(int id) {
		Log.d(TAG, "Content Change");
		infoMenu.stopSpeaking();
		// TextToSpeechUtility.getInstance().stopSpeaking();

		switch (id) {
		case PullUpView.CONTENT_ROUTING:

			if (!(this.pullUpContent == id)) {

				// -----------------------------------
				routingMenu.setVisibility(View.VISIBLE);
				poiMenu.setVisibility(View.GONE);
				favoriteMenu.setVisibility(View.GONE);
				roundtripMenu.setVisibility(View.GONE);
				searchMenu.setVisibility(View.GONE);
				infoMenu.setVisibility(View.GONE);
				optionMenu.setVisibility(View.GONE);
				// -----------------------------------

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
				}
			}
			break;
		case PullUpView.CONTENT_FAVORITE:

			if (!(this.pullUpContent == id)) {

				// -----------------------------------
				routingMenu.setVisibility(View.GONE);
				poiMenu.setVisibility(View.GONE);
				favoriteMenu.setVisibility(View.VISIBLE);
				roundtripMenu.setVisibility(View.GONE);
				searchMenu.setVisibility(View.GONE);
				infoMenu.setVisibility(View.GONE);
				optionMenu.setVisibility(View.GONE);
				// -----------------------------------

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
				}
			}

			break;
		case PullUpView.CONTENT_ROUNDTRIP:

			if (!(this.pullUpContent == id)) {

				// -----------------------------------
				routingMenu.setVisibility(View.GONE);
				poiMenu.setVisibility(View.GONE);
				favoriteMenu.setVisibility(View.GONE);
				roundtripMenu.setVisibility(View.VISIBLE);
				searchMenu.setVisibility(View.GONE);
				infoMenu.setVisibility(View.GONE);
				optionMenu.setVisibility(View.GONE);
				// -----------------------------------

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
				}
			}

			break;
		case PullUpView.CONTENT_POI:
			if (!(this.pullUpContent == id)) {

				// -----------------------------------
				routingMenu.setVisibility(View.GONE);
				poiMenu.setVisibility(View.VISIBLE);
				favoriteMenu.setVisibility(View.GONE);
				roundtripMenu.setVisibility(View.GONE);
				searchMenu.setVisibility(View.GONE);
				infoMenu.setVisibility(View.GONE);
				optionMenu.setVisibility(View.GONE);
				// -----------------------------------

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
				}
			}

			break;
		case PullUpView.CONTENT_SEARCH:

			if (!(this.pullUpContent == id)) {

				// -----------------------------------
				routingMenu.setVisibility(View.GONE);
				poiMenu.setVisibility(View.GONE);
				favoriteMenu.setVisibility(View.GONE);
				roundtripMenu.setVisibility(View.GONE);
				searchMenu.setVisibility(View.VISIBLE);
				infoMenu.setVisibility(View.GONE);
				optionMenu.setVisibility(View.GONE);
				// -----------------------------------

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
				}
			}

			break;
		case PullUpView.CONTENT_INFO:

			if (!(this.pullUpContent == id)) {

				// -----------------------------------
				routingMenu.setVisibility(View.GONE);
				poiMenu.setVisibility(View.GONE);
				favoriteMenu.setVisibility(View.GONE);
				roundtripMenu.setVisibility(View.GONE);
				searchMenu.setVisibility(View.GONE);
				infoMenu.setVisibility(View.VISIBLE);
				optionMenu.setVisibility(View.GONE);
				// -----------------------------------

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
				}
			}

			break;

		case PullUpView.CONTENT_OPTION:

			if (!(this.pullUpContent == id)) {

				// -----------------------------------
				routingMenu.setVisibility(View.GONE);
				poiMenu.setVisibility(View.GONE);
				favoriteMenu.setVisibility(View.GONE);
				roundtripMenu.setVisibility(View.GONE);
				searchMenu.setVisibility(View.GONE);
				infoMenu.setVisibility(View.GONE);
				optionMenu.setVisibility(View.VISIBLE);
				// -----------------------------------

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
				}
			}

			break;
		default:
			// -----------------------------------
			routingMenu.setVisibility(View.GONE);
			poiMenu.setVisibility(View.GONE);
			favoriteMenu.setVisibility(View.GONE);
			roundtripMenu.setVisibility(View.GONE);
			searchMenu.setVisibility(View.GONE);
			infoMenu.setVisibility(View.GONE);
			// -----------------------------------

			pullUpContent = -1;
			if (this.getY() == 0) {
				this.pullDown();
			}
			break;
		}

	}

	/**
	 * update Info View
	 * 
	 * @param poi
	 *            the new poi
	 */
	public void updateInfoView(POI poi) {

		// -----------------------------------
		routingMenu.setVisibility(View.GONE);
		poiMenu.setVisibility(View.GONE);
		favoriteMenu.setVisibility(View.GONE);
		roundtripMenu.setVisibility(View.GONE);
		searchMenu.setVisibility(View.GONE);
		infoMenu.setVisibility(View.VISIBLE);
		optionMenu.setVisibility(View.GONE);
		// -----------------------------------
		this.pullUp();
		infoMenu.update(poi);
	}

	/**
	 * animate pullup
	 */
	private void pullUp() {
		this.setY(0);
		TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
				nullSize, 0); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
		animation.setDuration(ANIMATION_DURATION); // animation duration
		this.startAnimation(animation); // start animation
	}

	/**
	 * animate pull down
	 */
	private void pullDown() {
		TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0,
				nullSize); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
		animation.setDuration(ANIMATION_DURATION); // animation duration

		this.startAnimation(animation); // start animation
		animation
				.setAnimationListener(new RegulatorAnimationListener(nullSize));
	}

	/**
	 * Static Touch Listener
	 * 
	 * @author Ludwig Biermann
	 * @version 1.0
	 * 
	 */
	private class StaticTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
				if (view.equals(routing) || view.equals(star) || view.equals(roundtrip) || view.equals(search) || view.equals(poi)) {
					int id = Integer.parseInt(view.getTag().toString());
					changeView(id);
					if (getY() != 0) {
						pullUp();
					}
					return true;
				}
			}
			return false;
		}
	}
	/**
	 * Static Touch Listener
	 * 
	 * @author Ludwig Biermann
	 * @version 1.0
	 * 
	 */
	private class RegulatorTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
				if (view.equals(regulator)) {
					pullDown();
					return true;
				}
			}
			return false;
		}
	}
	

	/**
	 * Implements the Animation listener of the transaction of the pullupview
	 * 
	 * @author Ludwig Biermann
	 * @version 1.0
	 * 
	 */
	private class RegulatorAnimationListener implements AnimationListener {

		float height;

		public RegulatorAnimationListener(float height) {
			this.height = height;
		}

		public void onAnimationEnd(Animation anim) {
			setY(height);
			clearAnimation();
		}

		public void onAnimationRepeat(Animation arg0) {
			Log.d(TAG, "Repeat Animation");

		}

		public void onAnimationStart(Animation arg0) {
			Log.d(TAG, "Repeat Animation");
		}

	}

	/**
	 * update route
	 */
	public void updateRoute() {
		routingMenu.updateRoute();
	}

	@Override
	public void onGoToMap() {
		this.changeView(-1);
	}

	@Override
	public void goToFavorite() {
		// -----------------------------------
		routingMenu.setVisibility(View.GONE);
		poiMenu.setVisibility(View.GONE);
		favoriteMenu.setVisibility(View.VISIBLE);
		roundtripMenu.setVisibility(View.GONE);
		searchMenu.setVisibility(View.GONE);
		infoMenu.setVisibility(View.GONE);
		optionMenu.setVisibility(View.GONE);
		// -----------------------------------

		pullUpContent = PullUpView.CONTENT_FAVORITE;
	}

	/**
	 * update favorite
	 */
	public void updateFavorite() {
		this.favoriteMenu.updateFavorites();
	}

	/**
	 * register roundtrip
	 * 
	 * @param listener
	 *            the new Listener
	 */
	public void registerComputeRoundtripListener(
			ComputeRoundtripListener listener) {
		roundtripMenu.registerComputeRoundtripListener(listener);
	}

	/**
	 * register poi change listener
	 * 
	 * @param listener
	 *            the new listener
	 */
	public void registerPOIChangeListener(POIChangeListener listener) {
		poiMenu.registerPOIChangeListener(listener);
	}

	/**
	 * register update map listener
	 * 
	 * @param listener
	 *            the new listener
	 */
	public void registerUpdateMapListener(UpdateMapListener listener) {
		searchMenu.registerUpdateMapListener(listener);
	}

}

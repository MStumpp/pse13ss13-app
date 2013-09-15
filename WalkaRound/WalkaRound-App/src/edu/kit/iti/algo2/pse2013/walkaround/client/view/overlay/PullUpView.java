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
import android.widget.TextView;
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
	private TextView title;

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
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		main.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		main.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		// this.getRootView().setBackgroundColor(Color.GRAY);

		main.height = size.y;
		main.width = size.x;

		this.setLayoutParams(main);
		this.nullSize = size.y - size.x / 4 + 7;
		this.setY(size.y - size.x / 4 + 7);

		// statische Menü

		routing = new ImageView(context);
		star = new ImageView(context);
		roundtrip = new ImageView(context);
		poi = new ImageView(context);
		search = new ImageView(context);

		this.unsetActiveMenu();

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
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		staticL.setOrientation(LinearLayout.VERTICAL);
		staticLParams.width = size.x;
		staticLParams.height = size.y;

		this.addView(staticL, staticLParams);

		LinearLayout lineOne = new LinearLayout(context, attrs);
		LinearLayout.LayoutParams lineOneLParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lineOne.setOrientation(LinearLayout.HORIZONTAL);
		lineOne.setGravity(Gravity.CENTER);

		staticL.addView(lineOne, lineOneLParams);

		LinearLayout.LayoutParams paramsRouting = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsRouting.width = size.x / 5;
		paramsRouting.height = size.x / 5;

		LinearLayout.LayoutParams paramsStar = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsStar.width = size.x / 5;
		paramsStar.height = size.x / 5;

		LinearLayout.LayoutParams paramsRoundtrip = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsRoundtrip.width = size.x / 5;
		paramsRoundtrip.height = size.x / 5;

		LinearLayout.LayoutParams paramsPOI = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsPOI.width = size.x / 5;
		paramsPOI.height = size.x / 5;

		LinearLayout.LayoutParams paramsSearch = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsSearch.width = size.x / 5;
		paramsSearch.height = size.x / 5;



		regulator = new ImageView(context, attrs);
		title = new TextView(context, attrs);
		title.setText("Test");
		title.setTextSize(30);

		LinearLayout tileLine = new LinearLayout(context, attrs);
		LinearLayout.LayoutParams titleLineParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		tileLine.setOrientation(LinearLayout.HORIZONTAL);
		titleLineParams.width = size.x;
		//tileLine.setBackgroundColor(Color.RED);



		regulator.setImageDrawable(context.getResources().getDrawable(
				R.drawable.closearrows));
		regulator.setRotation(90);
		regulator.setTag(-1);
		regulator.setId(5);


		search.setScaleType(ImageView.ScaleType.FIT_XY);

		LinearLayout.LayoutParams paramsRegulator = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsRegulator.weight = 0.1F;

		LinearLayout.LayoutParams tileParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		tileParams.weight = 0.9F;
		tileParams.width = size.x;
		tileParams.topMargin = 8;
		tileParams.bottomMargin = 8;
		tileParams.gravity = Gravity.CENTER;
		tileParams.leftMargin = 20;
		//title.setBackgroundColor(Color.RED);
		title.setTextAlignment(TEXT_ALIGNMENT_CENTER);
		title.setWidth(size.x);

		lineOne.addView(routing, paramsRouting);
		lineOne.addView(star, paramsStar);
		lineOne.addView(roundtrip, paramsRoundtrip);
		lineOne.addView(poi, paramsPOI);
		lineOne.addView(search, paramsSearch);

		tileLine.addView(regulator, paramsRegulator);
		tileLine.addView(title, tileParams);

		staticL.addView(tileLine, titleLineParams);
		staticL.getRootView().setBackgroundColor(Color.BLACK);



		// content = new RelativeLayout(context, attrs);
		LayoutParams paramsContent = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

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

	public void unsetActiveMenu() {
		routing.setImageResource(R.drawable.list);
		star.setImageResource(R.drawable.staticstar);
		roundtrip.setImageResource(R.drawable.rundkurs);
		poi.setImageResource(R.drawable.staticflag);
		search.setImageResource(R.drawable.loupe);
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
		this.unsetActiveMenu();

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
				this.routing.setImageResource(R.drawable.listactive);
				this.title.setText("Routenverwaltung");
				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
					pullUpContent = -1;
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
				this.star.setImageResource(R.drawable.staticstaractive);
				this.title.setText("Favoriten");

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
					pullUpContent = -1;
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
				this.roundtrip.setImageResource(R.drawable.rundkursactive);
				this.title.setText("Rundkurs erstellen");

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
					pullUpContent = -1;
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
				this.poi.setImageResource(R.drawable.staticflagactive);
				this.title.setText("Points of Interest");

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
					pullUpContent = -1;
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
				this.search.setImageResource(R.drawable.loupeactive);
				this.title.setText("Suche");

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
					pullUpContent = -1;
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
				this.unsetActiveMenu();
				this.title.setText("Infos");

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
					pullUpContent = -1;
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
				this.unsetActiveMenu();
				this.title.setText("Options");

				pullUpContent = id;
			} else {
				if (this.getY() == 0) {
					this.pullDown();
					pullUpContent = -1;
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
			this.title.setText("");

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

		this.unsetActiveMenu();
		// -----------------------------------
		routingMenu.setVisibility(View.GONE);
		poiMenu.setVisibility(View.GONE);
		favoriteMenu.setVisibility(View.GONE);
		roundtripMenu.setVisibility(View.GONE);
		searchMenu.setVisibility(View.GONE);
		infoMenu.setVisibility(View.VISIBLE);
		optionMenu.setVisibility(View.GONE);
		// -----------------------------------
		this.title.setText("Infos");
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

			if (action == MotionEvent.ACTION_UP) {
				if (view.equals(routing) || view.equals(star)
						|| view.equals(roundtrip) || view.equals(search)
						|| view.equals(poi)) {
					int id = Integer.parseInt(view.getTag().toString());
					changeView(id);
					if (getY() != 0) {
						pullUp();
					}
					return true;
				}
			}

			if (action == MotionEvent.ACTION_DOWN) {
				if (view.equals(routing) || view.equals(star)
						|| view.equals(roundtrip) || view.equals(search)
						|| view.equals(poi)) {
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

		@Override
		public void onAnimationEnd(Animation anim) {
			setY(height);
			clearAnimation();
			unsetActiveMenu();
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			Log.d(TAG, "Repeat Animation");

		}

		@Override
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
		unsetActiveMenu();
		// -----------------------------------
		routingMenu.setVisibility(View.GONE);
		poiMenu.setVisibility(View.GONE);
		favoriteMenu.setVisibility(View.VISIBLE);
		roundtripMenu.setVisibility(View.GONE);
		searchMenu.setVisibility(View.GONE);
		infoMenu.setVisibility(View.GONE);
		optionMenu.setVisibility(View.GONE);
		// -----------------------------------
		this.star.setImageResource(R.drawable.staticstaractive);
		this.title.setText("Favoriten hinzufügen");

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
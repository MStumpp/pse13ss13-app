package edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.activity.BootActivity;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

/**
 * This class show the Favorite Menu
 *
 * @author Ludwig Biermann
 * @version 1.1
 *
 */
public class Favorite extends RelativeLayout {

	private static final String TAG = Favorite.class.getSimpleName();
	private LinearLayout routeSide;
	private LinearLayout waypointSide;
	private LinearLayout main;
	private LinearLayout tabHost;
	private Button waypointButton;
	private Button routeButton;
	private Point size;

	private boolean selected = true;
	/**
	 * This create a new POIview.
	 *
	 * @param context the context of the app
	 * @param attrs the needed attributes
	 */
	public Favorite(Context context, AttributeSet attrs) {
		super(context, attrs);
		size = BoundingBox.getInstance(context).getDisplaySize();
		main = new LinearLayout(context, attrs);
		main.setOrientation(LinearLayout.VERTICAL);

		ScrollView scrollWay = new ScrollView(context);
		ScrollView scrollRoute = new ScrollView(context);

		LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		this.addView(main, mainParams);


		tabHost = new LinearLayout(context, attrs);
		tabHost.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams tabHostParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		tabHostParams.width = size.x;
		tabHostParams.bottomMargin = 20;

		main.addView(tabHost, tabHostParams);
		main.addView(scrollWay);
		main.addView(scrollRoute);

		waypointButton = new Button(context, attrs);
		waypointButton.setText(BootActivity.getAppContext().getString(R.string.term_location));
		waypointButton.setOnTouchListener(new ToogleTabListener());

		routeButton = new Button(context, attrs);
		routeButton.setText(BootActivity.getAppContext().getString(R.string.term_route));
		routeButton.setOnTouchListener(new ToogleTabListener());

		LinearLayout.LayoutParams waypointButtontParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		waypointButtontParams.height = size.y / 10;
		waypointButtontParams.width = size.x / 2;
		waypointButtontParams.topMargin = 10;
		waypointButtontParams.leftMargin = 0;
		waypointButtontParams.rightMargin = 0;

		LinearLayout.LayoutParams routeButtontParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		routeButtontParams.height = size.y / 10;
		routeButtontParams.width = size.x / 2;
		routeButtontParams.topMargin = 10;
		waypointButtontParams.leftMargin = 0;
		waypointButtontParams.rightMargin = 0;

		tabHost.addView(waypointButton, waypointButtontParams);
		tabHost.addView(routeButton, routeButtontParams);

		routeSide = new LinearLayout(context, attrs);
		routeSide.setOrientation(LinearLayout.VERTICAL);
		routeSide.setVisibility(GONE);

		waypointSide = new LinearLayout(context, attrs);
		waypointSide.setOrientation(LinearLayout.VERTICAL);
		waypointSide.setVisibility(VISIBLE);
		waypointButton.setTextColor(Color.RED);


		LinearLayout.LayoutParams routeSiedeParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams waypointSiedeParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		scrollRoute.addView(routeSide, routeSiedeParam);
		scrollWay.addView(waypointSide, waypointSiedeParam);

		this.updateFavorites();
	}

	/**
	 * A Listener wich toogles the tabs
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class ToogleTabListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_UP) {
				if (!view.isSelected()) {
					toogleTab();
				}
			}
			return false;
		}

	}

	/**
	 * toogles the tabs
	 */
	private void toogleTab() {
		if (selected) {
			waypointButton.setSelected(false);
			waypointButton.setTextColor(Color.BLACK);
			routeButton.setSelected(true);
			routeButton.setTextColor(Color.RED);
			routeSide.setVisibility(VISIBLE);
			waypointSide.setVisibility(GONE);
			selected = false;
		} else {
			waypointButton.setSelected(true);
			waypointButton.setTextColor(Color.RED);
			routeButton.setSelected(false);
			routeButton.setTextColor(Color.BLACK);
			routeSide.setVisibility(GONE);
			waypointSide.setVisibility(VISIBLE);
			selected = true;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Catch all Touch events
		return true;
	}

	/**
	 * update Favorite
	 */
	public void updateFavorites() {
		Log.d(TAG, "updateFavorite");
		routeSide.removeAllViews();
		waypointSide.removeAllViews();

		List<String> location = FavoriteManager.getInstance(getContext())
				.getNamesOfFavoriteLocations();
		// Category
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		param.width = size.x * 5 / 6;
		param.height = size.y / 10;

		LinearLayout.LayoutParams delParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		delParam.height = size.y / 10;
		delParam.width = size.x * 1 / 6;

		LinearLayout.LayoutParams lineParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lineParam.topMargin = 5;

		for (String w : location) {
			Button b = new Button(getContext());
			b.setText(w);
			b.setTag(w);
			b.setOnTouchListener(new FavoriteLocationListener());
			//b.setBackgroundColor(Color.GRAY);
			//b.setTextColor(Color.BLACK);
			//b.setSelected(false);
			//b.setGravity(Gravity.CENTER);

			ImageButton del = new ImageButton(getContext());
			del.setTag(w);
			del.setImageResource(R.drawable.delete);
			del.setScaleType(ImageView.ScaleType.FIT_XY);
			del.setOnTouchListener(new DeleteWaypointListener());

			LinearLayout linear = new LinearLayout(getContext());
			linear.setOrientation(LinearLayout.HORIZONTAL);

			waypointSide.addView(linear, lineParam);
			linear.addView(b, param);
			linear.addView(del, delParam);
		}

		List<String> routes = FavoriteManager.getInstance(getContext())
				.getNamesOfFavoriteRoutes();
		// Category

		for (String w : routes) {
			Button b = new Button(getContext());
			b.setText(w);
			b.setTag(w);
			b.setOnTouchListener(new FavoriteRouteListener());
			//b.setBackgroundColor(Color.GRAY);
			//b.setTextColor(Color.BLACK);
			//b.setSelected(false);
			//b.setGravity(Gravity.CENTER);


			ImageButton del = new ImageButton(getContext());
			del.setTag(w);
			del.setImageResource(R.drawable.delete);
			del.setScaleType(ImageView.ScaleType.FIT_XY);
			del.setOnTouchListener(new DeleteRouteListener());

			LinearLayout linear = new LinearLayout(getContext());
			linear.setOrientation(LinearLayout.HORIZONTAL);

			routeSide.addView(linear, lineParam);
			linear.addView(b, param);
			linear.addView(del, delParam);
		}
	}

	/**
	 * This class listener for a touch on a location
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class FavoriteLocationListener implements OnTouchListener {

		private View view;

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if(action == MotionEvent.ACTION_UP){
				this.view = view;
				this.alert();
			}
			return false;
		}

		/**
		 * makes a waypoint alert
		 */
		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle(BootActivity.getAppContext().getString(R.string.dialog_header_add_fav_to_route));
			alertDialog.setMessage(BootActivity.getAppContext().getString(R.string.dialog_text_add_fav_to_route, view.getTag().toString()));
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, BootActivity.getAppContext().getString(R.string.option_yes),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Location l = FavoriteManager.getInstance(getContext()).getFavoriteLocation(view.getTag().toString());
							Waypoint w = new Waypoint(l.getLatitude(), l.getLongitude(), l.getName(), l.getAddress());
							RouteController.getInstance().addWaypoint(w);
							BoundingBox.getInstance().setCenter(w);
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, BootActivity.getAppContext().getString(R.string.option_no),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Do nothing
						}
					});
			alertDialog.show();
		}
	}

	/**
	 * This class listener for a route touch
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class FavoriteRouteListener implements OnTouchListener {
		private View view;
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if(action == MotionEvent.ACTION_UP){
				this.view = view;
				this.alert();
			}
			return false;
		}

		/**
		 * makes a alert
		 */
		public void alert() {
			final RouteInfo route = FavoriteManager.getInstance(getContext()).getFavoriteRoute(view.getTag().toString());
			AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
			alertDialog.setTitle(BootActivity.getAppContext().getString(R.string.dialog_header_add_fav_to_route));
			alertDialog.setMessage(BootActivity.getAppContext().getString(R.string.dialog_text_add_fav_to_route, view.getTag().toString()));
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, BootActivity.getAppContext().getString(R.string.option_yes),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							RouteController.getInstance().addRoute(route);
							Log.d(TAG, String.format("Added favorite route with %d coordinates to current route: %s", route.getCoordinates().size(), route));
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, BootActivity.getAppContext().getString(R.string.option_no),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Do nothing
						}
					});
			alertDialog.show();
		}
	}

	/**
	 * This class listen for a delete route event
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class DeleteRouteListener implements OnTouchListener {

		private String id;

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();

			if(action == MotionEvent.ACTION_UP) {
				id = view.getTag().toString();
				this.alert();
			}

			return false;
		}

		/**
		 * makes a alert
		 */
		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle(BootActivity.getAppContext().getString(R.string.dialog_header_delete, BootActivity.getAppContext().getString(R.string.term_route)));
			alertDialog.setMessage(BootActivity.getAppContext().getString(R.string.dialog_text_delete, id));
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, BootActivity.getAppContext().getString(R.string.option_yes),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							FavoriteManager.getInstance(getContext()).deleteRoute(id);
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, BootActivity.getAppContext().getString(R.string.option_no),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Do nothing
						}
					});
			alertDialog.show();
		}

	}

	/**
	 * This class listen for a delete waypoint event
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class DeleteWaypointListener implements OnTouchListener {

		private String id;

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();

			if(action == MotionEvent.ACTION_UP) {
				id = view.getTag().toString();
				this.alert();
			}

			return false;
		}

		/**
		 * makes a alert
		 */
		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
			alertDialog.setTitle(BootActivity.getAppContext().getString(R.string.dialog_header_delete, BootActivity.getAppContext().getString(R.string.term_location)));
			alertDialog.setMessage(BootActivity.getAppContext().getString(R.string.dialog_text_delete, id));
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, BootActivity.getAppContext().getString(R.string.option_yes),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							FavoriteManager.getInstance(getContext()).deleteLocation(id);
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, BootActivity.getAppContext().getString(R.string.option_no),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Do nothing
						}
					});
			alertDialog.show();
		}

	}
}

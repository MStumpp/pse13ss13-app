package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.views;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class Favorite extends RelativeLayout {

	private static final String TAG = Favorite.class.getSimpleName();
	private LinearLayout routeSide;
	private LinearLayout waypointSide;
	private LinearLayout main;
	private LinearLayout tabHost;
	private Button waypointButton;
	private Button routeButton;
<<<<<<< HEAD
=======
	private static int routeNameId = R.string.route;
	private static String waypointName = "Wegpunkte";
>>>>>>> 8166cf5dd38089e17b8c0e3232b120ced854c432
	private boolean selected = true;
	private int width;
	private int height;

	public Favorite(Context context, AttributeSet attrs) {
		super(context, attrs);

		Point size = BoundingBox.getInstance(context).getDisplaySize();
		width = size.x;
		height = size.y / 20;

		main = new LinearLayout(context, attrs);
		main.setOrientation(LinearLayout.VERTICAL);

		ScrollView scrollWay = new ScrollView(context);
		ScrollView scrollRoute = new ScrollView(context);
		
		LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		this.addView(main, mainParams);

		
		tabHost = new LinearLayout(context, attrs);
		tabHost.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams tabHostParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tabHostParams.width = size.x;

		main.addView(tabHost, tabHostParams);
		main.addView(scrollWay);
		main.addView(scrollRoute);
		
		waypointButton = new Button(context, attrs);
		waypointButton.setText("Orte");
		waypointButton.setOnTouchListener(new ToogleTabListener());

		routeButton = new Button(context, attrs);
		routeButton.setText("Routen");
		routeButton.setOnTouchListener(new ToogleTabListener());

		LinearLayout.LayoutParams waypointButtontParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		waypointButtontParams.height = size.y / 10;
		waypointButtontParams.width = size.x / 2;
		waypointButtontParams.leftMargin = 0;
		waypointButtontParams.rightMargin = 0;

		LinearLayout.LayoutParams routeButtontParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		routeButtontParams.height = size.y / 10;
		routeButtontParams.width = size.x / 2;
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
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams waypointSiedeParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		scrollRoute.addView(routeSide, routeSiedeParam);
		scrollWay.addView(waypointSide, waypointSiedeParam);

		this.updateFavorites();
	}

	private class ToogleTabListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
				if (!view.isSelected()) {
					toogleTab();
				}
			}
			return false;
		}

	}

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

	public void updateFavorites() {
		Log.d(TAG, "updateFavorite");
		routeSide.removeAllViews();
		waypointSide.removeAllViews();
		
		List<String> location = FavoriteManager.getInstance(getContext())
				.getNamesOfFavoriteLocations();
		// Category
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.width = width - height;
		param.height = height;

		LinearLayout.LayoutParams delParam = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		delParam.height = height;
		delParam.width = height;

		LinearLayout.LayoutParams lineParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lineParam.topMargin = 5;

		for (String w : location) {
			TextView b = new TextView(getContext());
			b.setText(w);
			b.setTag(w);
			b.setOnTouchListener(new FavoriteLocationListener());
			b.setBackgroundColor(Color.GRAY);
			b.setTextColor(Color.BLACK);
			b.setSelected(false);
			b.setGravity(Gravity.CENTER);

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
			TextView b = new TextView(getContext());
			b.setText(w);
			b.setTag(w);
			b.setOnTouchListener(new FavoriteRouteListener());
			b.setBackgroundColor(Color.GRAY);
			b.setTextColor(Color.BLACK);
			b.setSelected(false);
			b.setGravity(Gravity.CENTER);


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
	
	private class FavoriteLocationListener implements OnTouchListener {

		View view;
		
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if(action == MotionEvent.ACTION_UP){
				this.view = view;
				this.alert();
			}
			
			return false;
		}
		
		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle("Wegpunkt hinzufügen");
			alertDialog.setMessage("Wirklich hinzufügen?");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Location l = FavoriteManager.getInstance(getContext()).getFavoriteLocation(view.getTag().toString());
							Waypoint w = new Waypoint(l.getLatitude(), l.getLongitude(), l.getName(), l.getAddress());
							RouteController.getInstance().addWaypoint(w);
						}
					});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alertDialog.show();
		}
		
	}

	private class FavoriteRouteListener implements OnTouchListener {

		View view;
		
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if(action == MotionEvent.ACTION_UP){
				this.view = view;
				this.alert();
			}
			return false;
		}

		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle("Route hinzufügen");
			alertDialog.setMessage("Wirklich hinzufügen?");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							RouteInfo l = FavoriteManager.getInstance(getContext()).getFavoriteRoute(view.getTag().toString());
							RouteController.getInstance().addRoute(l);
						}
					});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alertDialog.show();
		}
		
	}
	
	private class DeleteRouteListener implements OnTouchListener {

		String id;
		
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			
			if(action == MotionEvent.ACTION_UP) {
				id = view.getTag().toString();
				this.alert();
			}
			
			return false;
		}
		
		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle("Route löschen");
			alertDialog.setMessage(id + " wirklich löschen?");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							FavoriteManager.getInstance(getContext()).deleteRoute(id);
						}
					});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alertDialog.show();
		}

	}

	private class DeleteWaypointListener implements OnTouchListener {

		String id;
		
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			
			if(action == MotionEvent.ACTION_UP) {
				id = view.getTag().toString();
				this.alert();
			}
			
			return false;
		}
		
		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle("Route löschen");
			alertDialog.setMessage(id + " wirklich löschen?");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							FavoriteManager.getInstance(getContext()).deleteLocation(id);
						}
					});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alertDialog.show();
		}

	}
}

package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.FavoriteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;

public class RoutingView extends Fragment implements RouteListener{

	private final String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupRoutingSwitcher;
	
	private FavoriteMenuController favController;
	private RouteController routeController;
	
	private Button reset;
	private ImageView invert;
	private ImageView tsp;
	private ImageView load;
	private ImageView save;
	private Button addFavorite;
	private Button goToMap; 
	private EditText name;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create RoutingView");
		
		favController = FavoriteMenuController.getInstance();
		routeController = RouteController.getInstance();
		
		reset = (Button) this.getActivity().findViewById(R.id.reset);
		invert = (ImageView) this.getActivity().findViewById(R.id.invert);
		tsp = (ImageView) this.getActivity().findViewById(R.id.tsp);
		load = (ImageView) this.getActivity().findViewById(R.id.load);
		save = (ImageView) this.getActivity().findViewById(R.id.save);
		addFavorite = (Button) this.getActivity().findViewById(R.id.add_favorite);
		goToMap = (Button) this.getActivity().findViewById(R.id.go_to_map);
		name = (EditText) this.getActivity().findViewById(R.id.name_favorites);
		
		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		Log.d(TAG_PULLUP_CONTENT, "Einstellen der Größenverhältnisse");
		reset.setX(size.x / 5 * 0);
		reset.getLayoutParams().width = size.x / 5;
		reset.getLayoutParams().height = size.y / 8;
		invert.setX(size.x / 5 * 1);
		invert.getLayoutParams().width = size.x / 5;
		invert.getLayoutParams().height = size.y / 8;
		tsp.setX(size.x / 5 * 2);
		tsp.getLayoutParams().width = size.x / 5;
		tsp.getLayoutParams().height = size.y / 8;
		load.setX(size.x / 5 * 3);
		load.getLayoutParams().width = size.x / 5;
		load.getLayoutParams().height = size.y / 8;
		save.setX(size.x / 5 * 4);
		save.getLayoutParams().width = size.x / 5;
		save.getLayoutParams().height = size.y / 8;
		addFavorite.setX(size.x * 0);
		addFavorite.getLayoutParams().width = size.x / 2;
		goToMap.setX(size.x / 2);
		goToMap.getLayoutParams().width = size.x / 2;
		name.setX(size.x / 5);
		name.setY(size.y / 8);
		
		Log.d(TAG_PULLUP_CONTENT, "Zuweisung der Listener");
		reset.setOnTouchListener(new resetListener());
		invert.setOnTouchListener(new invertListener());
		tsp.setOnTouchListener(new tspListener());
		load.setOnTouchListener(new loadListener());
		save.setOnTouchListener(new saveListener());
		addFavorite.setOnTouchListener(new favoriteListener());
		goToMap.setOnTouchListener(new backToMapListener());
		name.setOnEditorActionListener(new saveFavoriteListener());

		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG_PULLUP_CONTENT, "Destroy RoutingView");
		this.getActivity().findViewById(switcher).setVisibility(View.GONE);
	}

	public boolean equals(Fragment f) {
		if (f.toString().equals(PullUpView.CONTENT_ROUTING)) {
			return true;
		}
		return false;
	}
	
	private class resetListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(reset)) {
				if(name.getVisibility() == View.VISIBLE) {
					name.setVisibility(View.INVISIBLE);
				}
				Log.d(TAG_PULLUP_CONTENT, "reset wurde gedr�ckt");
				routeController.resetRoute();
			}
			//TODO: refresh activity?
			return false;
		}
		
	}
	
	private class invertListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(invert)) {
				if(name.getVisibility() == View.VISIBLE) {
					name.setVisibility(View.INVISIBLE);
				}
				Log.d(TAG_PULLUP_CONTENT, "invert wurde gedr�ckt");
				routeController.revertRoute();
			}
			//TODO: refresh activity?
			return false;
		}
		
	}
	
	private class tspListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(tsp)) {
				if(name.getVisibility() == View.VISIBLE) {
					name.setVisibility(View.INVISIBLE);
				}
				Log.d(TAG_PULLUP_CONTENT, "tsp button wurde gedr�ckt");
				routeController.optimizeRoute();
			}
			return false;
		}
		
	}
	
	private class loadListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(load)) {
				if(name.getVisibility() == View.VISIBLE) {
					name.setVisibility(View.INVISIBLE);
				}
				Log.d(TAG_PULLUP_CONTENT, "load wurde gedr�ckt");
				//TODO: ansicht wechselt in die liste der favorisierten routen
			}
			return false;
		}
		
	}
	
	private class saveListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(save)) {
				if(name.getVisibility() == View.VISIBLE) {
					name.setVisibility(View.INVISIBLE);
				}
				Log.d(TAG_PULLUP_CONTENT, "save wurde gedr�ckt");
				name.setVisibility(View.VISIBLE);
				//TODO : routemenucontroller ruft save route auf, wie gibt man den namen mit!?
			}
			return false;
		}
		
	}
	
	private class favoriteListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(addFavorite)) {
				if(name.getVisibility() == View.VISIBLE) {
					name.setVisibility(View.INVISIBLE);
				}
				Log.d(TAG_PULLUP_CONTENT, "add favorite wurde gedr�ckt");
				//TODO: ansicht wechselt in die liste der favorisierten orte
			}
			return false;
		}
		
	}
	
	private class backToMapListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(goToMap)) {
				if(name.getVisibility() == View.VISIBLE) {
					name.setVisibility(View.INVISIBLE);
				}
				Log.d(TAG_PULLUP_CONTENT, "go to map wurde gedr�ckt");
				// TODO:pullup muss sich schlie�en
			}
			return false;
		}
		
	}
	
	private class saveFavoriteListener implements OnEditorActionListener {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (v.equals(name)) {
				Log.d(TAG_PULLUP_CONTENT, "Ein name wurde eingegeben wurde eingegeben");
			//	routeController.addRouteToFavorites((name.toString());
				name.setVisibility(View.INVISIBLE);
				//TODO:fix problem wie die route angegeben wird
			}
			return false;
		}
		
	}

	@Override
	public void onRouteChange(RouteInfo currentRoute) {
		
	}
}

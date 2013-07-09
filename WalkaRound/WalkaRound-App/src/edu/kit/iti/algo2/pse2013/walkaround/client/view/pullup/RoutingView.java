package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.FavoriteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoritesManager;

public class RoutingView extends Fragment {

	public String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupRoutingSwitcher;
	
	FavoriteMenuController favController;
	RouteMenuController routeController;
	
	private Button reset;
	private ImageView invert;
	private ImageView tsp;
	private ImageView load;
	private ImageView save;
	private Button addFavorite;
	private Button goToMap; 

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT, "Create RoutingView");
		
		favController = FavoriteMenuController.getInstance();
		routeController = RouteMenuController.getInstance();
		
		reset = (Button) this.getActivity().findViewById(R.id.reset);
		invert = (ImageView) this.getActivity().findViewById(R.id.invert);
		tsp = (ImageView) this.getActivity().findViewById(R.id.tsp);
		load = (ImageView) this.getActivity().findViewById(R.id.load);
		save = (ImageView) this.getActivity().findViewById(R.id.save);
		addFavorite = (Button) this.getActivity().findViewById(R.id.add_favorite);
		goToMap = (Button) this.getActivity().findViewById(R.id.go_to_map);
		
		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		Log.d(TAG_PULLUP_CONTENT, "Einstellen der GrÃ¶ÃŸenverhÃ¤ltnisse");
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
		
		Log.d(TAG_PULLUP_CONTENT, "Zuweisung der Listener");
		reset.setOnTouchListener(new resetListener());
		invert.setOnTouchListener(new invertListener());
		tsp.setOnTouchListener(new tspListener());
		load.setOnTouchListener(new loadListener());
		save.setOnTouchListener(new saveListener());
		addFavorite.setOnTouchListener(new favoriteListener());
		goToMap.setOnTouchListener(new backToMapListener());

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
				Log.d(TAG_PULLUP_CONTENT, "reset wurde gedrückt");
				routeController.resetRoute();
			}
			return false;
		}
		
	}
	
	private class invertListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(invert)) {
				Log.d(TAG_PULLUP_CONTENT, "invert wurde gedrückt");
				routeController.revertRoute();
			}
			return false;
		}
		
	}
	
	private class tspListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(tsp)) {
				Log.d(TAG_PULLUP_CONTENT, "tsp button wurde gedrückt");
				routeController.optimizeRoute();
			}
			return false;
		}
		
	}
	
	private class loadListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(load)) {
				Log.d(TAG_PULLUP_CONTENT, "load wurde gedrückt");
				//TODO: ansicht wechselt in die liste der favorisierten routen
			}
			return false;
		}
		
	}
	
	private class saveListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(save)) {
				Log.d(TAG_PULLUP_CONTENT, "save wurde gedrückt");
				//TODO : routemenucontroller ruft save route auf, wie gibt man den namen mit!?
			}
			return false;
		}
		
	}
	
	private class favoriteListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(addFavorite)) {
				Log.d(TAG_PULLUP_CONTENT, "add favorite wurde gedrückt");
				//TODO: ansicht wechselt in die liste der favorisierten orte
			}
			return false;
		}
		
	}
	
	private class backToMapListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(v.equals(goToMap)) {
				Log.d(TAG_PULLUP_CONTENT, "go to map wurde gedrückt");
				//TODO: ansicht wecheslt zurück zur karte
			}
			return false;
		}
		
	}
}

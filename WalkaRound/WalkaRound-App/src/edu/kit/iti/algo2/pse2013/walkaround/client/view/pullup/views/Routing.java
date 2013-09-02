package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.views;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.RelativeLayout.LayoutParams;

public class Routing extends RelativeLayout {

	private Button reset;
	private ImageButton invert;
	private Button addFavorite;
	private Button goToMap;
	private ImageButton save;
	private ScrollView scrollView;
	private LinearLayout content;
	private LinearLayout.LayoutParams textParams;
	private LinearLayout.LayoutParams saveParams;
	private LinearLayout.LayoutParams deleteParams;
	private RelativeLayout.LayoutParams lParams;
	private Context context;

	public Routing(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		this.getRootView().setBackgroundColor(Color.RED);
		
		Point size = BoundingBox.getInstance(context).getDisplaySize();
		
		//allocat
		reset = new Button(context, attrs);
		invert = new ImageButton(context, attrs);
		save = new ImageButton(context, attrs);
		
		scrollView = new ScrollView(context, attrs);
		content = new LinearLayout(context, attrs);
		content.setOrientation(LinearLayout.VERTICAL);
		
		addFavorite = new Button(context, attrs);
		goToMap = new Button(context, attrs);
		
		//images and text
		
		invert.setImageResource(R.drawable.invert);
		invert.setScaleType(ImageView.ScaleType.FIT_XY);
		
		save.setImageResource(R.drawable.favorite);
		save.setScaleType(ImageView.ScaleType.FIT_XY);
		
		reset.setText("Reset");
		addFavorite.setText("add Favorite");
		goToMap.setText("go to Map");
	
		// IDs
		
		reset.setId(1);
		invert.setId(2);
		addFavorite.setId(3);
		
		//Params
		//Reset
		RelativeLayout.LayoutParams paramsReset = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsReset.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsReset.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		paramsReset.topMargin = 10;
		paramsReset.height = size.y/10;
		paramsReset.width = size.y/10;
		
		//Invert
		RelativeLayout.LayoutParams paramsInvert = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsInvert.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsInvert.addRule(RelativeLayout.RIGHT_OF,
				1);
		paramsInvert.topMargin = 10;
		paramsInvert.height = size.y/10;
		paramsInvert.width = size.y/10;
		
		//Save
		RelativeLayout.LayoutParams paramsSave = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsSave.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsSave.addRule(RelativeLayout.RIGHT_OF,
				2);
		paramsSave.topMargin = 10;
		paramsSave.height = size.y/10;
		paramsSave.width = size.y/10;

		//ScrollView
		RelativeLayout.LayoutParams paramsScrollView = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		paramsScrollView.addRule(RelativeLayout.BELOW,
				1);
		paramsScrollView.addRule(RelativeLayout.ABOVE,
				3);
		paramsScrollView.height = 1000;

		//Content
		RelativeLayout.LayoutParams paramsContent = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		//addFavorite
		RelativeLayout.LayoutParams paramsAddFavorite = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsAddFavorite.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);
		paramsAddFavorite.bottomMargin = 10;
		paramsAddFavorite.height = size.y/10;

		//goToMap
		RelativeLayout.LayoutParams paramsGoToMap = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsGoToMap.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);
		paramsGoToMap.addRule(RelativeLayout.RIGHT_OF,
				3);
		paramsGoToMap.bottomMargin = 10;
		paramsGoToMap.height = size.y/10;
		
		//build View
		
		this.addView(reset, paramsReset);
		this.addView(invert, paramsInvert);
		this.addView(save, paramsSave);

		this.addView(scrollView, paramsScrollView);
		scrollView.getRootView().setBackgroundColor(Color.RED);
		scrollView.addView(content, paramsContent);

		this.addView(addFavorite, paramsAddFavorite);
		this.addView(goToMap, paramsGoToMap);
		
		// Params for Content

		//Delete
		deleteParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		deleteParams.height = size.x*1/5;
		deleteParams.width = size.x*1/5;

		//Save
		saveParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		saveParams.height = size.x*1/5;
		saveParams.width = size.x*1/5;
		
		//Text
		textParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textParams.height = size.x*1/5;
		textParams.width = size.x*3/5;

		//vertical Layout
		lParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		//Listener
		
		reset.setOnTouchListener(new ResetListener());
		invert.setOnTouchListener(new InvertListener());
		save.setOnTouchListener(new SaveListener());
		addFavorite.setOnTouchListener(new AddFavoriteListener());
		goToMap.setOnTouchListener(new GoToMapTouchListener());
		
		this.updateRoute();
	}
	
	public void updateRoute(){
		LinkedList<Waypoint> route = RouteController.getInstance().getCurrentRoute().getWaypoints();
		content.removeAllViews();
		for(Waypoint w:route){
			LinearLayout l = new LinearLayout(context);
			ImageButton delete = new ImageButton(context);
			ImageButton save = new ImageButton(context);
			Button text = new Button(context);
			
			delete.setImageResource(R.drawable.delete);
			save.setImageResource(R.drawable.favorite);
			text.setText(w.getName() + " " + w.getId());
			
			delete.setTag(w.getId());
			save.setTag(w.getId());
			text.setTag(w.getId());
			
			l.setOrientation(LinearLayout.HORIZONTAL);
			l.addView(text, textParams);
			l.addView(save, saveParams);
			l.addView(delete, deleteParams);
			
			content.addView(l, lParams);
			
			text.setOnTouchListener(new WaypointListener());
			save.setOnTouchListener(new SaveWaypointListener());
			delete.setOnTouchListener(new DeleteListener());
		}
	}
	
	
	private class WaypointListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			int id = Integer.parseInt(view.getTag()
					.toString());
			BoundingBox.getInstance().setCenter(RouteController.getInstance().getCurrentRoute().getWaypoint(id));
			notifyGoToMapListener();
			return false;
		}
		
	}


	private class DeleteListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			int id = Integer.parseInt(view.getTag()
					.toString());
			RouteController.getInstance().deleteActiveWaypoint(id);
			return false;
		}
		
	}
	

	private class SaveWaypointListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			int id = Integer.parseInt(view.getTag()
					.toString());
			//TODO save Waypoints
			return false;
		}
		
	}
	
	private class ResetListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			RouteController.getInstance().resetRoute();
			return false;
		}
		
	}

	private class InvertListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			RouteController.getInstance().invertRoute();
			return false;
		}
		
	}

	private class SaveListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			RouteInfo route = RouteController.getInstance().getCurrentRoute();
			// TODO Saving Route
			return false;
		}
		
	}

	private class AddFavoriteListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			notifyFavoriteListener();
			return false;
		}
		
	}

	private class GoToMapTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			notifyGoToMapListener();
			return false;
		}
		
	}
	
	//GoToMap
	
	LinkedList<GoToMapListener> rl = new LinkedList<GoToMapListener>();
	
	private void notifyGoToMapListener(){
		for(GoToMapListener l:rl) {
			l.onGoToMap();
		}
	}
	
	public void registerGoToMapListener(GoToMapListener listener){
		rl.add(listener);
	}
	
	public interface GoToMapListener {
		public void onGoToMap();
	}

	//Favorite 
	
	LinkedList<FavoriteListener> fav = new LinkedList<FavoriteListener>();
	
	private void notifyFavoriteListener(){
		for(FavoriteListener l:fav) {
			l.addFavorite();
		}
	}
	
	public void registerFavoriteListener(FavoriteListener listener){
		fav.add(listener);
	}
	
	public interface FavoriteListener {
		public void addFavorite();
	}
}

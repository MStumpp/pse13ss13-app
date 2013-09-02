package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.views;

import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
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
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

public class Search extends RelativeLayout {

	private LinearLayout routeSide;
	private LinearLayout waypointSide;
	private LinearLayout main;
	private LinearLayout tabHost;
	private Button waypointButton;
	private Button routeButton;
	private static int routeNameId = R.string.Route;
	private static String waypointName = "Wegpunkte";
	private boolean selected = true;

	public Search(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		Point size = BoundingBox.getInstance(context).getDisplaySize();
		

		
		main = new LinearLayout(context, attrs);
		main.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		this.addView(main, mainParams);

		tabHost = new LinearLayout(context, attrs);
		tabHost.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams tabHostParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tabHostParams.width = size.x;
		
		main.addView(tabHost, tabHostParams);
		
		waypointButton = new Button(context, attrs);
		waypointButton.setText("Orte");
		waypointButton.setOnTouchListener(new ToogleTabListener());
		
		routeButton = new Button(context, attrs);
		routeButton.setText("Routen");
		routeButton.setOnTouchListener(new ToogleTabListener());
		
		LinearLayout.LayoutParams waypointButtontParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		waypointButtontParams.height = size.y/10;
		waypointButtontParams.width = size.x/2;
		waypointButtontParams.leftMargin = 0;
		waypointButtontParams.rightMargin = 0;
		
		LinearLayout.LayoutParams routeButtontParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		routeButtontParams.height = size.y/10;
		routeButtontParams.width = size.x/2;
		waypointButtontParams.leftMargin = 0;
		waypointButtontParams.rightMargin = 0;
		
		tabHost.addView(waypointButton, waypointButtontParams);
		tabHost.addView(routeButton, routeButtontParams);
		
		routeSide = new LinearLayout(context,attrs);
		routeSide.setOrientation(LinearLayout.VERTICAL);
		routeSide.setVisibility(GONE);

		waypointSide = new LinearLayout(context,attrs);
		waypointSide.setOrientation(LinearLayout.VERTICAL);
		waypointSide.setVisibility(VISIBLE);
		waypointButton.setTextColor(Color.RED);
		
		routeSide.setBackgroundColor(Color.RED);

		LinearLayout.LayoutParams routeSiedeParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams waypointSiedeParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		main.addView(routeSide, routeSiedeParam);
		main.addView(waypointSide, waypointSiedeParam);
		

	}
	
	private class ToogleTabListener implements OnTouchListener{

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if(action == MotionEvent.ACTION_DOWN) {
				if(!view.isSelected()){
					toogleTab();
				}
			}
			return false;
		}
		
	}
	
	private void toogleTab(){
		if(selected){
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

}
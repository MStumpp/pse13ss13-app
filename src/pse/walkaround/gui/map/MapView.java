package pse.walkaround.gui.map;

import pse.walkaround.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MapView extends Activity implements OnTouchListener {

	private LinearLayout pull;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Views werden geparst
		Log.d("MAP_VIEW", "Die Klasse Map View wird gestartet.");
		this.setContentView(R.layout.map_view);
		
		// Display Details werden abgerufen
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		// Views werden geholt
		Log.d("MAP_VIEW", "Views werden gefetched.");
		pull = (LinearLayout)this.findViewById(R.id.overlay);
		LinearLayout map = (LinearLayout) this.findViewById(R.id.content);
		GridLayout navi = (GridLayout) this.findViewById(R.id.navi);
		ImageView iv = (ImageView) this.findViewById(R.id.ImageView01);
		
		// View C
		Log.d("MAP_VIEW", "Listener werden gesetzt.");
		map.setOnTouchListener(this);
		
		// Paramenter werden angepasst
		Log.d("MAP_VIEW", "Parameter werden angepasst.");
		
		// Parameter holen
		LayoutParams pull_para = (LayoutParams) pull.getLayoutParams();
		LayoutParams navi_para = (LayoutParams) navi.getLayoutParams();
		//LayoutParams iv_para = (LayoutParams) iv.getLayoutParams();
		
		// Paramter werden angepasst
		display.getSize(size);
		pull_para.height = size.y/2;
		Log.d("MAP_VIEW", "Die Höhe des Pull Ups wird gesetzt: " + pull_para.height);
		//iv_para.height = size.y;
	}

	public static Animation runFadeInAnimationOn(Activity ctx, View target) {
		  Animation animation = AnimationUtils.loadAnimation(ctx,
		                                                     android.R.anim.fade_in);
		  target.startAnimation(animation);
		  return animation;
	}

	public static Animation runFadeOutAnimationOn(Activity ctx, View target) {
		  Animation animation = AnimationUtils.loadAnimation(ctx,
		                                                     android.R.anim.fade_out);
		  target.startAnimation(animation);
		  return animation;
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent e) {
		Log.d("MOTION_EVENT", "Touch Event");
		if(e.getAction() == MotionEvent.ACTION_DOWN){
			if(pull.getVisibility() == View.GONE){
				Log.d("MOTION_EVENT", "Singel Touch");
				pull.setVisibility(View.VISIBLE);
				MapView.runFadeInAnimationOn(this,pull);
			} else {
				Log.d("MOTION_EVENT", "Singel Touch");
				MapView.runFadeOutAnimationOn(this,pull);
				pull.setVisibility(View.GONE);
			}
		}
		return false;
	}
	
}
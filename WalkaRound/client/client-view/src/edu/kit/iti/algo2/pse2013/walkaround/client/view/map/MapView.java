package edu.kit.iti.algo2.pse2013.walkaround.client.view.map;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayCoordinate;

//import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayCoordinate;

public class MapView extends Activity {

	private ImageView map;
	private ImageView routeOverlay;
	private ImageView user;

	public static final int USER_ARROW_IMAGE = R.drawable.user_arrow_30x38;
	private static final int USER_X_DELTA = 15;
	private static final int USER_Y_DELTA = 19;
	public static final int DEFAULT_PATTERN = R.drawable.fog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.mapview_main);

		//Log.d("MAP_VIEW", "Relative Layout Größe: " + rl.getLayoutParams().width + " * " + rl.getLayoutParams().height);
		
		Log.d("MAP_VIEW", "Rufe Display ab.");
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		this.setContentView(R.layout.map);

		Log.d("MAP_VIEW", "Karte wird erstellt.");
		map = (ImageView) this.findViewById(R.id.mapview_map);
		//map.setImageBitmap(this.getDefaultFogScreen());

		Log.d("MAP_VIEW", "User wird erstellt.");
		user = (ImageView) this.findViewById(R.id.mapview_user);
		user.setImageDrawable(this.getResources().getDrawable(USER_ARROW_IMAGE));
		user.getLayoutParams().width = USER_X_DELTA * 2;
		user.getLayoutParams().height = USER_Y_DELTA * 2;

		Log.d("MAP_VIEW", "User wird in die Mitte gestellt.");
		this.setUserPositionOverlayImage(new DisplayCoordinate(
				(float) size.x / 2, (float) size.y / 2), 180);
		
	}

	public void updateMapImage(Bitmap b) {
		this.map.setImageBitmap(b);
	}

	public void updateRouteOverlayImage(Bitmap b) {
		this.routeOverlay.setImageBitmap(b);
	}

	/**
	 * verschiebt die User Pfeil zu der Koordinate innerhalb einer Sekunde
	 * @param coor Zielkoordinate
	 * @param degree Rotation
	 */
	public void setUserPositionOverlayImage(DisplayCoordinate coor, float degree) {

		
		AnimatorSet s = new AnimatorSet();
		ObjectAnimator transX = ObjectAnimator.ofFloat(user, "x", coor.getX() - USER_X_DELTA);
		ObjectAnimator transY = ObjectAnimator.ofFloat(user, "y", coor.getY() - USER_Y_DELTA);
		ObjectAnimator rotate = ObjectAnimator.ofFloat(user, "rotation", degree);
		
		// duration verbessern!
		transX.setDuration(1000);
		transY.setDuration(1500);
		rotate.setDuration(1000);
		
		s.play(transX).with(transY);
		s.play(transY).with(rotate);
		s.play(transX).with(rotate);
		
		s.addListener(new UserAnimationListener(coor, degree));
		s.start();
		
		user.setX(coor.getX() - USER_X_DELTA);
		user.setY(coor.getY() - USER_Y_DELTA);

		Log.d("MAP_VIEW", "User hat die Position " + user.getX() + " * " + user.getY());
		Log.d("MAP_VIEW", "User Rotation: " + user.getRotation());
	}

	
	private class UserAnimationListener implements AnimatorListener {

		private float degree;
		private DisplayCoordinate coor;
		
		public UserAnimationListener(DisplayCoordinate coor, float degree) {
			this.degree = degree;
			this.coor = coor;
		}
		@Override
		public void onAnimationCancel(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationEnd(Animator animation) {
	        user.clearAnimation();

			Log.d("MAP_VIEW",
					"Setze User an die richtige Position. x: "
							+ (coor.getX() - USER_X_DELTA) + " y: "
							+ (coor.getY() - USER_Y_DELTA));

			user.setX(coor.getX() - USER_X_DELTA);
			user.setY(coor.getY() - USER_Y_DELTA);

			Log.d("MAP_VIEW", "Drehe den User um " + degree + " Grad");
			
			user.setRotation(degree);
			
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animator animation) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	/**
	 * Erstellt ein Muster aus einer Bitmap
	 * 
	 * Nicht mehr notwendig - möglicherweise für Mapmodel interressant
	 * 
	 * @return
	 */
	private Bitmap getDefaultFogScreen() {

		Log.d("MAP_VIEW", "Rufe Display ab.");
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		Log.d("MAP_VIEW", "Display wurde abgerufen. Breite: " + size.x
				+ " Höhe: " + size.y);

		Bitmap fog = BitmapFactory.decodeResource(getResources(),
				DEFAULT_PATTERN);

		int fogWidth = fog.getWidth();

		int width = ((int) Math.ceil((double) size.x) / fogWidth) * fogWidth;
		int height = ((int) Math.ceil((double) size.y) / fogWidth) * fogWidth;

		Log.d("MAP_VIEW", "Höhen wurden erstellt:" + width + " * " + height
				+ " wurde abgerufen.");

		Bitmap result = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(result);
		for (int x = width / fogWidth; x >= 0; x--) {
			for (int y = height / fogWidth; y >= 0; y--) {
				canvas.drawBitmap(fog, (x * fogWidth), (y * fogWidth), null);
			}
		}
		Log.d("MAP_VIEW", "Fog wurde erstellt.");

		return Bitmap.createScaledBitmap(result, size.x, size.y, false);

	}
	
	private class RotationListener implements AnimationListener{

		float degree;
		
		public RotationListener(float degree){
			this.degree = degree;
		}
		
	    @Override
	    public void onAnimationEnd(Animation animation) {
	        user.clearAnimation();
	       
			Log.d("MAP_VIEW", "Drehe den User um " + degree + " Grad");
			user.setRotation(degree);
	    }

	    @Override
	    public void onAnimationRepeat(Animation animation) {
	    }

	    @Override
	    public void onAnimationStart(Animation animation) {
	    }
	}
}

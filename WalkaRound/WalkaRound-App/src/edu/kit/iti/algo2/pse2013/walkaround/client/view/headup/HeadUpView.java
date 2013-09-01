package edu.kit.iti.algo2.pse2013.walkaround.client.view.headup;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpViewListener;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HeadUpView extends RelativeLayout {
	private final static String TAG = HeadUpViewOld.class.getSimpleName();

	private final static int user_lock = R.drawable.user_arrow_lock;
	private final static int user_unlock = R.drawable.user_arrow_unlock;
	private final static int USERLOCK = 0;
	private final static int OPTION = 1;

	private final static boolean defaultLockPosition = true;

	// Mögliche Piktogramme
	public final static int ARROW_RIGHT = R.drawable.pikto_rechts;

	HeadUpController headUpController;

	// Lock Bilder
	private boolean lockPosition;

	// Steuerelemente

	private ImageView plus;
	private ImageView minus;
	private ImageView option;
	private ImageView userLock;

	LinkedList<HeadUpViewListener> listener = new LinkedList<HeadUpViewListener>();

	public HeadUpView(Context context, AttributeSet attrs) {
		super(context, attrs);

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		plus = new ImageView(context);
		minus = new ImageView(context);
		option = new ImageView(context);
		userLock = new ImageView(context);

		plus.setImageDrawable(context.getResources().getDrawable(
				R.drawable.loupe_zoom_in));
		minus.setImageDrawable(context.getResources().getDrawable(
				R.drawable.loupe_zoom_out));
		option.setImageDrawable(context.getResources().getDrawable(
				R.drawable.options));
		userLock.setImageDrawable(context.getResources().getDrawable(user_lock));

		option.setScaleType(ImageView.ScaleType.FIT_XY);
		plus.setScaleType(ImageView.ScaleType.FIT_XY);
		userLock.setScaleType(ImageView.ScaleType.FIT_XY);

		option.setId(1);
		plus.setId(2);

		RelativeLayout.LayoutParams paramsOption = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsOption.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		paramsOption.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsOption.width = width / 5;
		paramsOption.height = width / 5;
		paramsOption.topMargin = 10;
		paramsOption.leftMargin = 10;
		this.addView(option, paramsOption);

		RelativeLayout.LayoutParams paramsPlus = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsOption.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsPlus.addRule(RelativeLayout.BELOW, 1);
		paramsPlus.width = width / 10;
		paramsPlus.height = width / 10;
		paramsPlus.topMargin = 10;
		paramsPlus.leftMargin = Math.abs(width / 10 - width / 5) / 2 + 10;
		this.addView(plus, paramsPlus);

		RelativeLayout.LayoutParams paramsMinus = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsOption.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsMinus.addRule(RelativeLayout.BELOW, 2);
		paramsMinus.width = width / 10;
		paramsMinus.height = width / 10;
		paramsMinus.topMargin = 10;
		paramsMinus.leftMargin = Math.abs(width / 10 - width / 5) / 2 + 10;
		this.addView(minus, paramsMinus);

		RelativeLayout.LayoutParams paramsUserLock = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsUserLock.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.TRUE);
		paramsUserLock.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsUserLock.width = width / 5;
		paramsUserLock.height = width / 5;
		paramsUserLock.topMargin = 10;
		paramsUserLock.rightMargin = 10;
		this.addView(userLock, paramsUserLock);

		plus.setOnTouchListener(new ZoomPlusListener());
		minus.setOnTouchListener(new ZoomMinusListener());
		userLock.setOnTouchListener(new UserLockListener());
		option.setOnTouchListener(new OptionListener());
	}

	/**
	 * Ein Listener der auf eine Vergrößern Eingabe wartet.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class ZoomPlusListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(plus)) {
				Log.d(TAG, "plus i pressed");
				notifyListener(+1.0F);
			}
			return false;
		}

	}

	public void setUserPositionLock(boolean lock) {
		if(lock) {
			userLock.setImageDrawable(this.getResources().getDrawable(user_lock));
		} else {
			userLock.setImageDrawable(this.getResources().getDrawable(user_unlock));
		}
	}
	
	/**
	 * Ein Listener der auf eine Verkleinern Eingabe wartet.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class ZoomMinusListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(minus)) {
				Log.d(TAG, "Minus is pressed");
				notifyListener(-1.0F);
			}
			return false;
		}

	}

	private class UserLockListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(userLock)) {
				Log.d(TAG, "UserLock is pressed");
				notifyListener(USERLOCK);
			}
			return false;
		}

	}

	private class OptionListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(userLock)) {
				Log.d(TAG, "UserLock is pressed");
				notifyListener(OPTION);
			}
			return false;
		}

	}
	
	private void notifyListener(float delta) {
		for (HeadUpViewListener l : this.listener) {
			l.onZoom(delta);
		}

	}

	private void notifyListener(int id) {

		switch (id) {

		case USERLOCK:
			for (HeadUpViewListener l : this.listener) {
				l.onUserLock();
			}
			break;

		case OPTION:
			for (HeadUpViewListener l : this.listener) {
				l.onOptionPressed();
			}
			break;
		default:
		}

	}

	public void registerListener(HeadUpViewListener listener) {
		this.listener.add(listener);
	}

	public void removeListener(HeadUpViewListener listener) {
		this.listener.remove(listener);
	}
}

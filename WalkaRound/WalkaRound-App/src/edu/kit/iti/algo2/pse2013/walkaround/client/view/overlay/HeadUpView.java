package edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay;

import java.util.LinkedList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;

/**
 * This class shows the headUpView
 *
 * @author Ludwig Biermann
 * @version 1.1
 *
 */
public class HeadUpView extends RelativeLayout {
	private final static String TAG = HeadUpView.class.getSimpleName();

	private final static int user_lock = R.drawable.user_arrow_lock;
	private final static int user_unlock = R.drawable.user_arrow_unlock;
	private final static int USERLOCK = 0;
	private final static int OPTION = 1;

	// Mögliche Piktogramme
	public final static int ARROW_RIGHT = R.drawable.pikto_rechts;

	// Steuerelemente

	private ImageView plus;
	private ImageView minus;
	private ImageView option;
	private ImageView userLock;

	private LinkedList<HeadUpViewListener> listener = new LinkedList<HeadUpViewListener>();

	/**
	 * This create a new POIview.
	 *
	 * @param context
	 *            the context of the app
	 * @param attrs
	 *            the needed attributes
	 */
	public HeadUpView(Context context, AttributeSet attrs) {
		super(context, attrs);

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;

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
		minus.setScaleType(ImageView.ScaleType.FIT_XY);
		userLock.setScaleType(ImageView.ScaleType.FIT_XY);

		option.setId(1);
		plus.setId(2);

		RelativeLayout.LayoutParams paramsOption = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
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
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsOption.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsPlus.addRule(RelativeLayout.BELOW, 1);
		paramsPlus.width = width / 10;
		paramsPlus.height = width / 10;
		paramsPlus.topMargin = 10;
		paramsPlus.leftMargin = Math.abs(width / 10 - width / 5) / 2 + 10;
		this.addView(plus, paramsPlus);

		RelativeLayout.LayoutParams paramsMinus = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsOption.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsMinus.addRule(RelativeLayout.BELOW, 2);
		paramsMinus.width = width / 10;
		paramsMinus.height = width / 10;
		paramsMinus.topMargin = 10;
		paramsMinus.leftMargin = Math.abs(width / 10 - width / 5) / 2 + 10;
		this.addView(minus, paramsMinus);

		RelativeLayout.LayoutParams paramsUserLock = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
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
		@Override
		public boolean onTouch(View view, MotionEvent me) {
			if (view.equals(plus) && MotionEvent.ACTION_DOWN == me.getAction()) {
				Log.d(TAG, "plus is pressed");
				notifyListener(+1.0F);
				return true;
			}
			return false;
		}

	}

	/**
	 * sets the position lock
	 *
	 * @param lock
	 *            true is locked
	 */
	public void setUserPositionLock(boolean lock) {
		if (lock) {
			userLock.setImageDrawable(this.getResources()
					.getDrawable(user_lock));
		} else {
			userLock.setImageDrawable(this.getResources().getDrawable(
					user_unlock));
		}
	}

	/**
	 * Ein Listener der auf eine Verkleinern Eingabe wartet.
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class ZoomMinusListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent me) {
			if (view.equals(minus) && MotionEvent.ACTION_DOWN == me.getAction()) {
				Log.d(TAG, "Minus is pressed");
				notifyListener(-1.0F);
				return true;
			}
			return false;
		}

	}

	/**
	 * user Lock Listener
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class UserLockListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(userLock) && arg1.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG, "UserLock is pressed");
				notifyListener(USERLOCK);
				return true;
			}
			return false;
		}

	}

	/**
	 * Option Listener
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class OptionListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(option)) {
				Log.d(TAG, "Option is pressed");

				notifyListener(OPTION);
				return true;
			}
			return false;
		}

	}

	/**
	 * notify all listener
	 *
	 * @param delta
	 *            the delta
	 */
	private void notifyListener(float delta) {
		for (HeadUpViewListener l : this.listener) {
			l.onZoom(delta);
		}

	}

	/**
	 * notify Listener
	 *
	 * @param id
	 *            the od
	 */
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

	/**
	 * register a new HeadUpView listener
	 * @param listener the new listener
	 */
	public void registerListener(HeadUpViewListener listener) {
		this.listener.add(listener);
	}

	/**
	 * remove a listener
	 * @param listener to remove
	 */
	public void removeListener(HeadUpViewListener listener) {
		this.listener.remove(listener);
	}

	/**
	 * HeadUpViewListener
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	public interface HeadUpViewListener {

		/**
		 * called on Zoom is pressed
		 * @param delta the delta of lod
		 */
		public void onZoom(float delta);

		/**
		 * called on Option is pressed
		 */
		public void onOptionPressed();

		/**
		 * called on User Lock is pressed
		 */
		public void onUserLock();
	}

}

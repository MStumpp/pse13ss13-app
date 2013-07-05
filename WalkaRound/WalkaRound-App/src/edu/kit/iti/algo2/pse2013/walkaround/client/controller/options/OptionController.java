package edu.kit.iti.algo2.pse2013.walkaround.client.controller.options;

import android.util.Log;

public class OptionController {
	private static final String TAG = OptionController.class.getSimpleName();
	private OptionController me;
	private OptionController() { }
	public OptionController getInstance() {
		if (me == null) {
			me = new OptionController();
		}
		return me;
	}
	public void onCreate() {
		Log.d(TAG, "Method stub onCreate()");
	}
	public void onClose() {
		Log.d(TAG, "Method stub onClose()");
	}
	public void changeValue(String key, Object value) {
		Log.d(TAG, "Method stub changeValue()");
	}
}
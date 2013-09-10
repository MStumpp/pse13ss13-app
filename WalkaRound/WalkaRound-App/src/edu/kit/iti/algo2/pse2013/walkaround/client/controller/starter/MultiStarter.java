package edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter;

import android.util.Log;

/**
 * @author Florian Sch&auml;fer
 *
 */
public abstract class MultiStarter implements StarterListener {

	private static final String TAG = MultiStarter.class.getSimpleName();
	private static int numStartedStarters = 0;
	private static int numFinishedStarters = numStartedStarters;
	private static boolean moreStartersWillCome = true;

	/**
	 * @param siSta the {@link SingleStarter} to be started
	 */
	public void start(SingleStarter siSta) {
		siSta.setInitListener(this);
		Thread t = new Thread(siSta);
		t.start();
		numStartedStarters++;
	}

	@Override
	public void onStarterFinished() {
		Log.d(TAG, String.format("%d/%d starters finished", numFinishedStarters, numStartedStarters));
		numFinishedStarters++;
		tryToFinish();
	}

	public void noMoreStarters() {
		Log.d(TAG, String.format("last starter already started", numFinishedStarters, numStartedStarters));
		moreStartersWillCome = false;
		tryToFinish();
	}

	public void tryToFinish() {
		if (!moreStartersWillCome && numFinishedStarters >= numStartedStarters) {
			actionWhenAllFinished();
		}
	}

	public abstract void actionWhenAllFinished();
}
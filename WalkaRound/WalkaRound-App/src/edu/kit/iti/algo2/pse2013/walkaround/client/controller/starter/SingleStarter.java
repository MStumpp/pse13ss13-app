package edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter;

/**
 * @author Florian Sch&auml;fer
 *
 */
public abstract class SingleStarter implements Runnable {

	private static final int[] STEPS = {};
	private int numSteps = 0;
	private StepCounter stepCounter;

	protected StarterListener listener;
	/**
	 * @param multiStarter the listener which will be notified when the starter finished successfully
	 */
	public void setInitListener(StarterListener listener) {
		this.listener = listener;
	}

	public void setStepCounter(StepCounter stepCounter) {
		this.stepCounter = stepCounter;
	}
	public int getSteps() {
		int sum = 0;
		for (int i : STEPS) {
			sum += i;
		}
		return sum;
	}

	protected void makeStep() {
		if (numSteps < STEPS.length) {
			stepCounter.makeStep(STEPS[numSteps++]);
		}
	}

	public abstract void finish();
}
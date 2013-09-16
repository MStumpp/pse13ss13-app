package edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter;

/**
 * @author Florian Sch&auml;fer
 *
 */
public abstract class SingleStarter implements Runnable {
	private StepCounter stepCounter;

	public void setStepCounter(StepCounter stepCounter) {
		this.stepCounter = stepCounter;
	}
	protected void makeStep(int index) {
		if (index < getSteps().length && index >= 0) {
			stepCounter.makeStep(getSteps()[index]);
		}
	}
	public abstract int[] getSteps();
}
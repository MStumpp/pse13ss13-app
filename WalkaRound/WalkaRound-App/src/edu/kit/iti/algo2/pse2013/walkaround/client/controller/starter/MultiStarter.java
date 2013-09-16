package edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Florian Sch&auml;fer
 *
 */
public abstract class MultiStarter {

	private StepCounter counter;
	private List<Thread> startedThreads = new ArrayList<Thread>();

	public MultiStarter(StepCounter counter) {
		this.counter = counter;
	}

	/**
	 * @param siSta the {@link SingleStarter} to be started
	 */
	public void start(SingleStarter siSta) {
		siSta.setStepCounter(counter);
		Thread t = new Thread(siSta);
		t.start();
		startedThreads.add(t);
	}

	public void noMoreStarters() {
		for (Thread t : startedThreads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		actionWhenAllFinished();
	}

	public abstract void actionWhenAllFinished();
}
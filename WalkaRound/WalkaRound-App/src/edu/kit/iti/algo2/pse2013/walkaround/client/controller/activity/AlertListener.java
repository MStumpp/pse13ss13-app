package edu.kit.iti.algo2.pse2013.walkaround.client.controller.activity;

public interface AlertListener {

	/**
	 * pushs a simple OK alert with given title and text
	 * 
	 * @param title the title of the alert window
	 * @param text the text in the alert window
	 */
	public void alert(String title, String text);
}

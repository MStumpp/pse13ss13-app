package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.client.BootActivity;


/**
 * This class tests the text-to-speech utility.
 *
 * @author Thomas Kadow
 * @version 1.0
 */

@RunWith(RobolectricTestRunner.class)
public class TextToSpeechUtilityTest extends TestCase {

	@BeforeClass
	public void testTextToSpeechInit() {
		TextToSpeechUtility.initialize(new BootActivity().getApplicationContext(), true);
	}
	
	@Test
	public void testIsReady(){
		assertTrue(TextToSpeechUtility.getInstance().isReady());
	}
	
	@Test
	public void testSpeaking(){
		assertTrue(TextToSpeechUtility.getInstance().speak("Test Test Test Test Test Test"));
	}
	
	@Test
	public void testStopSpeaking(){
		TextToSpeechUtility.getInstance().speak("Test Test Test Test Test Test");
		assertTrue(TextToSpeechUtility.getInstance().stopSpeaking());
	}
}
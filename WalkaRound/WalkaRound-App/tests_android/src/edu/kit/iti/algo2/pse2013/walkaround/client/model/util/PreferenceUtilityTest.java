package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.client.BootActivity;

@RunWith(RobolectricTestRunner.class)
public class PreferenceUtilityTest {

	@Test
	public void testChangingOptions() {
		PreferenceUtility.initialize(new BootActivity().getApplicationContext());
		assertNotNull(PreferenceUtility.getInstance().isPOITextSoundOn());
		assertNotNull(PreferenceUtility.getInstance().isPOITitleSoundOn());
		assertNotNull(PreferenceUtility.getInstance().isSoundOn());
	}
}

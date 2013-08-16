package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class CurrentMapStyleModelTest {

	CurrentMapStyleModel model;
	MapStyle defaultStyle = MapStyle.MAPSTYLE_MAPNIK;
	
	@Before
	public void setUp(){
		model = CurrentMapStyleModel.getInstance();
	}
	
	@Test
	public void testCurrentMapStyle(){
		assertEquals(model.getCurrentMapStyle(), defaultStyle);
	}

	@Test
	public void changeCurrentMapStyle(){
		MapStyle style = MapStyle.MAPSTYLE_MAPQUEST;
		model.setCurrentMapStyle(style);
		assertEquals(model.getCurrentMapStyle(), style);
	}

	@Test
	public void changeCurrentMapStyleById(){
		MapStyle style = MapStyle.MAPSTYLE_WANDERKARTE;
		model.setCurrentMapStyle(style.getName());
		assertEquals(model.getCurrentMapStyle(), style);
	}
}

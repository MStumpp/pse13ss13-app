package edu.kit.iti.algo2.pse2013.walkaround.client.controller.map;

import android.graphics.Bitmap;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayCoordinate;

/**
 * 
 * @author Ludwig Biermann
 *
 */
public class MapController {
	
	private MapController mapController;
	
	private MapController() {
		
	}
	
	public MapController getInstance() {
		if(mapController == null){
			mapController = new MapController();
		}
		return mapController;
	}
	
	public void onMapOverlayImageChange(Bitmap b) {
		
	}
	
	public void onRouteOverlayImageChange(Bitmap b) {
		
	}
	
	public void onDeletePoint(DisplayCoordinate dc) {
		
	}
	
	public void onCreatePoint(DisplayCoordinate dc) {
		
	}
	
	public void onLongPressPoint(DisplayCoordinate dc) {
		
	}
	
	public void onShift(DisplayCoordinate dc) {
		
	}
	
	public void containsWaypoint(DisplayCoordinate dc) {
		
	}
	
	public void onZoom(float z, DisplayCoordinate dc) {
		
	}
	
	public void onZoom(float z) {
		
	}
	
	public void onLockUserPosition() {
		
	}
	
	
}
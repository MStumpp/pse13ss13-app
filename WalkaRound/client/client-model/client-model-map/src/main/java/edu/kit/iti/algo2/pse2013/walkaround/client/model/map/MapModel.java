package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

public class MapModel {
	
	private static MapModel mapModel;
	
	private MapModel(){
		
	}
	
	public MapModel getInstance(){
		if(mapModel != null){
			mapModel = new MapModel();
		}
		return mapModel;
	}
	
	public void shift(Coordinate c) {
		
	}
	
	
	public void generateMapOverlayImage() {
		
	}
	
	public void generateRouteOverlayImage() {
		
	}
	
	public void setNewStyle() {
		
	}
	
	public void zoom(float lod, Coordinate c) {
		
	}
	
	public LinkedList<DisplayCoordinate> getPOIofDisplay(DisplayCoordinate dc, int[] category , int profile) {
		return null;
	}
	
	public int getCurrentLevelOfDetail() {
		return 0;
	}
	
	public void setCurrentLevelOfDetail(int lod) {
		
	}
	
	public void onChangeOfActivePOICategories() {
		
	}
	
	public Location getNearbyLocation(Coordinate c) {
		return null;
	}
}

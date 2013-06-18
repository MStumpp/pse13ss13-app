package edu.kit.iti.algo2.pse2013.walkaround.client.controller.poi;

public class POIMenuController {
	
	private static boolean intanceExists;
	private static POIMenuController instance;
	
	private POIMenuController POIMenuController() {
		
	}
	
	public POIMenuController getInstance() {
		if (!intanceExists) {
			this.instance = POIMenuController();
		}
		return this.instance;
	}
	
	
	public int[] requestAllActiveCategories() {
		// leite an eine Instanz des POI Managers weiter
	}
	
	public void addActiveCategory(int) {
		// TODO: leite an eine Instanz des POI Managers weiter
	}
	
	public void removeActiveCategory(int) {
		// TODO: leite an eine Instanz des POI Managers weiter
		
	}
	
	
	
	
}

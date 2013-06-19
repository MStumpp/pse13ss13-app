package edu.kit.iti.algo2.pse2013.walkaround.client.controller.poi;

public class POIMenuController {

	private static boolean intanceExists;
	private static POIMenuController instance;

	private POIMenuController() {

	}

	public POIMenuController getInstance() {
		if (!intanceExists) {
			this.instance = new POIMenuController();
		}
		return this.instance;
	}


	public int[] requestAllActiveCategories() {
		return null;
		// leite an eine Instanz des POI Managers weiter
	}

	public void addActiveCategory(int catIndex) {
		// TODO: leite an eine Instanz des POI Managers weiter
	}

	public void removeActiveCategory(int catIndex) {
		// TODO: leite an eine Instanz des POI Managers weiter

	}




}

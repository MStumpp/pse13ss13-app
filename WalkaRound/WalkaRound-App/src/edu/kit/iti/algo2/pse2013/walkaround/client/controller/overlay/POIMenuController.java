package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

public class POIMenuController {
	private static POIMenuController instance;

	private POIMenuController() {
		// TODO: Implement
	}

	public static POIMenuController getInstance() {
		if (instance == null) {
			instance = new POIMenuController();
		}
		return instance;
	}


	public int[] requestAllActiveCategories() {
		return null;
		// TODO: leite an eine Instanz des POI Managers weiter
	}

	public void addActiveCategory(int catIndex) {
		// TODO: leite an eine Instanz des POI Managers weiter
	}

	public void removeActiveCategory(int catIndex) {
		// TODO: leite an eine Instanz des POI Managers weiter

	}




}

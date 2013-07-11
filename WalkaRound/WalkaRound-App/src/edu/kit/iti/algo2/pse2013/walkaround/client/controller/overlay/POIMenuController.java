package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;

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


	/*	wird denke ich nicht benötigt (Thomas)
	 * public int[] requestAllActiveCategories() {
		return null;
		// TODO: leite an eine Instanz des POI Managers weiter
	}*/

	public void addActiveCategory(int catIndex) {
		POIManager.getInstance().addActivePOICategory(catIndex);
	}

	public void removeActiveCategory(int catIndex) {
		POIManager.getInstance().removeActivePOICategory(catIndex);
	}
}

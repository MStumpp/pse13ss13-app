package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import java.util.List;

import android.content.Context;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

public class SearchMenuController {
	private static SearchMenuController me;

	private SearchMenuController() {
	}

	public static SearchMenuController getInstance() {
		if (me == null) {
			me = new SearchMenuController();
		}
		return me;
	}

	public List<Location> requestSuggestionsByAddress(int postalCode,
			String city, String street, String number, Context context) {
		Address addr = new Address(street, number, city, postalCode);
		return POIManager.getInstance().searchPOIsByAddress(addr, context);
	}

	public List<POI> requestSuggestionsByText(String text) {
		return POIManager.getInstance().searchPOIsByQuery(text);
	}
}
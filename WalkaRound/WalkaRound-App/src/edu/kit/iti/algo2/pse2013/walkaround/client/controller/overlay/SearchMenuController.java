package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import java.util.List;

import android.content.Context;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

public class SearchMenuController {
	private SearchMenuController me;

	private SearchMenuController() { }

	public SearchMenuController getInstance() {
		if (me == null) {
			me = new SearchMenuController();
		}
		return me;
	}
	public List<Address> requestSuggestionsByAddress(Address addr, Context context) {
		return POIManager.getInstance(null/*TODO: replace with real LocationDataIO-Object*/).searchPOIsByAddress(addr, context);
	}
	public List<POI> requestSuggestionsByText(String text) {
		return POIManager.getInstance(null/*TODO: replace with real LocationDataIO-Object*/).searchPOIsByQuery(text);
	}
}
package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;

public class SearchMenuController {
	private SearchMenuController me;

	private SearchMenuController() { }

	public SearchMenuController getInstance() {
		if (me == null) {
			me = new SearchMenuController();
		}
		return me;
	}
	public List<Location> requestSuggestionsByAddress(Address addr) {
		return POIManager.getInstance(null/*TODO: replace with real LocationDataIO-Object*/).searchPOIsByAddress(addr);
	}
	public List<Location> requestSuggestionsByText(String text) {
		return POIManager.getInstance(null/*TODO: replace with real LocationDataIO-Object*/).searchPOIsByQuery(text);
	}
}
package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Category;

public class OSMCategoryFactory {
	public static final OSMCategory createFootwayCategory() {
		OSMTagCategory footway = new OSMTagCategory();
		footway.addTag("highway", "track");
		footway.addTag("highway", "path");
		footway.addTag("sidewalk", "left");
		footway.addTag("sidewalk", "right");
		footway.addTag("highway", "footway");
		footway.addTag("sidewalk", "both");
		return footway;
	}
	public static final OSMCategory createPOICategory(int catID) {
		if (catID == Category.POI_BARS_AND_PUBS) {
			return createBarAndPubCategory();
		} else if (catID == Category.POI_CINEMA) {
			return createCinemaCategory();
		} else if (catID == Category.POI_CLUBS_AND_NIGHTCLUBS) {
			return createClubCategory();
		} else if (catID == Category.POI_FAST_FOOD) {
			return createFastFoodCategory();
		} else if (catID == Category.POI_FOOD) {
			return createFoodCategory();
		} else if (catID == Category.POI_MUSEUM) {
			return createMuseumCategory();
		} else if (catID == Category.POI_PUBLIC_TRANSPORTATION) {
			return createPublicTransportCategory();
		} else if (catID == Category.POI_SHOP) {
			return createShopCategory();
		} else if (catID == Category.POI_SLEEPING_ACCOMODATIONS) {
			return createSleepingCategory();
		} else if (catID == Category.POI_SUPERMARKET) {
			return createSupermarketCategory();
		} else if (catID == Category.POI_THEATRE) {
			return createTheatreCategory();
		}
		Logger.getLogger(OSMCategoryFactory.class.getSimpleName()).log(Level.SEVERE, String.format("No poi-category for ID %d found!", catID));
		return null;
	}
	private static final OSMCategory createBarAndPubCategory() {
		OSMTagCategory barpub = new OSMTagCategory();
		barpub.addTag("amenity", "bar");
		barpub.addTag("amenity", "pub");
		return barpub;
	}
	private static final OSMCategory createCinemaCategory() {
		OSMTagCategory cinema = new OSMTagCategory();
		cinema.addTag("amenity", "cinema");
		return cinema;
	}
	private static final OSMCategory createClubCategory() {
		OSMTagCategory club = new OSMTagCategory();
		club.addTag("amenity", "club");
		club.addTag("amenity", "nightclub");
		return club;
	}
	private static final OSMCategory createFastFoodCategory() {
		OSMTagCategory fastFood = new OSMTagCategory();
		fastFood.addTag("amenity", "fast_food");
		return fastFood;
	}
	private static final OSMCategory createFoodCategory() {
		OSMTagCategory food = new OSMTagCategory();
		food.addTag("amenity", "restaurant");
		food.addTag("amenity", "cafe");
		food.addTag("amenity", "cafeteria");
		food.addTag("garden", "beergarden");
		food.addTag("garden", "beer_garden");
		return food;
	}
	private static final OSMCategory createMuseumCategory() {
		OSMTagCategory museum = new OSMTagCategory();
		museum.addTag("amenity", "museum");
		return museum;
	}
	private static final OSMCategory createPublicTransportCategory() {
		OSMTagCategory pub_trans = new OSMTagCategory();
		pub_trans.addTag("highway", "bus_stop");
		pub_trans.addTag("public_transport", "stop_position");
		pub_trans.addTag("public_transport", "platform"); // FIXME
		return new OSMTagCategory();
	}
	private static final OSMCategory createShopCategory() {
		OSMTagCategory shop = new OSMTagCategory();
		shop.addTag("shop", "clothes");
		shop.addTag("shop", "mall");
		return shop;
	}
	private static final OSMCategory createSleepingCategory() {
		OSMTagCategory sleep = new OSMTagCategory();
		sleep.addTag("amenity", "hotel");
		sleep.addTag("amenity", "hostel");
		sleep.addTag("amenity", "guest_house");
		return sleep;
	}
	private static final OSMCategory createSupermarketCategory() {
		OSMTagCategory supermarket = new OSMTagCategory();
		supermarket.addTag("amenity", "supermarket");
		return supermarket;
	}
	private static final OSMCategory createTheatreCategory() {
		OSMTagCategory theatre = new OSMTagCategory();
		theatre.addTag("amenity", "theatre");
		return theatre;
	}
	public static OSMCategory createAreaCategory(int catID) {
		if (catID == Category.AREA_FOREST) {
			return createForestCategory();
		} else if (catID == Category.AREA_GARDEN) {
			return createGardenCategory();
		}
		Logger.getLogger(OSMCategoryFactory.class.getSimpleName()).log(Level.SEVERE, String.format("No area category for ID %d found!", catID));
		return null;
	}
	private static final OSMCategory createForestCategory() {
		OSMTagCategory forest = new OSMTagCategory();
		forest.addTag("landuse", "forest");
		return forest;
	}
	private static final OSMCategory createGardenCategory() {
		OSMTagCategory garden = new OSMTagCategory();
		garden.addTag("leisure", "garden");
		garden.addTag("residential", "garden");
		return garden;
	}
}
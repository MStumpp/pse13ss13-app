package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

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
		if (catID == Category.BARS_AND_PUBS) {
			return createBarAndPubCategory();
		} else if (catID == Category.CINEMA) {
			return createCinemaCategory();
		} else if (catID == Category.CLUBS_AND_NIGHTCLUBS) {
			return createClubCategory();
		} else if (catID == Category.FAST_FOOD) {
			return createFastFoodCategory();
		} else if (catID == Category.FOOD) {
			return createFoodCategory();
		} else if (catID == Category.MUSEUM) {
			return createMuseumCategory();
		} else if (catID == Category.PUBLIC_TRANSPORTATION) {
			return createPublicTransportCategory();
		} else if (catID == Category.SHOP) {
			return createShopCategory();
		} else if (catID == Category.SLEEPING_ACCOMODATIONS) {
			return createSleepingCategory();
		} else if (catID == Category.SUPERMARKET) {
			return createSupermarketCategory();
		} else if (catID == Category.THEATRE) {
			return createTheatreCategory();
		}
		System.out.println("No cat found");
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
		return food;
	}
	private static final OSMCategory createMuseumCategory() {
		OSMTagCategory museum = new OSMTagCategory();
		museum.addTag("amenity", "museum");
		return museum;
	}
	private static final OSMCategory createPublicTransportCategory() {
		OSMTagCategory pub_trans = new OSMTagCategory();
		pub_trans.addTag("highway", "bus_stop"); // FIXME
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
}
package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Category;

public class OSMCategoryFactory {
	public static final OSMCategory createFootwayCategory() {
		OSMTagCategory footway = new OSMTagCategory();
		footway.addTag("highway", "track");
		//footway.addTag("highway", "path");
		footway.addTag("sidewalk", "left");
		footway.addTag("sidewalk", "right");
		footway.addTag("highway", "footway");
		//footway.addTag("sidewalk", "both");
		return footway;
	}
	public static final OSMCategory createPOICategory(Category c) {
		if (c == Category.BARS_AND_PUBS) {
			return createBarAndPubCategory();
		} else if (c == Category.CINEMA) {
			return createCinemaCategory();
		} else if (c == Category.CLUBS_AND_NIGHTCLUBS) {
			return createClubCategory();
		} else if (c == Category.FAST_FOOD) {
			return createFastFoodCategory();
		} else if (c == Category.FOOD) {
			return createFoodCategory();
		} else if (c == Category.MUSEUM) {
			return createMuseumCategory();
		} else if (c == Category.PUBLIC_TRANSPORTATION) {
			return createPublicTransportCategory();
		} else if (c == Category.SHOP) {
			return createShopCategory();
		}
		return null;
	}
	private static OSMCategory createPublicTransportCategory() {
		return null;// TODO Auto-generated method stub
	}
	private static OSMCategory createMuseumCategory() {
		OSMTagCategory museum = new OSMTagCategory();
		museum.addTag("amenity", "museum");
		return museum;
	}
	private static OSMCategory createFoodCategory() {
		OSMTagCategory food = new OSMTagCategory();
		food.addTag("amenity", "restaurant");
		food.addTag("amenity", "cafe");
		food.addTag("amenity", "cafeteria");
		return food;
	}
	private static OSMCategory createClubCategory() {
		OSMTagCategory club = new OSMTagCategory();
		club.addTag("amenity", "club");
		club.addTag("amenity", "nightclub");
		return club;
	}
	private static final OSMCategory createCinemaCategory() {
		OSMTagCategory cinema = new OSMTagCategory();
		cinema.addTag("amenity", "cinema");
		return cinema;
	}
	private static final OSMCategory createBarAndPubCategory() {
		OSMTagCategory barpub = new OSMTagCategory();
		barpub.addTag("amenity", "bar");
		barpub.addTag("amenity", "pub");
		return barpub;
	}
	private static final OSMCategory createFastFoodCategory() {
		OSMTagCategory fastFood = new OSMTagCategory();
		fastFood.addTag("amenity", "fast_food");
		return fastFood;
	}
	private static final OSMCategory createShopCategory() {
		OSMTagCategory shop = new OSMTagCategory();
		shop.addTag("shop", "clothes");
		shop.addTag("shop", "mall");
		return shop;
	}
}

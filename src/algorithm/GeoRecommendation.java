package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import db.MySQLConnection;
import entity.Item;

public class GeoRecommendation {
	public List<Item> recommendItems(String userId, double lat, double lon){
		// step 1, get connection and favorite itemID
		MySQLConnection conn = new MySQLConnection();
		Set<String> favoriteItems = conn.getFavoriteItemIds(userId);
		
		//step 2, get categories from favorite itemIds
		Set<String> allCategories = new HashSet<>();
		for(String itemId: favoriteItems) {
			allCategories.addAll(conn.getCategories(itemId));
			
		}
		// step 3, from all Categories to get recommend items
		Set<Item> recommendedItems = new HashSet<Item>(); // different categories could find same restaurant
		for(String category: allCategories) {
			List<Item> items = conn.searchItems(userId, lat, lon, category);
			recommendedItems.addAll(items);
		}
		
		// step 4, delete items been added to favorite, filter function
		List<Item> filteredItems = new ArrayList<>();
		for(Item item : recommendedItems) {
			if(!favoriteItems.contains(item.getItemId())) {
				filteredItems.add(item);
			}
		}
		// step 5, sort the filteredItems, use a comparator to sort items according distance
		Collections.sort(filteredItems, new Comparator<Item>() {
			@Override
			public int compare(Item item1, Item item2) {
				return Double.compare(item1.getDistance(), item2.getDistance()); 
				// compare double number 
			}
		});
		
		return filteredItems;
	}
}

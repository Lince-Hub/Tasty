package lt.linas_puplauskas.service;

import lt.linas_puplauskas.model.restaurant.Dish;
import lt.linas_puplauskas.model.restaurant.Restaurant;

import java.util.List;

public class RestaurantService extends UserService{
    public RestaurantService() {
        super(Restaurant.class);
    }

    public void removeDish(Restaurant restaurant, Dish dish){
        List<Dish> menu = restaurant.getMenu();
        menu.remove(dish);
        restaurant.setMenu(menu);
        this.update(restaurant);
    }
}

package lt.linas_puplauskas.service;

import com.vaadin.flow.server.VaadinSession;
import lt.linas_puplauskas.constants.SessionConstants;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.restaurant.Dish;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.service.user.WebRestaurantService;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final WebRestaurantService webRestaurantService;

    public CartService(WebRestaurantService webRestaurantService) {
        this.webRestaurantService = webRestaurantService;
    }

    public Order getCart() {
        return (Order) VaadinSession.getCurrent().getAttribute(SessionConstants.CART_KEY);
    }

    public Order getOrCreateCart(Restaurant restaurant) {
        Order cart = getCart();

        if (cart != null) {
            Restaurant cartRestaurant = (Restaurant) webRestaurantService.get(cart.getRestaurantId());
            if (!cartRestaurant.getId().equals(restaurant.getId())) {
                clearCart();
                cart = null;
            }
        }

        if (cart == null) {
            cart = new Order();
            cart.setRestaurantId(restaurant.getId());
            VaadinSession.getCurrent().setAttribute(SessionConstants.CART_KEY, cart);
        }

        return cart;
    }

    public void addDish(Restaurant restaurant, Dish dish) {
        Order cart = getOrCreateCart(restaurant);
        cart.getItems().stream()
                .filter(d -> d.getId().equals(dish.getId()))
                .findFirst()
                .ifPresentOrElse(
                        d -> d.setAmount(d.getAmount() + 1),
                        () -> {
                            dish.setAmount(1);
                            cart.getItems().add(dish);
                        }
                );
        recalculateTotal(cart);
    }

    public void removeDish(Dish dish) {
        Order cart = getCart();
        if (cart == null) return;
        cart.getItems().removeIf(d -> d.getId().equals(dish.getId()));
        recalculateTotal(cart);
    }

    public void clearCart() {
        VaadinSession.getCurrent().setAttribute(SessionConstants.CART_KEY, null);
    }


    public boolean isEmpty() {
        Order cart = getCart();
        return cart == null || cart.getItems().isEmpty();
    }

    public void recalculateTotal(Order cart) {
        double total = cart.getItems().stream()
                .mapToDouble(d -> d.getPrice() * d.getAmount())
                .sum();
        cart.setTotalPrice(total);
    }
}
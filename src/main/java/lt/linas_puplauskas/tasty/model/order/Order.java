package lt.linas_puplauskas.tasty.model.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.linas_puplauskas.tasty.model.client.Client;
import lt.linas_puplauskas.tasty.model.driver.Driver;
import lt.linas_puplauskas.tasty.model.driver.Message;
import lt.linas_puplauskas.tasty.model.restaurant.Dish;
import lt.linas_puplauskas.tasty.model.restaurant.Restaurant;
import lt.linas_puplauskas.tasty.model.review.Review;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Order {
    private ObjectId id;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
    private Client buyer;
    private Driver deliveryPerson;
    private Restaurant restaurant;
    private OrderStatus status;
    private int totalPrice;
    private int deliveryFee;
    private String paymentMethod;
    private int estimatedDeliveryMin;
    private String specialInstructions;
    private Review review;
    private List<Dish> items;
    private List<Message> messages;
}

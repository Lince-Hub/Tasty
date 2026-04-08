package lt.linas_puplauskas.model.review;

import lombok.Setter;
import lt.linas_puplauskas.model.driver.Driver;
import lt.linas_puplauskas.model.restaurant.Restaurant;

import java.time.LocalDateTime;

@Setter
public class Review {
    private int rating;
    private String title;
    private LocalDateTime createdAt;
    private Restaurant reviewedRestaurant;
    private Driver reviewedDriver;
    private String comment;
}

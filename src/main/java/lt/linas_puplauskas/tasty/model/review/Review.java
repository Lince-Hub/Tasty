package lt.linas_puplauskas.tasty.model.review;

import lt.linas_puplauskas.tasty.model.driver.Driver;
import lt.linas_puplauskas.tasty.model.restaurant.Restaurant;

import java.time.LocalDateTime;

public class Review {
    private int rating;
    private String title;
    private LocalDateTime reviewDate;
    private Restaurant reviewedRestaurant;
    private Driver reviewedDriver;
}

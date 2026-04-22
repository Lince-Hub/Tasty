package lt.linas_puplauskas.model.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.linas_puplauskas.model.driver.Driver;
import lt.linas_puplauskas.model.restaurant.Restaurant;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Review {
    private int rating;
    private String title;
    private LocalDateTime createdAt;
    private Restaurant reviewedRestaurant;
    private String comment;
}

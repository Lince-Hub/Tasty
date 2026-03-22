package lt.linas_puplauskas.tasty.model.restaurant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.linas_puplauskas.tasty.model.user.User;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends User {
    private String description;
    private String address;
    private String happyHoursStart;
    private String happyHoursEnd;
    private double rating;
    private boolean isOpen;
    private Cuisine cuisine;
    private String openingTime;
    private String closingTime;
    private List<Dish> menu;
}


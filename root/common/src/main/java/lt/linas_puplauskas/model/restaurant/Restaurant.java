package lt.linas_puplauskas.model.restaurant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.user.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends User {
    private String description;
    private String name;
    private String address;
    private String happyHoursStart;
    private String happyHoursEnd;
    private double rating;
    private boolean isOpen;
    private Cuisine cuisine;
    private String openingTime;
    private String closingTime;
    private List<Dish> menu;

    @Override
    public String toString() {
        return name;
    }
}


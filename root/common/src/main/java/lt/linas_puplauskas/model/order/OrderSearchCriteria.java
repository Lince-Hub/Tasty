package lt.linas_puplauskas.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
@Setter
public class OrderSearchCriteria {
    private ObjectId id;
    private Restaurant restaurant;

    public OrderSearchCriteria(ObjectId id) {
        this.id = id;
    }

    public OrderSearchCriteria(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

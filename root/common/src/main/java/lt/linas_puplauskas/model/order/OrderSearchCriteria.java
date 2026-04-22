package lt.linas_puplauskas.model.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
public class OrderSearchCriteria {
    private ObjectId restaurantId;

    public OrderSearchCriteria(ObjectId restaurantId) {
        this.restaurantId = restaurantId;
    }
}

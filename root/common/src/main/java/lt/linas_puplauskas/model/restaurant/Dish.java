package lt.linas_puplauskas.model.restaurant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Dish {
    private ObjectId id;
    private String title;
    private String description;
    private double price;
    private DishCategory category;
    private int preparationTimeMin;
    private boolean isAvailable;
    private String imageUrl;
    private float weight;
    private List<String> allergens;
    private int calories;
    private int amount;
}

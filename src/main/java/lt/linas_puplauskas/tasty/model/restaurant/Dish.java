package lt.linas_puplauskas.tasty.model.restaurant;

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
    private int price;
    private DishCategory category;
    private int preparationTimeMin;
    private boolean isAvailable;
    private String imageUrl;
    private float weight;
    private List<String> allergens;
    private int calories;
}

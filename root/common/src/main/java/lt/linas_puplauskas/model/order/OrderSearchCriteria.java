package lt.linas_puplauskas.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
@Setter
public class OrderSearchCriteria {
    private ObjectId id;
}

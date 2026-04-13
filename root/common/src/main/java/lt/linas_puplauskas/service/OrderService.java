package lt.linas_puplauskas.service;

import com.mongodb.client.model.Filters;
import lt.linas_puplauskas.constants.MongoConstants;
import lt.linas_puplauskas.database.DaoService;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.order.OrderSearchCriteria;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class OrderService extends DaoService<Order, OrderSearchCriteria> {
    public OrderService(Class<Order> entityClass) {
        super(entityClass);
    }

    @Override
    public String getTableName() {
        return MongoConstants.MONGO_COLLECTION_ORDERS;
    }

    @Override
    public Bson processCriteria(OrderSearchCriteria criteria) {
        List<Bson> filters = new ArrayList<>();

        if (criteria.getId() != null) {
            filters.add(Filters.eq("_id", criteria.getId()));
        }

        if (criteria.getRestaurant() != null) {
            filters.add(Filters.eq("restaurant._id", criteria.getRestaurant().getId()));
        }

        return filters.isEmpty() ? new Document() : Filters.and(filters);
    }
}

package lt.linas_puplauskas.service;

import com.mongodb.client.model.Filters;
import lt.linas_puplauskas.constants.MongoConstants;
import lt.linas_puplauskas.database.DaoService;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.order.OrderSearchCriteria;
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
        List<Bson> filter = new ArrayList<>();

        if (criteria.getId() != null) {
            filter.add(Filters.eq("_id", criteria.getId()));
        }

        return Filters.and(filter);
    }
}

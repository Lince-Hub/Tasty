package lt.linas_puplauskas.service;

import lt.linas_puplauskas.model.order.Order;
import org.springframework.stereotype.Service;

@Service
public class WebOrderService extends OrderService{

    public WebOrderService() {
        super(Order.class);
    }

    @Override
    public void save(Order obj) {
        if (obj.getId() == null) {
            obj.setId(new org.bson.types.ObjectId());
            this.getCollection().insertOne(obj);
        } else {
            this.getCollection().replaceOne(
                    com.mongodb.client.model.Filters.eq("_id", obj.getId()),
                    obj
            );
        }
    }
}

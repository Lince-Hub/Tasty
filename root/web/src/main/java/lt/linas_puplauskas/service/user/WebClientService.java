package lt.linas_puplauskas.service.user;

import com.mongodb.client.model.Filters;
import lt.linas_puplauskas.database.DaoService;
import lt.linas_puplauskas.model.client.Client;
import lt.linas_puplauskas.model.user.UserSearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebClientService extends DaoService<Client, UserSearchCriteria> {

    public WebClientService() {
        super(Client.class);
    }

    @Override
    public String getTableName() {
        return "users";
    }

    @Override
    public Bson processCriteria(UserSearchCriteria criteria) {
        List<Bson> filters = new ArrayList<>();

        if (StringUtils.isNotEmpty(criteria.getUsername())) {
            filters.add(Filters.eq("username", criteria.getUsername()));
        }

        if (StringUtils.isNotEmpty(criteria.getPassword())) {
            filters.add(Filters.eq("password", criteria.getPassword()));
        }

        if (criteria.getId() != null) {
            filters.add(Filters.eq("_id", criteria.getId()));
        }

        if (criteria.getRole() != null) {
            filters.add(Filters.eq("role", criteria.getRole().name()));
        }

        return filters.isEmpty() ? new Document() : Filters.and(filters);
    }

    @Override
    public void save(Client obj) {
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

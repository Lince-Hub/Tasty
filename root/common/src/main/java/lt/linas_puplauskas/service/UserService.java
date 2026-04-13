package lt.linas_puplauskas.service;

import com.mongodb.client.model.Filters;
import lt.linas_puplauskas.constants.MongoConstants;
import lt.linas_puplauskas.database.DaoService;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserSearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class UserService<T extends User> extends DaoService<T, UserSearchCriteria> {

    public UserService(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public String getTableName() {
        return MongoConstants.MONGO_COLLECTION_USERS;
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
}

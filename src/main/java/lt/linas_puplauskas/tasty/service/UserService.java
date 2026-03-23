package lt.linas_puplauskas.tasty.service;

import com.mongodb.client.model.Filters;
import lt.linas_puplauskas.tasty.constants.MongoConstants;
import lt.linas_puplauskas.tasty.database.DaoService;
import lt.linas_puplauskas.tasty.model.user.UserSearchCriteria;
import lt.linas_puplauskas.tasty.model.user.User;
import org.apache.commons.lang3.StringUtils;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class UserService extends DaoService<User, UserSearchCriteria> {
    public UserService(Class<User> entityClass) {
        super(entityClass);
    }

    @Override
    public String getTableName() {
        return MongoConstants.MONGO_COLLECTION_USERS;
    }

    @Override
    public Bson processCriteria(UserSearchCriteria criteria) {
        List<Bson> filter = new ArrayList<>();

        if (StringUtils.isNotEmpty(criteria.getUsername())) {
            filter.add(Filters.eq("username", criteria.getUsername()));
        }

        if (StringUtils.isNotEmpty(criteria.getPassword())) {
            filter.add(Filters.eq("username", criteria.getUsername()));
        }

        if (criteria.getId() != null) {
            filter.add(Filters.eq("_id", criteria.getId()));
        }

        return Filters.and(filter);
    }
}

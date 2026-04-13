package lt.linas_puplauskas.model.restaurant;

import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.model.user.UserSearchCriteria;

public class RestaurantSearchCriteria extends UserSearchCriteria {
    public RestaurantSearchCriteria(UserRole userRole) {
        super(userRole);
    }
}

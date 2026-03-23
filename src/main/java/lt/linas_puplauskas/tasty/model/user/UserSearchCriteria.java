package lt.linas_puplauskas.tasty.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
@Setter
public class UserSearchCriteria {
    String username;
    String password;
    ObjectId id;

    public UserSearchCriteria(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public UserSearchCriteria(ObjectId id) {
        this.id = id;
    }
}

package lt.linas_puplauskas.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSearchCriteria {
    private ObjectId id;
    private String username;
    private String password;
    private UserRole role;

    public UserSearchCriteria(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public UserSearchCriteria(ObjectId id) {
        this.id = id;
    }

    public UserSearchCriteria(UserRole role) {
        this.role = role;
    }
}

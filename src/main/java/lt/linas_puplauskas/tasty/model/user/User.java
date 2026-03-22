package lt.linas_puplauskas.tasty.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.linas_puplauskas.tasty.database.DaoObject;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class User extends DaoObject {
    protected ObjectId id;
    protected String username;
    protected String password;
    protected String email;
    protected LocalDateTime dateCreated = LocalDateTime.now();
    protected String phoneNum;
    protected UserRole role;
}

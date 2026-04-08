package lt.linas_puplauskas.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.linas_puplauskas.database.DaoObject;
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

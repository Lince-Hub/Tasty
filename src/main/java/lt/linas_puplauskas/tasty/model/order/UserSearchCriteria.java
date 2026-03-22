package lt.linas_puplauskas.tasty.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserSearchCriteria {
    String username;
    String password;
}

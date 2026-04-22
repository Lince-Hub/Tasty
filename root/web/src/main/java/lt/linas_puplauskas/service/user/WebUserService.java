package lt.linas_puplauskas.service.user;

import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class WebUserService extends UserService {
    public WebUserService() {
        super(User.class);
    }
}

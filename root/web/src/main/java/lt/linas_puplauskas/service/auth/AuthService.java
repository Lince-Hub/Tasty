package lt.linas_puplauskas.service.auth;

import com.vaadin.flow.server.VaadinSession;
import lt.linas_puplauskas.constants.SessionConstants;
import lt.linas_puplauskas.model.client.Client;
import lt.linas_puplauskas.model.driver.Driver;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.model.user.UserSearchCriteria;
import lt.linas_puplauskas.service.user.WebClientService;
import lt.linas_puplauskas.service.user.WebDriverService;
import lt.linas_puplauskas.service.user.WebUserService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final WebUserService webUserService;
    private final WebClientService webClientService;
    private final WebDriverService webDriverService;

    public AuthService(WebUserService webUserService, WebClientService webClientService, WebDriverService webDriverService) {
        this.webUserService = webUserService;
        this.webClientService = webClientService;
        this.webDriverService = webDriverService;
    }

    public boolean login(String username, String password) {

        User user = (User) webUserService.findFirst(new UserSearchCriteria(username, password));

        if (user == null) return false;

        setCurrentUser(user);

        if (user.getRole().equals(UserRole.CLIENT)) {
            Client client = webClientService.findFirst(new UserSearchCriteria(username, password));
            VaadinSession.getCurrent().setAttribute("client", client);
        } else {
            Driver driver = webDriverService.findFirst(new UserSearchCriteria(username, password));
            VaadinSession.getCurrent().setAttribute("driver", driver);
        }

        return true;
    }

    public void setCurrentUser(User user) {
        VaadinSession.getCurrent().setAttribute(SessionConstants.USER, user);
    }

    public User getCurrentUser() {
        return (User) VaadinSession.getCurrent().getAttribute(SessionConstants.USER);
    }

    public Client getCurrentClient() {
        return (Client) VaadinSession.getCurrent().getAttribute(SessionConstants.CLIENT);
    }

    public Driver getCurrentDriver() {
        return (Driver) VaadinSession.getCurrent().getAttribute(SessionConstants.DRIVER);
    }

    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }
}

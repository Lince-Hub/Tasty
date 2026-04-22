package lt.linas_puplauskas.service.auth;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
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
import lt.linas_puplauskas.ui.view.LoginView;
import lt.linas_puplauskas.ui.view.RegisterView;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class SecurityService implements BeforeEnterListener {

    private final AuthService authService;
    private final WebUserService webUserService;
    private final WebClientService webClientService;
    private final WebDriverService webDriverService;

    public SecurityService(AuthService authService, WebUserService webUserService, WebClientService webClientService, WebDriverService webDriverService) {
        this.authService = authService;
        this.webUserService = webUserService;
        this.webClientService = webClientService;
        this.webDriverService = webDriverService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!authService.isLoggedIn()) {
            devClient(webClientService, webUserService);
//            devDriver(webDriverService, webUserService);
        }

        boolean loggedIn = authService.isLoggedIn();

        boolean isPublicPage =
                event.getNavigationTarget().equals(LoginView.class) ||
                        event.getNavigationTarget().equals(RegisterView.class);

        if (!loggedIn && !isPublicPage) {
            event.rerouteTo(LoginView.class);
        }
    }

    private void devClient(WebClientService clientService, WebUserService userService) {
        UserSearchCriteria criteria = new UserSearchCriteria(new ObjectId("650000000000000000000005"));
        criteria.setRole(UserRole.CLIENT);

        User user = (User) userService.findFirst(criteria);
        Client client = (Client) clientService.findFirst(criteria);

        System.out.println("Dev user role: " + (user != null ? user.getRole() : "null"));
        System.out.println("Dev client: " + client);

        VaadinSession.getCurrent().setAttribute(SessionConstants.USER, user);
        VaadinSession.getCurrent().setAttribute(SessionConstants.CLIENT, client);
    }

    private void devDriver(WebDriverService driverService, WebUserService userService) {
        User user = (User) userService.findFirst(new UserSearchCriteria(new ObjectId("650000000000000000000007")));
        Driver driver = driverService.findFirst(new UserSearchCriteria(new ObjectId("650000000000000000000007")));
        VaadinSession.getCurrent().setAttribute(SessionConstants.USER, user);
        VaadinSession.getCurrent().setAttribute(SessionConstants.DRIVER, driver);
    }
}

package lt.linas_puplauskas.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.model.user.UserSearchCriteria;
import lt.linas_puplauskas.service.auth.AuthService;
import lt.linas_puplauskas.service.user.WebUserService;

@Route("login")
@PageTitle("Login – Tasty")
public class LoginView extends VerticalLayout {

    public LoginView(AuthService authService, WebUserService webUserService) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "var(--lumo-contrast-5pct)");

        VerticalLayout card = new VerticalLayout();
        card.setWidth("360px");
        card.setPadding(true);
        card.setSpacing(true);
        card.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 24px rgba(0,0,0,0.10)")
                .set("padding", "2rem");

        // --- Header ---
        H2 title = new H2("🍽 Tasty");
        title.getStyle().set("margin", "0 0 0.25rem 0");

        Paragraph subtitle = new Paragraph("Sign in to your account");
        subtitle.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.Margin.NONE);

        TextField username = new TextField("Username");
        username.setWidth("100%");
        username.setPlaceholder("Enter your username");

        PasswordField password = new PasswordField("Password");
        password.setWidth("100%");
        password.setPlaceholder("Enter your password");

        Button loginButton = new Button("Sign in", event -> {
            String user = username.getValue();
            String pass = password.getValue();

            if (user.isBlank() || pass.isBlank()) {
                Notification error = Notification.show("Please fill in both fields");
                error.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (authService.login(user, pass)) {
                User loggedInUser = (User) webUserService.findFirst(new UserSearchCriteria(user, pass));
                authService.setCurrentUser(loggedInUser);
                if (loggedInUser.getRole().equals(UserRole.CLIENT)) {
                    getUI().ifPresent(ui -> ui.navigate("restaurants"));
                } else {
                    getUI().ifPresent(ui -> ui.navigate("orders"));
                }
            } else {
                Notification error = Notification.show("Invalid username or password");
                error.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        loginButton.setWidth("100%");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);

        Button registerButton = new Button("Create an account", event ->
                getUI().ifPresent(ui -> ui.navigate("register"))
        );
        registerButton.setWidth("100%");
        registerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        card.add(title, subtitle, username, password, loginButton, registerButton);
        add(card);
    }
}
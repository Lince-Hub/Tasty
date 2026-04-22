package lt.linas_puplauskas.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lt.linas_puplauskas.model.client.Client;
import lt.linas_puplauskas.model.driver.Driver;
import lt.linas_puplauskas.model.driver.VehicleType;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.service.user.WebClientService;
import lt.linas_puplauskas.service.user.WebDriverService;

@Route("register")
@PageTitle("Register – Tasty")
public class RegisterView extends VerticalLayout {

    public RegisterView(WebDriverService webDriverService, WebClientService webClientService) {

        setAlignItems(Alignment.CENTER);
        getStyle().set("background", "var(--lumo-contrast-5pct)");

        VerticalLayout card = new VerticalLayout();
        card.setWidth("420px");
        card.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "16px")
                .set("box-shadow", "0 4px 32px rgba(0,0,0,0.12)")
                .set("padding", "2rem")
                .set("box-sizing", "border-box");
        card.setSpacing(false);
        card.setPadding(false);

        Span emoji = new Span("🍽");
        emoji.getStyle().set("font-size", "2.5rem");

        H2 title = new H2("Create Account");
        title.getStyle()
                .set("margin", "0.5rem 0 0.25rem 0")
                .set("font-size", "1.6rem");

        Paragraph subtitle = new Paragraph("Join Tasty as a client or driver");
        subtitle.addClassNames(LumoUtility.TextColor.SECONDARY);
        subtitle.getStyle().set("margin", "0 0 1rem 0");

        RadioButtonGroup<String> roleGroup = new RadioButtonGroup<>();
        roleGroup.setLabel("Register as");
        roleGroup.setItems("Client", "Driver");
        roleGroup.setValue("Client");
        roleGroup.getStyle().set("margin-bottom", "0.5rem");

        Hr divider = new Hr();
        divider.getStyle().set("margin", "0.75rem 0");

        TextField name = field("First Name");
        TextField surname = field("Last Name");
        TextField username = field("Username");
        EmailField email = new EmailField("Email");
        email.setWidth("100%");
        TextField phone = field("Phone Number");
        PasswordField password = new PasswordField("Password");
        password.setWidth("100%");
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        confirmPassword.setWidth("100%");
        DatePicker dateOfBirth = new DatePicker("Date of Birth");
        dateOfBirth.setWidth("100%");

        VerticalLayout clientFields = new VerticalLayout();
        clientFields.setPadding(false);
        clientFields.setSpacing(true);
        clientFields.setVisible(true);

        TextField address = field("Delivery Address");
        TextField cardNo = field("Card Number");
        cardNo.setPlaceholder("XXXX XXXX XXXX XXXX");
        clientFields.add(address, cardNo);

        VerticalLayout driverFields = new VerticalLayout();
        driverFields.setPadding(false);
        driverFields.setSpacing(true);
        driverFields.setVisible(false);

        TextField driverLicence = field("Driver Licence Number");
        TextField vehiclePlate = field("Vehicle Plate Number");
        ComboBox<VehicleType> vehicleType = new ComboBox<>("Vehicle Type");
        vehicleType.setItems(VehicleType.values());
        vehicleType.setWidth("100%");
        driverFields.add(driverLicence, vehiclePlate, vehicleType);

        roleGroup.addValueChangeListener(e -> {
            boolean isDriver = "Driver".equals(e.getValue());
            clientFields.setVisible(!isDriver);
            driverFields.setVisible(isDriver);
        });

        Button registerButton = new Button("Create Account", event -> {
            if (name.isEmpty() || surname.isEmpty() || username.isEmpty()
                    || email.isEmpty() || password.isEmpty() || dateOfBirth.isEmpty()) {
                error("Please fill in all required fields");
                return;
            }
            if (!password.getValue().equals(confirmPassword.getValue())) {
                error("Passwords do not match");
                return;
            }

            if ("Driver".equals(roleGroup.getValue())) {
                if (driverLicence.isEmpty() || vehiclePlate.isEmpty() || vehicleType.isEmpty()) {
                    error("Please fill in all driver fields");
                    return;
                }
                Driver driver = new Driver();
                driver.setUsername(username.getValue());
                driver.setPassword(password.getValue());
                driver.setEmail(email.getValue());
                driver.setPhoneNum(phone.getValue());
                driver.setName(name.getValue());
                driver.setSurname(surname.getValue());
                driver.setDateOfBirth(dateOfBirth.getValue());
                driver.setDriverLicence(driverLicence.getValue());
                driver.setVehiclePlateNumber(vehiclePlate.getValue());
                driver.setVehicleType(vehicleType.getValue());
                driver.setRole(UserRole.DRIVER);
                webDriverService.save(driver);

            } else {
                Client client = new Client();
                client.setUsername(username.getValue());
                client.setPassword(password.getValue());
                client.setEmail(email.getValue());
                client.setPhoneNum(phone.getValue());
                client.setName(name.getValue());
                client.setSurname(surname.getValue());
                client.setDateOfBirth(dateOfBirth.getValue());
                client.setAddress(address.getValue());
                client.setCardNo(cardNo.getValue());
                client.setRole(UserRole.CLIENT);
                webClientService.save(client);
            }

            Notification n = Notification.show("Account created! Please sign in.");
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            getUI().ifPresent(ui -> ui.navigate("login"));
        });
        registerButton.setWidth("100%");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        registerButton.getStyle().set("margin-top", "1rem");

        Button backToLogin = new Button("Already have an account? Sign in",
                e -> getUI().ifPresent(ui -> ui.navigate("login")));
        backToLogin.setWidth("100%");
        backToLogin.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        card.add(
                emoji, title, subtitle,
                roleGroup, divider,
                name, surname, username, email, phone,
                password, confirmPassword, dateOfBirth,
                clientFields, driverFields,
                registerButton, backToLogin
        );

        add(card);
    }

    private TextField field(String label) {
        TextField f = new TextField(label);
        f.setWidth("100%");
        return f;
    }

    private void error(String message) {
        Notification n = Notification.show(message);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
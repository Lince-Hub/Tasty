package lt.linas_puplauskas.ui.view.user;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lt.linas_puplauskas.model.client.Client;
import lt.linas_puplauskas.model.driver.Driver;
import lt.linas_puplauskas.model.driver.VehicleType;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.service.auth.AuthService;
import lt.linas_puplauskas.service.user.WebClientService;
import lt.linas_puplauskas.service.user.WebDriverService;
import lt.linas_puplauskas.ui.MainLayout;

@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile – Tasty")
public class ProfileView extends VerticalLayout {

    public ProfileView(AuthService authService,
                       WebClientService webClientService,
                       WebDriverService webDriverService) {

        setAlignItems(Alignment.CENTER);
        getStyle().set("min-height", "100vh").set("padding", "2rem 0");
        getStyle().set("background", "var(--lumo-contrast-5pct)");

        VerticalLayout card = new VerticalLayout();
        card.setWidth("420px");
        card.setSpacing(false);
        card.setPadding(false);
        card.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "16px")
                .set("box-shadow", "0 4px 32px rgba(0,0,0,0.12)")
                .set("padding", "2rem")
                .set("box-sizing", "border-box");

        Span emoji = new Span("👤");
        emoji.getStyle().set("font-size", "2.5rem");

        H2 title = new H2("My Profile");
        title.getStyle().set("margin", "0.5rem 0 0.25rem 0").set("font-size", "1.6rem");

        UserRole role = authService.getCurrentUser().getRole();
        Paragraph subtitle = new Paragraph("Logged in as " + role.name().charAt(0)
                + role.name().substring(1).toLowerCase());
        subtitle.addClassNames(LumoUtility.TextColor.SECONDARY);
        subtitle.getStyle().set("margin", "0 0 1rem 0");

        Hr divider = new Hr();
        divider.getStyle().set("margin", "0.75rem 0");

        card.add(emoji, title, subtitle, divider);

        if (role == UserRole.CLIENT) {
            buildClientForm(card, authService.getCurrentClient(), webClientService);
        } else if (role == UserRole.DRIVER) {
            buildDriverForm(card, authService.getCurrentDriver(), webDriverService);
        }

        add(card);
    }

    private void buildClientForm(VerticalLayout card, Client client,
                                 WebClientService webClientService) {

        TextField name = field("First Name", client.getName());
        TextField surname = field("Last Name", client.getSurname());
        EmailField email = emailField(client.getEmail());
        TextField phone = field("Phone Number", client.getPhoneNum());
        DatePicker dateOfBirth = datePicker(client.getDateOfBirth());
        TextField address = field("Delivery Address", client.getAddress());
        TextField cardNo = field("Card Number", client.getCardNo());

        Span bonusPoints = new Span("⭐ Bonus points: " + client.getBonusPoints());
        bonusPoints.getStyle().set("font-size", "0.9rem").set("color", "var(--lumo-secondary-text-color)");

        Span balance = new Span("💳 Balance: €" + String.format("%.2f", client.getBalance()));
        balance.getStyle().set("font-size", "0.9rem").set("color", "var(--lumo-secondary-text-color)");

        Button save = saveButton(click -> {
            client.setName(name.getValue());
            client.setSurname(surname.getValue());
            client.setEmail(email.getValue());
            client.setPhoneNum(phone.getValue());
            client.setDateOfBirth(dateOfBirth.getValue());
            client.setAddress(address.getValue());
            client.setCardNo(cardNo.getValue());
            webClientService.update(client);
            success("Profile updated!");
        });

        card.add(name, surname, email, phone, dateOfBirth, address, cardNo,
                bonusPoints, balance, save);
    }

    private void buildDriverForm(VerticalLayout card, Driver driver,
                                 WebDriverService webDriverService) {

        TextField name = field("First Name", driver.getName());
        TextField surname = field("Last Name", driver.getSurname());
        EmailField email = emailField(driver.getEmail());
        TextField phone = field("Phone Number", driver.getPhoneNum());
        DatePicker dateOfBirth = datePicker(driver.getDateOfBirth());
        TextField driverLicence = field("Driver Licence", driver.getDriverLicence());
        TextField vehiclePlate = field("Vehicle Plate", driver.getVehiclePlateNumber());

        ComboBox<VehicleType> vehicleType =
                new ComboBox<>("Vehicle Type");
        vehicleType.setItems(lt.linas_puplauskas.model.driver.VehicleType.values());
        vehicleType.setValue(driver.getVehicleType());
        vehicleType.setWidth("100%");

        Span rating = new Span("⭐ Rating: " + String.format("%.1f", driver.getRating()));
        rating.getStyle().set("font-size", "0.9rem").set("color", "var(--lumo-secondary-text-color)");

        Span deliveries = new Span("🚚 Total deliveries: " + driver.getTotalDeliveries());
        deliveries.getStyle().set("font-size", "0.9rem").set("color", "var(--lumo-secondary-text-color)");

        Span available = new Span(driver.isAvailable() ? "Currently available" : "Not available");
        available.getStyle().set("font-size", "0.9rem");

        Button save = saveButton(click -> {
            driver.setName(name.getValue());
            driver.setSurname(surname.getValue());
            driver.setEmail(email.getValue());
            driver.setPhoneNum(phone.getValue());
            driver.setDateOfBirth(dateOfBirth.getValue());
            driver.setDriverLicence(driverLicence.getValue());
            driver.setVehiclePlateNumber(vehiclePlate.getValue());
            driver.setVehicleType(vehicleType.getValue());
            webDriverService.save(driver);
            success("Profile updated!");
        });

        card.add(name, surname, email, phone, dateOfBirth,
                driverLicence, vehiclePlate, vehicleType,
                rating, deliveries, available, save);
    }

    private TextField field(String label, String value) {
        TextField f = new TextField(label);
        f.setValue(value != null ? value : "");
        f.setWidth("100%");
        return f;
    }

    private EmailField emailField(String value) {
        EmailField f = new EmailField("Email");
        f.setValue(value != null ? value : "");
        f.setWidth("100%");
        return f;
    }

    private DatePicker datePicker(java.time.LocalDate value) {
        DatePicker f = new DatePicker("Date of Birth");
        if (value != null) f.setValue(value);
        f.setWidth("100%");
        return f;
    }

    private Button saveButton(ComponentEventListener<ClickEvent<Button>> listener) {
        Button b = new Button("Save Changes", listener);
        b.setWidth("100%");
        b.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        b.getStyle().set("margin-top", "1rem");
        return b;
    }

    private void success(String message) {
        Notification n = Notification.show(message);
        n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
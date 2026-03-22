package lt.linas_puplauskas.tasty.fxControllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lt.linas_puplauskas.tasty.database.MongoConfig;
import lt.linas_puplauskas.tasty.model.restaurant.Cuisine;
import lt.linas_puplauskas.tasty.model.restaurant.Restaurant;
import lt.linas_puplauskas.tasty.model.user.User;
import lt.linas_puplauskas.tasty.model.user.UserRole;
import lt.linas_puplauskas.tasty.service.RouteService;
import lt.linas_puplauskas.tasty.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RegisterRestaurantController implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatPasswordField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextArea descriptionField;
    @FXML
    public TextField addressField;
    @FXML
    public ComboBox cuisineComboBox;
    @FXML
    public TextField openingTimeField;
    @FXML
    public TextField closingTimeField;
    @FXML
    public TextField happyHourStartField;
    @FXML
    public TextField happyHourEndField;

    private final UserService userService = new UserService(User.class);

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cuisineComboBox.setItems(FXCollections.observableArrayList(Cuisine.values()));
    }

    public void createRestaurant() throws IOException {

        validateFields();

        Restaurant restaurant = new Restaurant();

        restaurant.setUsername(usernameField.getText());
        restaurant.setPassword(passwordField.getText());
        restaurant.setAddress(addressField.getText());
        restaurant.setEmail(emailField.getText());
        restaurant.setPhoneNum(phoneField.getText());
        restaurant.setDescription(descriptionField.getText());
        restaurant.setCuisine((Cuisine) cuisineComboBox.getSelectionModel().getSelectedItem());
        restaurant.setOpeningTime(openingTimeField.getText());
        restaurant.setClosingTime(closingTimeField.getText());
        restaurant.setHappyHoursStart(happyHourStartField.getText());
        restaurant.setHappyHoursEnd(happyHourEndField.getText());
        restaurant.setRole(UserRole.RESTAURANT);

        userService.save(restaurant);
        RouteService.route("login-in-view.fxml");
    }

    private void validateFields() {

    }

    public void goBack() throws IOException {
        RouteService.route("login-in-view.fxml");
    }
}

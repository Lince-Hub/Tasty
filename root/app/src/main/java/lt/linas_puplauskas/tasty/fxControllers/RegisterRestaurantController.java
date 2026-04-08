package lt.linas_puplauskas.tasty.fxControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lt.linas_puplauskas.model.restaurant.Cuisine;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.service.UserService;
import lt.linas_puplauskas.tasty.service.RouteService;

import java.io.IOException;
import java.net.URL;
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
        RouteService.route("login-view.fxml");
    }

    private void validateFields() {

    }

    public void goBack() throws IOException {
        RouteService.route("login-view.fxml");
    }
}

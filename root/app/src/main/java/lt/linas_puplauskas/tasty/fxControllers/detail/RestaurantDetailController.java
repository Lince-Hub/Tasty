package lt.linas_puplauskas.tasty.fxControllers.detail;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lt.linas_puplauskas.model.restaurant.Cuisine;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.service.RestaurantService;
import lt.linas_puplauskas.tasty.service.RouteService;

import java.io.IOException;

public class RestaurantDetailController implements DetailController{
    public TextField usernameField;
    public TextField emailField;
    public TextField phoneField;
    public TextField addressField;
    public TextArea descriptionField;
    public ComboBox<Cuisine> cuisineComboBox;
    public TextField openingTimeField;
    public TextField closingTimeField;
    public TextField happyHourStartField;
    public TextField happyHourEndField;
    public TextField ratingField;

    private Restaurant restaurant;
    private final RestaurantService restaurantService = new RestaurantService();

    @Override
    public void setUser(User user) {
        this.restaurant = (Restaurant) restaurantService.get(user.getId());

        usernameField.setText(restaurant.getUsername());
        emailField.setText(restaurant.getEmail());
        phoneField.setText(restaurant.getPhoneNum());
        addressField.setText(restaurant.getAddress());
        descriptionField.setText(restaurant.getDescription());
        openingTimeField.setText(restaurant.getOpeningTime());
        closingTimeField.setText(restaurant.getClosingTime());
        happyHourStartField.setText(restaurant.getHappyHoursStart());
        happyHourEndField.setText(restaurant.getHappyHoursEnd());
        ratingField.setText(String.valueOf(restaurant.getRating()));
        ratingField.setEditable(false);

        cuisineComboBox.getItems().addAll(Cuisine.values());
        cuisineComboBox.setValue(restaurant.getCuisine());
    }

    public void save() {
        restaurant.setUsername(usernameField.getText());
        restaurant.setEmail(emailField.getText());
        restaurant.setPhoneNum(phoneField.getText());
        restaurant.setAddress(addressField.getText());
        restaurant.setDescription(descriptionField.getText());
        restaurant.setOpeningTime(openingTimeField.getText());
        restaurant.setClosingTime(closingTimeField.getText());
        restaurant.setHappyHoursStart(happyHourStartField.getText());
        restaurant.setHappyHoursEnd(happyHourEndField.getText());
        restaurant.setCuisine(cuisineComboBox.getValue());

        restaurantService.update(restaurant);
    }

    public void close() throws IOException {
        RouteService.route("main-view.fxml");
    }

    public void viewUserDetails() throws IOException {
        UserDetailController controller = RouteService.routeAndGetController("detail/user-detail.fxml");
        controller.setUser(restaurant);
    }
}

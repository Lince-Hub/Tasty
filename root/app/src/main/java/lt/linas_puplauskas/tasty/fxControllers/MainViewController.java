package lt.linas_puplauskas.tasty.fxControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.tasty.Application;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.tasty.fxControllers.detail.RestaurantDetailController;
import lt.linas_puplauskas.tasty.service.RouteService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainViewController implements Initializable {
    @FXML
    public Label loggedInLabel;
    @FXML
    public BorderPane borderPane;
    @FXML
    public Button usersButton;
    @FXML
    public Button menuButton;
    @FXML
    public Button statisticsButton;
    @FXML
    public Button showProfile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(Application.getCurrentUser() != null) {
            User user = Application.getCurrentUser();
            loggedInLabel.setText("Logged in as: " + user.getUsername());
            if(!(user.getRole() == UserRole.ADMIN)) {
                usersButton.setVisible(false);
                usersButton.setManaged(false);
                statisticsButton.setVisible(false);
                statisticsButton.setManaged(false);
                showProfile.setVisible(true);
            }
        }
    }

    public void showUsers() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("component/users-component.fxml"));
        borderPane.setCenter(loader.load());
    }

    public void showOrders() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("component/order-component.fxml"));
        borderPane.setCenter(loader.load());
    }

    public void showMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("component/menu-component.fxml"));
        borderPane.setCenter(loader.load());
    }

    public void showStatistics() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("component/statistics-component.fxml"));
        borderPane.setCenter(loader.load());
    }

    public void logOut() throws IOException {
        Application.setCurrentUser(null);
        RouteService.route("login-view.fxml");
    }

    public void showProfile() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("detail/restaurant-detail.fxml"));
        RestaurantDetailController restaurantDetailController = RouteService.routeAndGetController("detail/restaurant-detail.fxml");
        restaurantDetailController.setUser(Application.getCurrentUser());
        borderPane.setCenter(loader.load());
    }
}

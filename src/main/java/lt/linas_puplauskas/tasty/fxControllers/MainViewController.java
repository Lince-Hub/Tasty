package lt.linas_puplauskas.tasty.fxControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lt.linas_puplauskas.tasty.Application;
import lt.linas_puplauskas.tasty.model.restaurant.Restaurant;
import lt.linas_puplauskas.tasty.model.user.User;
import lt.linas_puplauskas.tasty.service.RouteService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainViewController {
    @FXML
    public Label loggedInLabel;
    @FXML
    public BorderPane borderPane;

    private User user;
    private Restaurant restaurant = null;

    public void setUser(User user) {
        this.user = user;
        boolean isRestaurant = user instanceof Restaurant;
        loggedInLabel.setText("Logged in as: " + user.getUsername());
        if(isRestaurant) {
            this.restaurant = (Restaurant) user;
        }
    }

    public void showUsers() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("component/users-component.fxml"));
        borderPane.setCenter(loader.load());
    }

    public void showOrders() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("component/orders-component.fxml"));
        borderPane.setCenter(loader.load());
    }

    public void showStatistics() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("component/statistics-component.fxml"));
        borderPane.setCenter(loader.load());
    }

    public void logOut() throws IOException {
        user = null;
        restaurant = null;
        RouteService.route("login-view.fxml");
    }
}

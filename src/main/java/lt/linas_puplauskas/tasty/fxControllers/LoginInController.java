package lt.linas_puplauskas.tasty.fxControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lt.linas_puplauskas.tasty.model.user.UserSearchCriteria;
import lt.linas_puplauskas.tasty.model.user.User;
import lt.linas_puplauskas.tasty.model.user.UserRole;
import lt.linas_puplauskas.tasty.service.RouteService;
import lt.linas_puplauskas.tasty.service.UserService;

import java.io.IOException;

public class LoginInController {
    private final UserService userService = new UserService(User.class);
    @FXML
    public TextField usernameTextField;
    @FXML
    public PasswordField passwordPasswordField;

    public void loginIn() throws IOException {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Info");
            alert.setContentText("Empty Fields");
            alert.showAndWait();
        } else {
            User user = userService.find(new UserSearchCriteria(username, password));

            if (user != null
                    && (user.getRole().equals(UserRole.ADMIN)
                    || user.getRole().equals(UserRole.RESTAURANT))) {
                FXMLLoader fxmlLoader = RouteService.routeAndData("main-view.fxml");
                MainViewController controller = fxmlLoader.getController();
                controller.setUser(user);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Info");
                alert.setContentText("Wrong credentials");
                alert.showAndWait();
            }
        }
    }

    public void routeToRegister() throws IOException {
        RouteService.route("register-restaurant-view.fxml");
    }
}

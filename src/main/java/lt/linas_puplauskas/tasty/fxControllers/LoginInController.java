package lt.linas_puplauskas.tasty.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lt.linas_puplauskas.tasty.model.order.UserSearchCriteria;
import lt.linas_puplauskas.tasty.model.user.User;
import lt.linas_puplauskas.tasty.model.user.UserRole;
import lt.linas_puplauskas.tasty.service.RouteService;
import lt.linas_puplauskas.tasty.service.UserService;

import java.io.IOException;

public class LoginInController {
    @FXML
    public TextField usernameTextField;
    @FXML
    public PasswordField passwordPasswordField;

    private final UserService userService = new UserService(User.class);

    public void loginIn() throws IOException {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        User user = userService.find(new UserSearchCriteria(username, password));

        if (user != null) {
            if(user.getRole().equals(UserRole.ADMIN)) {
                RouteService.route("main-view-admin.fxml");
            } else if (user.getRole().equals(UserRole.RESTAURANT)){
                RouteService.route("main-view-restaurant.fxml");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Info");
            alert.setContentText("Wrong credentials");
            alert.showAndWait();
        }
    }

    public void routeToRegister() throws IOException {
        RouteService.route("register-restaurant-view.fxml");
    }
}

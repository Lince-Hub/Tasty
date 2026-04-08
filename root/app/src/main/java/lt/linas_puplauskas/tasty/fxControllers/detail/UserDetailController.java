package lt.linas_puplauskas.tasty.fxControllers.detail;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.service.UserService;
import lt.linas_puplauskas.tasty.service.RouteService;

import java.io.IOException;
import java.time.LocalDateTime;

public class UserDetailController implements DetailController{
    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField phoneField;
    @FXML
    public ComboBox<UserRole> roleField;
    @FXML
    public TextField dateCreatedField;

    private User user;
    private UserService userService = new UserService(User.class);

    @Override
    public void setUser(User user) {
        this.user = user;

        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhoneNum());
        roleField.getItems().addAll(UserRole.values());
        roleField.setValue(user.getRole());
        dateCreatedField.setText(user.getDateCreated().toString());
    }

    public void save() {
        user.setId(user.getId());
        user.setUsername(usernameField.getText());
        user.setPassword(passwordField.getText());
        user.setEmail(emailField.getText());
        user.setPhoneNum(phoneField.getText());
        user.setRole(roleField.getValue());
        user.setDateCreated(LocalDateTime.parse(dateCreatedField.getText()));

        userService.update(user);
    }

    public void close() throws IOException {
        RouteService.route("main-view.fxml");
    }
}

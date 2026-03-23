package lt.linas_puplauskas.tasty.fxControllers.component;

import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lt.linas_puplauskas.tasty.model.user.User;
import lt.linas_puplauskas.tasty.service.UserService;

import java.net.URL;
import java.util.ResourceBundle;

public class UsersComponentController implements Initializable {
    public TableView<User> usersTable;
    public TableColumn<User, String> idColumn;
    public TableColumn<User, String> usernameColumn;
    public TableColumn<User, String> passwordColumn;
    public TableColumn<User, String> emailColumn;
    public TableColumn<User, String> phoneNumColumn;
    public TableColumn<User, String> roleColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        UserService userService = new UserService(User.class);
        usersTable.getItems().addAll(userService.findAll());
    }
}

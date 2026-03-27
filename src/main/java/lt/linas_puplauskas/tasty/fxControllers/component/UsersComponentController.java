package lt.linas_puplauskas.tasty.fxControllers.component;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lt.linas_puplauskas.tasty.Application;
import lt.linas_puplauskas.tasty.fxControllers.detail.DetailController;
import lt.linas_puplauskas.tasty.model.client.Client;
import lt.linas_puplauskas.tasty.model.driver.Driver;
import lt.linas_puplauskas.tasty.model.restaurant.Restaurant;
import lt.linas_puplauskas.tasty.model.user.User;
import lt.linas_puplauskas.tasty.model.user.UserRole;
import lt.linas_puplauskas.tasty.model.user.UserSearchCriteria;
import lt.linas_puplauskas.tasty.service.RouteService;
import lt.linas_puplauskas.tasty.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UsersComponentController implements Initializable {
    @FXML
    public TableView<User> usersTable;
    @FXML
    public TableColumn<User, String> idColumn;
    @FXML
    public TableColumn<User, String> usernameColumn;
    @FXML
    public TableColumn<User, String> passwordColumn;
    @FXML
    public TableColumn<User, String> emailColumn;
    @FXML
    public TableColumn<User, String> phoneNumColumn;
    @FXML
    public TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, Void> actionsColumn;
    private final UserService userService = new UserService(User.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        actionsColumn.setCellFactory(col -> new TableCell<>(){
            private final Button viewBtn = new Button("\uD83D\uDC41");
            private final Button deleteBtn = new Button("✖");
            private final HBox buttons = new HBox(5, viewBtn, deleteBtn);

            {
                viewBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    try {
                        openDetailView(user);
                    } catch (Exception ex) {
                       ex.printStackTrace();
                    }
                });

                deleteBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    userService.remove(new UserSearchCriteria(user.getId()));
                    getTableView().getItems().remove(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        usersTable.getItems().addAll(userService.findAll());
    }

    private void openDetailView(User user) throws IOException {
        String fxml;
        if (user.getRole().equals(UserRole.RESTAURANT)) {
            fxml = "detail/restaurant-detail.fxml";
        } else if (user.getRole().equals(UserRole.CLIENT)) {
            fxml = "detail/client-detail.fxml";
        } else if (user.getRole().equals(UserRole.DRIVER)) {
            fxml = "detail/driver-detail.fxml";
        } else {
            fxml = "detail/user-detail.fxml";
        }

        FXMLLoader loader = RouteService.routeAndData(fxml); // your service
        DetailController controller = loader.getController();
        controller.setUser(user);
    }
}

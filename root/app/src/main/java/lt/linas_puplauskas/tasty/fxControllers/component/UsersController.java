package lt.linas_puplauskas.tasty.fxControllers.component;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.model.user.UserSearchCriteria;
import lt.linas_puplauskas.tasty.fxControllers.detail.DetailController;
import lt.linas_puplauskas.service.UserService;
import lt.linas_puplauskas.tasty.service.RouteService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController implements Initializable {

    @FXML public TableView<User> usersTable;
    @FXML public TableColumn<User, String> idColumn;
    @FXML public TableColumn<User, String> usernameColumn;
    @FXML public TableColumn<User, String> passwordColumn;
    @FXML public TableColumn<User, String> emailColumn;
    @FXML public TableColumn<User, String> phoneNumColumn;
    @FXML public TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, Void> actionsColumn;
    @FXML private TextField searchField;
    @FXML private ComboBox<UserRole> roleFilterCombo;

    private final UserService userService = new UserService(User.class);
    private List<User> allUsers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupColumns();
        setupFilters();
        loadUsers();
    }

    private void setupColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        actionsColumn.setCellFactory(col -> new TableCell<>() {
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
                    allUsers.remove(user);
                    getTableView().getItems().remove(user);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    private void setupFilters() {
        roleFilterCombo.getItems().add(null);
        roleFilterCombo.getItems().addAll(UserRole.values());

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        roleFilterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void loadUsers() {
        allUsers = userService.findAll(new UserSearchCriteria());
        usersTable.getItems().setAll(allUsers);
    }

    private void applyFilters() {
        String search = searchField.getText().toLowerCase();
        UserRole selectedRole = roleFilterCombo.getValue();

        List<User> filtered = allUsers.stream()
                .filter(u -> selectedRole == null || u.getRole().equals(selectedRole))
                .filter(u -> search.isBlank()
                        || u.getUsername().toLowerCase().contains(search)
                        || u.getEmail().toLowerCase().contains(search))
                .toList();

        usersTable.getItems().setAll(filtered);
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

        DetailController controller = RouteService.routeAndGetController(fxml);
        controller.setUser(user);
    }
}

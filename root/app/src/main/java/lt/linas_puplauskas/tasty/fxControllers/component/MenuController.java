package lt.linas_puplauskas.tasty.fxControllers.component;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import lt.linas_puplauskas.model.restaurant.Dish;
import lt.linas_puplauskas.model.restaurant.DishCategory;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.model.restaurant.RestaurantSearchCriteria;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.model.user.UserSearchCriteria;
import lt.linas_puplauskas.service.RestaurantService;
import lt.linas_puplauskas.tasty.Application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private ComboBox<Restaurant> restaurantComboBox;
    @FXML
    private TableView<Dish> dishTable;
    @FXML
    private TableColumn<Dish, String> titleCol;
    @FXML
    private TableColumn<Dish, DishCategory> categoryCol;
    @FXML
    private TableColumn<Dish, Double> priceCol;
    @FXML
    private TableColumn<Dish, Integer> prepTimeCol;
    @FXML
    private TableColumn<Dish, Integer> caloriesCol;
    @FXML
    private TableColumn<Dish, Float> weightCol;
    @FXML
    private TableColumn<Dish, Boolean> availableCol;
    @FXML
    public TableColumn<Dish, String> allergensCol;
    @FXML
    public TableColumn<Dish, Integer> amountCol;
    @FXML
    public TableColumn<Dish, String> imageUrlCol;
    @FXML
    private TableColumn<Dish, Void> actionsCol;
    @FXML
    private VBox dishFormPane;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<DishCategory> categoryCombo;
    @FXML
    private TextField prepTimeField;
    @FXML
    private TextField caloriesField;
    @FXML
    private TextField weightField;
    @FXML
    public TextField allergensField;
    @FXML
    public TextField amountField;
    @FXML
    public TextField imageUrlField;
    @FXML
    private CheckBox availableCheck;

    private final RestaurantService restaurantService = new RestaurantService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadRestaurants();
        setupTableColumns();
        categoryCombo.getItems().setAll(DishCategory.values());
    }

    private void loadRestaurants() {
        User user = Application.getCurrentUser();

        if (user.getRole() == UserRole.RESTAURANT) {
            Restaurant selectedRestaurant = (Restaurant) restaurantService.findFirst(
                    new UserSearchCriteria(user.getId())
            );
            restaurantComboBox.getItems().setAll(selectedRestaurant);
            restaurantComboBox.setValue(selectedRestaurant);
            restaurantComboBox.setDisable(true);
            onSelectRestaurant();
        } else {
            List<Restaurant> restaurants = restaurantService.findAll(
                    new RestaurantSearchCriteria(UserRole.RESTAURANT)
            );
            restaurantComboBox.getItems().setAll(restaurants);
        }
    }

    private void setupTableColumns() {
        dishTable.setEditable(true);

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        titleCol.setOnEditCommit(e -> {
            e.getRowValue().setTitle(e.getNewValue());
            saveRestaurant();
        });

        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setCellFactory(ComboBoxTableCell.forTableColumn(DishCategory.values()));
        categoryCol.setOnEditCommit(e -> {
            e.getRowValue().setCategory(e.getNewValue());
            saveRestaurant();
        });

        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceCol.setOnEditCommit(e -> {
            e.getRowValue().setPrice(e.getNewValue());
            saveRestaurant();
        });

        prepTimeCol.setCellValueFactory(new PropertyValueFactory<>("preparationTimeMin"));
        prepTimeCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        prepTimeCol.setOnEditCommit(e -> {
            e.getRowValue().setPreparationTimeMin(e.getNewValue());
            saveRestaurant();
        });

        caloriesCol.setCellValueFactory(new PropertyValueFactory<>("calories"));
        caloriesCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        caloriesCol.setOnEditCommit(e -> {
            e.getRowValue().setCalories(e.getNewValue());
            saveRestaurant();
        });

        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        weightCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        weightCol.setOnEditCommit(e -> {
            e.getRowValue().setWeight(e.getNewValue());
            saveRestaurant();
        });

        availableCol.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isAvailable())
        );
        availableCol.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    Dish dish = getTableView().getItems().get(getIndex());
                    dish.setAvailable(checkBox.isSelected());
                    saveRestaurant();
                });
            }

            @Override
            protected void updateItem(Boolean value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(value);
                    setGraphic(checkBox);
                }
            }
        });

        allergensCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getAllergens() != null
                                ? String.join(", ", cellData.getValue().getAllergens())
                                : ""
                )
        );
        allergensCol.setCellFactory(TextFieldTableCell.forTableColumn());
        allergensCol.setOnEditCommit(e -> {
            String raw = e.getNewValue();
            List<String> allergens = raw.isBlank()
                    ? List.of()
                    : List.of(raw.split(",\\s*"));
            e.getRowValue().setAllergens(allergens);
            saveRestaurant();
        });

        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        amountCol.setOnEditCommit(e -> {
            e.getRowValue().setAmount(e.getNewValue());
            saveRestaurant();
        });

        imageUrlCol.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        imageUrlCol.setCellFactory(TextFieldTableCell.forTableColumn());
        imageUrlCol.setOnEditCommit(e -> {
            e.getRowValue().setImageUrl(e.getNewValue());
            saveRestaurant();
        });

        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("✖");
            private final HBox buttons = new HBox(5, deleteBtn);

            {
                deleteBtn.setOnAction(e -> {
                    Dish dish = getTableView().getItems().get(getIndex());
                    Restaurant selected = restaurantComboBox.getValue();
                    selected.getMenu().remove(dish);
                    saveRestaurant();
                    dishTable.getItems().remove(dish);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    private void saveRestaurant() {
        Restaurant selected = restaurantComboBox.getValue();
        if (selected != null) {
            restaurantService.update(selected);
        }
    }

    @FXML
    private void onSelectRestaurant() {
        Restaurant selected = restaurantComboBox.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        dishTable.getItems().clear();
        if (selected.getMenu() != null) {
            dishTable.getItems().setAll(selected.getMenu());
        }
    }

    @FXML
    private void onToggleDishForm() {
        if (restaurantComboBox.getSelectionModel().getSelectedItem() == null) return;
        boolean visible = dishFormPane.isVisible();
        dishFormPane.setVisible(!visible);
        dishFormPane.setManaged(!visible);
        if (!visible) clearForm();
    }

    @FXML
    private void onAddDish() {
        Restaurant selected = restaurantComboBox.getValue();
        if (selected == null) return;

        Dish dish = new Dish();
        dish.setTitle(titleField.getText());
        dish.setDescription(descriptionField.getText());
        dish.setPrice(Double.parseDouble(priceField.getText()));
        dish.setCategory(categoryCombo.getValue());
        dish.setPreparationTimeMin(Integer.parseInt(prepTimeField.getText()));
        dish.setCalories(Integer.parseInt(caloriesField.getText()));
        dish.setWeight(Float.parseFloat(weightField.getText()));
        dish.setAvailable(availableCheck.isSelected());
        dish.setAllergens(
                allergensField.getText().isBlank()
                        ? List.of()
                        : List.of(allergensField.getText().split(",\\s*"))
        );
        dish.setAmount(Integer.parseInt(amountField.getText()));
        dish.setImageUrl(imageUrlField.getText());

        selected.getMenu().add(dish);
        restaurantService.update(selected);
        dishTable.getItems().add(dish);

        onToggleDishForm();
    }

    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        priceField.clear();
        categoryCombo.setValue(null);
        prepTimeField.clear();
        caloriesField.clear();
        weightField.clear();
        availableCheck.setSelected(false);
        allergensField.clear();
        amountField.clear();
        imageUrlField.clear();
    }
}

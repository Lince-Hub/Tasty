package lt.linas_puplauskas.tasty.fxControllers.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lt.linas_puplauskas.model.driver.Message;
import lt.linas_puplauskas.model.driver.VehicleType;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.order.OrderSearchCriteria;
import lt.linas_puplauskas.model.order.OrderStatus;
import lt.linas_puplauskas.model.restaurant.Dish;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.model.restaurant.RestaurantSearchCriteria;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.service.OrderService;
import lt.linas_puplauskas.service.RestaurantService;
import lt.linas_puplauskas.tasty.fxControllers.component.order.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML
    public TextField searchField;
    @FXML
    public ComboBox<OrderStatus> statusFilterCombo;
    @FXML
    public TabPane tabPane;

    @FXML
    private TableView<Order> orderTable;
    @FXML
    public TableColumn<Order, String> idColumn;
    @FXML
    public TableColumn<Order, String> clientColumn;
    @FXML
    public TableColumn<Order, String> restaurantColumn;
    @FXML
    public TableColumn<Order, String> statusColumn;
    @FXML
    public TableColumn<Order, Double> totalPriceColumn;
    @FXML
    public TableColumn<Order, String> paymentMethodColumn;
    @FXML
    public TableColumn<Order, String> createdAtColumn;
    @FXML
    public TableColumn<Order, String> deliveredAtColumn;
    @FXML
    public TableColumn<Order, Integer> deliveryFeeColumn;
    @FXML
    public TableColumn<Order, Integer> estDeliveryColumn;
    @FXML
    public TableColumn<Order, Void> actionsColumn;
    @FXML
    private ComboBox<Restaurant> restaurantComboBox;

    @FXML
    private Label orderIdLabel;

    @FXML
    private ComboBox<OrderStatus> statusCombo;
    @FXML
    private TextField paymentMethodField;
    @FXML
    private TextField totalPriceField;
    @FXML
    private TextField deliveryFeeField;
    @FXML
    private TextField estimatedDeliveryField;
    @FXML
    private TextField deliveredAtField;
    @FXML
    private TextArea specialInstructionsArea;

    @FXML
    private TextField clientNameField;
    @FXML
    private TextField clientSurnameField;
    @FXML
    private TextField clientEmailField;
    @FXML
    private TextField clientPhoneField;
    @FXML
    private TextField clientAddressField;
    @FXML
    private TextField clientBalanceField;
    @FXML
    private TextField clientBonusField;

    @FXML
    private TextField driverNameField;
    @FXML
    private TextField driverSurnameField;
    @FXML
    private TextField driverPhoneField;
    @FXML
    private TextField driverLicenceField;
    @FXML
    private ComboBox<VehicleType> driverVehicleCombo;
    @FXML
    private TextField driverPlateField;
    @FXML
    private TextField driverRatingField;
    @FXML
    private CheckBox driverAvailableCheck;
    @FXML
    private TableView<Dish> itemsTable;

    @FXML
    private TableColumn<Dish, String> itemTitleCol;
    @FXML
    private TableColumn<Dish, String> itemCategoryCol;
    @FXML
    private TableColumn<Dish, Integer> itemPriceCol;
    @FXML
    private TableColumn<Dish, Integer> itemAmountCol;
    @FXML
    private TableColumn<Dish, Integer> itemCalCol;
    @FXML
    private TableColumn<Dish, Float> itemWeightCol;

    @FXML
    private TextField reviewTitleField;
    @FXML
    private Spinner<Integer> reviewRatingSpinner;
    @FXML
    private TextField reviewCreatedAtField;
    @FXML
    private TextArea reviewCommentArea;

    @FXML
    private TableView<Message> messagesTable;
    @FXML
    private TableColumn<Message, String> msgSenderCol;
    @FXML
    private TableColumn<Message, String> msgReceiverCol;
    @FXML
    private TableColumn<Message, String> msgContentCol;
    @FXML
    private TableColumn<Message, String> msgSentAtCol;
    @FXML
    private TableColumn<Message, Boolean> msgReadCol;
    @FXML
    private TableColumn<Message, Void> msgActionsCol;

    @FXML
    private VBox messageFormPane;
    @FXML
    private ComboBox<User> msgSenderCombo;
    @FXML
    private ComboBox<User> msgReceiverCombo;
    @FXML
    private TextArea msgContentField;

    private OrderFormHandler orderHandler;
    private ClientFormHandler clientHandler;
    private DriverFormHandler driverHandler;
    private ItemHandler itemHandler;
    private ReviewHandler reviewHandler;
    private MessageHandler messageHandler;

    private final OrderService orderService = new OrderService(Order.class);
    private final RestaurantService restaurantService = new RestaurantService();

    private Restaurant selectedRestaurant;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tabPane.setDisable(true);
        initStatusSearch();
        loadRestaurants();
        setupTableSelection();

        orderHandler = new OrderFormHandler(
                orderIdLabel,
                statusCombo,
                paymentMethodField,
                totalPriceField,
                deliveryFeeField,
                estimatedDeliveryField,
                deliveredAtField,
                specialInstructionsArea
        );

        clientHandler = new ClientFormHandler(
                clientNameField,
                clientSurnameField,
                clientEmailField,
                clientPhoneField,
                clientAddressField,
                clientBalanceField,
                clientBonusField
        );

        driverHandler = new DriverFormHandler(
                driverNameField,
                driverSurnameField,
                driverPhoneField,
                driverLicenceField,
                driverVehicleCombo,
                driverPlateField,
                driverRatingField,
                driverAvailableCheck
        );

        itemHandler = new ItemHandler(
                itemsTable,
                itemTitleCol,
                itemCategoryCol,
                itemPriceCol,
                itemAmountCol,
                itemCalCol,
                itemWeightCol
        );

        reviewHandler = new ReviewHandler(
                reviewTitleField,
                reviewRatingSpinner,
                reviewCreatedAtField,
                reviewCommentArea
        );

        messageHandler = new MessageHandler(
                messagesTable,
                msgSenderCol,
                msgReceiverCol,
                msgContentCol,
                msgSentAtCol,
                msgReadCol,
                msgSenderCombo,
                msgReceiverCombo,
                msgContentField,
                messageFormPane
        );
    }

    private void loadRestaurants() {
        List<Restaurant> restaurantList = restaurantService.findAll(new RestaurantSearchCriteria(UserRole.RESTAURANT));
        restaurantComboBox.getItems().setAll(restaurantList);
    }

    private void setupTableSelection() {
        orderTable.getSelectionModel().selectedItemProperty().addListener((_, _, order) -> {
            if (order != null){
                fillForm(order);
                tabPane.setDisable(false);
            }
        });
    }

    private void fillForm(Order order) {
        orderHandler.fill(order);
        clientHandler.fill(order.getBuyer());
        driverHandler.fill(order.getDeliveryPerson());
        itemHandler.fill(order.getItems());
        reviewHandler.fill(order.getReview());
        messageHandler.fill(order.getMessages());
    }

    @FXML
    private void onSave() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Order Selected");
            alert.setContentText("Please select an order from the table to save changes.");
            alert.showAndWait();
            return;
        }

        orderHandler.update(selectedOrder);

        if (selectedOrder.getBuyer() != null) {
            clientHandler.update(selectedOrder.getBuyer());
        }

        if (selectedOrder.getDeliveryPerson() != null) {
            driverHandler.update(selectedOrder.getDeliveryPerson());
        }

        if (selectedOrder.getReview() != null) {
            reviewHandler.update(selectedOrder.getReview());
        }

        orderService.update(selectedOrder);
        orderTable.refresh();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Order Updated");
        alert.setContentText("Order #" + selectedOrder.getId() + " has been successfully updated.");
    }

    @FXML
    private void onSelectRestaurant() {
        selectedRestaurant = restaurantComboBox.getSelectionModel().getSelectedItem();
        initOrderTable();
    }

    @FXML
    private void onToggleMessageForm() {
        messageHandler.toggleForm();
    }

    @FXML
    private void onSendMessage() {

        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        Message msg = messageHandler.buildMessage();

        if (msg == null) {
            System.out.println("Cannot send message - missing fields");
            return;
        }

        selectedOrder.getMessages().add(msg);
        orderService.update(selectedOrder);
        messageHandler.addMessage(msg);
    }

    @FXML
    private void onClearReview() {
        reviewHandler.clear();
    }

    private void initOrderTable() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        clientColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getBuyer() != null
                                ? data.getValue().getBuyer().toString()
                                : ""
                )
        );

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        deliveredAtColumn.setCellValueFactory(new PropertyValueFactory<>("deliveredAt"));

        deliveryFeeColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryFee"));

        estDeliveryColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedDeliveryMin"));

        actionsColumn.setCellFactory(col -> new TableCell<>() {

            private final Button deleteBtn = new Button("✖");
            private final HBox buttons = new HBox(5, deleteBtn);

            {
                deleteBtn.setOnAction(e -> {
                    Order order = getTableView().getItems().get(getIndex());

                    orderService.remove(new OrderSearchCriteria(order.getId()));

                    getTableView().getItems().remove(order);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        orderTable.getItems().clear();

        List<Order> orders = orderService.findAll(
                new OrderSearchCriteria(selectedRestaurant)
        );

        orderTable.getItems().addAll(orders);
        orderTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        orderTable.setFixedCellSize(32);
    }

    private void initStatusSearch() {
        statusFilterCombo.getItems().setAll(OrderStatus.values());
    }
}
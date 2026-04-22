package lt.linas_puplauskas.tasty.fxControllers.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lt.linas_puplauskas.model.client.Client;
import lt.linas_puplauskas.model.driver.Driver;
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
import lt.linas_puplauskas.model.user.UserSearchCriteria;
import lt.linas_puplauskas.service.ClientService;
import lt.linas_puplauskas.service.DriverService;
import lt.linas_puplauskas.service.OrderService;
import lt.linas_puplauskas.service.RestaurantService;
import lt.linas_puplauskas.tasty.Application;
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
    public TableColumn<Order, Double> deliveryFeeColumn;
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
    private TableColumn<Dish, Double> itemPriceCol;
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
    private final ClientService clientService = new ClientService();
    private final DriverService driverService = new DriverService();

    private Restaurant selectedRestaurant;
    private List<Order> allOrders;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabPane.setDisable(true);
        initStatusSearch();
        setupTableSelection();

        User user = Application.getCurrentUser();

        if (user.getRole() != UserRole.ADMIN) {
            restaurantComboBox.setVisible(false);
            restaurantComboBox.setManaged(false);

            selectedRestaurant = (Restaurant) restaurantService.findFirst(
                    new UserSearchCriteria(user.getId())
            );
            initOrderTable();

            paymentMethodField.setEditable(false);
            totalPriceField.setEditable(false);
            deliveryFeeField.setEditable(false);
            estimatedDeliveryField.setEditable(false);
            deliveredAtField.setEditable(false);
            specialInstructionsArea.setEditable(false);

            tabPane.getTabs().get(1).setDisable(true);
            tabPane.getTabs().get(2).setDisable(true);
            tabPane.getTabs().get(4).setDisable(true);
            tabPane.getTabs().get(5).setDisable(true);

            actionsColumn.setVisible(false);

        } else {
            loadRestaurants();
        }

        orderHandler = new OrderFormHandler(
                orderIdLabel, statusCombo, paymentMethodField,
                totalPriceField, deliveryFeeField, estimatedDeliveryField,
                deliveredAtField, specialInstructionsArea
        );

        clientHandler = new ClientFormHandler(
                clientNameField, clientSurnameField, clientEmailField,
                clientPhoneField, clientAddressField, clientBalanceField, clientBonusField
        );

        driverHandler = new DriverFormHandler(
                driverNameField, driverSurnameField, driverPhoneField,
                driverLicenceField, driverVehicleCombo, driverPlateField,
                driverRatingField, driverAvailableCheck
        );

        itemHandler = new ItemHandler(
                itemsTable, itemTitleCol, itemCategoryCol,
                itemPriceCol, itemAmountCol, itemCalCol, itemWeightCol
        );

        reviewHandler = new ReviewHandler(
                reviewTitleField, reviewRatingSpinner,
                reviewCreatedAtField, reviewCommentArea
        );

        messageHandler = new MessageHandler(
                messagesTable, msgSenderCol, msgReceiverCol,
                msgContentCol, msgSentAtCol, msgReadCol,
                msgSenderCombo, msgReceiverCombo, msgContentField, messageFormPane
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

        Client client = (Client) clientService.get(order.getClientId());
        clientHandler.fill(client);

        Driver driver = (Driver) driverService.get(order.getDriverId());
        driverHandler.fill(driver);

        itemHandler.fill(order.getItems());
        reviewHandler.fill(order.getReview());
        messageHandler.fill(order.getMessages());
    }

    @FXML
    private void onSave() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an order.").showAndWait();
            return;
        }

        User user = Application.getCurrentUser();
        Client client = (Client) clientService.get(selectedOrder.getClientId());
        Driver driver = (Driver) driverService.get(selectedOrder.getDriverId());

        if (user.getRole() != UserRole.ADMIN) {
            selectedOrder.setStatus(statusCombo.getValue());
        } else {
            orderHandler.update(selectedOrder);
            if (client != null) clientHandler.update(client);
            if (driver != null) driverHandler.update(driver);
            if (selectedOrder.getReview() != null) reviewHandler.update(selectedOrder.getReview());
        }

        orderService.update(selectedOrder);
        orderTable.refresh();

        new Alert(Alert.AlertType.INFORMATION, "Order #" + selectedOrder.getId() + " updated.").showAndWait();
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

                        data.getValue().getClientId() != null
                                ? clientService.get(data.getValue().getClientId()).toString()
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

        allOrders = orderService.findAll(new OrderSearchCriteria(selectedRestaurant.getId()));
        orderTable.getItems().addAll(allOrders);
        orderTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        orderTable.setFixedCellSize(32);
    }

    private void initStatusSearch() {
        statusFilterCombo.getItems().add(null);
        statusFilterCombo.getItems().addAll(OrderStatus.values());

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        statusFilterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void applyFilters() {
        if (allOrders == null) return;

        String search = searchField.getText().toLowerCase();
        OrderStatus selectedStatus = statusFilterCombo.getValue();

        List<Order> filtered = allOrders.stream()
                .filter(o -> selectedStatus == null || o.getStatus().equals(selectedStatus))
                .filter(o -> search.isBlank()
                        || (clientService.get(o.getClientId()) != null && clientService.get(o.getClientId()).toString().toLowerCase().contains(search))
                        || (o.getId() != null && o.getId().toString().toLowerCase().contains(search))
                        || (o.getPaymentMethod() != null && o.getPaymentMethod().toLowerCase().contains(search)))
                .toList();

        orderTable.getItems().setAll(filtered);
    }

    public void selectSender() {
        messageHandler.onSelection();
    }
}
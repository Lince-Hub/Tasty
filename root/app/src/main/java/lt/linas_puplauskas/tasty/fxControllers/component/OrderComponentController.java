package lt.linas_puplauskas.tasty.fxControllers.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.service.OrderService;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderComponentController implements Initializable {
    @FXML
    public TableView<Order> orderTable;
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

    OrderService orderService = new OrderService(Order.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getBuyer().toString()));
        restaurantColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRestaurant().toString()));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        orderTable.getItems().addAll(orderService.findAll());
    }
}

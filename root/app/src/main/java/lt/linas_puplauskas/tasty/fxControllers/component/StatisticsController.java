package lt.linas_puplauskas.tasty.fxControllers.component;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.order.OrderSearchCriteria;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserSearchCriteria;
import lt.linas_puplauskas.service.OrderService;
import lt.linas_puplauskas.service.UserService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {

    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label totalRevenueLabel;

    private final UserService userService = new UserService(User.class);
    private final OrderService orderService = new OrderService(Order.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadStatistics();
    }

    private void loadStatistics() {
        List<Order> orders = orderService.findAll(new OrderSearchCriteria());
        double revenue = orders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();

        int totalUsers = userService.findAll(new UserSearchCriteria()).size();
        int totalOrders = orders.size();

        updateUI(totalUsers, totalOrders, revenue);
    }

    private void updateUI(int users, int orders, double revenue) {
        totalUsersLabel.setText(String.valueOf(users));
        totalOrdersLabel.setText(String.valueOf(orders));
        totalRevenueLabel.setText(String.format("%.2f", revenue));
    }
}
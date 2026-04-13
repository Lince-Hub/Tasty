package lt.linas_puplauskas.tasty.fxControllers.component.order;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.order.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderFormHandler {

    private final Label orderIdLabel;
    private final ComboBox<OrderStatus> statusCombo;
    private final TextField paymentMethodField;
    private final TextField totalPriceField;
    private final TextField deliveryFeeField;
    private final TextField estimatedDeliveryField;
    private final TextField deliveredAtField;
    private final TextArea specialInstructionsArea;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public OrderFormHandler(
            Label orderIdLabel,
            ComboBox<OrderStatus> statusCombo,
            TextField paymentMethodField,
            TextField totalPriceField,
            TextField deliveryFeeField,
            TextField estimatedDeliveryField,
            TextField deliveredAtField,
            TextArea specialInstructionsArea
    ) {
        this.orderIdLabel = orderIdLabel;
        this.statusCombo = statusCombo;
        this.paymentMethodField = paymentMethodField;
        this.totalPriceField = totalPriceField;
        this.deliveryFeeField = deliveryFeeField;
        this.estimatedDeliveryField = estimatedDeliveryField;
        this.deliveredAtField = deliveredAtField;
        this.specialInstructionsArea = specialInstructionsArea;

        statusCombo.getItems().setAll(OrderStatus.values());
    }

    public void fill(Order order) {
        if (order == null) {
            clear();
            return;
        }

        orderIdLabel.setText(order.getId() != null ? order.getId().toHexString() : "New Order");
        statusCombo.setValue(order.getStatus());
        paymentMethodField.setText(order.getPaymentMethod());
        totalPriceField.setText(String.valueOf(order.getTotalPrice()));
        deliveryFeeField.setText(String.valueOf(order.getDeliveryFee()));
        estimatedDeliveryField.setText(String.valueOf(order.getEstimatedDeliveryMin()));

        if (order.getDeliveredAt() != null) {
            deliveredAtField.setText(order.getDeliveredAt().format(DATE_FORMATTER));
        } else {
            deliveredAtField.clear();
        }

        specialInstructionsArea.setText(order.getSpecialInstructions());
    }

    public void update(Order order) {
        if (order == null) return;

        order.setStatus(statusCombo.getValue());
        order.setPaymentMethod(paymentMethodField.getText());

        try {
            order.setTotalPrice(Integer.parseInt(totalPriceField.getText()));
        } catch (NumberFormatException e) {
            order.setTotalPrice(0);
        }

        try {
            order.setDeliveryFee(Integer.parseInt(deliveryFeeField.getText()));
        } catch (NumberFormatException e) {
            order.setDeliveryFee(0);
        }

        try {
            order.setEstimatedDeliveryMin(Integer.parseInt(estimatedDeliveryField.getText()));
        } catch (NumberFormatException e) {
            order.setEstimatedDeliveryMin(30);
        }

        order.setSpecialInstructions(specialInstructionsArea.getText());
    }

    public void clear() {
        orderIdLabel.setText("No order selected");
        statusCombo.setValue(null);
        paymentMethodField.clear();
        totalPriceField.clear();
        deliveryFeeField.clear();
        estimatedDeliveryField.clear();
        deliveredAtField.clear();
        specialInstructionsArea.clear();
    }

    public Order createNewOrder() {
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        return order;
    }
}
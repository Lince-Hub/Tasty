package lt.linas_puplauskas.ui.view.driver;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lt.linas_puplauskas.model.driver.Driver;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.order.OrderSearchCriteria;
import lt.linas_puplauskas.model.order.OrderStatus;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.service.WebOrderService;
import lt.linas_puplauskas.service.auth.AuthService;
import lt.linas_puplauskas.service.user.WebRestaurantService;
import lt.linas_puplauskas.ui.MainLayout;

import java.util.List;

@Route(value = "orders", layout = MainLayout.class)
@PageTitle("Available Orders – Tasty")
public class DriverOrdersView extends VerticalLayout {

    private final WebOrderService webOrderService;
    private final WebRestaurantService webRestaurantService;
    private final AuthService authService;
    private Boolean hasClaimed = false;
    private Order selectedOrder = null;
    private final VerticalLayout orderListLayout = new VerticalLayout();

    public DriverOrdersView(WebOrderService webOrderService, WebRestaurantService webRestaurantService, AuthService authService) {
        this.webOrderService = webOrderService;
        this.webRestaurantService = webRestaurantService;
        this.authService = authService;

        setAlignItems(Alignment.CENTER);
        getStyle().set("min-height", "100vh").set("padding", "2rem 0");
        getStyle().set("background", "var(--lumo-contrast-5pct)");

        VerticalLayout card = new VerticalLayout();
        card.setWidth("660px");
        card.setSpacing(false);
        card.setPadding(false);
        card.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "16px")
                .set("box-shadow", "0 4px 32px rgba(0,0,0,0.12)")
                .set("padding", "2rem")
                .set("box-sizing", "border-box");

        Span emoji = new Span("🚗");
        emoji.getStyle().set("font-size", "2.5rem");

        H2 title = new H2("Available Orders");
        title.getStyle().set("margin", "0.5rem 0 0.25rem 0").set("font-size", "1.6rem");

        Paragraph subtitle = new Paragraph("Select an order to claim it for delivery.");
        subtitle.addClassNames(LumoUtility.TextColor.SECONDARY);
        subtitle.getStyle().set("margin", "0 0 1rem 0");

        Hr divider = new Hr();
        divider.getStyle().set("margin", "0.75rem 0");

        orderListLayout.setPadding(false);
        orderListLayout.setSpacing(true);
        orderListLayout.setWidthFull();

        card.add(emoji, title, subtitle, divider, orderListLayout);
        add(card);

        loadOrders();
    }

    private void loadOrders() {
        orderListLayout.removeAll();

        Driver driver = authService.getCurrentDriver();

        List<Order> allOrders = webOrderService.findAll(new OrderSearchCriteria());

        // Find this driver's active order if any
        Order activeOrder = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.IN_DELIVERY
                        && driver != null
                        && driver.getId().equals(o.getDriverId()))
                .findFirst()
                .orElse(null);

        List<Order> readyOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.READY_FOR_PICKUP)
                .toList();

        if (activeOrder != null) {
            hasClaimed = true;
            selectedOrder = activeOrder;

            // Show active order card with delivered button
            H3 activeTitle = new H3("Your Current Delivery");
            activeTitle.getStyle().set("margin", "0 0 0.5rem 0");
            orderListLayout.add(activeTitle);

            VerticalLayout activeCard = orderCard(activeOrder, readyOrders);
            activeCard.getStyle()
                    .set("border-color", "#16a34a")
                    .set("border-width", "2px")
                    .set("background", "#f0fdf4");

            activeCard.remove(activeCard.getComponentAt(activeCard.getComponentCount() - 1));

            Button chatBtn = new Button("Open Chat", VaadinIcon.CHAT.create());
            chatBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            chatBtn.setWidthFull();
            chatBtn.addClickListener(e ->
                    getUI().ifPresent(ui -> ui.navigate("chat/" + activeOrder.getId().toHexString())));

            Button deliveredBtn = new Button("Mark as Delivered", VaadinIcon.CHECK_CIRCLE.create());
            deliveredBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
            deliveredBtn.setWidthFull();
            deliveredBtn.getStyle().set("margin-top", "0.75rem");
            deliveredBtn.addClickListener(e -> markAsDelivered(activeOrder));

            activeCard.add(chatBtn, deliveredBtn);
            
            orderListLayout.add(activeCard);

            Hr divider = new Hr();
            divider.getStyle().set("margin", "1rem 0");
            orderListLayout.add(divider);
        }

        if (readyOrders.isEmpty() && activeOrder == null) {
            Paragraph empty = new Paragraph("No orders available for pickup right now.");
            empty.addClassNames(LumoUtility.TextColor.SECONDARY);
            empty.getStyle().set("text-align", "center").set("padding", "2rem 0");
            orderListLayout.add(empty);
            return;
        }

        if (!readyOrders.isEmpty()) {
            H3 availableTitle = new H3("Available Orders");
            availableTitle.getStyle().set("margin", "0 0 0.5rem 0");
            orderListLayout.add(availableTitle);

            for (Order order : readyOrders) {
                VerticalLayout card = orderCard(order, readyOrders);
                if (hasClaimed) {
                    card.getStyle().set("opacity", "0.4").set("pointer-events", "none");
                }
                orderListLayout.add(card);
            }
        }
    }
    private VerticalLayout orderCard(Order order, List<Order> allOrders) {
        VerticalLayout card = new VerticalLayout();
        card.setSpacing(false);
        card.setPadding(false);
        card.setWidthFull();
        card.getStyle()
                .set("border", "2px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "12px")
                .set("padding", "1rem")
                .set("transition", "opacity 0.2s");

        // Restaurant name
        Restaurant restaurant = (Restaurant) webRestaurantService.get(order.getRestaurantId());
        String restaurantName = restaurant != null ? restaurant.getName() : "Unknown Restaurant";

        H3 restName = new H3(restaurantName);
        restName.getStyle().set("margin", "0 0 0.25rem 0").set("font-size", "1.1rem");

        // Order meta row
        Span timeSpan = new Span(order.getCreatedAt() != null
                ? "🕐 " + order.getCreatedAt().toLocalTime().withSecond(0).withNano(0).toString()
                : "🕐 N/A");
        timeSpan.addClassNames(LumoUtility.TextColor.SECONDARY);
        timeSpan.getStyle().set("font-size", "0.85rem");

        Span itemCount = new Span("📦 " + order.getItems().size() + " item(s)");
        itemCount.addClassNames(LumoUtility.TextColor.SECONDARY);
        itemCount.getStyle().set("font-size", "0.85rem");

        Span total = new Span("💶 €" + order.getTotalPrice());
        total.getStyle().set("font-size", "0.85rem");

        HorizontalLayout meta = new HorizontalLayout(timeSpan, itemCount, total);
        meta.setSpacing(true);
        meta.setPadding(false);
        meta.getStyle().set("margin-bottom", "0.75rem");

        // Items summary
        VerticalLayout itemsList = new VerticalLayout();
        itemsList.setPadding(false);
        itemsList.setSpacing(false);
        order.getItems().forEach(dish -> {
            Span dishSpan = new Span("• " + dish.getTitle() + " x" + dish.getAmount());
            dishSpan.getStyle().set("font-size", "0.9rem");
            itemsList.add(dishSpan);
        });

        // Special instructions
        if (order.getSpecialInstructions() != null && !order.getSpecialInstructions().isBlank()) {
            Span instructions = new Span("📝 " + order.getSpecialInstructions());
            instructions.addClassNames(LumoUtility.TextColor.SECONDARY);
            instructions.getStyle().set("font-size", "0.85rem").set("font-style", "italic");
            itemsList.add(instructions);
        }

        // Claim button
        Button claimButton = new Button("Claim Order", VaadinIcon.TRUCK.create());
        claimButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        claimButton.setWidthFull();
        claimButton.getStyle().set("margin-top", "0.75rem");
        claimButton.addClickListener(e -> claimOrder(order, allOrders));
        claimButton.setEnabled(!hasClaimed);
        if (hasClaimed) {
            claimButton.setText("Unavailable");
            claimButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        }

        card.add(restName, meta, itemsList, claimButton);
        return card;
    }

    private void claimOrder(Order order, List<Order> allOrders) {
        selectedOrder = order;

        Driver driver = authService.getCurrentDriver();
        if (driver == null) {
            Notification.show("You must be logged in as a driver.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        order.setDriverId(driver.getId());
        order.setStatus(OrderStatus.IN_DELIVERY);
        webOrderService.save(order);
        hasClaimed = true;

        Notification n = Notification.show("Order claimed! Head to the restaurant for pickup.");
        n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        orderListLayout.removeAll();
        for (Order o : allOrders) {
            if (o.getStatus() != OrderStatus.READY_FOR_PICKUP && !o.getId().equals(order.getId())) continue;

            VerticalLayout card = orderCard(o, allOrders);

            if (!o.getId().equals(order.getId())) {
                card.getStyle().set("opacity", "0.4").set("pointer-events", "none");
            } else {
            card.getStyle()
                    .set("border-color", "#16a34a")
                    .set("border-width", "2px")
                    .set("background", "#f0fdf4");

            card.remove(card.getComponentAt(card.getComponentCount() - 1));

            Button deliveredBtn = new Button("Mark as Delivered", VaadinIcon.CHECK_CIRCLE.create());
            deliveredBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
            deliveredBtn.setWidthFull();
            deliveredBtn.getStyle().set("margin-top", "0.75rem");
            deliveredBtn.addClickListener(e -> markAsDelivered(order));
            card.add(deliveredBtn);
        }

            orderListLayout.add(card);
        }
    }

    private void markAsDelivered(Order order) {
        order.setStatus(OrderStatus.DELIVERED);
        order.setDeliveredAt(java.time.LocalDateTime.now());
        webOrderService.save(order);
        hasClaimed = false;
        selectedOrder = null;

        Notification n = Notification.show("Order marked as delivered! Great job 🎉");
        n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        loadOrders();
    }
}
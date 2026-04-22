package lt.linas_puplauskas.ui.view.client;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lt.linas_puplauskas.model.client.Client;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.order.OrderSearchCriteria;
import lt.linas_puplauskas.model.order.OrderStatus;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.service.WebOrderService;
import lt.linas_puplauskas.service.auth.AuthService;
import lt.linas_puplauskas.service.user.WebRestaurantService;
import lt.linas_puplauskas.ui.MainLayout;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Route(value = "history", layout = MainLayout.class)
@PageTitle("Order History – Tasty")
public class OrderHistoryView extends VerticalLayout {

    private final AuthService authService;
    private final WebOrderService webOrderService;
    private final WebRestaurantService webRestaurantService;

    public OrderHistoryView(AuthService authService,
                            WebOrderService webOrderService,
                            WebRestaurantService webRestaurantService) {
        this.authService = authService;
        this.webOrderService = webOrderService;
        this.webRestaurantService = webRestaurantService;

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

        Span emoji = new Span("🧾");
        emoji.getStyle().set("font-size", "2.5rem");

        H2 title = new H2("Order History");
        title.getStyle().set("margin", "0.5rem 0 0.25rem 0").set("font-size", "1.6rem");

        Paragraph subtitle = new Paragraph("All your past and active orders.");
        subtitle.addClassNames(LumoUtility.TextColor.SECONDARY);
        subtitle.getStyle().set("margin", "0 0 1rem 0");

        Hr divider = new Hr();
        divider.getStyle().set("margin", "0.75rem 0");

        VerticalLayout listLayout = new VerticalLayout();
        listLayout.setPadding(false);
        listLayout.setSpacing(true);
        listLayout.setWidthFull();

        card.add(emoji, title, subtitle, divider, listLayout);
        add(card);

        loadOrders(listLayout);
    }

    private void loadOrders(VerticalLayout listLayout) {
        Client client = authService.getCurrentClient();
        if (client == null) return;

        List<Order> orders = webOrderService.findAll(new OrderSearchCriteria()).stream()
                .filter(o -> o.getClientId() != null
                        && o.getClientId().equals(client.getId()))
                .sorted(Comparator.comparing(Order::getCreatedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        if (orders.isEmpty()) {
            Paragraph empty = new Paragraph("You haven't placed any orders yet.");
            empty.addClassNames(LumoUtility.TextColor.SECONDARY);
            empty.getStyle().set("text-align", "center").set("padding", "2rem 0");
            listLayout.add(empty);
            return;
        }

        for (Order order : orders) {
            listLayout.add(orderCard(order));
        }
    }

    private VerticalLayout orderCard(Order order) {
        VerticalLayout card = new VerticalLayout();
        card.setSpacing(false);
        card.setPadding(false);
        card.setWidthFull();

        boolean isCompleted = order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.CANCELLED;

        card.getStyle()
                .set("border", "2px solid " + (isCompleted
                        ? "var(--lumo-contrast-20pct)"
                        : "var(--lumo-primary-color)"))
                .set("border-radius", "12px")
                .set("padding", "1rem");

        // Restaurant name
        Restaurant restaurant = order.getRestaurantId() != null
                ? (Restaurant) webRestaurantService.get(order.getRestaurantId())
                : null;
        String restaurantName = restaurant != null ? restaurant.getName() : "Unknown Restaurant";

        H3 restName = new H3(restaurantName);
        restName.getStyle().set("margin", "0 0 0.25rem 0").set("font-size", "1.1rem");

        // Status badge
        Span statusBadge = new Span(formatStatus(order.getStatus()));
        statusBadge.getStyle()
                .set("font-size", "0.78rem")
                .set("font-weight", "600")
                .set("padding", "0.2rem 0.6rem")
                .set("border-radius", "999px")
                .set("background", statusColor(order.getStatus()))
                .set("color", "white");

        HorizontalLayout headerRow = new HorizontalLayout(restName, statusBadge);
        headerRow.setAlignItems(Alignment.CENTER);
        headerRow.setJustifyContentMode(JustifyContentMode.BETWEEN);
        headerRow.setWidthFull();
        headerRow.setPadding(false);

        // Meta
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Span date = new Span("🕐 " + (order.getCreatedAt() != null
                ? order.getCreatedAt().format(formatter)
                : "N/A"));
        date.addClassNames(LumoUtility.TextColor.SECONDARY);
        date.getStyle().set("font-size", "0.85rem");

        Span total = new Span("💶 €" + order.getTotalPrice());
        total.getStyle().set("font-size", "0.85rem");

        Span payment = new Span("💳 " + (order.getPaymentMethod() != null
                ? order.getPaymentMethod()
                : "N/A"));
        payment.getStyle().set("font-size", "0.85rem");

        HorizontalLayout meta = new HorizontalLayout(date, total, payment);
        meta.setSpacing(true);
        meta.setPadding(false);
        meta.getStyle().set("margin", "0.5rem 0");

        // Items
        VerticalLayout itemsList = new VerticalLayout();
        itemsList.setPadding(false);
        itemsList.setSpacing(false);
        order.getItems().forEach(dish -> {
            Span dishSpan = new Span("• " + dish.getTitle() + " x" + dish.getAmount()
                    + "  (€" + dish.getPrice() + " each)");
            dishSpan.getStyle().set("font-size", "0.9rem");
            itemsList.add(dishSpan);
        });

        // Special instructions
        if (order.getSpecialInstructions() != null
                && !order.getSpecialInstructions().isBlank()) {
            Span instructions = new Span("📝 " + order.getSpecialInstructions());
            instructions.addClassNames(LumoUtility.TextColor.SECONDARY);
            instructions.getStyle().set("font-size", "0.85rem").set("font-style", "italic");
            itemsList.add(instructions);
        }

        // Buttons row
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthFull();
        buttons.setSpacing(true);
        buttons.setPadding(false);
        buttons.getStyle().set("margin-top", "0.75rem");

        // Chat button — read only if completed
        Button chatBtn = new Button(
                isCompleted ? "View Chat" : "Open Chat",
                isCompleted ? VaadinIcon.EYE.create() : VaadinIcon.CHAT.create()
        );
        chatBtn.addThemeVariants(isCompleted
                ? ButtonVariant.LUMO_CONTRAST
                : ButtonVariant.LUMO_PRIMARY);
        chatBtn.addClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate("chat/" + order.getId().toHexString())));

        buttons.add(chatBtn);
        buttons.expand(chatBtn);

        card.add(headerRow, meta, itemsList, buttons);
        return card;
    }

    private String formatStatus(OrderStatus status) {
        if (status == null) return "Unknown";
        return switch (status) {
            case PENDING -> "Pending";
            case CONFIRMED -> "Confirmed";
            case PREPARING -> "Preparing";
            case READY_FOR_PICKUP -> "Ready for Pickup";
            case IN_DELIVERY -> "In Delivery";
            case DELIVERED -> "Delivered";
            case CANCELLED -> "Cancelled";
        };
    }

    private String statusColor(OrderStatus status) {
        if (status == null) return "#94a3b8";
        return switch (status) {
            case PENDING -> "#f59e0b";
            case CONFIRMED -> "#3b82f6";
            case PREPARING -> "#8b5cf6";
            case READY_FOR_PICKUP -> "#06b6d4";
            case IN_DELIVERY -> "#f97316";
            case DELIVERED -> "#16a34a";
            case CANCELLED -> "#ef4444";
        };
    }
}
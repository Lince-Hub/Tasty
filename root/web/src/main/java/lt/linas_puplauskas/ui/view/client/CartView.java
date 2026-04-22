package lt.linas_puplauskas.ui.view.client;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lt.linas_puplauskas.model.client.Client;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.order.OrderStatus;
import lt.linas_puplauskas.model.restaurant.Dish;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.model.review.Review;
import lt.linas_puplauskas.service.CartService;
import lt.linas_puplauskas.service.LoyaltyService;
import lt.linas_puplauskas.service.WebOrderService;
import lt.linas_puplauskas.service.auth.AuthService;
import lt.linas_puplauskas.service.user.WebRestaurantService;
import lt.linas_puplauskas.ui.MainLayout;

@Route(value = "cart", layout = MainLayout.class)
@PageTitle("Cart – Tasty")
public class CartView extends VerticalLayout {

    private final CartService cartService;
    private final AuthService authService;
    private final WebOrderService webOrderService;
    private final WebRestaurantService webRestaurantService;
    private final LoyaltyService loyaltyService;
    private final VerticalLayout itemsLayout = new VerticalLayout();
    private final Span totalSpan = new Span();
    private final Button checkoutButton = new Button("Place Order", VaadinIcon.CHECK.create());

    // New fields
    private final ComboBox<String> paymentComboBox = new ComboBox<>("Payment Method");
    private final TextField specialInstructions = new TextField("Special Instructions");
    private final RadioButtonGroup<Integer> ratingGroup = new RadioButtonGroup<>();
    private final TextField reviewTitle = new TextField("Review Title");
    private final TextArea reviewComment = new TextArea("Comment");

    public CartView(CartService cartService, WebRestaurantService webRestaurantService, AuthService authService, WebOrderService webOrderService, WebRestaurantService webRestaurantService1, LoyaltyService loyaltyService) {
        this.cartService = cartService;
        this.authService = authService;
        this.webOrderService = webOrderService;
        this.webRestaurantService = webRestaurantService1;
        this.loyaltyService = loyaltyService;

        setAlignItems(Alignment.CENTER);
        getStyle().set("min-height", "100vh").set("padding", "2rem 0");
        getStyle().set("background", "var(--lumo-contrast-5pct)");

        // --- Card ---
        VerticalLayout card = new VerticalLayout();
        card.setWidth("600px");
        card.setSpacing(false);
        card.setPadding(false);
        card.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "16px")
                .set("box-shadow", "0 4px 32px rgba(0,0,0,0.12)")
                .set("padding", "2rem")
                .set("box-sizing", "border-box");

        // --- Header ---
        Span emoji = new Span("🛒");
        emoji.getStyle().set("font-size", "2.5rem");

        H2 title = new H2("Your Cart");
        title.getStyle().set("margin", "0.5rem 0 0.25rem 0").set("font-size", "1.6rem");

        Order cart = cartService.getCart();
        Restaurant restaurant = (cart != null)
                ? (Restaurant) webRestaurantService.get(cart.getRestaurantId())
                : null;

        Paragraph restaurantParagraph = new Paragraph(
                cart != null && restaurant != null
                        ? "From: " + restaurant.getName()
                        : "No restaurant selected"
        );
        restaurantParagraph.addClassNames(LumoUtility.TextColor.SECONDARY);
        restaurantParagraph.getStyle().set("margin", "0 0 1rem 0");

        Hr divider = new Hr();
        divider.getStyle().set("margin", "0.75rem 0");

        // --- Items ---
        itemsLayout.setPadding(false);
        itemsLayout.setSpacing(false);
        itemsLayout.setWidth("100%");

        // --- Payment ---
        Hr paymentDivider = new Hr();
        paymentDivider.getStyle().set("margin", "0.75rem 0");

        H3 paymentTitle = new H3("Payment & Instructions");
        paymentTitle.getStyle().set("margin", "0.5rem 0");

        paymentComboBox.setItems("Cash", "Card");
        paymentComboBox.setPlaceholder("Select payment method");
        paymentComboBox.setWidthFull();
        paymentComboBox.setRequired(true);

        specialInstructions.setPlaceholder("e.g. no onions, ring the bell...");
        specialInstructions.setWidthFull();

        // --- Review ---
        Hr reviewDivider = new Hr();
        reviewDivider.getStyle().set("margin", "0.75rem 0");

        H3 reviewTitle2 = new H3("Leave a Review");
        reviewTitle2.getStyle().set("margin", "0.5rem 0");

        Span ratingLabel = new Span("Rating");
        ratingLabel.getStyle().set("font-size", "0.875rem").set("font-weight", "500")
                .set("color", "var(--lumo-secondary-text-color)");

        ratingGroup.setLabel("Rating");
        ratingGroup.setItems(1, 2, 3, 4, 5);
        ratingGroup.setRenderer(new com.vaadin.flow.data.renderer.ComponentRenderer<>(rating -> {
            Span star = new Span("★".repeat(rating));
            star.getStyle().set("color", "#f59e0b").set("font-size", "1.1rem");
            return star;
        }));

        reviewTitle.setPlaceholder("e.g. Great food!");
        reviewTitle.setWidthFull();

        reviewComment.setPlaceholder("Tell us about your experience...");
        reviewComment.setWidthFull();
        reviewComment.setMinHeight("100px");

        // --- Footer ---
        Hr footerDivider = new Hr();
        footerDivider.getStyle().set("margin", "0.75rem 0");

        totalSpan.getStyle().set("font-size", "1.2rem").set("font-weight", "700");

        checkoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        checkoutButton.setWidth("100%");
        checkoutButton.getStyle().set("margin-top", "1rem");
        checkoutButton.addClickListener(e -> placeOrder());

        Button continueShopping = new Button("Continue Shopping",
                VaadinIcon.ARROW_LEFT.create(),
                e -> getUI().ifPresent(ui -> ui.navigate("restaurants")));
        continueShopping.setWidth("100%");
        continueShopping.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        card.add(
                emoji, title, restaurantParagraph, divider,
                itemsLayout,
                paymentDivider, paymentTitle, paymentComboBox, specialInstructions,
                reviewDivider, reviewTitle2, ratingGroup, reviewTitle, reviewComment,
                footerDivider, totalSpan, checkoutButton, continueShopping
        );
        add(card);

        refreshCart();
    }

    private void refreshCart() {
        itemsLayout.removeAll();
        Order cart = cartService.getCart();

        if (cart == null || cart.getItems().isEmpty()) {
            Paragraph empty = new Paragraph("Your cart is empty.");
            empty.addClassNames(LumoUtility.TextColor.SECONDARY);
            empty.getStyle().set("text-align", "center").set("padding", "2rem 0");
            itemsLayout.add(empty);
            totalSpan.setText("Total: €0");
            checkoutButton.setEnabled(false);
            return;
        }

        checkoutButton.setEnabled(true);

        for (Dish dish : cart.getItems()) {
            itemsLayout.add(dishRow(dish));
        }

        totalSpan.setText("Total: €" + cart.getTotalPrice());
    }

    private HorizontalLayout dishRow(Dish dish) {
        HorizontalLayout row = new HorizontalLayout();
        row.setWidthFull();
        row.setAlignItems(Alignment.CENTER);
        row.getStyle()
                .set("padding", "0.75rem 0")
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)");

        VerticalLayout info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);

        Span name = new Span(dish.getTitle());
        name.getStyle().set("font-weight", "600");

        Span meta = new Span("€" + dish.getPrice() + " each");
        meta.addClassNames(LumoUtility.TextColor.SECONDARY);
        meta.getStyle().set("font-size", "0.85rem");

        info.add(name, meta);

        Button minus = new Button(VaadinIcon.MINUS.create(), e -> {
            if (dish.getAmount() <= 1) {
                cartService.removeDish(dish);
            } else {
                dish.setAmount(dish.getAmount() - 1);
                cartService.recalculateTotal(cartService.getCart());
            }
            refreshCart();
        });
        minus.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);

        Span qty = new Span(String.valueOf(dish.getAmount()));
        qty.getStyle().set("min-width", "24px").set("text-align", "center").set("font-weight", "600");

        Button plus = new Button(VaadinIcon.PLUS.create(), e -> {
            dish.setAmount(dish.getAmount() + 1);
            cartService.recalculateTotal(cartService.getCart());
            refreshCart();
        });
        plus.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);

        Span lineTotal = new Span("€" + (dish.getPrice() * dish.getAmount()));
        lineTotal.getStyle().set("font-weight", "700").set("min-width", "60px")
                .set("text-align", "right");

        Button remove = new Button(VaadinIcon.TRASH.create(), e -> {
            cartService.removeDish(dish);
            refreshCart();
        });
        remove.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_TERTIARY);

        row.add(info, minus, qty, plus, lineTotal, remove);
        row.expand(info);
        return row;
    }

    private void placeOrder() {
        Order cart = cartService.getCart();
        if (cart == null || cart.getItems().isEmpty()) return;

        if (paymentComboBox.isEmpty()) {
            Notification.show("Please select a payment method.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        Client client = authService.getCurrentClient();
        if (client == null) {
            Notification.show("You must be logged in to place an order.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        cart.setClientId(client.getId());
        cart.setPaymentMethod(paymentComboBox.getValue());
        cart.setSpecialInstructions(specialInstructions.getValue());
        cart.setDeliveryFee(Math.round((1 + Math.random() * 5) * 100.0) / 100.0);
        cart.setStatus(OrderStatus.PENDING);
        cart.setCreatedAt(java.time.LocalDateTime.now());

        if (ratingGroup.getValue() != null) {
            Review review = new Review();
            review.setRating(ratingGroup.getValue());
            review.setTitle(reviewTitle.getValue());
            review.setComment(reviewComment.getValue());
            review.setCreatedAt(java.time.LocalDateTime.now());
            cart.setReview(review);
        }

        webOrderService.save(cart);

        Restaurant restaurant = (Restaurant) webRestaurantService.get(cart.getRestaurantId());
        int pointsEarned = loyaltyService.calculatePoints(cart.getTotalPrice(), restaurant);
        loyaltyService.awardPoints(client, cart, restaurant);

        cartService.clearCart();

        String msg = "Order placed successfully! You earned " + pointsEarned + " bonus points!";
        Notification n = Notification.show(msg);
        n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        getUI().ifPresent(ui -> ui.navigate("restaurants"));
    }
}
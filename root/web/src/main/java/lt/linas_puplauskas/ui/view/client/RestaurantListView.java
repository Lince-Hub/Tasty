package lt.linas_puplauskas.ui.view.client;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lt.linas_puplauskas.model.restaurant.Dish;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.model.restaurant.RestaurantSearchCriteria;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.service.CartService;
import lt.linas_puplauskas.service.PricingService;
import lt.linas_puplauskas.service.user.WebRestaurantService;
import lt.linas_puplauskas.ui.MainLayout;

import java.util.List;

@Route(value = "restaurants", layout = MainLayout.class)
@PageTitle("Restaurants – Tasty")
public class RestaurantListView extends VerticalLayout {

    private final VerticalLayout menuPanel = new VerticalLayout();
    private final CartService cartService;
    private final PricingService pricingService;
    private Restaurant selectedRestaurant = null;

    public RestaurantListView(WebRestaurantService webRestaurantService,
                              CartService cartService,
                              PricingService pricingService) {
        this.cartService = cartService;
        this.pricingService = pricingService;

        setSpacing(false);
        setPadding(false);
        getStyle().set("background", "var(--lumo-contrast-5pct)").set("min-height", "100vh");

        VerticalLayout header = new VerticalLayout();
        header.setPadding(true);
        header.setSpacing(false);
        header.getStyle().set("background", "var(--lumo-base-color)")
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)");

        H2 title = new H2("🍽 Restaurants");
        title.getStyle().set("margin", "0");
        Paragraph subtitle = new Paragraph("Pick a restaurant to see their menu");
        subtitle.addClassNames(LumoUtility.TextColor.SECONDARY);
        subtitle.getStyle().set("margin", "0");
        header.add(title, subtitle);

        FlexLayout grid = new FlexLayout();
        grid.getStyle()
                .set("flex-wrap", "wrap")
                .set("gap", "1rem")
                .set("padding", "1.5rem");

        List<Restaurant> restaurants = webRestaurantService.findAll(new RestaurantSearchCriteria(UserRole.RESTAURANT));
        if (restaurants.isEmpty()) {
            Paragraph empty = new Paragraph("No restaurants available right now.");
            empty.addClassNames(LumoUtility.TextColor.SECONDARY);
            grid.add(empty);
        } else {
            restaurants.forEach(r -> grid.add(restaurantCard(r)));
        }

        menuPanel.setVisible(false);
        menuPanel.setPadding(true);
        menuPanel.setSpacing(true);
        menuPanel.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-top", "1px solid var(--lumo-contrast-10pct)")
                .set("padding", "1.5rem");

        add(header, grid, menuPanel);
    }

    private Div restaurantCard(Restaurant r) {
        Div card = new Div();
        card.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "12px")
                .set("box-shadow", "0 2px 12px rgba(0,0,0,0.08)")
                .set("padding", "1.25rem")
                .set("cursor", "pointer")
                .set("width", "280px")
                .set("transition", "box-shadow 0.2s")
                .set("border", "2px solid transparent");

        Span status = new Span(r.isOpen() ? "🟢 Open" : "🔴 Closed");
        status.getStyle().set("font-size", "0.75rem").set("font-weight", "600");

        H3 name = new H3(r.getName() != null ? r.getName() : "Unknown");
        name.getStyle().set("margin", "0.25rem 0");

        Span cuisine = new Span(r.getCuisine() != null ? r.getCuisine().name() : "");
        cuisine.getStyle()
                .set("font-size", "0.8rem")
                .set("background", "var(--lumo-contrast-10pct)")
                .set("padding", "2px 8px")
                .set("border-radius", "999px");

        Paragraph desc = new Paragraph(r.getDescription() != null ? r.getDescription() : "");
        desc.getStyle().set("font-size", "0.875rem")
                .set("color", "var(--lumo-secondary-text-color)")
                .set("margin", "0.5rem 0");

        Span address = new Span("📍 " + (r.getAddress() != null ? r.getAddress() : ""));
        address.getStyle().set("font-size", "0.8rem").set("color", "var(--lumo-secondary-text-color)");

        Span hours = new Span("🕐 " + (r.getOpeningTime() != null ? r.getOpeningTime() : "?")
                + " – " + (r.getClosingTime() != null ? r.getClosingTime() : "?"));
        hours.getStyle().set("font-size", "0.8rem").set("color", "var(--lumo-secondary-text-color)");

        Span rating = new Span("⭐ " + String.format("%.1f", r.getRating()));
        rating.getStyle().set("font-size", "0.85rem").set("font-weight", "600");

        card.add(status, name, cuisine, desc, address, hours, rating);

        if (pricingService.isPeakHour(r)) {
            Span happyHours = new Span("🎉 Happy Hours! 30% off  "
                    + r.getHappyHoursStart() + " – " + r.getHappyHoursEnd());
            happyHours.getStyle()
                    .set("font-size", "0.75rem")
                    .set("font-weight", "600")
                    .set("background", "#fef9c3")
                    .set("color", "#854d0e")
                    .set("padding", "3px 8px")
                    .set("border-radius", "999px")
                    .set("display", "block")
                    .set("margin-top", "0.4rem");
            card.add(happyHours);
        }

        card.addClickListener(e -> {
            selectedRestaurant = r;
            showMenu(r);
            card.getStyle().set("border", "2px solid var(--lumo-primary-color)");
        });

        return card;
    }

    private void showMenu(Restaurant r) {
        menuPanel.removeAll();
        menuPanel.setVisible(true);

        boolean isHappyHour = pricingService.isPeakHour(r);

        H3 menuTitle = new H3("Menu – " + r.getName());
        menuTitle.getStyle().set("margin", "0 0 0.5rem 0");
        menuPanel.add(menuTitle);

        if (isHappyHour) {
            Span badge = new Span("🎉 Happy Hours active – all prices 30% off!");
            badge.getStyle()
                    .set("font-size", "0.85rem")
                    .set("font-weight", "600")
                    .set("color", "#854d0e")
                    .set("background", "#fef9c3")
                    .set("padding", "4px 12px")
                    .set("border-radius", "999px")
                    .set("display", "inline-block")
                    .set("margin-bottom", "1rem");
            menuPanel.add(badge);
        }

        List<Dish> menu = r.getMenu();
        if (menu == null || menu.isEmpty()) {
            menuPanel.add(new Paragraph("No dishes available."));
            return;
        }

        Div dishGrid = new Div();
        dishGrid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(5, 1fr)")
                .set("gap", "1rem")
                .set("width", "100%");

        menu.forEach(d -> dishGrid.add(dishCard(d, r)));
        menuPanel.add(dishGrid);
    }

    private Div dishCard(Dish d, Restaurant r) {
        Div card = new Div();
        card.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "10px")
                .set("padding", "1rem")
                .set("box-sizing", "border-box")
                .set("width", "100%")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.07)");

        Span category = new Span(d.getCategory() != null ? d.getCategory().name() : "");
        category.getStyle()
                .set("font-size", "0.7rem")
                .set("font-weight", "600")
                .set("background", "var(--lumo-primary-color-10pct)")
                .set("color", "var(--lumo-primary-color)")
                .set("padding", "2px 8px")
                .set("border-radius", "999px");

        Span available = new Span(d.isAvailable() ? "✅ Available" : "❌ Unavailable");
        available.getStyle().set("font-size", "0.75rem").set("display", "block")
                .set("margin-top", "0.4rem");

        H4 title = new H4(d.getTitle() != null ? d.getTitle() : "");
        title.getStyle().set("margin", "0.25rem 0 0 0");

        Paragraph desc = new Paragraph(d.getDescription() != null ? d.getDescription() : "");
        desc.getStyle()
                .set("font-size", "0.8rem")
                .set("color", "var(--lumo-secondary-text-color)")
                .set("margin", "0.25rem 0");

        double finalPrice = pricingService.getPrice(d, r);
        boolean isHappyHour = pricingService.isPeakHour(r);

        VerticalLayout priceLayout = new VerticalLayout();
        priceLayout.setPadding(false);
        priceLayout.setSpacing(false);

        if (isHappyHour && finalPrice != d.getPrice()) {
            Span originalPrice = new Span("€" + d.getPrice());
            originalPrice.getStyle()
                    .set("text-decoration", "line-through")
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "0.85rem");

            Span discountedPrice = new Span("€" + finalPrice);
            discountedPrice.getStyle()
                    .set("font-weight", "700")
                    .set("font-size", "1rem")
                    .set("color", "#16a34a");

            priceLayout.add(originalPrice, discountedPrice);
        } else {
            Span price = new Span("€" + finalPrice);
            price.getStyle().set("font-weight", "700").set("font-size", "1rem");
            priceLayout.add(price);
        }

        Span meta = new Span("⏱ " + d.getPreparationTimeMin() + " min  •  "
                + d.getCalories() + " kcal  •  " + d.getWeight() + "g");
        meta.getStyle()
                .set("font-size", "0.75rem")
                .set("color", "var(--lumo-secondary-text-color)")
                .set("display", "block")
                .set("margin-top", "0.25rem");

        Button addToCart = new Button("Add to Cart", VaadinIcon.CART.create());
        addToCart.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        addToCart.setWidth("100%");
        addToCart.getStyle().set("margin-top", "0.75rem");
        addToCart.setEnabled(d.isAvailable());

        addToCart.addClickListener(e -> {
            // Set the discounted price before adding to cart
            d.setPrice(finalPrice);
            cartService.addDish(r, d);
            Notification n = Notification.show(d.getTitle() + " added to cart!");
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        card.add(category, available, title, desc, priceLayout, meta, addToCart);
        return card;
    }
}
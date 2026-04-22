package lt.linas_puplauskas.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.service.auth.AuthService;
import lt.linas_puplauskas.ui.view.client.CartView;
import lt.linas_puplauskas.ui.view.client.OrderHistoryView;
import lt.linas_puplauskas.ui.view.client.RestaurantListView;
import lt.linas_puplauskas.ui.view.driver.DriverOrdersView;
import lt.linas_puplauskas.ui.view.user.ProfileView;

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private final AuthService authService;
    private final Tabs tabs = new Tabs();

    public MainLayout(AuthService authService) {
        this.authService = authService;
        addToNavbar(createHeader(), createTabs());
    }

    private Component createHeader() {
        var logo = VaadinIcon.CUTLERY.create();
        logo.setSize("28px");

        var title = new Span("Tasty");
        title.getStyle()
                .set("font-weight", "600")
                .set("font-size", "20px");

        var header = new HorizontalLayout(logo, title);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        header.setSpacing(true);
        header.setPadding(true);

        return header;
    }

    private Tabs createTabs() {
        User user = authService.getCurrentUser();

        if (user == null) return tabs;
        tabs.add(
                createTab("History", VaadinIcon.RECORDS, OrderHistoryView.class),
                createTab("Profile", VaadinIcon.USER, ProfileView.class)
        );

        if (user.getRole() == UserRole.CLIENT) {
            tabs.add(
                    createTab("Restaurants", VaadinIcon.CUTLERY, RestaurantListView.class),
                    createTab("Cart", VaadinIcon.CART, CartView.class)
            );
        }

        if (user.getRole() == UserRole.DRIVER) {
            tabs.add(
                    createTab("Orders", VaadinIcon.PACKAGE, DriverOrdersView.class)
            );
        }

        return tabs;
    }

    private Tab createTab(String label, VaadinIcon icon, Class<? extends Component> view) {
        Icon tabIcon = icon.create();
        RouterLink link = new RouterLink();
        link.add(tabIcon, new Span(label));
        link.setRoute(view);

        return new Tab(link);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!authService.isLoggedIn()) {
            event.forwardTo("login");
            return;
        }

        if (tabs.getComponentCount() == 0) {
            addToNavbar(createTabs());
        }

        tabs.getChildren()
                .filter(Tab.class::isInstance)
                .map(Tab.class::cast)
                .filter(tab -> tab.getChildren()
                        .filter(RouterLink.class::isInstance)
                        .map(RouterLink.class::cast)
                        .anyMatch(link -> link.getHref().equals(
                                event.getLocation().getPath())))
                .findFirst()
                .ifPresent(tabs::setSelectedTab);
    }
}

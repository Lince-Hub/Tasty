package lt.linas_puplauskas.ui.view.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lt.linas_puplauskas.model.driver.Message;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.order.OrderSearchCriteria;
import lt.linas_puplauskas.model.order.OrderStatus;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.service.auth.AuthService;
import lt.linas_puplauskas.service.WebOrderService;
import lt.linas_puplauskas.ui.MainLayout;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "chat/:orderId", layout = MainLayout.class)
@PageTitle("Chat – Tasty")
public class ChatView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthService authService;
    private final WebOrderService webOrderService;

    private Order order;
    private User currentUser;

    private final VerticalLayout messagesLayout = new VerticalLayout();
    private final TextField messageInput = new TextField();

    public ChatView(AuthService authService, WebOrderService webOrderService) {
        this.authService = authService;
        this.webOrderService = webOrderService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String orderId = event.getRouteParameters().get("orderId").orElse(null);
        if (orderId == null) {
            event.forwardTo("orders");
            return;
        }

        currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            event.forwardTo("login");
            return;
        }

        order = (Order) webOrderService.findAll(new OrderSearchCriteria()).stream()
                .filter(o -> o.getId().toHexString().equals(orderId))
                .findFirst()
                .orElse(null);

        if (order == null) {
            event.forwardTo("orders");
            return;
        }

        buildUi();
    }

    private void buildUi() {
        setAlignItems(Alignment.CENTER);
        getStyle().set("min-height", "100vh").set("padding", "2rem 0");
        getStyle().set("background", "var(--lumo-contrast-5pct)");

        VerticalLayout card = new VerticalLayout();
        card.setWidth("620px");
        card.setSpacing(false);
        card.setPadding(false);
        card.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "16px")
                .set("box-shadow", "0 4px 32px rgba(0,0,0,0.12)")
                .set("padding", "2rem")
                .set("box-sizing", "border-box");

        // --- Header ---
        Span emoji = new Span("💬");
        emoji.getStyle().set("font-size", "2.5rem");

        H2 title = new H2("Order Chat");
        title.getStyle().set("margin", "0.5rem 0 0.25rem 0").set("font-size", "1.6rem");

        Paragraph subtitle = new Paragraph("Order #" + order.getId().toHexString().substring(18));
        subtitle.addClassNames(LumoUtility.TextColor.SECONDARY);
        subtitle.getStyle().set("margin", "0 0 1rem 0");

        Hr divider = new Hr();
        divider.getStyle().set("margin", "0.75rem 0");

        // --- Messages area ---
        messagesLayout.setPadding(false);
        messagesLayout.setSpacing(false);
        messagesLayout.setWidthFull();
        messagesLayout.getStyle()
                .set("min-height", "300px")
                .set("max-height", "400px")
                .set("overflow-y", "auto")
                .set("padding", "0.5rem 0");

        renderMessages();

        Hr footerDivider = new Hr();
        footerDivider.getStyle().set("margin", "0.75rem 0");

        // --- Input area ---
        boolean isCompleted = order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.CANCELLED;

        if (isCompleted) {
            Paragraph readOnly = new Paragraph("🔒 This order is completed. Chat is read-only.");
            readOnly.addClassNames(LumoUtility.TextColor.SECONDARY);
            readOnly.getStyle().set("text-align", "center").set("font-style", "italic");
            card.add(emoji, title, subtitle, divider, messagesLayout, footerDivider, readOnly);
        } else {
            messageInput.setPlaceholder("Type a message...");
            messageInput.setWidthFull();
            messageInput.getStyle().set("flex", "1");

            Button sendBtn = new Button(VaadinIcon.PAPERPLANE.create());
            sendBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            sendBtn.addClickListener(e -> sendMessage());
            // Also send on Enter
            messageInput.addKeyPressListener(
                    com.vaadin.flow.component.Key.ENTER,
                    e -> sendMessage()
            );

            HorizontalLayout inputRow = new HorizontalLayout(messageInput, sendBtn);
            inputRow.setWidthFull();
            inputRow.setAlignItems(Alignment.BASELINE);
            inputRow.expand(messageInput);

            card.add(emoji, title, subtitle, divider, messagesLayout, footerDivider, inputRow);
        }

        add(card);
    }

    private void renderMessages() {
        messagesLayout.removeAll();

        List<Message> messages = order.getMessages();

        if (messages == null || messages.isEmpty()) {
            Paragraph empty = new Paragraph("No messages yet. Say hello! 👋");
            empty.addClassNames(LumoUtility.TextColor.SECONDARY);
            empty.getStyle().set("text-align", "center").set("padding", "2rem 0");
            messagesLayout.add(empty);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Message message : messages) {
            boolean isMine = message.getSender() != null
                    && message.getSender().getId().equals(currentUser.getId());

            VerticalLayout bubble = new VerticalLayout();
            bubble.setPadding(false);
            bubble.setSpacing(false);
            bubble.getStyle()
                    .set("max-width", "75%")
                    .set("padding", "0.6rem 1rem")
                    .set("border-radius", isMine ? "16px 16px 4px 16px" : "16px 16px 16px 4px")
                    .set("background", isMine ? "var(--lumo-primary-color)" : "var(--lumo-contrast-10pct)")
                    .set("margin-bottom", "0.5rem");

            Span content = new Span(message.getContent());
            content.getStyle()
                    .set("color", "var(--lumo-body-text-color)")
                    .set("font-size", "0.95rem");

            String senderName = message.getSender() != null
                    ? message.getSender().getUsername()
                    : "Unknown";
            String time = message.getSentAt() != null
                    ? message.getSentAt().format(formatter)
                    : "";

            Span meta = new Span(senderName + " · " + time);
            meta.getStyle()
                    .set("font-size", "0.75rem")
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("margin-top", "0.2rem");

            bubble.add(content, meta);

            HorizontalLayout row = new HorizontalLayout(bubble);
            row.setWidthFull();
            row.setJustifyContentMode(isMine
                    ? JustifyContentMode.END
                    : JustifyContentMode.START);
            row.setPadding(false);

            messagesLayout.add(row);
        }
    }

    private void sendMessage() {
        String text = messageInput.getValue();
        if (text == null || text.isBlank()) return;

        Message message = new Message();
        message.setId(new ObjectId());
        message.setSender(currentUser);
        message.setContent(text.trim());
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);

        order.getMessages().add(message);
        webOrderService.save(order);

        messageInput.clear();
        renderMessages();
    }
}
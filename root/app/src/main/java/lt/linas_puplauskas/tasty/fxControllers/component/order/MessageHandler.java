package lt.linas_puplauskas.tasty.fxControllers.component.order;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lt.linas_puplauskas.model.client.Client;
import lt.linas_puplauskas.model.driver.Driver;
import lt.linas_puplauskas.model.driver.Message;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserRole;
import lt.linas_puplauskas.model.user.UserSearchCriteria;
import lt.linas_puplauskas.service.ClientService;
import lt.linas_puplauskas.service.DriverService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessageHandler {

    private final TableView<Message> messageTable;
    private final TableColumn<Message, String> senderCol;
    private final TableColumn<Message, String> receiverCol;
    private final TableColumn<Message, String> contentCol;
    private final TableColumn<Message, String> sentAtCol;
    private final TableColumn<Message, Boolean> readCol;

    private final ComboBox<User> senderCombo;
    private final ComboBox<User> receiverCombo;
    private final TextArea contentField;
    private final VBox formPane;

    private final ClientService clientService = new ClientService();
    private final DriverService driverService = new DriverService();

    public MessageHandler(TableView<Message> messageTable,
                          TableColumn<Message, String> senderCol,
                          TableColumn<Message, String> receiverCol,
                          TableColumn<Message, String> contentCol,
                          TableColumn<Message, String> sentAtCol,
                          TableColumn<Message, Boolean> readCol,
                          ComboBox<User> senderCombo,
                          ComboBox<User> receiverCombo,
                          TextArea contentField,
                          VBox formPane) {
        this.messageTable = messageTable;
        this.senderCol = senderCol;
        this.receiverCol = receiverCol;
        this.contentCol = contentCol;
        this.sentAtCol = sentAtCol;
        this.readCol = readCol;
        this.senderCombo = senderCombo;
        this.receiverCombo = receiverCombo;
        this.contentField = contentField;
        this.formPane = formPane;

        setupTableColumns();
    }

    private void setupTableColumns() {
        senderCol.setCellValueFactory(cellData -> {
            Message message = cellData.getValue();
            if (message.getSender() != null) {
                String name = getUserDisplayName(message.getSender());
                return new SimpleStringProperty(name);
            }
            return new SimpleStringProperty("Unknown");
        });

        receiverCol.setCellValueFactory(cellData -> {
            Message message = cellData.getValue();
            if (message.getReceiver() != null) {
                String name = getUserDisplayName(message.getReceiver());
                return new SimpleStringProperty(name);
            }
            return new SimpleStringProperty("Unknown");
        });

        contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));

        sentAtCol.setCellValueFactory(cellData -> {
            Message message = cellData.getValue();
            if (message.getSentAt() != null) {
                return new SimpleStringProperty(
                        message.getSentAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                );
            }
            return new SimpleStringProperty("");
        });

        readCol.setCellValueFactory(cellData -> {
            Message message = cellData.getValue();
            return new SimpleBooleanProperty(message.isRead());
        });

        readCol.setCellFactory(col -> new TableCell<Message, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(Boolean isRead, boolean empty) {
                super.updateItem(isRead, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(isRead != null && isRead);
                    checkBox.setDisable(true);
                    setGraphic(checkBox);
                }
            }
        });
    }

    private void setupComboBoxes(List<Message> messages) {
        Message first = messages.getFirst();
        User sender = first.getSender();
        User receiver = first.getReceiver();

        senderCombo.getItems().setAll(sender, receiver);
        receiverCombo.getItems().setAll(sender, receiver);
        receiverCombo.setDisable(true);

        applyUserCellFactory(senderCombo);
        applyUserCellFactory(receiverCombo);
    }

    private void applyUserCellFactory(ComboBox<User> combo) {
        combo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : getUserDisplayName(user));
            }
        });
        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : getUserDisplayName(user));
            }
        });
    }

    public void onSelection() {
        User selected = senderCombo.getValue();
        if (selected == null) return;

        User other = senderCombo.getItems().stream()
                .filter(u -> !u.getId().equals(selected.getId()))
                .findFirst()
                .orElse(null);

        receiverCombo.setValue(other);
    }

    private String getUserDisplayName(User user) {
        if (user == null) {
            return "Unknown";
        }

        String name = "";
        String surname = "";

        if (user.getRole().equals(UserRole.CLIENT)) {
            Client client = (Client) clientService.findFirst(new UserSearchCriteria(user.getId()));
            name = client.getName();
            surname = client.getSurname();
        } else if (user.getRole().equals(UserRole.DRIVER)) {
            Driver driver = (Driver) driverService.findFirst(new UserSearchCriteria(user.getId()));
            name = driver.getName();
            surname = driver.getSurname();
        }

        return name + " " + surname;
    }

    public void fill(List<Message> messages) {
        clear();
        if (messages != null && !messages.isEmpty()) {
            messageTable.getItems().setAll(messages);
            setupComboBoxes(messages);
        }
    }

    public void clear() {
        messageTable.getItems().clear();
    }

    public void addMessage(Message message) {
        if (message != null) {
            messageTable.getItems().add(message);
            clearForm();
        }
    }

    public void toggleForm() {
        boolean visible = formPane.isVisible();
        formPane.setVisible(!visible);
        formPane.setManaged(!visible);
        if (!visible) {
            clearForm();
        }
    }

    public void clearForm() {
        senderCombo.setValue(null);
        receiverCombo.setValue(null);
        contentField.clear();
    }

    public Message buildMessage() {
        User sender = senderCombo.getValue();
        User receiver = receiverCombo.getValue();
        String content = contentField.getText();

        if (sender == null || receiver == null || content == null || content.trim().isEmpty()) {
            System.out.println("Warning: Cannot build message - missing required fields");
            return null;
        }

        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(content);
        msg.setSentAt(LocalDateTime.now());
        msg.setRead(false);

        return msg;
    }
}
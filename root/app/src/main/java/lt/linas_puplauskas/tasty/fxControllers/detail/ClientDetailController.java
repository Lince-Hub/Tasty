package lt.linas_puplauskas.tasty.fxControllers.detail;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lt.linas_puplauskas.model.client.Client;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.service.ClientService;
import lt.linas_puplauskas.tasty.service.RouteService;

import java.io.IOException;

public class ClientDetailController implements DetailController{
    public TextField usernameField;
    public TextField emailField;
    public TextField phoneField;
    public TextField nameField;
    public TextField surnameField;
    public TextField addressField;
    public TextField cardNoField;
    public TextField bonusPointsField;
    public TextField balanceField;
    public DatePicker dateOfBirthPicker;

    private final ClientService clientService = new ClientService();
    private Client client;

    @Override
    public void setUser(User user) {
        this.client = (Client) clientService.get(user.getId());

        usernameField.setText(client.getUsername());
        emailField.setText(client.getEmail());
        phoneField.setText(client.getPhoneNum());
        nameField.setText(client.getName());
        surnameField.setText(client.getSurname());
        addressField.setText(client.getAddress());
        cardNoField.setText(client.getCardNo());
        bonusPointsField.setText(String.valueOf(client.getBonusPoints()));
        bonusPointsField.setEditable(false);
        balanceField.setText(String.valueOf(client.getBalance()));
        balanceField.setEditable(false);
        dateOfBirthPicker.setValue(client.getDateOfBirth());
    }

    public void save() {
        client.setUsername(usernameField.getText());
        client.setEmail(emailField.getText());
        client.setPhoneNum(phoneField.getText());
        client.setName(nameField.getText());
        client.setSurname(surnameField.getText());
        client.setAddress(addressField.getText());
        client.setCardNo(cardNoField.getText());
        client.setDateOfBirth(dateOfBirthPicker.getValue());

        clientService.update(client);
    }

    public void close() throws IOException {
        RouteService.route("main-view.fxml");
    }

    public void viewUserDetails() throws IOException {
        UserDetailController controller = RouteService.routeAndGetController("detail/user-detail.fxml");
        controller.setUser(client);
    }
}

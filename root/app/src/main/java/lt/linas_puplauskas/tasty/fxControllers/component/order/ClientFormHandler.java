package lt.linas_puplauskas.tasty.fxControllers.component.order;

import javafx.scene.control.TextField;
import lt.linas_puplauskas.model.client.Client;

public class ClientFormHandler {

    private final TextField name;
    private final TextField surname;
    private final TextField email;
    private final TextField phone;
    private final TextField address;
    private final TextField balance;
    private final TextField bonus;

    public ClientFormHandler(
            TextField name,
            TextField surname,
            TextField email,
            TextField phone,
            TextField address,
            TextField balance,
            TextField bonus
    ) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
        this.bonus = bonus;
    }

    public void fill(Client client) {
        if (client == null) {
            clear();
            return;
        }

        name.setText(client.getName());
        surname.setText(client.getSurname());
        email.setText(client.getEmail());
        phone.setText(client.getPhoneNum());
        address.setText(client.getAddress());
        balance.setText(String.valueOf(client.getBalance()));
        bonus.setText(String.valueOf(client.getBonusPoints()));
    }

    public void update(Client client) {
        if (client == null) return;

        client.setName(name.getText());
        client.setSurname(surname.getText());
        client.setEmail(email.getText());
        client.setPhoneNum(phone.getText());
        client.setAddress(address.getText());

        try {
            client.setBalance(Double.parseDouble(balance.getText()));
        } catch (NumberFormatException e) {
            client.setBalance(0.0);
        }

        try {
            client.setBonusPoints(Integer.parseInt(bonus.getText()));
        } catch (NumberFormatException e) {
            client.setBonusPoints(0);
        }
    }

    public void clear() {
        name.clear();
        surname.clear();
        email.clear();
        phone.clear();
        address.clear();
        balance.clear();
        bonus.clear();
    }
}
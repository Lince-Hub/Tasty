package linas.puplauskas.tasty.fxControllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import lt.linas_puplauskas.model.driver.VehicleType;

public class CreateUserForm implements Initializable {
    public ToggleGroup userType;
    public RadioButton clientRadio;
    public RadioButton driverRadio;
    public RadioButton restaurantRadio;
    public Pane userPane;
    public TextField usernameField;
    public PasswordField passwordField;
    public PasswordField repeatPasswordField;
    public TextField emailField;
    public TextField phoneField;
    public Pane clientPane;
    public TextField clientNameField;
    public TextField clientSurnameField;
    public TextField cardNoField;
    public TextField clientAddressField;
    public DatePicker clientBirthDatePicker;
    public Pane driverPane;
    public TextField driverNameField;
    public TextField driverSurnameField;
    public TextField driverLicenceField;
    public ComboBox<VehicleType> vehicleTypeCombo;
    public DatePicker driverBirthDatePicker;
    public TextField vehiclePlateField;
    public Pane restaurantPane;
    public TextArea restaurantDescField;
    public TextField restaurantAddressField;
    public TextField cuisineTypeField;
    public TextField openingTimeField;
    public TextField closingTimeField;

    public void radioChanged() {
        this.clientPane.setDisable(true);
        this.clientPane.setVisible(false);
        this.driverPane.setDisable(true);
        this.driverPane.setVisible(false);
        this.restaurantPane.setDisable(true);
        this.restaurantPane.setVisible(false);
        if (this.clientRadio.isSelected()) {
            this.clientPane.setDisable(false);
            this.clientPane.setVisible(true);
        } else if (this.driverRadio.isSelected()) {
            this.driverPane.setDisable(false);
            this.driverPane.setVisible(true);
        } else if (this.restaurantRadio.isSelected()) {
            this.restaurantPane.setDisable(false);
            this.restaurantPane.setVisible(true);
        }

    }

    public void initialize(URL location, ResourceBundle resources) {
        this.driverPane.setVisible(false);
        this.vehicleTypeCombo.getItems().addAll(VehicleType.values());
    }
}
package lt.linas_puplauskas.tasty.fxControllers.detail;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lt.linas_puplauskas.model.driver.Driver;
import lt.linas_puplauskas.model.driver.VehicleType;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.service.DriverService;
import lt.linas_puplauskas.tasty.service.RouteService;

import java.io.IOException;

public class DriverDetailController implements DetailController{
    public TextField usernameField;
    public TextField emailField;
    public TextField phoneField;
    public TextField nameField;
    public TextField surnameField;
    public TextField licenceField;
    public ComboBox<VehicleType> vehicleTypeComboBox;
    public TextField plateNumberField;
    public TextField ratingField;
    public TextField totalDeliveriesField;
    public CheckBox availableCheckBox;

    private final DriverService driverService = new DriverService();
    private Driver driver;

    @Override
    public void setUser(User user) {
        this.driver = (Driver) driverService.get(user.getId());

        usernameField.setText(driver.getUsername());
        emailField.setText(driver.getEmail());
        phoneField.setText(driver.getPhoneNum());
        nameField.setText(driver.getName());
        surnameField.setText(driver.getSurname());
        licenceField.setText(driver.getDriverLicence());
        plateNumberField.setText(driver.getVehiclePlateNumber());
        ratingField.setText(String.valueOf(driver.getRating()));
        totalDeliveriesField.setText(String.valueOf(driver.getTotalDeliveries()));
        availableCheckBox.setSelected(driver.isAvailable());

        vehicleTypeComboBox.getItems().addAll(VehicleType.values());
        vehicleTypeComboBox.setValue(driver.getVehicleType());
    }

    public void save() {
        driver.setUsername(usernameField.getText());
        driver.setEmail(emailField.getText());
        driver.setPhoneNum(phoneField.getText());
        driver.setName(nameField.getText());
        driver.setSurname(surnameField.getText());
        driver.setDriverLicence(licenceField.getText());
        driver.setVehiclePlateNumber(plateNumberField.getText());
        driver.setVehicleType(vehicleTypeComboBox.getValue());
        driver.setAvailable(availableCheckBox.isSelected());

        driverService.update(driver);
    }

    public void close() throws IOException {
        RouteService.route("main-view.fxml");
    }

    public void viewUserDetails() throws IOException {
        UserDetailController controller = RouteService.routeAndGetController("detail/user-detail.fxml");
        controller.setUser(driver);
    }
}

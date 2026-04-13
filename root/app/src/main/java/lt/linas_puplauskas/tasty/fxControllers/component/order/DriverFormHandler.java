package lt.linas_puplauskas.tasty.fxControllers.component.order;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lt.linas_puplauskas.model.driver.Driver;
import lt.linas_puplauskas.model.driver.VehicleType;

public class DriverFormHandler {

    private final TextField name;
    private final TextField surname;
    private final TextField phone;
    private final TextField licence;
    private final ComboBox<VehicleType> vehicle;
    private final TextField plate;
    private final TextField rating;
    private final CheckBox available;

    public DriverFormHandler(
            TextField name,
            TextField surname,
            TextField phone,
            TextField licence,
            ComboBox<VehicleType> vehicle,
            TextField plate,
            TextField rating,
            CheckBox available
    ) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.licence = licence;
        this.vehicle = vehicle;
        this.plate = plate;
        this.rating = rating;
        this.available = available;

        vehicle.getItems().addAll(VehicleType.values());
    }

    public void fill(Driver driver) {
        if (driver == null) {
            clear();
            return;
        }

        name.setText(driver.getName());
        surname.setText(driver.getSurname());
        phone.setText(driver.getPhoneNum());
        licence.setText(driver.getDriverLicence());
        vehicle.setValue(driver.getVehicleType());
        plate.setText(driver.getVehiclePlateNumber());
        rating.setText(String.valueOf(driver.getRating()));
        available.setSelected(driver.isAvailable());
    }

    public void update(Driver driver) {
        if (driver == null) return;

        driver.setName(name.getText());
        driver.setSurname(surname.getText());
        driver.setPhoneNum(phone.getText());
        driver.setDriverLicence(licence.getText());
        driver.setVehicleType(vehicle.getValue());
        driver.setVehiclePlateNumber(plate.getText());

        try {
            driver.setRating(Double.parseDouble(rating.getText()));
        } catch (NumberFormatException e) {
            driver.setRating(0.0);
        }

        driver.setAvailable(available.isSelected());
    }

    public void clear() {
        name.clear();
        surname.clear();
        phone.clear();
        licence.clear();
        vehicle.setValue(null);
        plate.clear();
        rating.clear();
        available.setSelected(false);
    }
}
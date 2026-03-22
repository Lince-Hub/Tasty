package lt.linas_puplauskas.tasty.model.driver;

import lt.linas_puplauskas.tasty.model.user.User;

import java.time.LocalDate;

public class Driver extends User {
    private String name;
    private String surname;
    private String driverLicence;
    private VehicleType vehicleType;
    private LocalDate birthDate;
    private boolean isAvailable;
    private double rating;
    private int totalDeliveries;
    private String vehiclePlateNumber;
}

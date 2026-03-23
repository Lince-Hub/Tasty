package lt.linas_puplauskas.tasty.model.driver;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.linas_puplauskas.tasty.model.user.User;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
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

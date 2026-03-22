package lt.linas_puplauskas.tasty.model.client;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.linas_puplauskas.tasty.model.user.User;

@Getter
@Setter
@NoArgsConstructor
public class Client extends User {
    private String name;
    private String surname;
    private String cardNo;
    private int bonusPoints;
    private String address;
    private LocalDate dateOfBirth;
    private double balance;
}
package linas.puplauskas.tasty.model.client;

import java.time.LocalDate;

import linas.puplauskas.tasty.model.user.User;

public class Client extends User {
    private String name;
    private String surname;
    private String cardNo;
    private int bonusPoints;
    private String address;
    private LocalDate dateOfBirth;
    private double balance;
}
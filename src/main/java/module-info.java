module linas.puplauskas.tasty {
    requires javafx.controls;
    requires javafx.fxml;


    opens linas.puplauskas.tasty to javafx.fxml;
    exports linas.puplauskas.tasty;
}
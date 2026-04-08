module linas.puplauskas.tasty {
    requires javafx.controls;
    requires javafx.fxml;

    requires linas.puplauskas.common;
    requires static lombok;
    requires mongo.java.driver;

    opens lt.linas_puplauskas.tasty.fxControllers to javafx.fxml;
    opens lt.linas_puplauskas.tasty.fxControllers.component to javafx.fxml;
    opens lt.linas_puplauskas.tasty.fxControllers.detail to javafx.fxml;
    opens lt.linas_puplauskas.tasty to javafx.fxml;

    exports lt.linas_puplauskas.tasty.fxControllers;
    exports lt.linas_puplauskas.tasty.fxControllers.component;

    exports lt.linas_puplauskas.tasty;
}
module linas.puplauskas.tasty {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.naming;
    requires mongo.java.driver;
    requires org.apache.commons.lang3;

    opens lt.linas_puplauskas.tasty.model.user to mongo.java.driver;
    opens lt.linas_puplauskas.tasty.model.order to mongo.java.driver;
    opens lt.linas_puplauskas.tasty.model.restaurant to mongo.java.driver;
    opens lt.linas_puplauskas.tasty.database to mongo.java.driver;

    opens lt.linas_puplauskas.tasty to javafx.fxml;
    exports lt.linas_puplauskas.tasty;

    opens lt.linas_puplauskas.tasty.fxControllers to javafx.fxml;
    exports lt.linas_puplauskas.tasty.fxControllers;
}
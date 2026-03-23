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
    opens lt.linas_puplauskas.tasty.model.client to mongo.java.driver;
    opens lt.linas_puplauskas.tasty.model.driver to mongo.java.driver;
    opens lt.linas_puplauskas.tasty.database to mongo.java.driver;

    opens lt.linas_puplauskas.tasty.fxControllers to javafx.fxml;
    opens lt.linas_puplauskas.tasty to javafx.fxml;

    exports lt.linas_puplauskas.tasty.fxControllers;
    exports lt.linas_puplauskas.tasty.fxControllers.component;

    exports lt.linas_puplauskas.tasty;
    exports lt.linas_puplauskas.tasty.model.user;
    exports lt.linas_puplauskas.tasty.model.order;
    exports lt.linas_puplauskas.tasty.model.restaurant;
    exports lt.linas_puplauskas.tasty.model.client;
    exports lt.linas_puplauskas.tasty.model.driver;
    exports lt.linas_puplauskas.tasty.database;
    exports lt.linas_puplauskas.tasty.service;
}
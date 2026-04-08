module linas.puplauskas.common {
    requires static lombok;
    requires mongo.java.driver;
    requires java.naming;
    requires org.apache.commons.lang3;

    exports lt.linas_puplauskas.model.user;
    exports lt.linas_puplauskas.model.client;
    exports lt.linas_puplauskas.model.driver;
    exports lt.linas_puplauskas.model.order;
    exports lt.linas_puplauskas.model.restaurant;
    exports lt.linas_puplauskas.model.review;
    exports lt.linas_puplauskas.database;
    exports lt.linas_puplauskas.service;
}
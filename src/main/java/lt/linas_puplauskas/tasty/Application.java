package lt.linas_puplauskas.tasty;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lt.linas_puplauskas.tasty.database.MongoConfig;

import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        MongoConfig.connect();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-in-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() {
        MongoConfig.connect();
    }

    @Override
    public void stop() {
        MongoConfig.disconnect();
    }
}

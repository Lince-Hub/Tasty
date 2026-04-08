package lt.linas_puplauskas.tasty.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lt.linas_puplauskas.tasty.Application;

import java.io.IOException;

public class RouteService {

    private static Stage stage;

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    private static FXMLLoader load(String viewFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource(viewFile));
        Parent root = loader.load();

        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }

        stage.show();
        return loader;
    }

    public static void route(String viewFile) throws IOException {
        load(viewFile);
    }

    public static <T> T routeAndGetController(String viewFile) throws IOException {
        return load(viewFile).getController();
    }
}
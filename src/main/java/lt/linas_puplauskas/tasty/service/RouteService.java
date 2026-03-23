package lt.linas_puplauskas.tasty.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lt.linas_puplauskas.tasty.Application;

import java.io.IOException;

public class RouteService {
    private static FXMLLoader load(String viewFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(viewFile));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);

        Stage stage = (Stage) Stage.getWindows()
                .stream()
                .filter(w -> w.isShowing())
                .findFirst()
                .orElse(new Stage());

        stage.setScene(scene);
        stage.show();

        return fxmlLoader;
    }

    public static void route(String viewFile) throws IOException {
        load(viewFile);
    }

    public static FXMLLoader routeAndData(String viewFile) throws IOException {
        return load(viewFile);
    }
}

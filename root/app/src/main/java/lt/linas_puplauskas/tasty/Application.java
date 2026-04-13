package lt.linas_puplauskas.tasty;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lt.linas_puplauskas.database.MongoConfig;
import lt.linas_puplauskas.model.user.User;
import lt.linas_puplauskas.model.user.UserSearchCriteria;
import lt.linas_puplauskas.service.UserService;
import lt.linas_puplauskas.tasty.service.RouteService;
import org.bson.types.ObjectId;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Getter
    @Setter
    private static User currentUser;
    @Override
    public void start(Stage stage) throws IOException {
        RouteService.init(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        stage.setX((screen.getWidth()  - stage.getWidth())  / 2);
        stage.setY((screen.getHeight() - stage.getHeight()) / 2);
        stage.setTitle("Tasty");
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(700);
        stage.show();
//        stage.centerOnScreen();

        initDev();
    }

    @Override
    public void init() {
        MongoConfig.connect();
    }

    @Override
    public void stop() {
        MongoConfig.disconnect();
    }

    public void initDev() throws IOException {
        UserService userService = new UserService(User.class);
        currentUser = (User) userService.findFirst(new UserSearchCriteria(new ObjectId("65000000000000000000000a")));

        RouteService.route("main-view.fxml");
    }
}

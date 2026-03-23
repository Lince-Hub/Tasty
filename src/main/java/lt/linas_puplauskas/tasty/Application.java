package lt.linas_puplauskas.tasty;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lt.linas_puplauskas.tasty.database.MongoConfig;
import lt.linas_puplauskas.tasty.fxControllers.MainViewController;
import lt.linas_puplauskas.tasty.model.user.User;
import lt.linas_puplauskas.tasty.model.user.UserSearchCriteria;
import lt.linas_puplauskas.tasty.service.RouteService;
import lt.linas_puplauskas.tasty.service.UserService;
import org.bson.types.ObjectId;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
        UserService userService = new UserService(User.class);

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
        Parent root = fxmlLoader.load();

        MainViewController mainViewController = fxmlLoader.getController();

        User user = userService.find(new UserSearchCriteria(new ObjectId("65000000000000000000000a")));
        mainViewController.setUser(user);

        Scene scene = new Scene(root);
        stage.setTitle("Tasty");
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

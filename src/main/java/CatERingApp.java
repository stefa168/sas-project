import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CatERingApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ui/main.fxml"));
            primaryStage.setTitle("Cat&Ring");
            primaryStage.setScene(new Scene(root, 1080, 720));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

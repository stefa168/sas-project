package ui.task;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ui.Main;

public class TaskManagement {
    private Stage window;

    @FXML
    private BorderPane containerPane;


    public void endMenuManagement() {
        Main.showMainWindow();
        window.hide();
    }

    public void setStage(Stage stage) {
        window = stage;
    }

    public void initialize() {

    }
}

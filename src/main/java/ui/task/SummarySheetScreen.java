package ui.task;

import businesslogic.CatERing;
import businesslogic.kitchentask.KitchenTaskManager;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SummarySheetScreen {

    @FXML
    private BorderPane containerPane;
    @FXML
    private Text summarySheetDetails;

    private Stage window;
    private KitchenTaskManager ktm;

    private void initialize() {
        this.ktm = CatERing.getInstance().getKitchenTaskManager();
    }

    public void setStage(Stage taskWindow) {
        this.window = taskWindow;
    }

    public void endSummarySheetManagement() {
        window.hide();
    }

    public Stage startSummarySheetManagement() {
        return window;
    }
}

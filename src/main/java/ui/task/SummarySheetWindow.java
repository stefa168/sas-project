package ui.task;

import businesslogic.CatERing;
import businesslogic.kitchentask.KitchenTaskManager;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SummarySheetWindow {

    @FXML
    private BorderPane containerPane;
    @FXML
    private Text summarySheetDetails;

    private Stage window;
    private KitchenTaskManager ktm;

    private void initialize() {
        this.ktm = CatERing.getInstance().getKitchenTaskManager();
    }

    public void endSummarySheetManagement() {
        window.hide();
    }

    public Stage startSummarySheetManagement() {
        return window;
    }

    public void setWindow(Stage taskWindow) {
        this.window = taskWindow;
    }

    public void setParentWindow(Stage parentWindow) {
        window.initOwner(parentWindow);
        window.initModality(Modality.WINDOW_MODAL);
    }
}

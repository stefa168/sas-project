package ui.task;

import businesslogic.CatERing;
import businesslogic.kitchentask.KitchenTaskManager;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import ui.WindowController;

public class SummarySheetWindow extends WindowController {

    @FXML
    private BorderPane containerPane;
    @FXML
    private Text summarySheetDetails;

    private KitchenTaskManager ktm;

    @Override
    protected void initialize() {
        this.ktm = CatERing.getInstance().getKitchenTaskManager();
    }
}

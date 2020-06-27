package ui;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class Start {

    private Main mainPaneController;
    private Stage taskManagementWindow;

    @FXML
    void beginMenuManagement() {
        mainPaneController.startMenuManagement();
    }

    @FXML
    void manageTasks() {
        mainPaneController.manageTasks();
    }

    public void setParent(Main main) {
        this.mainPaneController = main;
    }

    public void initialize() {
    }


}

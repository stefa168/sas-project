package ui;

import javafx.fxml.FXML;

public class Start {

    private Main mainPaneController;

    @FXML
    void beginMenuManagement() {
        mainPaneController.startMenuManagement();
    }

    public void setParent(Main main) {
        this.mainPaneController = main;
    }

    public void initialize() {
    }
}

package ui;

import businesslogic.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Start {

    @FXML
    public Button gestireMenuButton;
    @FXML
    public Button gestireCompitiCucinaButton;
    private Main mainPaneController;
    private Stage taskManagementWindow;
    private User userLogin;

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
    public void setUser(User user) {
        this.userLogin = user;
        if(userLogin.isChef()){
            gestireCompitiCucinaButton.setDisable(false);
        }

    }


    public void initialize() {
    }


}

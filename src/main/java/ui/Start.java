package ui;

import businesslogic.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Start {

    @FXML
    public Button gestireMenuButton;
    @FXML
    public Button gestireCompitiCucinaButton;
    private Main mainPaneController;
    private Stage taskManagementWindow;
    private User userLogin;

    AnchorPane paneContainerPrincipale;

    @FXML
    void beginMenuManagement() {
        mainPaneController.startMenuManagement();
    }

    @FXML
    void manageTasks() {
        mainPaneController.manageTasks();
    }

    public void setPrincipale(AnchorPane anchorPane){this.paneContainerPrincipale=anchorPane;}
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


    public void backToLogin(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene root = new Scene(loader.load(), 700, 400);
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(root);
            stage.show();


            Stage current = (Stage) paneContainerPrincipale.getScene().getWindow();
            current.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}

package ui;

import businesslogic.CatERing;
import businesslogic.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import ui.menu.MenuManagement;
import ui.task.TaskManagement;

import java.io.IOException;

public class Main {


    private static Stage mainStage;

    @FXML
    AnchorPane paneContainer;

    @FXML
    FlowPane startPane;

    @FXML
    Start startPaneController;

    BorderPane menuManagementPane;
    MenuManagement menuManagementPaneController;

    TaskManagement taskManagementPaneController;

    public void setUsernameUserLogin(User user) {
        CatERing.getInstance().getUserManager().login(user);
        startPaneController.setUser(user);
    }

    public void initialize() throws Exception {

        startPaneController.setParent(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu/menu-management.fxml"));
        try {
            menuManagementPane = loader.load();
            menuManagementPaneController = loader.getController();
            menuManagementPaneController.setMainPaneController(this);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // non avendo trovato un posto migliore per l'inizializzazione della seconda finestra, lo metto qui.
        initTaskManagementWindow();

    }

    public void startMenuManagement() {
        // CatERing.getInstance().getUserManager().login(userLogin.getUserName());

        menuManagementPaneController.initialize();
        paneContainer.getChildren().remove(startPane);
        paneContainer.getChildren().add(menuManagementPane);
        AnchorPane.setTopAnchor(menuManagementPane, 0.0);
        AnchorPane.setBottomAnchor(menuManagementPane, 0.0);
        AnchorPane.setLeftAnchor(menuManagementPane, 0.0);
        AnchorPane.setRightAnchor(menuManagementPane, 0.0);

    }

    public void showStartPane() {
        startPaneController.initialize();
        paneContainer.getChildren().remove(menuManagementPane);
        paneContainer.getChildren().add(startPane);
    }

    private void initTaskManagementWindow() throws IOException {
        Stage taskWindow = new Stage();

        FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("task/task-management.fxml"));
        Scene primaryScene = new Scene(rootLoader.load());

        TaskManagement controller = rootLoader.getController();

        taskWindow.setTitle("Gestione Compiti - Selezione Servizio");
        taskWindow.setScene(primaryScene);

        taskWindow.setOnCloseRequest(event -> taskManagementPaneController.closeWindow());

        taskManagementPaneController = controller;
        taskManagementPaneController.setWindow(taskWindow);
    }

    public void manageTasks() {
        taskManagementPaneController.showWindow();
    }

    public void passMainWindowStage(Stage primaryStage) {
        mainStage = primaryStage;
        taskManagementPaneController.setParentWindow(mainStage);
    }

    public void backToLogin(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene root = new Scene(loader.load(), 700, 400);
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(root);
            stage.show();

            mainStage.hide();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}

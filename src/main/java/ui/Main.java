package ui;

import businesslogic.CatERing;
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


    // Necessario per poter nascondere la finestra di base.
    // Normalmente la finestra verrebbe riciclata, ma il codice fornito è molto complesso ed è decisamente più
    // semplice adattarlo per funzionare come siamo abituati. Questo solo perchè non funziona correttamente
    // showAndWait, che avrebbe facilmente risolto le cose. (sospetto che il malfunzionamento sia causato da come è
    // inizializzata la finestra)
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
    public static void showMainWindow() {
        mainStage.show();
    }

    public static void hideMainWindow() {
        mainStage.hide();
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
        CatERing.getInstance().getUserManager().fakeLogin("Tony");

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
        Scene primaryScene = new Scene(rootLoader.load(), 600, 400);

        TaskManagement controller = rootLoader.getController();
        controller.setStage(taskWindow);

        taskWindow.setTitle("Gestione Compiti - Selezione Servizio");
        taskWindow.setScene(primaryScene);

        taskWindow.setOnCloseRequest(event -> taskManagementPaneController.endMenuManagement());

        taskManagementPaneController = controller;
    }

    public void manageTasks() {
        taskManagementPaneController.startMenuManagement();
    }

    public void passMainWindowStage(Stage primaryStage) {
        mainStage = primaryStage;
    }
}

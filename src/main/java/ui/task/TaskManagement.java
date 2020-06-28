package ui.task;

import businesslogic.CatERing;
import businesslogic.EventException;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.EventItemInfo;
import businesslogic.event.Service;
import businesslogic.kitchentask.KitchenTaskManager;
import businesslogic.kitchentask.SummarySheet;
import businesslogic.user.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.WindowController;

import java.io.IOException;
import java.util.Optional;

public class TaskManagement extends WindowController {
    private Stage window;
    @FXML
    private BorderPane containerPane;
    @FXML
    private TreeView<EventItemInfo> eventServiceTree;
    @FXML
    private Button apriButton;
    @FXML
    private Text userNameField;

    private SummarySheetWindow summarySheetWindowController;

    public void initialize() throws IOException {
        eventServiceTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        eventServiceTree.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldSelection, newSelection) -> {
                    User u = CatERing.getInstance().getUserManager().getCurrentUser();

                    EventItemInfo selection = newSelection.getValue();

                    apriButton.setDisable(selection == null ||
                                          selection instanceof Event ||
                                          ((Service) selection).getParentEvent().getAssignedChef() != u ||
                                          ((Service) selection).getState() != Service.State.CONFERMATO);
                });

        initSummarySheetScreen();
    }

    private EventItemInfo getSelectedTreeItem() {
        return eventServiceTree.getSelectionModel()
                               .getSelectedItem()
                               .getValue();
    }

    @FXML
    private void clickSuBottoneApri() {
        KitchenTaskManager ktm = CatERing.getInstance().getKitchenTaskManager();
        Service service = (Service) getSelectedTreeItem();
        SummarySheet sheet = service.getSheet();

        try {
            if (sheet == null) {

                Optional<ButtonType> choice = new Alert(Alert.AlertType.CONFIRMATION,
                                                        "Al momento non esiste un foglio riepilogativo per questo " +
                                                        "Servizio.\nVuoi crearlo?").showAndWait();

                if (choice.isPresent() && choice.orElseThrow().equals(ButtonType.OK)) {
                    ktm.createSummarySheet(service.getParentEvent(), service);
                }
            } else {
                ktm.openSummarySheetForEditing(service.getParentEvent(), service);
            }

            summarySheetWindowController.showWindow();
        } catch (UseCaseLogicException | EventException e) {
            e.printStackTrace();
        }
    }

    // Questo metodo implicitamente chiama l'initialize() di SummarySheetWindow
    private void initSummarySheetScreen() throws IOException {

        Stage sheetWindow = new Stage();

        FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("summary-sheet-screen.fxml"));
        Scene primaryScene = new Scene(rootLoader.load());

        SummarySheetWindow controller = rootLoader.getController();
        controller.setWindow(sheetWindow);

        sheetWindow.setTitle("Gestione Compiti - Foglio riepilogativo");
        sheetWindow.setScene(primaryScene);

        sheetWindow.setOnCloseRequest(event -> controller.closeWindow());

        this.summarySheetWindowController = controller;
        this.childrenWindows.add(summarySheetWindowController);
    }

    @Override
    public void showWindow() {
        CatERing.getInstance().getUserManager().fakeLogin("Marinella");

        userNameField.setText(CatERing.getInstance().getUserManager().getCurrentUser().getUserName());

        loadEventInfo();

        super.showWindow();
    }

    private void loadEventInfo() {
        ObservableList<Event> all = CatERing.getInstance().getEventManager().getEventInfo();
        eventServiceTree.setShowRoot(false);
        TreeItem<EventItemInfo> root = new TreeItem<>(null);

        for (Event e : all) {
            TreeItem<EventItemInfo> node = new TreeItem<>(e);
            root.getChildren().add(node);
            ObservableList<Service> serv = e.getServices();
            for (Service s : serv) {
                node.getChildren().add(new TreeItem<>(s));
            }
        }

        eventServiceTree.setRoot(root);
    }
}

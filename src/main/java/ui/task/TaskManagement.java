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
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ui.Main;

import java.util.Optional;

public class TaskManagement {
    private Stage window;
    @FXML
    private BorderPane containerPane;
    @FXML
    private TreeView<EventItemInfo> eventServiceTree;
    @FXML
    private Button apriButton;

    public void setStage(Stage stage) {
        window = stage;
    }

    public void initialize() {
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
    }

    private EventItemInfo getSelectedTreeItem() {
        return eventServiceTree.getSelectionModel()
                               .getSelectedItem()
                               .getValue();
    }

    public void clickSuBottoneApri() {
        KitchenTaskManager ktm = CatERing.getInstance().getKitchenTaskManager();
        Service service = (Service) getSelectedTreeItem();
        SummarySheet sheet = service.getSheet();

        try {
            if (sheet == null) {

                Optional<ButtonType> choice = new Alert(Alert.AlertType.CONFIRMATION,
                                                        "Al momento non esiste un foglio riepilogativo per questo " +
                                                        "Servizio.\nVuoi crearlo?")
                        .showAndWait();

                if (choice.isPresent() && choice.orElseThrow().equals(ButtonType.OK)) {
                    ktm.createSummarySheet(service.getParentEvent(), service);
                }
            } else {
                ktm.openSummarySheetForEditing(service.getParentEvent(), service);
            }
        } catch (UseCaseLogicException | EventException e) {
            e.printStackTrace();
        }
    }

    public void startMenuManagement() {
        CatERing.getInstance().getUserManager().fakeLogin("Marinella");

        Main.hideMainWindow();
        window.show();

        loadEventInfo();
    }

    public void endMenuManagement() {
        Main.showMainWindow();
        window.hide();
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

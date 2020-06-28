package ui.task;

import businesslogic.CatERing;
import businesslogic.event.Event;
import businesslogic.event.EventItemInfo;
import businesslogic.event.Service;
import businesslogic.user.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ui.Main;

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
                                          ((Service)selection).getState() != Service.State.CONFERMATO);
                });
    }

    private EventItemInfo getSelectedTreeItem() {
        return eventServiceTree.getSelectionModel()
                               .getSelectedItem()
                               .getValue();
    }

    public void clickSuBottoneApri() {
        // try {
        //
        // }
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

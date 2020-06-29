package ui.task;

import businesslogic.CatERing;
import businesslogic.TaskException;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchentask.KitchenJob;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CookAssign {

    @FXML
    public Button assegnaButton;
    @FXML
    public Button eliminaButton;
    @FXML
    public TreeView<Object> cookTree;
    private Stage myStage;
    private KitchenJob kitchenJob;

    public void initialize() {

    }

    public void okButtonPressed(ActionEvent actionEvent) {
        myStage.close();
    }

    public void setOwnStage(Stage stage) {
        myStage = stage;
    }


    public void setKitchenJob(KitchenJob kitchenJob) {
        this.kitchenJob = kitchenJob;
        cookTree.setShowRoot(false);
        TreeItem<Object> root = new TreeItem<>(null);

        ArrayList<User> cooks = User.getUsersByTurnAvailabilities(kitchenJob.getTurn().getTurn_id());

        ObservableList<User> allCooks = FXCollections.observableArrayList(cooks);

        for (User user : allCooks) {
            TreeItem<Object> node = new TreeItem<>(user);
            root.getChildren().add(node);
        }

        cookTree.setRoot(root);
        eliminaButton.setDisable(kitchenJob.getCook() == null);
    }

    public void optionCookButton(ActionEvent actionEvent) throws UseCaseLogicException, TaskException {
        TreeItem<Object> row = cookTree.getSelectionModel().getSelectedItem();
        Object obj = row.getValue();
        if (obj instanceof User) {
            User user = (User) obj;
            try {
                CatERing.getInstance().getKitchenTaskManager().assignCook(kitchenJob, user);
                myStage.close();
            } catch (TaskException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        }

    }

    public void rimuoviCuoco(ActionEvent actionEvent) throws UseCaseLogicException, TaskException {
        if (kitchenJob.getCook() != null) {
            CatERing.getInstance().getKitchenTaskManager().assignCook(kitchenJob, null);
            myStage.close();
        }
    }
}

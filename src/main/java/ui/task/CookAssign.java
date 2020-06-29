package ui.task;

import businesslogic.CatERing;
import businesslogic.TaskException;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchentask.KitchenJob;
import businesslogic.kitchentask.Task;
import businesslogic.recipe.KitchenDuty;
import businesslogic.turn.KitchenTurn;
import businesslogic.turn.Turn;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CookAssign {

    @FXML
    public Button assegnaButton;
    @FXML
    public Button eliminaButton;
    private Stage myStage;

    private KitchenJob kitchenJob;
    @FXML
    public TreeView<Object> cookTree;

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

        for(User user: allCooks){
            TreeItem<Object> node = new TreeItem<>(user);
            root.getChildren().add(node);
        }



        cookTree.setRoot(root);

        cookTree.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldSelection, newSelection) ->{
                    User selectedUser = (User) newSelection.getValue();
                    if(kitchenJob.getCook().equals(selectedUser)){
                        assegnaButton.setDisable(true);
                        eliminaButton.setDisable(false);
                    }
                    else{
                        eliminaButton.setDisable(true);
                        assegnaButton.setDisable(false);
                    }
                });
    }

    public void optionCookButton(ActionEvent actionEvent) throws UseCaseLogicException, TaskException {
        TreeItem<Object> row = cookTree.getSelectionModel().getSelectedItem();
        Object obj = row.getValue();
        if(obj instanceof User){
            User user = (User) obj;
            CatERing.getInstance().getKitchenTaskManager().assignCook(kitchenJob,user);
            myStage.close();
        }

    }

    public void rimuoviCuoco(ActionEvent actionEvent) throws UseCaseLogicException, TaskException {
        TreeItem<Object> row = cookTree.getSelectionModel().getSelectedItem();
        Object obj = row.getValue();
        if(obj instanceof User && ((User)obj).equals(kitchenJob.getCook())){
            CatERing.getInstance().getKitchenTaskManager().assignCook(kitchenJob,null);
            myStage.close();
        }
    }
}

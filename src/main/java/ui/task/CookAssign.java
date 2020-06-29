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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CookAssign {

    private Stage myStage;

    private KitchenJob kitchenJob;
    @FXML
    public TreeView<Object> cookTree;

    public void initialize() {
        ObservableList<User> allUsers = CatERing.getInstance().getUserManager().getAllUsers();
        cookTree.setShowRoot(false);
        TreeItem<Object> root = new TreeItem<>(null);

        for(User user: allUsers){
            if(user.isCook()){

                ArrayList<KitchenTurn> allTurns = KitchenTurn.getAllKitchenTurn();
                for (KitchenTurn kitchenTurn: allTurns){
                    if(kitchenTurn.equals(kitchenJob.getTurn()) && kitchenTurn.getAvailableCooks().contains(user)){
                        TreeItem<Object> node = new TreeItem<>(user);
                        root.getChildren().add(node);

                        ArrayList<KitchenTurn> availabilities = KitchenTurn.loadTurnAvailabilitiesByCookId(user.getId());
                        ObservableList<KitchenTurn> availabilitesObservable = FXCollections.observableArrayList(availabilities);
                        for(KitchenTurn availability: availabilitesObservable){
                            node.getChildren().add(new TreeItem<>(availability));
                        }


                    }
                }

            }
        }


        cookTree.setRoot(root);
    }

    public void okButtonPressed(ActionEvent actionEvent) {
        myStage.close();
    }
    public void setOwnStage(Stage stage) {
        myStage = stage;
    }


    public void setKitchenJob(KitchenJob kitchenJob) {
        this.kitchenJob = kitchenJob;
    }

    public void optionCookButton(ActionEvent actionEvent) throws UseCaseLogicException, TaskException {
        TreeItem<Object> row = cookTree.getSelectionModel().getSelectedItem();
        Object obj = row.getValue();
        if(obj instanceof User){
            User user = (User) obj;
            if(kitchenJob.getCook() == null){
                CatERing.getInstance().getKitchenTaskManager().assignCook(kitchenJob,user);
            }

        }

    }
}

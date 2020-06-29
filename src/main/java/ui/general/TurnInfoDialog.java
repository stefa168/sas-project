package ui.general;


import businesslogic.CatERing;
import businesslogic.TaskException;
import businesslogic.event.*;
import businesslogic.turn.KitchenTurn;
import businesslogic.turn.TurnItemInfo;
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

public class TurnInfoDialog {

    private Stage myStage;

    @FXML
    TreeView<Object> turnTree;

    public void initialize() {
        ObservableList<KitchenTurn> allKitchenTurn = CatERing.getInstance().getTurnManager().getTurnInfo();
        turnTree.setShowRoot(false);
        TreeItem<Object> root = new TreeItem<>(null);

        for (KitchenTurn turn: allKitchenTurn) {
            TreeItem<Object> node = new TreeItem<>(turn);
            root.getChildren().add(node);

            HashMap<User, Duration> assignedCooks = turn.getAssignedCooks();
            Set<User> users = assignedCooks.keySet();
            ArrayList<User> cookWithDuration = new ArrayList<>(users);

            ArrayList<String> allCook = new ArrayList<>();

            for (User user : cookWithDuration) {
                Duration duration = assignedCooks.get(user);
                String cook = "Cuoco assegnato: " + user.getUserName() + " con tempo in minuti: " + duration.toMinutes();
                allCook.add(cook);
            }

            for(User user: turn.getAvailableCooks()){
                String cookAvailable = "Cuoco disponibile: " +user.getUserName();
                allCook.add(cookAvailable);
            }

            ObservableList<String> allCooksInformation = FXCollections.observableArrayList(allCook);

            for (String s: allCooksInformation) {
                node.getChildren().add(new TreeItem<>(s));
            }
        }

        turnTree.setRoot(root);
    }

    @FXML
    public void okButtonPressed() {
        myStage.close();
    }

    public void setOwnStage(Stage stage) {
        myStage = stage;
    }

    public void completeTurn(ActionEvent actionEvent) throws TaskException {
        TreeItem<Object> row = turnTree.getSelectionModel().getSelectedItem();
        Object turn = row.getValue();
        if(turn instanceof KitchenTurn){
            KitchenTurn kitchenTurn = (KitchenTurn) turn;
            CatERing.getInstance().getKitchenTaskManager().changeKitchenTurnState(kitchenTurn,true);
        }
    }
}

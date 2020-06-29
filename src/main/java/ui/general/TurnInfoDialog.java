package ui.general;


import businesslogic.CatERing;
import businesslogic.event.*;
import businesslogic.turn.KitchenTurn;
import businesslogic.turn.TurnItemInfo;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class TurnInfoDialog {

    private Stage myStage;

    @FXML
    TreeView<TurnItemInfo> eventTree;

    public void initialize() {
        ObservableList<KitchenTurn> allKitchenTurn = CatERing.getInstance().getTurnManager().getTurnInfo();
        eventTree = new TreeView<>();
        TreeItem<TurnItemInfo> root = new TreeItem<>(null);

        for (KitchenTurn turn: allKitchenTurn) {
            TreeItem<TurnItemInfo> node = new TreeItem<>(turn);
            root.getChildren().add(node);
        }

        eventTree.setRoot(root);
    }

    @FXML
    public void okButtonPressed() {
        myStage.close();
    }

    public void setOwnStage(Stage stage) {
        myStage = stage;
    }
}

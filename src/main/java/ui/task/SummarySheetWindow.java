package ui.task;

import businesslogic.CatERing;
import businesslogic.kitchentask.KitchenJob;
import businesslogic.kitchentask.KitchenTaskManager;
import businesslogic.kitchentask.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import ui.WindowController;

import java.util.Comparator;

public class SummarySheetWindow extends WindowController {

    @FXML
    TreeView<TaskItemInfo> contentTree;
    @FXML
    private BorderPane containerPane;
    private KitchenTaskManager ktm;

    @Override
    protected void initialize() {
        this.ktm = CatERing.getInstance().getKitchenTaskManager();
    }

    @Override
    public void showWindow() {
        initContentTree();
        super.showWindow();
    }

    private void initContentTree() {
        contentTree.setShowRoot(false);
        TreeItem<TaskItemInfo> root = new TreeItem<>(null);

        if (this.ktm == null) {
            initialize();
        }

        for (Task task : ktm.getCurrentSheet().getTasks()) {
            TreeItem<TaskItemInfo> node = new TreeItem<>(task);

            for (KitchenJob job : task.getJobs()) {
                TreeItem<TaskItemInfo> jobNode = new TreeItem<>(job);
                node.getChildren().add(jobNode);
            }

            root.getChildren().add(node);
        }

        contentTree.setRoot(root);

        sortTree();
    }

    private void sortTree() {
        contentTree.getRoot()
                   .getChildren()
                   .sort(Comparator.comparing(o -> (Task) o.getValue()));
    }
}

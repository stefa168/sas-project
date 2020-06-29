package ui.task;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchentask.KitchenJob;
import businesslogic.kitchentask.KitchenTaskManager;
import businesslogic.kitchentask.Task;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import ui.WindowController;

import java.util.Comparator;

public class SummarySheetWindow extends WindowController {

    @FXML
    public Button addRecipeButton;
    @FXML
    public Button deleteRecipeButton;
    @FXML
    public Button upTaskButton;
    @FXML
    public Button downTaskButton;
    @FXML
    public Button changeDetailsButton;
    @FXML
    public Button addTaskButton;
    @FXML
    public Button deleteTaskButton;
    @FXML
    public Button cookButton;
    @FXML
    public Button endButton;
    @FXML
    TreeView<TaskItemInfo> contentTree;
    @FXML
    private BorderPane containerPane;


    private KitchenTaskManager ktm;
    private boolean controlsEnabled;

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

    public void toggleControls(boolean b) {
        this.controlsEnabled = b;
    }

    public TaskItemInfo getSelectedItem() {
        return contentTree.getSelectionModel()
                          .getSelectedItem()
                          .getValue();
    }

    public void addrecipe(ActionEvent actionEvent) {
    }

    public void deleteRecipe(ActionEvent actionEvent) {
    }

    public void goUpTask(ActionEvent actionEvent) {
        TreeItem<TaskItemInfo> itemRow = contentTree.getSelectionModel()
                                                    .getSelectedItem();
        TaskItemInfo selectedItem = itemRow.getValue();


        if (!(selectedItem instanceof Task)) {
            return;
        }

        ObservableList<TreeItem<TaskItemInfo>> children = contentTree.getRoot().getChildren();
        Task task = ((Task) selectedItem);
        int taskIndex = children.indexOf(itemRow);

        if (taskIndex > 0) {

            Task nextTask = (Task) children.get(taskIndex - 1).getValue();

            try {
                ktm.changeTaskOrder(task, nextTask);
                sortTree();

                contentTree.getSelectionModel().select(contentTree.getRow(itemRow));
            } catch (UseCaseLogicException e) {
                e.printStackTrace();
            }
        }
    }

    public void goDownTask(ActionEvent actionEvent) {
        TreeItem<TaskItemInfo> itemRow = contentTree.getSelectionModel()
                                                    .getSelectedItem();
        TaskItemInfo selectedItem = itemRow.getValue();


        if (!(selectedItem instanceof Task)) {
            return;
        }

        ObservableList<TreeItem<TaskItemInfo>> children = contentTree.getRoot().getChildren();
        Task task = ((Task) selectedItem);
        int taskIndex = children.indexOf(itemRow);

        if (children.size() - 1 > taskIndex) {

            Task nextTask = (Task) children.get(taskIndex + 1).getValue();

            try {
                ktm.changeTaskOrder(task, nextTask);
                sortTree();

                contentTree.getSelectionModel().select(contentTree.getRow(itemRow));
            } catch (UseCaseLogicException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeDetailsTask(ActionEvent actionEvent) {
    }

    public void addTask(ActionEvent actionEvent) {
    }

    public void deleteTask(ActionEvent actionEvent) {
    }

    public void cookOperations(ActionEvent actionEvent) {
    }
}

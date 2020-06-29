package ui.task;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchentask.KitchenJob;
import businesslogic.kitchentask.KitchenTaskManager;
import businesslogic.kitchentask.Task;
import businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.WindowController;
import ui.general.EventsInfoDialog;
import ui.general.TurnInfoDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public Button addKitchenJobButton;
    @FXML
    public Button deleteKitchenJobButton;
    @FXML
    public Button cookButton;
    @FXML
    public Button endButton;
    @FXML
    public Button eventDetails;
    @FXML
    public Button turnDetails;
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

    public void addExtraDuty(ActionEvent actionEvent) {
        List<Recipe> choices = Recipe.loadAllRecipes();

        ChoiceDialog<Recipe> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Scegli");
        dialog.setHeaderText("Quale ricetta deve essere aggiunta al foglio riepilogativo?");

        Optional<Recipe> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                ArrayList<Task> newTasks = ktm.addExtraDuty(result.orElseThrow());

                for (Task task : newTasks) {
                    contentTree.getRoot().getChildren().add(new TreeItem<>(task));
                }

                sortTree();
            } catch (UseCaseLogicException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteExtraDuty(ActionEvent actionEvent) {
        TreeItem<TaskItemInfo> taskItemInfoTreeItem = getTaskItemInfoTreeItem();
        TaskItemInfo selectedItem = getSelectedItem();

        if (selectedItem instanceof Task) {
            Task task = (Task) selectedItem;

            if (task.isOptionalDuty()) {
                try {
                    ktm.deleteExtraDuty(task.getDuty());
                } catch (UseCaseLogicException e) {
                    e.printStackTrace();
                }

                contentTree.getRoot().getChildren().remove(taskItemInfoTreeItem);
            }
        }
    }

    public void goUpTask(ActionEvent actionEvent) {
        TreeItem<TaskItemInfo> itemRow = getTaskItemInfoTreeItem();
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

    private TreeItem<TaskItemInfo> getTaskItemInfoTreeItem() {
        return contentTree.getSelectionModel()
                          .getSelectedItem();
    }

    public void goDownTask(ActionEvent actionEvent) {
        TreeItem<TaskItemInfo> itemRow = getTaskItemInfoTreeItem();
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

    public void addKitchenJob(ActionEvent actionEvent) {

    }

    public void deleteKitchenJob(ActionEvent actionEvent) {
    }

    public void cookOperations(ActionEvent actionEvent) {
    }

    public void showEventDetails(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../general/events-info-dialog.fxml"));
        try {
            BorderPane pane = loader.load();
            EventsInfoDialog controller = loader.getController();

            Stage stage = new Stage();

            controller.setOwnStage(stage);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Eventi presenti nel sistema");
            stage.setScene(new Scene(pane, 600, 400));


            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showTurnDetails(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../general/turn-info-dialog.fxml"));
        try {
            BorderPane pane = loader.load();
            TurnInfoDialog controller = loader.getController();

            Stage stage = new Stage();

            controller.setOwnStage(stage);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Info Turni di Cucina");
            stage.setScene(new Scene(pane, 600, 400));


            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

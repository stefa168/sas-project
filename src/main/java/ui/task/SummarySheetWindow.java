package ui.task;

import businesslogic.CatERing;
import businesslogic.TaskException;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchentask.KitchenJob;
import businesslogic.kitchentask.KitchenTaskManager;
import businesslogic.kitchentask.Task;
import businesslogic.recipe.Recipe;
import businesslogic.turn.KitchenTurn;
import businesslogic.user.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.WindowController;
import ui.general.EventsInfoDialog;
import ui.general.TurnInfoDialog;

import java.io.IOException;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

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

        contentTree.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldSelection, newSelection) -> {
                    boolean controls = !controlsEnabled;
                    addRecipeButton.setDisable(controls);
                    deleteRecipeButton.setDisable(controls);

                    TaskItemInfo selection = newSelection.getValue();

                    boolean correctSelection = controls || !(selection instanceof Task);
                    upTaskButton.setDisable(correctSelection);
                    downTaskButton.setDisable(correctSelection);

                    changeDetailsButton.setDisable(controls);

                    addKitchenJobButton.setDisable(correctSelection);
                    deleteKitchenJobButton.setDisable(controls || !(selection instanceof KitchenJob));
                    cookButton.setDisable(controls || !(selection instanceof KitchenJob));
                }
        );
    }

    //innner
    static class EditValues {
        public final int amount;
        public final int time;
        public final boolean toDo;

        public EditValues(int amount, int time, boolean toDo) {
            this.amount = amount;
            this.time = time;
            this.toDo = toDo;
        }
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
        controlsEnabled = b;

        addRecipeButton.setDisable(!b);
        deleteRecipeButton.setDisable(!b);
        upTaskButton.setDisable(!b);
        downTaskButton.setDisable(!b);
        changeDetailsButton.setDisable(!b);
        addKitchenJobButton.setDisable(!b);
        cookButton.setDisable(!b);
        eventDetails.setDisable(!b);
        turnDetails.setDisable(!b);
        deleteKitchenJobButton.setDisable(!b);
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

    public void changeDetailsSelectedElement(ActionEvent actionEvent) {
        TaskItemInfo selectedItem = getSelectedItem();



        if (selectedItem instanceof Task) {
            Task task = ((Task) selectedItem);

            Dialog<EditValues> detailsDialog = new Dialog<>();
            detailsDialog.setTitle("Modifica dettagli Compito");
            detailsDialog.setHeaderText("Imposta i nuovi valori...");

            ButtonType applyButton = new ButtonType("Applica", ButtonData.APPLY);
            detailsDialog.getDialogPane().getButtonTypes().addAll(applyButton, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField amount = new TextField(Integer.toString(task.getAmount()));
            amount.setPromptText("Quantità");
            TextField estimatedTime = new TextField(Long.toString(task.getEstimatedDuration().toMinutes()));
            estimatedTime.setPromptText("Tempo Stimato (minuti)");
            CheckBox toDoCheckbox = new CheckBox("Compito da farsi?");
            toDoCheckbox.setSelected(task.isToDo());

            grid.add(new Label("Quantità:"), 0, 0);
            grid.add(amount, 1, 0);
            grid.add(new Label("Tempo Stimato (minuti)"), 0, 1);
            grid.add(estimatedTime, 1, 1);
            grid.add(toDoCheckbox, 1, 2);

            Node applyNode = detailsDialog.getDialogPane().lookupButton(applyButton);

            amount.textProperty().addListener((observableValue, oldValue, newValue) -> {
                applyNode.setDisable(!isInteger(newValue) || !isInteger(estimatedTime.getText()));
            });

            estimatedTime.textProperty().addListener((observableValue, oldValue, newValue) -> {
                applyNode.setDisable(!isInteger(newValue) || !isInteger(amount.getText()));
            });

            detailsDialog.getDialogPane().setContent(grid);

            detailsDialog.setResultConverter(buttonType -> {
                if (buttonType == applyButton) {
                    return new EditValues(Integer.parseInt(amount.getText()),
                            Integer.parseInt(estimatedTime.getText()),
                            toDoCheckbox.isSelected());
                } else {
                    return null;
                }
            });

            Optional<EditValues> result = detailsDialog.showAndWait();

            if (result.isPresent()) {
                EditValues values = result.orElseThrow();
                try {
                    ktm.editTask(task,
                                 values.amount != task.getAmount() ? values.amount : null,
                                 values.time != task.getEstimatedDuration().toMinutes() ?
                                         Duration.ofMinutes(values.time) :
                                         null,
                                 values.toDo != task.isToDo() ? values.toDo : null);

                    contentTree.refresh();
                } catch (UseCaseLogicException | TaskException e) {
                    e.printStackTrace();
                }
            }

        } else {
            KitchenJob job = ((KitchenJob) selectedItem);

            Dialog<EditValues> detailsDialog = new Dialog<>();
            detailsDialog.setTitle("Modifica dettagli Incarico");
            detailsDialog.setHeaderText("Imposta i nuovi valori...");

            ButtonType applyButton = new ButtonType("Applica", ButtonData.APPLY);
            detailsDialog.getDialogPane().getButtonTypes().addAll(applyButton, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField amount = new TextField(Integer.toString(job.getAmount()));
            amount.setPromptText("Quantità");
            TextField estimatedTime = new TextField(Long.toString(job.getDuration().toMinutes()));
            estimatedTime.setPromptText("Tempo Stimato (minuti)");

            grid.add(new Label("Quantità:"), 0, 0);
            grid.add(amount, 1, 0);
            grid.add(new Label("Tempo Stimato (minuti)"), 0, 1);
            grid.add(estimatedTime, 1, 1);

            Node applyNode = detailsDialog.getDialogPane().lookupButton(applyButton);

            amount.textProperty().addListener((observableValue, oldValue, newValue) -> {
                applyNode.setDisable(!isInteger(newValue) || !isInteger(estimatedTime.getText()));
            });

            estimatedTime.textProperty().addListener((observableValue, oldValue, newValue) -> {
                applyNode.setDisable(!isInteger(newValue) || !isInteger(amount.getText()));
            });

            detailsDialog.getDialogPane().setContent(grid);

            detailsDialog.setResultConverter(buttonType -> {
                if (buttonType == applyButton) {
                    return new EditValues(Integer.parseInt(amount.getText()),
                            Integer.parseInt(estimatedTime.getText()),
                            false);
                } else {
                    return null;
                }
            });

            Optional<EditValues> result = detailsDialog.showAndWait();

            if (result.isPresent()) {
                EditValues values = result.orElseThrow();
                try {
                    ktm.editKitchenJob(job,
                                       values.amount != job.getAmount() ? values.amount : null,
                                       values.time != job.getDuration().toMinutes() ?
                                               Duration.ofMinutes(values.time) : null);

                    contentTree.refresh();
                } catch (TaskException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addKitchenJob(ActionEvent actionEvent) {
        TreeItem<TaskItemInfo> itemRow = getTaskItemInfoTreeItem();
        TaskItemInfo selectedItem = itemRow.getValue();

        if (selectedItem instanceof Task) {
            Task task = (Task) selectedItem;
            ArrayList<Instant> dates = Task.getDatesService(task.getTask_id());

            ArrayList<KitchenTurn> turniPossibili = KitchenTurn.loadTurnByDate(dates.get(0),dates.get(1));
            ChoiceDialog turni = new ChoiceDialog("turno", turniPossibili);
            turni.setTitle("Turni");
            turni.setHeaderText("Scegli il turno per l'incarico");
            turni.setContentText("Scegli turno:");

            Optional<KitchenTurn> result = turni.showAndWait();
            KitchenTurn turnoScelto = null;
            if (result.isPresent()) {
                turnoScelto = result.get();
            }

            if(turnoScelto!=null) {
                ArrayList<User> cooks = User.getUsersByTurnAvailabilities(turnoScelto.getTurn_id());

                ChoiceDialog cuochi = new ChoiceDialog("cuoco", cooks);
                cuochi.setTitle("Cuochi");
                cuochi.setHeaderText("Scegli il cuoco per l'incarico");
                cuochi.setContentText("Scegli cuoco:");

                Optional<User> cookResult = turni.showAndWait();
            }











        }
    }

    public void deleteKitchenJob(ActionEvent actionEvent) {
    }

    public void cookOperations(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../task/cook_assign.fxml"));
        try {

            TreeItem<TaskItemInfo> itemRow = getTaskItemInfoTreeItem();
            TaskItemInfo selectedItem = itemRow.getValue();

            if (selectedItem instanceof KitchenJob) {
                KitchenJob kitchenJob = (KitchenJob) selectedItem;


                BorderPane pane = loader.load();
                CookAssign controller = loader.getController();

                Stage stage = new Stage();

                controller.setOwnStage(stage);

                controller.setKitchenJob(kitchenJob);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Cuochi disponibili");
                stage.setScene(new Scene(pane, 600, 400));

                stage.showAndWait();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

    private boolean isInteger(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+?");

        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
}

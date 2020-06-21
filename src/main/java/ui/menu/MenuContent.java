package ui.menu;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuItem;
import businesslogic.menu.Section;
import businesslogic.recipe.Recipe;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.general.EventsInfoDialog;

import java.io.IOException;
import java.util.Optional;

public class MenuContent {
    @FXML
    Label titleLabel;

    @FXML
    ListView<Section> sectionList;

    @FXML
    Label itemsTitle;

    @FXML
    ToggleButton freeItemsToggle;

    @FXML
    ListView<MenuItem> itemsList;

    @FXML
    Button deleteSectionButton;
    @FXML
    Button editSectionButton;
    @FXML
    Button upSectionButton;
    @FXML
    Button downSectionButton;
    @FXML
    Button upItemButton;
    @FXML
    Button downItemButton;
    @FXML
    Button spostaItemButton;
    @FXML
    Button modificaItemButton;
    @FXML
    Button deleteItem;

    @FXML
    Button addItemButton;

    @FXML
    Pane itemsPane;
    @FXML
    GridPane centralPane;
    Pane emptyPane;
    boolean paneVisible = true;


    MenuManagement menuManagementController;

    public void initialize() {
        Menu toview = CatERing.getInstance().getMenuManager().getCurrentMenu();
        if (toview != null) {
            titleLabel.setText(toview.getTitle());

            sectionList.setItems(toview.getSections());
        }

        sectionList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        sectionList.getSelectionModel().select(null);
        sectionList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Section>() {
            @Override
            public void changed(ObservableValue<? extends Section> observableValue, Section oldSection, Section newSection) {
                if (newSection != null && newSection != oldSection) {
                    if (!paneVisible) {
                        centralPane.getChildren().remove(emptyPane);
                        centralPane.add(itemsPane, 1, 0);
                        paneVisible = true;
                    }
                    itemsTitle.setText("Voci di " + newSection.getName());
                    freeItemsToggle.setSelected(false);
                    itemsList.setItems(newSection.getItems());
                    // enable other section actions
                    addItemButton.setDisable(false);
                    deleteSectionButton.setDisable(false);
                    editSectionButton.setDisable(false);
                    int pos = sectionList.getSelectionModel().getSelectedIndex();
                    upSectionButton.setDisable(pos <= 0);
                    downSectionButton.setDisable(pos >= (CatERing.getInstance().getMenuManager().getCurrentMenu().getSectionCount()-1));
                } else if (newSection == null) {
                    // disable section actions
                    deleteSectionButton.setDisable(true);
                    editSectionButton.setDisable(true);
                    upSectionButton.setDisable(true);
                    downSectionButton.setDisable(true);
                }
            }
        });

        itemsList.setItems(FXCollections.emptyObservableList());
        itemsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        itemsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MenuItem>() {
            @Override
            public void changed(ObservableValue<? extends MenuItem> observableValue, MenuItem oldItem, MenuItem newItem) {
                if (newItem != null && newItem != oldItem) {
                    int pos = itemsList.getSelectionModel().getSelectedIndex();
                    int count = 0;
                    if (freeItemsToggle.isSelected()) count = CatERing.getInstance().getMenuManager().getCurrentMenu().getFreeItemCount();
                    else {
                        Section sec = sectionList.getSelectionModel().getSelectedItem();
                        if (sec != null) {
                            count = sec.getItemsCount();
                        }
                    }
                    upItemButton.setDisable(pos <= 0);
                    downItemButton.setDisable(pos >= (count-1));
                    spostaItemButton.setDisable(false);
                    modificaItemButton.setDisable(false);
                    deleteItem.setDisable(false);
                } else if (newItem == null) {
                    upItemButton.setDisable(true);
                    downItemButton.setDisable(true);
                    spostaItemButton.setDisable(true);
                    modificaItemButton.setDisable(true);
                    deleteItem.setDisable(true);
                }
            }
        });
        emptyPane = new BorderPane();
        centralPane.getChildren().remove(itemsPane);
        centralPane.add(emptyPane, 1, 0);
        paneVisible = false;

        freeItemsToggle.setSelected(false);
    }

    @FXML
    public void exitButtonPressed() {
        menuManagementController.showMenuList(CatERing.getInstance().getMenuManager().getCurrentMenu());
    }

    @FXML
    public void publishButtonPressed() {
        Menu m = CatERing.getInstance().getMenuManager().getCurrentMenu();
        try {
            CatERing.getInstance().getMenuManager().publish();
        } catch (UseCaseLogicException ex) {
            ex.printStackTrace();
        }
        menuManagementController.showMenuList(m);
    }

    @FXML
    public void addSectionPressed() {
        TextInputDialog dial = new TextInputDialog("Sezione");
        dial.setTitle("Aggiungi una sezione");
        dial.setHeaderText("Scegli il nome per la nuova sezione");
        Optional<String> result = dial.showAndWait();

        if (result.isPresent()) {
            try {
                CatERing.getInstance().getMenuManager().defineSection(result.get());
            } catch (UseCaseLogicException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    void addItemPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("insert-item-dialog.fxml"));
        try {
            BorderPane pane = loader.load();
            InsertItemDialog controller = loader.getController();

            Stage stage = new Stage();

            controller.setOwnStage(stage);

            stage.initModality(Modality.APPLICATION_MODAL);
            Section selSection = sectionList.getSelectionModel().getSelectedItem();
            stage.setTitle("Inserisci una voce " + (selSection == null ?
                    "libera" : "nella sezione " + selSection.getName()));
            stage.setScene(new Scene(pane));

            stage.showAndWait();

            Optional<Recipe> chosen = controller.getSelectedRecipe();
            Optional<String> desc = controller.getDescription();
            if (chosen.isPresent()) {
                if (selSection != null) {
                    if (desc.isPresent()) {
                        CatERing.getInstance().getMenuManager().insertItem(chosen.get(), selSection, desc.get());
                    } else {
                        CatERing.getInstance().getMenuManager().insertItem(chosen.get(), selSection);
                    }
                } else {
                    if (desc.isPresent()) {
                        CatERing.getInstance().getMenuManager().insertItem(chosen.get(), desc.get());
                    } else {
                        CatERing.getInstance().getMenuManager().insertItem(chosen.get());
                    }
                }
            }
        } catch (IOException | UseCaseLogicException ex) {
            ex.printStackTrace();
        }
    }

    public void setMenuManagementController(MenuManagement menuManagement) {
        menuManagementController = menuManagement;
    }

    @FXML
    public void eventInfoButtonPressed() {
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

    @FXML
    public void freeItemsToggleChanged() {
        if (freeItemsToggle.isSelected()) {
            this.sectionList.getSelectionModel().select(null);
            if (!paneVisible) {
                centralPane.getChildren().remove(emptyPane);
                centralPane.add(itemsPane, 1, 0);
                paneVisible = true;
            }
            itemsTitle.setText("Voci libere:");
            itemsList.setItems(CatERing.getInstance().getMenuManager().getCurrentMenu().getFreeItems());
            addItemButton.setDisable(false);
        } else {
            itemsTitle.setText("Voci");
            itemsList.setItems(FXCollections.emptyObservableList());
            addItemButton.setDisable(sectionList.getSelectionModel().getSelectedItem() == null);
            if (sectionList.getSelectionModel().getSelectedItem() == null && paneVisible) {
                centralPane.getChildren().remove(itemsPane);
                centralPane.add(emptyPane, 1, 0);
                paneVisible = false;
            }
        }
    }

    @FXML
    public void editFeaturesButtonPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu-features-dialog.fxml"));
        try {
            BorderPane pane = loader.load();
            MenuFeaturesDialog controller = loader.getController();

            Stage stage = new Stage();

            controller.setOwnStage(stage);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Caratteristiche del menu");
            stage.setScene(new Scene(pane));


            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void editTitleButtonPressed() {
        TextInputDialog dial = new TextInputDialog(CatERing.getInstance().getMenuManager().getCurrentMenu().getTitle());
        dial.setTitle("Modifica il titolo");
        dial.setHeaderText("Scegli un nuovo titolo");
        Optional<String> result = dial.showAndWait();

        if (result.isPresent()) {
            try {
                CatERing.getInstance().getMenuManager().changeTitle(result.get());
                this.titleLabel.setText(result.get());
            } catch (UseCaseLogicException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void deleteSectionButtonPressed() {
        Section sec = sectionList.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancellazione di una sezione");
        alert.setHeaderText("Vuoi cancellare anche le voci?");
        alert.setContentText("Se sceglierai di non cancellarle verranno conservate fra le voci libere.");

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        boolean deleteItems = false;
        if (result.get() == buttonTypeOne) {
            deleteItems = true;
        }
        try {
            CatERing.getInstance().getMenuManager().deleteSection(sec, deleteItems);
        } catch (UseCaseLogicException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void editSectionButtonPressed() {
        Section sec = sectionList.getSelectionModel().getSelectedItem();
        TextInputDialog dial = new TextInputDialog(sec.getName());
        dial.setTitle("Modifica sezione");
        dial.setHeaderText("Scegli un nuovo nome per la sezione");
        Optional<String> result = dial.showAndWait();

        if (result.isPresent()) {
            try {
                CatERing.getInstance().getMenuManager().changeSectionName(sec, result.get());
                sectionList.refresh();
            } catch (UseCaseLogicException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void upSectionPressed() {
        this.changeSectionPosition(-1);
    }


    @FXML
    public void downSectionPressed() {
        this.changeSectionPosition(+1);
    }

    private void changeSectionPosition(int change) {
        int newpos = sectionList.getSelectionModel().getSelectedIndex() + change;
        Section sec = sectionList.getSelectionModel().getSelectedItem();
        try {
            CatERing.getInstance().getMenuManager().moveSection(sec, newpos);
            sectionList.refresh();
            sectionList.getSelectionModel().select(newpos);
        } catch (UseCaseLogicException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void upItemPressed() {
        this.changeItemPosition(-1);
    }


    @FXML
    public void downItemPressed() {
        this.changeItemPosition(+1);
    }

    @FXML
    public void spostaItemPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("choose-section-dialog.fxml"));
        try {
            BorderPane pane = loader.load();
            ChooseSectionDialog controller = loader.getController();

            Stage stage = new Stage();

            controller.setOwnStage(stage);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Scegli la nuova sezione");
            stage.setScene(new Scene(pane));


            stage.showAndWait();

            if (controller.isOK()) {
                MenuItem mi = this.itemsList.getSelectionModel().getSelectedItem();
                Section s = controller.getChosenSection();
                try {
                    if (s == null) {
                        CatERing.getInstance().getMenuManager().assignItemToSection(mi);
                        itemsList.refresh();
                    } else {
                        CatERing.getInstance().getMenuManager().assignItemToSection(mi, s);
                        itemsList.refresh();
                    }
                } catch (UseCaseLogicException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void modificaItemPressed() {
        MenuItem mi = itemsList.getSelectionModel().getSelectedItem();
        TextInputDialog dial = new TextInputDialog(mi.getDescription());
        dial.setTitle("Modifica la voce");
        dial.setHeaderText("Scegli una nuova descrizione");
        Optional<String> result = dial.showAndWait();

        if (result.isPresent() && !result.equals(mi.getDescription())) {
            try {
                CatERing.getInstance().getMenuManager().editMenuItemDescription(mi, result.get());
                itemsList.refresh();
            } catch (UseCaseLogicException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void deleteItemPressed() {
        MenuItem mi = itemsList.getSelectionModel().getSelectedItem();
        try {
            CatERing.getInstance().getMenuManager().deleteItem(mi);
            itemsList.refresh();
        } catch (UseCaseLogicException ex) {
            ex.printStackTrace();
        }
    }

    private void changeItemPosition(int change) {
        int newpos = itemsList.getSelectionModel().getSelectedIndex() + change;
        Section sec = sectionList.getSelectionModel().getSelectedItem();
        MenuItem mi = itemsList.getSelectionModel().getSelectedItem();
        try {
            if (sec == null) {
                CatERing.getInstance().getMenuManager().moveMenuItem(mi, newpos);
            }
            else {
                CatERing.getInstance().getMenuManager().moveMenuItem(mi, sec, newpos);
            }
            itemsList.refresh();
            itemsList.getSelectionModel().select(newpos);
        } catch (UseCaseLogicException ex) {
            ex.printStackTrace();
        }
    }
}

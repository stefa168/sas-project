package ui.menu;

import businesslogic.CatERing;
import businesslogic.menu.Section;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ChooseSectionDialog {
    @FXML
    ComboBox<Section> sectionComboBox;

    @FXML
    CheckBox freeItemsCheckBox;

    @FXML
    Button okButton;

    private boolean ok = false;
    private Stage myStage;

    public void initialize() {
        ok = false;
        sectionComboBox.setItems(CatERing.getInstance().getMenuManager().getCurrentMenu().getSections());
    }

    public void setOwnStage(Stage stage) {
        myStage = stage;
    }


    public boolean isOK() {
        return ok;
    }

    @FXML
    public void okButtonPressed() {
        ok = true;
        myStage.close();

    }

    @FXML
    public void cancelButtonPressed() {
        ok = false;
        myStage.close();
    }

    @FXML
    public void freeItemsToggled() {
        this.sectionComboBox.setDisable(freeItemsCheckBox.isSelected());
        okButton.setDisable(!freeItemsCheckBox.isSelected() && sectionComboBox.getValue() == null);
    }

    @FXML void sectionComboChanged() {
        okButton.setDisable(!freeItemsCheckBox.isSelected() && sectionComboBox.getValue() == null);
    }

    public Section getChosenSection() {
        if (this.freeItemsCheckBox.isSelected()) return null;
        return this.sectionComboBox.getValue();
    }
}

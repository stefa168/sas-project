package ui.menu;

import businesslogic.CatERing;
import businesslogic.recipe.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class InsertItemDialog {
    Stage myStage;

    @FXML
    ComboBox<Recipe> recipeCombo;

    @FXML
    CheckBox descCheck;

    @FXML
    TextField descField;

    @FXML
    Button insertButton;

    // Results
    private Recipe selectedRecipe;
    private boolean hasDescription;
    private String description;
    private boolean confirmed;

    public void initialize() {
        recipeCombo.setItems(CatERing.getInstance().getRecipeManager().getRecipes());
        confirmed = false;
    }

    public void setOwnStage(Stage stage) {
        myStage = stage;
    }

    @FXML
    public void recipeComboSelection() {
        Recipe sel = recipeCombo.getValue();
        descCheck.setDisable(sel == null);
        descField.setDisable(sel == null || !descCheck.isSelected());
        insertButton.setDisable(sel == null);

    }

    @FXML
    public void descCheckSelection() {
        if (descCheck.isSelected()) {
            descField.setDisable(false);
        } else {
            descField.setText("");
            descField.setDisable(true);
        }
    }

    @FXML
    public void aggiungiButtonPressed() {
        confirmed = true;
        selectedRecipe = recipeCombo.getValue();
        hasDescription = descCheck.isSelected();
        description = descField.getText();
        myStage.close();
    }

    @FXML
    public void annullaButtonPressed() {
        confirmed = false;
        myStage.close();
    }

    public Optional<Recipe> getSelectedRecipe() {
        if (!confirmed) selectedRecipe = null;
        return Optional.ofNullable(selectedRecipe);
    }

    public Optional<String> getDescription() {
        if (!confirmed || !hasDescription) description = null;
        return Optional.ofNullable(description);
    }
}

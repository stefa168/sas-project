package businesslogic.recipe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class RecipeManager {

    private ArrayList<Recipe> recipes;

    public RecipeManager() {
        Recipe.loadAllRecipes();
    }

    public ObservableList<Recipe> getRecipes() {
        return FXCollections.unmodifiableObservableList(Recipe.getAllRecipes());
    }

    public ArrayList<Recipe> getRecipeBook(){
        return recipes;
    }
}

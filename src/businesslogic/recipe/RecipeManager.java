package businesslogic.recipe;

import persistence.PersistenceManager;

import java.util.ArrayList;

public class RecipeManager {
    private ArrayList<Recipe> recipes;

    public RecipeManager() {
        recipes = Recipe.loadAllRecipes();
    }

    public Recipe[] getRecipes() {
        return this.recipes.toArray(new Recipe[recipes.size()]);
    }
}

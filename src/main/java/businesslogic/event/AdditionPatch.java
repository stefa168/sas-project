package businesslogic.event;

import businesslogic.recipe.Recipe;

public class AdditionPatch implements Patch{
    private Recipe duty;

    AdditionPatch(Recipe recipe){
        this.duty = recipe;
    }
}

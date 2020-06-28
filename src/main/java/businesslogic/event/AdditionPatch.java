package businesslogic.event;

import businesslogic.recipe.Recipe;

public class AdditionPatch extends Patch{
    private Recipe duty;


    AdditionPatch(Recipe recipe, int patch_id){
        super(patch_id);
        this.duty = recipe;

    }

    public Recipe getDuty() {
        return duty;
    }
}

package businesslogic.event;

import businesslogic.menu.MenuItem;

public class RemovalPatch extends Patch {
    private MenuItem menuItem;


    RemovalPatch(MenuItem menuItem, int patch_id){
        super(patch_id);
        this.patch_id = patch_id;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }


}

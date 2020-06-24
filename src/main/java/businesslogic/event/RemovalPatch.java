package businesslogic.event;

import businesslogic.menu.MenuItem;

class RemovalPatch implements Patch {
    private MenuItem menuItem;

    RemovalPatch(MenuItem menuItem){
        this.menuItem = menuItem;
    }
}

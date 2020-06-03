package persistence;

import businesslogic.menu.Menu;
import businesslogic.menu.MenuEventReceiver;

public class MenuPersistence implements MenuEventReceiver {

    @Override
    public void updateMenuCreated(Menu m) {
        Menu.saveNewMenu(m);
    }
}

package businesslogic.menu;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class MenuManager {
    private String[] menuFeatures = {"Richiede cucina", "Richiede cuoco", "Finger food", "Buffet", "Piatti caldi"};
    private Menu currentMenu;
    private ArrayList<MenuEventReceiver> eventReceivers;

    public MenuManager() {
        eventReceivers = new ArrayList<>();
    }

    public Menu createMenu() throws UseCaseLogicException {
        return this.createMenu(null);
    }

    public Menu createMenu(String title) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }

        Menu m = new Menu(user, title, menuFeatures);
        this.setCurrentMenu(m);
        this.notifyMenuAdded(m);

        return m;
    }

    private void notifyMenuAdded(Menu m) {
        for (MenuEventReceiver er: this.eventReceivers) {
            er.updateMenuCreated(m);
        }
    }

    public void setCurrentMenu(Menu m) {
        this.currentMenu = m;
    }

    public Menu getCurrentMenu() {
        return this.currentMenu;
    }

    public ObservableList<Menu> getAllMenus() {
        // TODO: implementazione fittizia
        return FXCollections.observableArrayList();
    }

    public void addEventReceiver(MenuEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    public void removeEventReceiver(MenuEventReceiver rec) {
        this.eventReceivers.remove(rec);
    }
}

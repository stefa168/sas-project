package businesslogic;

import businesslogic.event.EventManager;
import businesslogic.kitchentask.KitchenTaskManager;
import businesslogic.menu.MenuManager;
import businesslogic.recipe.RecipeManager;
import businesslogic.user.UserManager;
import persistence.KitchenTaskPersistence;
import persistence.MenuPersistence;

public class CatERing {
    private static CatERing singleInstance;
    private MenuManager menuMgr;
    private RecipeManager recipeMgr;
    private UserManager userMgr;
    private EventManager eventMgr;
    private KitchenTaskManager kitchenTaskMgr;
    private MenuPersistence menuPersistence;
    private KitchenTaskPersistence kitchenTaskPersistence;

    private CatERing() {
        menuMgr = new MenuManager();
        recipeMgr = new RecipeManager();
        userMgr = new UserManager();
        eventMgr = new EventManager();
        menuPersistence = new MenuPersistence();
        menuMgr.addEventReceiver(menuPersistence);
        kitchenTaskMgr = new KitchenTaskManager();
        kitchenTaskPersistence = new KitchenTaskPersistence();
        kitchenTaskMgr.addReceiver(kitchenTaskPersistence);
    }

    public static CatERing getInstance() {
        if (singleInstance == null) {
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public RecipeManager getRecipeManager() {
        return recipeMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() { return eventMgr; }

    public KitchenTaskManager getKitchenTaskManager() {return kitchenTaskMgr;}

}

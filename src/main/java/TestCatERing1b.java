import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuException;
import businesslogic.menu.Section;
import businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.Map;

public class TestCatERing1b {
    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            Menu m = CatERing.getInstance().getMenuManager().createMenu("Menu Pinco Pallino");

            Section sec = CatERing.getInstance().getMenuManager().defineSection("Antipasti");

            ObservableList<Recipe> recipes = CatERing.getInstance().getRecipeManager().getRecipes();
            for (int i=0; i < 4 && i < recipes.size(); i++) {
                CatERing.getInstance().getMenuManager().insertItem(recipes.get(i), sec);
            }

            for (int i=recipes.size()-1; i > recipes.size()-4 && i >= 0; i--) {
                CatERing.getInstance().getMenuManager().insertItem(recipes.get(i));
            }

            CatERing.getInstance().getMenuManager().publish();
            System.out.println("\nMENU CREATO");
            System.out.println(m.testString());

            System.out.println("\nTEST DELETE");
            CatERing.getInstance().getMenuManager().deleteMenu(m);

        } catch (UseCaseLogicException | MenuException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}

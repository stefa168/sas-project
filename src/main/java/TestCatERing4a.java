import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuItem;
import businesslogic.menu.Section;
import businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;

public class TestCatERing4a {
    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());
            Menu m = CatERing.getInstance().getMenuManager().createMenu("Menu Pinco Pallino");
            Section sec = CatERing.getInstance().getMenuManager().defineSection("Antipasti");

            ObservableList<Recipe> recipes = CatERing.getInstance().getRecipeManager().getRecipes();
            MenuItem[] its = new MenuItem[4];
            for (int i=0; i < 4 && i < recipes.size(); i++) {
                its[i] = CatERing.getInstance().getMenuManager().insertItem(recipes.get(i), sec);
            }

            MenuItem[] its2 = new MenuItem[3];
            for (int i=recipes.size()-1; i > recipes.size()-4 && i >= 0; i--) {
                its2[recipes.size()-1-i] = CatERing.getInstance().getMenuManager().insertItem(recipes.get(i));

            }
            Section sec1 = CatERing.getInstance().getMenuManager().defineSection("Primi");
            Section sec2 = CatERing.getInstance().getMenuManager().defineSection("Secondi");
            System.out.println(m.testString());

            System.out.println("\nTEST CHANGE ITEM SECTION");
            CatERing.getInstance().getMenuManager().assignItemToSection(its[0], sec2);
            CatERing.getInstance().getMenuManager().assignItemToSection(its[1], sec2);
            CatERing.getInstance().getMenuManager().assignItemToSection(its[2]);
            CatERing.getInstance().getMenuManager().assignItemToSection(its[3]);
            CatERing.getInstance().getMenuManager().assignItemToSection(its2[0], sec1);
            CatERing.getInstance().getMenuManager().assignItemToSection(its2[1], sec1);
            CatERing.getInstance().getMenuManager().assignItemToSection(its2[2], sec1);
            System.out.println(m.testString());

        } catch (UseCaseLogicException ex) {
            System.out.println("Errore di logica nello use case");
        }
    }
}

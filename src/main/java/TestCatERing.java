import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.menu.Menu;
import persistence.PersistenceManager;

public class TestCatERing {
    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            System.out.println("\n TEST CREATE MENU");
            Menu m = CatERing.getInstance().getMenuManager().createMenu("Prova menu");
            System.out.println(m.toString());
        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}

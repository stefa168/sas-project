package businesslogic.recipe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Recipe extends KitchenDuty {
    private static Map<Integer, Recipe> all = new HashMap<>();
    private ArrayList<KitchenDuty> subDuties;

    private int id;

    public Recipe(String name, String instruction, int resultingAmount, Duration constantConcreteActivityTime,
                  Duration variableConcreteActivityTime) {
        super(name, instruction, resultingAmount, constantConcreteActivityTime, variableConcreteActivityTime);
        subDuties = new ArrayList<>();
    }

    //Questi 2 costruttori vengono usati sotto per l'accesso al DB
    public Recipe(String name) {
        super(name);
        id = 0;
        subDuties = new ArrayList<>();
    }

    private Recipe() {
        super();
        subDuties = new ArrayList<>();
    }

    public static ObservableList<Recipe> loadAllRecipes() {
        String query = "SELECT * FROM Recipes";
        PersistenceManager.executeQuery(query, rs -> {
            int id = rs.getInt("id");
            if (all.containsKey(id)) {
                Recipe rec = all.get(id);
                rec.name = rs.getString("name");
            } else {
                Recipe rec = new Recipe(rs.getString("name"));
                rec.id = id;
                rec.constantConcreteActivityTime = Duration.ofMinutes(rs.getInt("tacc"));
                rec.variableConcreteActivityTime = Duration.ofMinutes(rs.getInt("tacv"));
                rec.subDuties = KitchenDuty.loadAllSubKitchenDuty(rec.id);
                all.put(rec.id, rec);
            }
        });
        ObservableList<Recipe> ret = FXCollections.observableArrayList(all.values());
        ret.sort(Comparator.comparing(Recipe::getName));
        return ret;
    }

    public static ObservableList<Recipe> getAllRecipes() {
        return FXCollections.observableArrayList(all.values());
    }

    public static Recipe getRecipeById(int id) {
        String query = "SELECT * FROM Recipes WHERE id =" + id;
        Recipe recipe = new Recipe();

        PersistenceManager.executeQuery(query, rs -> {
            recipe.id = rs.getInt("id");
            recipe.name = rs.getString("name");
            ArrayList<KitchenDuty> kitchenDuties = new ArrayList<>();
            kitchenDuties = KitchenDuty.loadAllSubKitchenDuty(recipe.id);
            recipe.subDuties = KitchenDuty.loadAllSubKitchenDuty(recipe.id);
            recipe.constantConcreteActivityTime = Duration.ofMinutes(rs.getInt("tacc"));
            recipe.variableConcreteActivityTime = Duration.ofMinutes(rs.getInt("tacv"));
        });
        return recipe;
    }

    public static Recipe loadRecipeById(int id) {
        if (all.containsKey(id)) return all.get(id);
        Recipe rec = new Recipe();
        String query = "SELECT * FROM Recipes WHERE id = " + id;
        PersistenceManager.executeQuery(query, rs -> {
            rec.name = rs.getString("name");
            rec.id = id;
            rec.constantConcreteActivityTime = Duration.ofMinutes(rs.getInt("tacc"));
            rec.variableConcreteActivityTime = Duration.ofMinutes(rs.getInt("tacv"));
            rec.subDuties = KitchenDuty.loadAllSubKitchenDuty(rec.id);
            all.put(rec.id, rec);
        });
        return rec;
    }

    public String getName() {
        return name;
    }

    // STATIC METHODS FOR PERSISTENCE

    public int getId() {
        return id;
    }

    public String toString() {
        return String.format("%s (TACc %d', TACv %d')",
                             name,
                             constantConcreteActivityTime.toMinutes(),
                             variableConcreteActivityTime.toMinutes());
    }

    @Override
    public ArrayList<KitchenDuty> getSubDuties() {
        return subDuties;
    }

    @Override
    public int getKitchenDutyId() {
        return getId();
    }
}

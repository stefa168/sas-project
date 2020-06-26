package businesslogic.recipe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;

public class Recipe extends KitchenDuty{
    private static Map<Integer, Recipe> all = new HashMap<>();
    private ArrayList<Preparation> subDuties;

    private int id;

    public Recipe(String name, String instruction, int resultingAmount, Duration constantConcreteActivityTime, Duration variableConcreteActivityTime){
        super(name,instruction,resultingAmount,constantConcreteActivityTime,variableConcreteActivityTime);
        subDuties = new ArrayList<>();
    }

    //Questi 2 costruttori vengono usati sotto per l'accesso al DB
    public Recipe(String name) {
        super(name);
        id = 0;
        subDuties = new ArrayList<>();
    }

    private Recipe(){
        super();
        subDuties = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return name;
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<Recipe> loadAllRecipes() {
        String query = "SELECT * FROM Recipes";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                if (all.containsKey(id)) {
                    Recipe rec = all.get(id);
                    rec.name = rs.getString("name");
                } else {
                    Recipe rec = new Recipe(rs.getString("name"));
                    rec.id = id;
                    all.put(rec.id, rec);
                }
            }
        });
        ObservableList<Recipe> ret =  FXCollections.observableArrayList(all.values());
        Collections.sort(ret, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe o1, Recipe o2) {
                return (o1.getName().compareTo(o2.getName()));
            }
        });
        return ret;
    }

    public static ObservableList<Recipe> getAllRecipes() {
        return FXCollections.observableArrayList(all.values());
    }

    public static Recipe loadRecipeById(int id) {
        if (all.containsKey(id)) return all.get(id);
        Recipe rec = new Recipe();
        String query = "SELECT * FROM Recipes WHERE id = " + id;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                    rec.name = rs.getString("name");
                    rec.id = id;
                    all.put(rec.id, rec);
            }
        });
        return rec;
    }


    @Override
    public ArrayList<KitchenDuty> getSubDuties() {
        //TODO
        return null;
    }
}

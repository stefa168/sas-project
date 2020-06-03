package businesslogic.recipe;

import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Recipe {
    private int id;
    private String name;

    public Recipe(String name) {
        id = 0;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


    public static ArrayList<Recipe> loadAllRecipes() {
        ArrayList<Recipe> result = new ArrayList<>();
        String query = "SELECT * FROM Recipes";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Recipe rec = new Recipe(rs.getString("name"));
                rec.id = rs.getInt("id");
                result.add(rec);
            }
        });
        return result;
    }
}

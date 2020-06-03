package businesslogic.menu;

import businesslogic.recipe.Recipe;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuItem {
    private int id;
    private String description;
    private Recipe itemRecipe;

    public MenuItem(Recipe rec) {
        this(rec, rec.getName());
    }

    public MenuItem(Recipe rec, String desc) {
        id = 0;
        itemRecipe = rec;
        description = desc;
    }
    public String toString() {
        return description;
    }

    public static void saveAllNewItems(int menuid, int sectionid, ArrayList<MenuItem> items) {
        String itemInsert = "INSERT INTO catering.MenuItems (menu_id, section_id, description, recipe_id, position) VALUES (?, ?, ?, ?, ?);";
        PersistenceManager.executeUpdate(itemInsert, items.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, menuid);
                ps.setInt(2, sectionid);
                ps.setString(3, items.get(batchCount).description);
                ps.setInt(4, items.get(batchCount).itemRecipe.getId());
                ps.setInt(5, batchCount);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                items.get(count).id = rs.getInt(1);
            }
        });
    }
}

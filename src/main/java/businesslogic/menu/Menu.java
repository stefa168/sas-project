package businesslogic.menu;

import businesslogic.CatERing;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Menu {
    private int id;
    private String title;
    private boolean published;
    private boolean inUse;
    private String[] features;
    private boolean[] featureValues;
    private ArrayList<MenuItem> freeItems;
    private ArrayList<Section> sections;

    private User owner;

    public Menu(User user, String title, String[] menuFeatures) {
        id = 0;

        if (title != null) {
            this.title = title;
        }

        this.owner = user;

        this.features = menuFeatures;

        this.sections = new ArrayList<>();
        this.freeItems = new ArrayList<>();

        this.featureValues = new boolean[menuFeatures.length];
    }

    public String toString() {
        String result = title + " (" + id + ")" + "\n";
        for (int i=0; i < features.length; i++) {
            result += features[i] + ": " + featureValues[i] + "\n";
        }

        result += "\n";
        for (Section sec: sections) {
            result += sec.toString();
            result += "\n";
        }

        if (freeItems.size() > 0) {
            result+="\n" + "VOCI LIBERE:";
        }
        for (MenuItem mi: freeItems) {
            result += mi.toString();
        }

        return result;
    }

    public String getTitle() {
        return this.title;
    }

    public void addFakeSections() {
        this.sections.add(new Section("Antipasti"));
        this.sections.add(new Section("Primi"));
        this.sections.add(new Section("Secondi"));
        this.sections.add(new Section("Dessert"));

        Recipe[] all = CatERing.getInstance().getRecipeManager().getRecipes();
        freeItems.add(new MenuItem(all[3]));
        freeItems.add(new MenuItem(all[4]));
        freeItems.add(new MenuItem(all[5]));
    }


    public static void saveNewMenu(Menu m) {
        String menuInsert = "INSERT INTO catering.Menus (title, owner_id, published) VALUES (?, ?, ?);";
        int[] result = PersistenceManager.executeUpdate(menuInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setString(1, m.title);
                ps.setInt(2, m.owner.getId());
                ps.setBoolean(3, m.published);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    m.id = rs.getInt(1);
                }
            }
        });

        if (result[0] > 0) { // menu effettivamente inserito
            // salva le features
            String featureInsert = "INSERT INTO catering.MenuFeatures (menu_id, name, value) VALUES (?, ?, ?)";
            PersistenceManager.executeUpdate(featureInsert, m.features.length, new BatchUpdateHandler() {
                @Override
                public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                    ps.setInt(1, m.id);
                    ps.setString(2, m.features[batchCount]);
                    ps.setBoolean(3, m.featureValues[batchCount]);
                }

                @Override
                public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                    // non ci sono id autogenerati in MenuFeatures
                }
            });

            // salva le sezioni
            if (m.sections.size() > 0) {
                Section.saveAllNewSections(m.id, m.sections);
            }

            // salva le voci libere
            if (m.freeItems.size() > 0) {
                MenuItem.saveAllNewItems(m.id, 0, m.freeItems);
            }
        }
    }
}

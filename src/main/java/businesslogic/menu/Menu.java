package businesslogic.menu;

import businesslogic.CatERing;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Menu {
    private static Map<Integer, Menu> loadedMenus = FXCollections.observableHashMap();
    private int id;
    private String title;
    private boolean published;
    private boolean inUse;

    private ObservableMap<String, Boolean> featuresMap;
    private ObservableList<MenuItem> freeItems;
    private ObservableList<Section> sections;

    private User owner;

    private Menu() {
        this.featuresMap = FXCollections.observableHashMap();
    }

    public Menu(User user, String title, String[] menuFeatures) {
        id = 0;

        if (title != null) {
            this.title = title;
        }

        this.owner = user;

        this.featuresMap = FXCollections.observableHashMap();


        for (String s : menuFeatures) {
            this.featuresMap.put(s, false);
        }

        this.sections = FXCollections.observableArrayList();
        this.freeItems = FXCollections.observableArrayList();

    }

    public Menu(User owner, Menu m) {
        this.id = 0;
        this.title = m.title;
        this.published = false;
        this.inUse = false;
        this.owner = owner;
        this.featuresMap = FXCollections.observableHashMap();
        for (String feat: m.featuresMap.keySet()) {
            this.featuresMap.put(feat, m.featuresMap.get(feat));
        }

        this.sections = FXCollections.observableArrayList();
        for (Section original: m.sections) {
            this.sections.add(new Section(original));
        }

        this.freeItems = FXCollections.observableArrayList();
        for (MenuItem original: m.freeItems) {
            this.freeItems.add(new MenuItem(original));
        }

    }

    public static void savefreeItemDeleted(Menu m, MenuItem mi) {

    }

    public boolean getFeatureValue(String feature) {
        return this.featuresMap.get(feature);
    }

    public void setFeatureValue(String feature, boolean val) {
        if (this.featuresMap.containsKey(feature)) {
            this.featuresMap.put(feature, val);
        }
    }

    public String testString() {
        String result = this.toString() + "\n";
        for (String f : featuresMap.keySet()) {
            result += f + ": " + featuresMap.get(f) + "\n";
        }

        result += "\n";
        for (Section sec : sections) {
            result += sec.testString();
            result += "\n";
        }

        if (freeItems.size() > 0) {
            result += "\n" + "VOCI LIBERE:\n";
        }
        for (MenuItem mi : freeItems) {
            result += "\t" + mi.toString() + "\n";
        }

        return result;
    }

    public String toString() {
        return title + " (autore: " + owner.getUserName() + ")," + (published ? " " : " non ") +
                "pubblicato," + (inUse ? " " : " non ") + "in uso";
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return this.title;
    }

    public void addFakeSections() {
        this.sections.add(new Section("Antipasti"));
        this.sections.add(new Section("Primi"));
        this.sections.add(new Section("Secondi"));
        this.sections.add(new Section("Dessert"));

        Recipe[] all = CatERing.getInstance().getRecipeManager().getRecipes().toArray(new Recipe[0]);
        freeItems.add(new MenuItem(all[3]));
        freeItems.add(new MenuItem(all[4]));
        freeItems.add(new MenuItem(all[5]));
    }


    public Section addSection(String name) {
        Section sec = new Section(name);
        this.sections.add(sec);
        return sec;
    }

    public MenuItem addItem(Recipe recipe, Section sec, String desc) {
        MenuItem mi = new MenuItem(recipe, desc);
        if (sec != null) {
            sec.addItem(mi);
        } else {
            this.freeItems.add(mi);
        }
        return mi;
    }

    public int getSectionPosition(Section sec) {
        return this.sections.indexOf(sec);
    }

    public ObservableList<Section> getSections() {
        return FXCollections.unmodifiableObservableList(this.sections);
    }

    public Section getSectionForItem(MenuItem mi) {
        for (Section sec : sections) {
            if (sec.getItemPosition(mi) >= 0)
                return sec;
        }
        if (freeItems.indexOf(mi) >= 0) return null;
        throw new IllegalArgumentException();
    }

    public int getFreeItemPosition(MenuItem mi) {
        return freeItems.indexOf(mi);
    }

    public ObservableList<MenuItem> getFreeItems() {
        return FXCollections.unmodifiableObservableList(this.freeItems);
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setPublished(boolean b) {
        published = b;
    }


    public boolean isInUse() {
        return this.inUse;
    }

    public boolean isOwner(User u) {
        return u.getId() == this.owner.getId();
    }

    public ObservableMap<String, Boolean> getFeatures() {
        return FXCollections.unmodifiableObservableMap(this.featuresMap);
    }

    public void updateFreeItems(ObservableList<MenuItem> newItems) {
        ObservableList<MenuItem> updatedList = FXCollections.observableArrayList();
        for (int i = 0; i < newItems.size(); i++) {
            MenuItem mi = newItems.get(i);
            MenuItem prev = this.findItemById(mi.getId());
            if (prev == null) {
                updatedList.add(mi);
            } else {
                prev.setDescription(mi.getDescription());
                prev.setItemRecipe(mi.getItemRecipe());
                updatedList.add(prev);
            }
        }
        this.freeItems.clear();
        this.freeItems.addAll(updatedList);
    }

    private MenuItem findItemById(int id) {
        for (MenuItem mi : freeItems) {
            if (mi.getId() == id) return mi;
        }
        return null;
    }

    private void updateSections(ObservableList<Section> newSections) {
        ObservableList<Section> updatedList = FXCollections.observableArrayList();
        for (int i = 0; i < newSections.size(); i++) {
            Section sec = newSections.get(i);
            Section prev = this.findSectionById(sec.getId());
            if (prev == null) {
                updatedList.add(sec);
            } else {
                prev.setName(sec.getName());
                prev.updateItems(sec.getItems());
                updatedList.add(prev);
            }
        }
        this.sections.clear();
        this.sections.addAll(updatedList);
    }

    private Section findSectionById(int id) {
        for (Section s : sections) {
            if (s.getId() == id) return s;
        }
        return null;
    }


    public void removeSection(Section s, boolean deleteItems) {
        if (!deleteItems) {
            this.freeItems.addAll(s.getItems());
        }
        this.sections.remove(s);
    }

    public int getSectionCount() {
        return sections.size();
    }

    public int getFreeItemCount() {
        return freeItems.size();
    }


    public void moveSection(Section sec, int position) {
        sections.remove(sec);
        sections.add(position, sec);
    }


    public void changeItemSection(MenuItem mi, Section oldsec, Section sec) {
        if (oldsec == null) {
            freeItems.remove(mi);
        } else {
            oldsec.removeItem(mi);
        }

        if (sec == null) {
            freeItems.add(mi);
        } else {
            sec.addItem(mi);
        }
    }

    public void moveFreeItem(MenuItem mi, int position) {
        this.freeItems.remove(mi);
        this.freeItems.add(position, mi);
    }

    public void removeItem(MenuItem mi) {
        Section sec = getSectionForItem(mi);
        if (sec == null) freeItems.remove(mi);
        else sec.removeItem(mi);
    }

    // STATIC METHODS FOR PERSISTENCE

    public static void saveNewMenu(Menu m) {
        String menuInsert = "INSERT INTO catering.Menus (title, owner_id, published) VALUES (?, ?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(menuInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setString(1, PersistenceManager.escapeString(m.title));
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
            featuresToDB(m);

            // salva le sezioni
            if (m.sections.size() > 0) {
                Section.saveAllNewSections(m.id, m.sections);
            }

            // salva le voci libere
            if (m.freeItems.size() > 0) {
                MenuItem.saveAllNewItems(m.id, 0, m.freeItems);
            }
            loadedMenus.put(m.id, m);
        }
    }

    public static void saveMenuTitle(Menu m) {
        String upd = "UPDATE Menus SET title = '" + PersistenceManager.escapeString(m.getTitle()) + "'" +
                " WHERE id = " + m.getId();
        PersistenceManager.executeUpdate(upd);
    }

    public static void saveMenuFeatures(Menu m) {
        // Delete existing features if any
        String updDel = "DELETE FROM MenuFeatures WHERE menu_id = " + m.getId();
        int ret = PersistenceManager.executeUpdate(updDel);

        featuresToDB(m);
    }


    public static void saveMenuPublished(Menu m) {
        String upd = "UPDATE Menus SET published = " + m.published +
                " WHERE id = " + m.getId();
        PersistenceManager.executeUpdate(upd);
    }

    private static void featuresToDB(Menu m) {
        // Save features
        String featureInsert = "INSERT INTO catering.MenuFeatures (menu_id, name, value) VALUES (?, ?, ?)";
        String[] features = m.featuresMap.keySet().toArray(new String[0]);
        PersistenceManager.executeBatchUpdate(featureInsert, features.length, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, m.id);
                ps.setString(2, PersistenceManager.escapeString(features[batchCount]));
                ps.setBoolean(3, m.featuresMap.get(features[batchCount]));
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // non ci sono id autogenerati in MenuFeatures
            }
        });
    }


    public static void deleteMenu(Menu m) {
        // delete sections
        String delSec = "DELETE FROM MenuSections WHERE menu_id = " + m.id;
        PersistenceManager.executeUpdate(delSec);

        // delete free & section items
        String delIt = "DELETE FROM MenuItems WHERE menu_id = " + m.id;
        PersistenceManager.executeUpdate(delIt);

        // delete features
        String delFeat = "DELETE FROM MenuFeatures WHERE menu_id = " + m.getId();
        PersistenceManager.executeUpdate(delFeat);


        String del = "DELETE FROM Menus WHERE id = " + m.getId();
        PersistenceManager.executeUpdate(del);
        loadedMenus.remove(m);
    }

    public static ObservableList<Menu> loadAllMenus() {
        String query = "SELECT * FROM Menus WHERE " + true;
        ArrayList<Menu> newMenus = new ArrayList<>();
        ArrayList<Integer> newMids = new ArrayList<>();
        ArrayList<Menu> oldMenus = new ArrayList<>();
        ArrayList<Integer> oldMids = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                if (loadedMenus.containsKey(id)) {
                    Menu m = loadedMenus.get(id);
                    m.title = rs.getString("title");
                    m.published = rs.getBoolean("published");
                    oldMids.add(rs.getInt("owner_id"));
                    oldMenus.add(m);
                } else {
                    Menu m = new Menu();
                    m.id = id;
                    m.title = rs.getString("title");
                    m.published = rs.getBoolean("published");
                    newMids.add(rs.getInt("owner_id"));
                    newMenus.add(m);
                }
            }
        });

        for (int i = 0; i < newMenus.size(); i++) {
            Menu m = newMenus.get(i);
            m.owner = User.loadUserById(newMids.get(i));

            // load features
            String featQ = "SELECT * FROM MenuFeatures WHERE menu_id = " + m.id;
            PersistenceManager.executeQuery(featQ, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    m.featuresMap.put(rs.getString("name"), rs.getBoolean("value"));
                }
            });

            // load sections
            m.sections = Section.loadSectionsFor(m.id);

            // load free items
            m.freeItems = MenuItem.loadItemsFor(m.id, 0);

            // find if "in use"
            String inuseQ = "SELECT * FROM Services WHERE approved_menu_id = " + m.id;
            PersistenceManager.executeQuery(inuseQ, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    // se c'è anche un solo risultato vuol dire che il menù è in uso
                    m.inUse = true;
                }
            });
        }

        for (int i = 0; i < oldMenus.size(); i++) {
            Menu m = oldMenus.get(i);
            m.owner = User.loadUserById(oldMids.get(i));

            // load features
            m.featuresMap.clear();
            String featQ = "SELECT * FROM MenuFeatures WHERE menu_id = " + m.id;
            PersistenceManager.executeQuery(featQ, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    m.featuresMap.put(rs.getString("name"), rs.getBoolean("value"));
                }
            });

            // load sections
            m.updateSections(Section.loadSectionsFor(m.id));

            // load free items
            m.updateFreeItems(MenuItem.loadItemsFor(m.id, 0));

            // find if "in use"
            String inuseQ = "SELECT * FROM Services WHERE approved_menu_id = " + m.id +
                    " OR " +
                    "proposed_menu_id = "+ m.id;
            PersistenceManager.executeQuery(inuseQ, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    // se c'è anche un solo risultato vuol dire che il menù è in uso
                    m.inUse = true;
                }
            });
        }
        for (Menu m: newMenus) {
            loadedMenus.put(m.id, m);
        }
        return FXCollections.observableArrayList(loadedMenus.values());
    }

    public static void saveSectionOrder(Menu m) {
        String upd = "UPDATE MenuSections SET position = ? WHERE id = ?";
        PersistenceManager.executeBatchUpdate(upd, m.sections.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, batchCount);
                ps.setInt(2, m.sections.get(batchCount).getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });
    }


    public static void saveFreeItemOrder(Menu m) {
        String upd = "UPDATE MenuItems SET position = ? WHERE id = ?";
        PersistenceManager.executeBatchUpdate(upd, m.freeItems.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, batchCount);
                ps.setInt(2, m.freeItems.get(batchCount).getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });
    }
}

package businesslogic.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Section {
    private int id;
    private String name;
    private ObservableList<MenuItem> sectionItems;

    public Section(String name) {
        id = 0;
        this.name = name;
        sectionItems = FXCollections.observableArrayList();
    }

    public Section(Section s) {
        this.id = 0;
        this.name = s.name;
        this.sectionItems = FXCollections.observableArrayList();
        for (MenuItem original: s.sectionItems) {
            this.sectionItems.add(new MenuItem(original));
        }
    }

    public void addItem(MenuItem mi) {
        this.sectionItems.add(mi);
    }


    public void updateItems(ObservableList<MenuItem> newItems) {
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
        this.sectionItems.clear();
        this.sectionItems.addAll(updatedList);
    }

    private MenuItem findItemById(int id) {
        for (MenuItem mi: sectionItems) {
            if (mi.getId() == id) return mi;
        }
        return null;
    }


    public int getItemPosition(MenuItem mi) {
        return this.sectionItems.indexOf(mi);
    }

    public int getId() {
        return id;
    }

    public String testString() {
        String result = name + "\n";
        for (MenuItem mi: sectionItems) {
            result += "\t" + mi.toString() + "\n";
        }
        return result;
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<MenuItem> getItems() {
        return FXCollections.unmodifiableObservableList(this.sectionItems);
    }

    public int getItemsCount() {
        return sectionItems.size();
    }


    public void moveItem(MenuItem mi, int position) {
        sectionItems.remove(mi);
        sectionItems.add(position, mi);
    }

    public void removeItem(MenuItem mi) {
        sectionItems.remove(mi);
    }


    // STATIC METHODS FOR PERSISTENCE
    public static void saveNewSection(int menuid, Section sec, int posInMenu) {
        String secInsert = "INSERT INTO catering.MenuSections (menu_id, name, position) VALUES (" +
                menuid + ", " +
                "'" + PersistenceManager.escapeString(sec.name) + "', " +
                posInMenu +
                ");";
        PersistenceManager.executeUpdate(secInsert);
        sec.id = PersistenceManager.getLastId();

        if (sec.sectionItems.size() > 0) {
            MenuItem.saveAllNewItems(menuid, sec.id, sec.sectionItems);
        }
    }

    public static void saveAllNewSections(int menuid, List<Section> sections) {
        String secInsert = "INSERT INTO catering.MenuSections (menu_id, name, position) VALUES (?, ?, ?);";
        PersistenceManager.executeBatchUpdate(secInsert, sections.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, menuid);
                ps.setString(2, PersistenceManager.escapeString(sections.get(batchCount).name));
                ps.setInt(3, batchCount);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                sections.get(count).id = rs.getInt(1);
            }
        });

        // salva le voci delle sezioni
        for (Section s: sections) {
            if (s.sectionItems.size() > 0) {
                MenuItem.saveAllNewItems(menuid, s.id, s.sectionItems);
            }
        }
    }


    public static ObservableList<Section> loadSectionsFor(int menu_id) {
        ObservableList<Section> result = FXCollections.observableArrayList();
        String query = "SELECT * FROM MenuSections WHERE menu_id = " + menu_id +
                " ORDER BY position";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Section s = new Section(rs.getString("name"));
                s.id = rs.getInt("id");
                result.add(s);
            }
        });

        for (Section s: result) {
            // load items
            s.sectionItems = MenuItem.loadItemsFor(menu_id, s.id);
        }

        return result;
    }


    public static void deleteSection(int menu_id, Section s) {
        // delete items
        String itemdel = "DELETE FROM MenuItems WHERE section_id = " + s.id +
                " AND menu_id = " + menu_id;
        PersistenceManager.executeUpdate(itemdel);

        String secdel = "DELETE FROM MenuSections WHERE id = " + s.id;
        PersistenceManager.executeUpdate(secdel);
    }


    public static void saveSectionName(Section s) {
        String upd = "UPDATE MenuSections SET name = '" + PersistenceManager.escapeString(s.name) + "'" +
                " WHERE id = " + s.id;
        PersistenceManager.executeUpdate(upd);
    }


    public static void saveItemOrder(Section s) {
        String upd = "UPDATE MenuItems SET position = ? WHERE id = ?";
        PersistenceManager.executeBatchUpdate(upd, s.sectionItems.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, batchCount);
                ps.setInt(2, s.sectionItems.get(batchCount).getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });
    }
}

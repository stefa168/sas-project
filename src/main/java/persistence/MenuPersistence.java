package persistence;

import businesslogic.menu.Menu;
import businesslogic.menu.MenuEventReceiver;
import businesslogic.menu.MenuItem;
import businesslogic.menu.Section;

public class MenuPersistence implements MenuEventReceiver {

    @Override
    public void updateMenuCreated(Menu m) {
        Menu.saveNewMenu(m);
    }

    @Override
    public void updateSectionAdded(Menu m, Section sec) {
        Section.saveNewSection(m.getId(), sec, m.getSectionPosition(sec));
    }

    @Override
    public void updateMenuItemAdded(Menu m, MenuItem mi) {
        Section sec = m.getSectionForItem(mi);
        int sec_id = (sec == null ? 0 : sec.getId());
        int pos = (sec == null ? m.getFreeItemPosition(mi) : sec.getItemPosition(mi));
        MenuItem.saveNewItem(m.getId(), sec_id, mi, pos);
    }

    @Override
    public void updateMenuFeaturesChanged(Menu m) {
        Menu.saveMenuFeatures(m);
    }

    @Override
    public void updateMenuTitleChanged(Menu m) {
        Menu.saveMenuTitle(m);
    }

    @Override
    public void updateMenuPublishedState(Menu m) {
        Menu.saveMenuPublished(m);
    }

    @Override
    public void updateMenuDeleted(Menu m) {
        Menu.deleteMenu(m);
    }

    @Override
    public void updateSectionDeleted(Menu m, Section s, boolean itemsDeleted) {
        Section.deleteSection(m.getId(), s);
        if (!itemsDeleted) MenuItem.saveAllNewItems(m.getId(), 0, s.getItems());
    }

    @Override
    public void updateSectionChangedName(Menu m, Section s) {
        Section.saveSectionName(s);
    }

    @Override
    public void updateSectionsRearranged(Menu m) {
        Menu.saveSectionOrder(m);
    }

    @Override
    public void updateFreeItemsRearranged(Menu m) {
        Menu.saveFreeItemOrder(m);
    }

    @Override
    public void updateSectionItemsRearranged(Menu m, Section s) {
        Section.saveItemOrder(s);
    }

    @Override
    public void updateItemSectionChanged(Menu m, Section s, MenuItem mi) {
        int sid = (s == null ? 0 : s.getId());
        MenuItem.saveSection(sid, mi);
    }

    @Override
    public void updateItemDescriptionChanged(Menu m, MenuItem mi) {
        MenuItem.saveDescription(mi);
    }

    @Override
    public void updateItemDeleted(Menu m, Section sec, MenuItem mi) {
        MenuItem.removeItem(mi);
        if (sec != null) {
            Section.saveItemOrder(sec);
        } else Menu.saveFreeItemOrder(m);
    }
}

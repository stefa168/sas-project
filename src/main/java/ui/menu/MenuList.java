package ui.menu;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.menu.Menu;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class MenuList {
    private MenuManagement menuManagementController;

    @FXML
    ListView<Menu> menuList;

    ObservableList<Menu> menuListItems;

    @FXML
    public void nuovoButtonPressed() {
        TextInputDialog dial = new TextInputDialog("Menu");
        dial.setTitle("Crea un nuovo menu");
        dial.setHeaderText("Scegli il titolo per il nuovo menu");
        Optional<String> result = dial.showAndWait();

        if (result.isPresent()) {
            try {
                Menu m = CatERing.getInstance().getMenuManager().createMenu(result.get());
                menuListItems.add(m);
                menuManagementController.showCurrentMenu();
            } catch(UseCaseLogicException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void fineButtonPressed() {
        menuManagementController.endMenuManagement();
    }

    public void setParent(MenuManagement menuManagement) {
        menuManagementController = menuManagement;
    }

    public void initialize() {
        if (menuListItems == null) {
            menuListItems = CatERing.getInstance().getMenuManager().getAllMenus();
            menuList.setItems(menuListItems);
        }
    }
}

package ui.menu;

import businesslogic.CatERing;
import businesslogic.menu.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MenuContent {
    @FXML
    Label titleLabel;

    MenuManagement menuManagementController;

    public void initialize() {
        Menu toview = CatERing.getInstance().getMenuManager().getCurrentMenu();
        if (toview != null) {
            titleLabel.setText(toview.getTitle());

            // TODO: inizializzare la lista delle sezioni
        }
    }

    @FXML
    public void okButtonPressed() {
        menuManagementController.showMenuList();
    }

    public void setMenuManagementController(MenuManagement menuManagement) {
        menuManagementController = menuManagement;
    }
}

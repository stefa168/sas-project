package ui;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public abstract class WindowController {
    protected Stage window;
    protected ArrayList<WindowController> childrenWindows = new ArrayList<>();

    protected abstract void initialize();

    public void showWindow() {
        window.showAndWait();
    }

    public void closeWindow() {
        window.hide();
    }

    public final void setWindow(Stage myWindow) {
        this.window = myWindow;
        childrenWindows.forEach(w -> w.setParentWindow(myWindow));
    }

    public final void setParentWindow(Stage parentWindow) {
        window.initOwner(parentWindow);
        window.initModality(Modality.WINDOW_MODAL);
    }
}

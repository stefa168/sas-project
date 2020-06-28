package ui;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public abstract class WindowController {
    protected Stage window;
    protected ArrayList<WindowController> childrenWindows = new ArrayList<>();

    protected abstract void initialize() throws IOException;

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
        if (parentWindow == null) {
            throw new RuntimeException("Received null as the parent. This should NOT happen.");
        }
        window.initOwner(parentWindow);
        window.initModality(Modality.WINDOW_MODAL);
    }
}

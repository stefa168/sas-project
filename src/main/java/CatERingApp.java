import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.Main;

import java.io.IOException;
// import java.time.LocalDate;

public class CatERingApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/main.fxml"));
            Scene root = new Scene(loader.load(), 1080, 720);
            primaryStage.setTitle("Cat&Ring");
            primaryStage.setScene(root);
            // Questa riga è necessaria per poter passare una reference della finestra creata qui, dato che questa
            // classe non è raggiungibile per ragioni sconosciute. E' un workaround per il codice molto convoluto.
            ((Main) loader.getController()).passMainWindowStage(primaryStage);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public interface FrequencyGenerator {
        LocalDate nextDate(LocalDate from);
    }

    public abstract class AbstractFrequencyGenerator implements FrequencyGenerator {
        protected int baseFrequency;

        public AbstractFrequencyGenerator(int baseFrequency) {
            this.baseFrequency = baseFrequency;
        }
    }

    public class DailyFrequencyGenerator extends AbstractFrequencyGenerator {
        public DailyFrequencyGenerator(int baseFrequency) {
            super(baseFrequency);
        }

        @Override
        public LocalDate nextDate(LocalDate from) {
            return from.plusDays(baseFrequency);
        }
    }*/
}

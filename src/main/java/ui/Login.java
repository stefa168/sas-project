package ui;

import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Login {
    private static Stage mainStage;
    @FXML
    public ComboBox<String> loginComboBox;
    @FXML
    public Button loginButton;
    @FXML
    public AnchorPane anchorPaneLogin;

    private HashMap<String, User> usersMap;


    public void initialize() {
        loginComboBox.setPromptText("nome utente");
        usersMap = new HashMap<>();
        ArrayList<User> users = User.loadAllUsersLogin();
        ObservableList<String> list = FXCollections.observableArrayList();


        for(User user: users){
            if(user.isChef() || user.isCook()) {
                list.add(user.getUserName() + ": " + user.getLoginRoles());
                usersMap.put(user.getUserName(), user);
            }
        }
        loginComboBox.setItems(list);


    }

    public void passMainWindowStage(Stage primaryStage) {
        mainStage = primaryStage;
    }

    public void loginButtonPreessed(ActionEvent actionEvent) {
        if (loginComboBox.getValue()!=null) {
            String[] valori = (loginComboBox.getValue()).split(":");
            //System.out.println(valori[0]);

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
                Scene root = new Scene(loader.load(), 1080, 720);
                Stage stage = new Stage();
                stage.setTitle("Cat&Ring");
                stage.setScene(root);
                // Questa riga è necessaria per poter passare una reference della finestra creata qui, dato che questa
                // classe non è raggiungibile per ragioni sconosciute. E' un workaround per il codice molto convoluto.
                ((Main) loader.getController()).setUsernameUserLogin(usersMap.get(valori[0]));
                ((Main) loader.getController()).passMainWindowStage(stage);
                stage.show();


                Stage current = (Stage) anchorPaneLogin.getScene().getWindow();
                current.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

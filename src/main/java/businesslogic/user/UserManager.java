package businesslogic.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class UserManager {
    private User currentUser;

    public void login(User user) {
        // this.currentUser = User.loadUser(username);
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public ObservableList<User> getAllUsers(){
        ArrayList<User> users = User.loadAllUsersLogin();
        return FXCollections.observableArrayList(users);
    }

    public void loginWithUsername(String username) {
        this.currentUser = User.loadUser(username);
    }
}

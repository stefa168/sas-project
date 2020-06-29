package businesslogic.user;

public class UserManager {
    private User currentUser;

    public void login(User user) {
        // this.currentUser = User.loadUser(username);
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void loginWithUsername(String username) {
        this.currentUser = User.loadUser(username);
    }
}

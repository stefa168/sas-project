package businesslogic.user;

public class UserManager {
    private User currentUser;

    public void login(String username)
    {
        this.currentUser = User.loadUser(username);
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}

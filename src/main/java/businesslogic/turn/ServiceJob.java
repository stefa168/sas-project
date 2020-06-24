package businesslogic.turn;

import businesslogic.user.User;

import java.util.List;

public class ServiceJob {
    private String role;
    private List<User> workers;

    public ServiceJob(String role, List<User> workers) {
        this.role = role;
        this.workers = workers;
    }
}

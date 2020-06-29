package businesslogic.user;

import javafx.collections.FXCollections;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import javax.management.relation.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class User {
    private static Map<Integer, User> loadedUsers = FXCollections.observableHashMap();
    private int id;
    private String username;
    private Set<Role> roles;

    public User() {
        id = 0;
        username = "";
        this.roles = new HashSet<>();
    }

    public static User loadUserById(int uid) {
        if (loadedUsers.containsKey(uid)) return loadedUsers.get(uid);

        User load = new User();
        String userQuery = "SELECT * FROM Users WHERE id='" + uid + "'";
        PersistenceManager.executeQuery(userQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                load.id = rs.getInt("id");
                load.username = rs.getString("username");
            }
        });
        if (load.id > 0) {
            loadedUsers.put(load.id, load);
            String roleQuery = "SELECT * FROM UserRoles WHERE user_id=" + load.id;
            PersistenceManager.executeQuery(roleQuery, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    String role = rs.getString("role_id");
                    switch (role.charAt(0)) {
                        case 'c':
                            load.roles.add(User.Role.CUOCO);
                            break;
                        case 'h':
                            load.roles.add(User.Role.CHEF);
                            break;
                        case 'o':
                            load.roles.add(User.Role.ORGANIZZATORE);
                            break;
                        case 's':
                            load.roles.add(User.Role.SERVIZIO);
                    }
                }
            });
        }
        return load;
    }

    public static ArrayList<User> loadAllUsersLogin() {
        ArrayList<User> users = new ArrayList<>();
        String userQuery = "SELECT * FROM Users WHERE" + true;

        PersistenceManager.executeQuery(userQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                User u = new User();
                u.id = rs.getInt("id");
                u.username = rs.getString("username");
                String roleQuery = "SELECT * FROM UserRoles WHERE user_id=" + u.id;
                PersistenceManager.executeQuery(roleQuery, new ResultHandler() {
                    @Override
                    public void handle(ResultSet rs) throws SQLException {
                        String role = rs.getString("role_id");
                        switch (role.charAt(0)) {
                            case 'c':
                                u.roles.add(User.Role.CUOCO);
                                break;
                            case 'h':
                                u.roles.add(User.Role.CHEF);
                                break;
                            case 'o':
                                u.roles.add(User.Role.ORGANIZZATORE);
                                break;
                            case 's':
                                u.roles.add(User.Role.SERVIZIO);
                        }
                    }
                });
                users.add(u);
            }
        });
        return users;
    }

    public static User loadUser(String username) {
        User u = new User();
        String userQuery = "SELECT * FROM Users WHERE username='" + username + "'";
        PersistenceManager.executeQuery(userQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                u.id = rs.getInt("id");
                u.username = rs.getString("username");
            }
        });
        if (u.id > 0) {
            loadedUsers.put(u.id, u);
            String roleQuery = "SELECT * FROM UserRoles WHERE user_id=" + u.id;
            PersistenceManager.executeQuery(roleQuery, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    String role = rs.getString("role_id");
                    switch (role.charAt(0)) {
                        case 'c':
                            u.roles.add(User.Role.CUOCO);
                            break;
                        case 'h':
                            u.roles.add(User.Role.CHEF);
                            break;
                        case 'o':
                            u.roles.add(User.Role.ORGANIZZATORE);
                            break;
                        case 's':
                            u.roles.add(User.Role.SERVIZIO);
                    }
                }
            });
        }
        return u;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isChef() {
        return roles.contains(Role.CHEF);
    }

    public boolean isCook() {
        return roles.contains(Role.CUOCO);
    }

    public boolean isOrganizer() {
        return roles.contains(Role.ORGANIZZATORE);
    }

    public String getUserName() {
        return username;
    }

    public int getId() {
        return this.id;
    }

    public String getLoginRoles(){
        StringBuilder result = new StringBuilder();

        for(Role role: roles){
            switch (role){
                case CHEF -> result.append("CHEF ");
                case CUOCO -> result.append("CUOCO ");
                case ORGANIZZATORE -> result.append("ORGANIZZATORE ");
                case SERVIZIO -> result.append("SERVIZIO ");
            }
        }

        return result.toString();
    }

    // STATIC METHODS FOR PERSISTENCE

    public String toString() {
        String result = username;
        if (roles.size() > 0) {
            result += ": ";

            for (User.Role r : roles) {
                result += r.toString() + " ";
            }
        }
        return result;
    }

    public static ArrayList<User> getUsersByTurnAvailabilities(int turn_id){
        ArrayList<User> cooks = new ArrayList<>();
        String query = "SELECT * FROM Availabilities WHERE turn_id = " + turn_id;

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                User user = User.loadUserById(rs.getInt("user_id"));
                cooks.add(user);
            }
        });
        return cooks;
    }

    public static enum Role {SERVIZIO, CUOCO, CHEF, ORGANIZZATORE}
}

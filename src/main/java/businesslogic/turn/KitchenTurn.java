package businesslogic.turn;

import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class KitchenTurn extends Turn {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL uuuu HH:mm")
                                                                        .withLocale(Locale.ITALY)
                                                                        .withZone(ZoneId.systemDefault());
    private boolean complete;
    private HashMap<User, Duration> assignedCooks = new HashMap<>();
    private ArrayList<User> availableCooks = new ArrayList<>();

    public KitchenTurn(Instant start, Instant end) {
        super(start, end);
        complete = false;
    }

    public KitchenTurn(Instant start, Instant end, int turn_id) {
        super(start, end, turn_id);
        complete = false;
    }

    public KitchenTurn() {}

    public static ArrayList<KitchenTurn> getAllKitchenTurn() {
        String query = "SELECT * FROM Turn WHERE " + true;
        ArrayList<KitchenTurn> kitchenTurns = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                KitchenTurn turn = new KitchenTurn();
                turn.complete = rs.getBoolean("complete");
                turn.start = rs.getTimestamp("startDate").toInstant();
                turn.end = rs.getTimestamp("endDate").toInstant();
                turn.turn_id = rs.getInt("turn_id");

                String getCooks = "SELECT * FROM Availabilities WHERE turn_id = " + turn.turn_id;
                PersistenceManager.executeQuery(getCooks, new ResultHandler() {
                    @Override
                    public void handle(ResultSet rs) throws SQLException {
                        int user_id = rs.getInt("user_id");
                        User user = User.loadUserById(user_id);
                        turn.availableCooks.add(user);

                        String getDurationCook = "SELECT SUM(estimatedDuration) AS estimatedDuration FROM KitchenJob " +
                                                 "WHERE cook_id = " + user_id + " AND turn_id = " + turn.turn_id;
                        PersistenceManager.executeQuery(getDurationCook, new ResultHandler() {
                            @Override
                            public void handle(ResultSet rs) throws SQLException {
                                int durationMinutes = rs.getInt("estimatedDuration");
                                if (durationMinutes > 0) {
                                    Duration estimatedDuration = Duration.ofMinutes(durationMinutes);
                                    turn.assignedCooks.put(user, estimatedDuration);
                                }
                            }
                        });

                    }
                });

                kitchenTurns.add(turn);
            }
        });

        return kitchenTurns;
    }

    public static KitchenTurn loadKitchenTurnById(int turn_id) {
        String query = "SELECT * FROM Turn WHERE turn_id = " + turn_id;
        KitchenTurn turn = new KitchenTurn();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                turn.complete = rs.getBoolean("complete");
                turn.start = rs.getTimestamp("startDate").toInstant();
                turn.end = rs.getTimestamp("endDate").toInstant();
                turn.turn_id = rs.getInt("turn_id");

                String getCooks = "SELECT * FROM Availabilities WHERE turn_id = " + turn.turn_id;
                PersistenceManager.executeQuery(getCooks, new ResultHandler() {
                    @Override
                    public void handle(ResultSet rs) throws SQLException {
                        int user_id = rs.getInt("user_id");
                        User user = User.loadUserById(user_id);
                        turn.availableCooks.add(user);

                        String getDurationCook = "SELECT SUM(estimatedDuration) AS estimatedDuration FROM KitchenJob " +
                                                 "WHERE cook_id = " + user_id + " AND turn_id = " + turn.turn_id;
                        PersistenceManager.executeQuery(getDurationCook, new ResultHandler() {
                            @Override
                            public void handle(ResultSet rs) throws SQLException {
                                int durationMinutes = rs.getInt("estimatedDuration");
                                if (durationMinutes > 0) {
                                    Duration estimatedDuration = Duration.ofMinutes(durationMinutes);
                                    turn.assignedCooks.put(user, estimatedDuration);
                                }
                            }
                        });

                    }
                });
            }
        });

        return turn;
    }

    public static void changeComplete(int turn_id, int new_complete) {
        String upd = "UPDATE Turn SET complete = '" + new_complete +
                     "' WHERE turn_id = " + turn_id;
        PersistenceManager.executeUpdate(upd);
    }

    public static ObservableList<KitchenTurn> loadAllTurnInfo() {
        ArrayList<KitchenTurn> kitchenTurns = KitchenTurn.getAllKitchenTurn();
        return FXCollections.observableArrayList(kitchenTurns);
    }

    public static ArrayList<KitchenTurn> loadTurnAvailabilitiesByCookId(int cook_id) {
        String query = "SELECT * FROM Availabilities WHERE user_id = " + cook_id;
        ArrayList<KitchenTurn> availabilities = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                KitchenTurn kitchenTurn = KitchenTurn.loadKitchenTurnById(rs.getInt("turn_id"));
                availabilities.add(kitchenTurn);
            }
        });
        return availabilities;
    }

    public static ArrayList<KitchenTurn> loadTurnByDate(Instant startDate, Instant endDate) {
        //language=MySQL
        String query = "SELECT * FROM Turn WHERE complete = 0 AND startDate >= '" + Timestamp.from(startDate) + "' " +
                       "AND" +
                       "  endDate <= '" + Timestamp.from(endDate) + "'";
        ArrayList<KitchenTurn> kitchenTurns = new ArrayList<>();
        PersistenceManager.executeQuery(query, rs -> {
            KitchenTurn kitchenTurn = KitchenTurn.loadKitchenTurnById(rs.getInt("turn_id"));
            kitchenTurns.add(kitchenTurn);
        });
        return kitchenTurns;
    }

    public ArrayList<User> getAvailableCooks() {
        return availableCooks;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String toString() {
        return "Inizio: " + formatter.format(start) + ", Fine: " + formatter.format(end) + ", " +
               (complete ? "Al completo" : "Non al completo");
    }

    //metodi db

    public HashMap<User, Duration> getAssignedCooks() {
        return assignedCooks;
    }

    @Override
    public boolean hasConcluded() {
        return end.compareTo(Instant.now()) <= 0;
    }

    public boolean hasUserEnoughTime(User user, Duration estimatedDuration) {
        if (user == null || !availableCooks.contains(user)) {
            return false;
        }

        if (!assignedCooks.containsKey(user)) {
            return true;
        }

        Duration occupiedTime = assignedCooks.get(user);

        return this.start.plus(occupiedTime).plus(estimatedDuration).compareTo(this.end) <= 0;
    }

    public boolean hasUserEnoughTime(User user, Duration oldDuration, Duration newDuration) {
        if (user == null || !availableCooks.contains(user)) {
            return false;
        }

        if (!assignedCooks.containsKey(user)) {
            return true;
        }

        Duration occupiedTime = assignedCooks.get(user);

        return this.start.plus(occupiedTime).minus(oldDuration).plus(newDuration).compareTo(this.end) <= 0;
    }

    public void takeTime(User cook, Duration estimatedDuration) {
        Duration cookOccupiedTime = assignedCooks.get(cook);

        if (cookOccupiedTime == null) {
            cookOccupiedTime = estimatedDuration;
        } else {
            cookOccupiedTime = cookOccupiedTime.plus(estimatedDuration);
        }

        assignedCooks.put(cook, cookOccupiedTime);
    }

    public void freeTime(User cook, Duration estimatedDuration) {
        Duration cookOccupiedTime = assignedCooks.get(cook);

        if (cookOccupiedTime == null) {
            return;
        }

        cookOccupiedTime = cookOccupiedTime.minus(estimatedDuration);

        if (cookOccupiedTime.isNegative()) {
            throw new RuntimeException("Resulting Occupied time is negative");
        }

        if (cookOccupiedTime.isZero()) {
            assignedCooks.remove(cook);
        } else {
            assignedCooks.put(cook, cookOccupiedTime);
        }
    }


}

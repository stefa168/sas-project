package businesslogic.turn;

import businesslogic.event.Event;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class KitchenTurn extends Turn {
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

    public KitchenTurn(){}

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String toSring(){
        return "Inizio: " + start.toString() + ", Fine: " + end.toString() + ", completo: " + complete;
    }

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
        //todo
        throw new RuntimeException("Not Implemented");
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

    //metodi db

    public static ArrayList<KitchenTurn> getAllKitchenTurn(){
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

                        String getDurationCook = "SELECT SUM(estimatedDuration) AS estimatedDuration FROM KitchenJob WHERE cook_id = " + user_id + " AND turn_id = " + turn.turn_id;
                        PersistenceManager.executeQuery(getDurationCook, new ResultHandler() {
                            @Override
                            public void handle(ResultSet rs) throws SQLException {
                                Duration estimatedDuration = Duration.ofMinutes(rs.getInt("estimatedDuration"));
                                turn.assignedCooks.put(user,estimatedDuration);
                            }
                        });

                    }
                });

                kitchenTurns.add(turn);
            }
        });

        return kitchenTurns;
    }

    public static KitchenTurn loadKitchenTurnById(int turn_id){
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

                        String getDurationCook = "SELECT SUM(estimatedDuration) AS estimatedDuration FROM KitchenJob WHERE cook_id = " + user_id + " AND turn_id = " + turn.turn_id;
                        PersistenceManager.executeQuery(getDurationCook, new ResultHandler() {
                            @Override
                            public void handle(ResultSet rs) throws SQLException {
                                Duration estimatedDuration = Duration.ofMinutes(rs.getInt("estimatedDuration"));
                                turn.assignedCooks.put(user,estimatedDuration);
                            }
                        });

                    }
                });
            }
        });

        return turn;
    }

    public static void changeComplete(int turn_id, boolean new_complete){
        String upd = "UPDATE Turn SET complete = '" + new_complete +
                "' WHERE id = " + turn_id;
        PersistenceManager.executeUpdate(upd);
    }

    public static ObservableList<KitchenTurn> loadAllTurnInfo() {
        ArrayList<KitchenTurn> kitchenTurns = KitchenTurn.getAllKitchenTurn();
        return FXCollections.observableArrayList(kitchenTurns);
    }

}

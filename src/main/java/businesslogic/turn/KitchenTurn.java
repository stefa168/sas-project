package businesslogic.turn;

import businesslogic.user.User;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

public class KitchenTurn extends Turn {
    private boolean complete;
    private HashMap<User, Duration> assignedCooks;
    public KitchenTurn(Instant start, Instant end) {
        super(start, end);
        assignedCooks = new HashMap<>();
        complete = false;
    }

    public KitchenTurn(){}

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public HashMap<User, Duration> getAssignedCooks() {
        return assignedCooks;
    }

    @Override
    public boolean hasConcluded() {
        return end.compareTo(Instant.now()) <= 0;
    }

    public boolean hasUserEnoughTime(User user, Duration estimatedDuration) {
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

            }
        });

        return turn;
    }
}

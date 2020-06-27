package businesslogic.turn;

import businesslogic.user.User;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;

public class KitchenTurn extends Turn {
    private boolean complete;
    private HashMap<User, Duration> assignedCooks;
    public KitchenTurn(Instant start, Instant end) {
        super(start, end);
        assignedCooks = new HashMap<>();
        complete = false;
    }

    public boolean isComplete() {
        return complete;
    }

    public HashMap<User, Duration> getAssignedCooks() {
        return assignedCooks;
    }

    public KitchenTurn(LocalDate start, LocalDate end) {
        super(start, end);
        assignedCooks = new HashMap<>();
        complete = false;
    }

    public boolean hasUserEnoughTime(User user, Duration estimatedDuration) {
        if (!assignedCooks.containsKey(user)) {
            return true;
        }

        Duration occupiedTime = assignedCooks.get(user);

        return this.start.plus(occupiedTime).plus(estimatedDuration).compareTo(this.end) <= 0;
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

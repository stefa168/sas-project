package businesslogic.kitchentask;

import businesslogic.turn.KitchenTurn;
import businesslogic.user.User;
import persistence.PersistenceManager;
import persistence.ResultHandler;
import ui.task.TaskItemInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;

public class KitchenJob implements TaskItemInfo {
    private int amount;
    private Duration estimatedDuration;
    private KitchenTurn turn;
    private User cook;

    public KitchenJob(KitchenTurn turn, int amount, Duration estimatedDuration) {
        this.amount = amount;
        this.turn = turn;
        this.estimatedDuration = estimatedDuration;
    }

    public KitchenJob() {

    }

    public static ArrayList<KitchenJob> getAllKitchenJobs() {
        String query = "SELECT * FROM KitchenJob WHERE " + true;
        ArrayList<KitchenJob> kitchenJobs = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                KitchenJob kitchenJob = new KitchenJob();
                kitchenJob.amount = rs.getInt("amount");
                int duration = rs.getInt("estimatedDuration");
                kitchenJob.estimatedDuration = Duration.ofMinutes(duration);
                kitchenJob.cook = User.loadUserById(rs.getInt("cook_id"));
                kitchenJob.turn = KitchenTurn.loadKitchenTurnById(rs.getInt("turn_id"));
                kitchenJobs.add(kitchenJob);
            }
        });
        return kitchenJobs;
    }

    public static KitchenJob getKitchenJobById(int task_id, int cook_id, int turn_id) {
        String query = "SELECT * FROM KitchenJob WHERE task_id = " + task_id + "AND cook_id = " + cook_id + "AND " +
                       "turn_id = " + turn_id;
        KitchenJob kitchenJob = new KitchenJob();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                kitchenJob.amount = rs.getInt("amount");
                int duration = rs.getInt("estimatedDuration");
                kitchenJob.estimatedDuration = Duration.ofMinutes(duration);
                kitchenJob.cook = User.loadUserById(rs.getInt("cook_id"));
                kitchenJob.turn = KitchenTurn.loadKitchenTurnById(rs.getInt("turn_id"));
            }
        });
        return kitchenJob;
    }

    public void edit(int amount, Duration estimatedDuration) {
        //TODO
    }

    public void assignCook(User user) {
        if (user.isCook()) {
            this.cook = user;
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(Integer newAmount) {
        amount = newAmount;
    }

    public Duration getDuration() {
        return estimatedDuration;
    }

    public void setDuration(Duration newDuration) {
        this.estimatedDuration = newDuration;
    }

    //metodi per il db

    public KitchenTurn getTurn() {
        return turn;
    }

    public User getCook() {
        return cook;
    }

    @Override
    public String toString() {
        return cook != null ?
                String.format("Incarico per %d porzioni con una durata di %d minuti.\nAssegnato nel turno %s\nCuoco " +
                              "scelto: %s.",
                              amount,
                              estimatedDuration.toMinutes(),
                              turn.toString(),
                              cook.toString()) :
                String.format("Incarico per %d porzioni con una durata di %d minuti.\nAssegnato nel turno %s\nNessun " +
                              "cuoco scelto.",
                              amount,
                              estimatedDuration.toMinutes(),
                              turn.toString());
    }
}

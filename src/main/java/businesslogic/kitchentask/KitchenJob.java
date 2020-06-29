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
    private int kitchenJob_id;
    private int task_id;

    public KitchenJob(KitchenTurn turn, int amount, Duration estimatedDuration) {
        this.amount = amount;
        this.turn = turn;
        this.estimatedDuration = estimatedDuration;
    }

    public KitchenJob() {

    }

    // attenzione metodo non aggiornato
    public static ArrayList<KitchenJob> getAllKitchenJobs() {
        String query = "SELECT * FROM KitchenJob WHERE " + true;
        ArrayList<KitchenJob> kitchenJobs = new ArrayList<>();

        PersistenceManager.executeQuery(query, rs -> {
            KitchenJob kitchenJob = new KitchenJob();
            kitchenJob.amount = rs.getInt("amount");
            int duration = rs.getInt("estimatedDuration");
            kitchenJob.estimatedDuration = Duration.ofMinutes(duration);
            kitchenJob.cook = User.loadUserById(rs.getInt("cook_id"));
            kitchenJob.turn = KitchenTurn.loadKitchenTurnById(rs.getInt("turn_id"));
            kitchenJobs.add(kitchenJob);
        });
        return kitchenJobs;
    }

    // attenzione metodo non aggiornato
    public static KitchenJob getKitchenJobById(int kitchenJob_id) {
        String query = "SELECT * FROM KitchenJob WHERE kitchenJob_id = " + kitchenJob_id;
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

    public static void deleteKitchenJob(int kitchenJob_id) {
        String rem = "DELETE FROM KitchenJob WHERE kitchenJob_id = " + kitchenJob_id;
        PersistenceManager.executeUpdate(rem);
    }

    public static void createKitchenJob(KitchenJob kitchenJob, int task_id) {
        //language=MySQL
        String itemInsert = "INSERT INTO catering.kitchenJob (task_id, turn_id, amount, estimatedDuration) " +
                            "VALUES (" +
                            task_id + "," +
                            kitchenJob.getTurn().getTurn_id() + "," +
                            kitchenJob.amount + "," +
                            kitchenJob.estimatedDuration.toMinutes() + ")";

        PersistenceManager.executeUpdate(itemInsert);
        kitchenJob.kitchenJob_id = PersistenceManager.getLastId();
    }

    public static void changeCook(int kitchenJob_id, int new_cook_id) {
        String upd = "UPDATE KitchenJob SET cook_id = " + new_cook_id +
                     "WHERE kitchenJob_id = " + kitchenJob_id;
        PersistenceManager.executeUpdate(upd);
    }

    public static void changeAmount(int kitchenJob_id, int new_amount) {
        String upd = "UPDATE KitchenJob SET amount = " + new_amount +
                     "WHERE kitchenJob_id = " + kitchenJob_id;
        PersistenceManager.executeUpdate(upd);
    }

    public static void changeEstimatedDuration(int kitchenJob_id, Duration estimatedDuration) {
        int new_estimatedDuration = (int) (estimatedDuration.getSeconds()) / 60;
        String upd = "UPDATE KitchenJob SET amount = " + new_estimatedDuration +
                     "WHERE kitchenJob_id = " + kitchenJob_id;
        PersistenceManager.executeUpdate(upd);
    }

    public static ArrayList<KitchenJob> loadByTask(Task task) {
        ArrayList<KitchenJob> jobs = new ArrayList<>();
        //language=MySQL
        String query = "SELECT * FROM kitchenjob WHERE task_id = " + task.getTask_id();
        PersistenceManager.executeQuery(query, rs -> {
            KitchenJob job = new KitchenJob();
            job.kitchenJob_id = rs.getInt("kitchenJob_id");
            job.task_id = rs.getInt("task_id");
            job.turn = KitchenTurn.loadKitchenTurnById(rs.getInt("turn_id"));
            int plausibleUserId = rs.getInt("cook_id");
            // Questo è l'unico modo per verificare se il valore letto in precedenza era null.
            // https://stackoverflow.com/questions/2920364/checking-for-a-null-int-value-from-a-java-resultset
            job.cook = rs.wasNull() ? null : User.loadUserById(plausibleUserId);
            job.amount = rs.getInt("amount");
            job.estimatedDuration = Duration.ofMinutes(rs.getInt("estimatedDuration"));
            jobs.add(job);
        });

        return jobs;
    }

    public void edit(int amount, Duration estimatedDuration) {
        //TODO
    }

    public void assignCook(User user) {
        if (user == null || user.isCook()) {
            this.cook = user;
        }
    }


    public int getKitchenJob_id() {
        return kitchenJob_id;
    }


    //metodi per il db

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

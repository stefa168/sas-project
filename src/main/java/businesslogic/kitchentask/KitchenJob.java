package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;
import businesslogic.turn.KitchenTurn;
import businesslogic.turn.Turn;
import businesslogic.user.User;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;

public class KitchenJob {
    private int amount;
    private Duration estimatedDuration;
    private KitchenTurn turn;
    private User cook;

    public KitchenJob(KitchenTurn turn, int amount, Duration estimatedDuration) {
        this.amount = amount;
        this.turn = turn;
        this.estimatedDuration = estimatedDuration;
    }

    public KitchenJob (){

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

    public KitchenTurn getTurn() {
        return turn;
    }

    public User getCook() {
        return cook;
    }

    public void setDuration(Duration newDuration) {
        this.estimatedDuration = newDuration;
    }

    //metodi per il db

    public static ArrayList<KitchenJob> getAllKitchenJobs(){
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

    public static KitchenJob getKitchenJobById(int task_id, int cook_id, int turn_id){
        String query = "SELECT * FROM KitchenJob WHERE task_id = " + task_id + "AND cook_id = " + cook_id + "AND turn_id = " + turn_id;
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

    public static void deleteKitchenJob(int task_id, int cook_id, int turn_id){
        String rem = "DELETE FROM KitchenJob WHERE task_id = " + task_id + "AND cook_id = " + cook_id + "AND turn_id = " + turn_id;
        PersistenceManager.executeUpdate(rem);
    }

    public static void createKitchenJob(KitchenJob kitchenJob, int task_id){
        String itemInsert = "INSERT INTO catering.kitchenJob (task_id, cook_id, turn_id, amount, estimatedDuration) VALUES ("
                + task_id + "," + kitchenJob.getCook().getId() + "," + kitchenJob.getTurn().getTurn_id() + "," + kitchenJob.amount + "," + kitchenJob.estimatedDuration +")";

        PersistenceManager.executeUpdate(itemInsert);
    }
    public static void changeAmount(int task_id, int cook_id, int turn_id, int new_amount){
        String upd = "UPDATE KitchenJob SET amount = " + new_amount +
                "WHERE task_id = " + task_id + "AND cook_id = " + cook_id + "AND turn_id = " + turn_id;
        PersistenceManager.executeUpdate(upd);
    }

    public static void changeEstimatedDuration(int task_id, int cook_id, int turn_id, Duration estimatedDuration){
        int new_estimatedDuration = (int)(estimatedDuration.getSeconds())/60;
        String upd = "UPDATE KitchenJob SET amount = " + new_estimatedDuration +
                "WHERE task_id = " + task_id + "AND cook_id = " + cook_id + "AND turn_id = " + turn_id;
        PersistenceManager.executeUpdate(upd);
    }
}

package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;
import businesslogic.recipe.Preparation;
import businesslogic.recipe.Recipe;
import businesslogic.turn.KitchenTurn;
import persistence.PersistenceManager;
import persistence.ResultHandler;
import ui.task.TaskItemInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;

public class Task implements TaskItemInfo, Comparable<Task> {
    private int amount;
    private Duration estimatedDuration;
    private boolean toDo;
    private boolean optionalDuty;
    private ArrayList<KitchenJob> jobs;
    private KitchenDuty duty;

    //Per il collegamento al db
    private int task_id;
    private int order_numer;

    public Task(boolean optionalDuty, KitchenDuty duty) {
        this.optionalDuty = optionalDuty;
        this.duty = duty;
        jobs = new ArrayList<>();
        toDo = true;
        amount = 0;
        estimatedDuration = Duration.ZERO;
    }

    public Task(boolean optionalDuty, KitchenDuty duty, int service_id, int order_numer) {
        this.optionalDuty = optionalDuty;
        this.duty = duty;
        jobs = new ArrayList<>();
        toDo = true;
        amount = 0;
        estimatedDuration = Duration.ZERO;
        this.order_numer = order_numer;
        createTask(this, service_id);
    }

    public Task(int task_id, KitchenDuty kitchenDuty, Duration estimatedDuration, boolean toDo, boolean optionalDuty,
                int order_number) {
        this.task_id = task_id;
        this.duty = kitchenDuty;
        this.estimatedDuration = estimatedDuration;
        this.toDo = toDo;
        this.optionalDuty = optionalDuty;
        this.order_numer = order_number;
        this.jobs = new ArrayList<>();
    }

    //Metodi per il db
    public static void createTask(Task task, int service_id) {
        String itemInsert = "INSERT INTO catering.Task " +
                            "(id, kitchenDuty_id, service_id, estimatedDuration, toDo, optionalDuty, order_number, " +
                            "isRecipe) " +
                            "VALUES ("
                            + task.task_id + "," + task.getDuty()
                                                       .getKitchenDutyId() + "," + service_id + "," + task.estimatedDuration
                                    .toMinutes() + "," + task.toDo + ","
                            + task.optionalDuty + "," + task.order_numer + "," + (task.duty instanceof Recipe) + ")";

        PersistenceManager.executeUpdate(itemInsert);
    }

    public static ArrayList<Task> getAllTasks(int service_id) {
        //language=MySQL
        String query = "SELECT * FROM task WHERE service_id = " + service_id;
        ArrayList<Task> tasks = new ArrayList<>();

        PersistenceManager.executeQuery(query, rs -> {
            int id = rs.getInt("id");
            int kitchenDuty_id = rs.getInt("kitchenDuty_id");
            Duration estimatedDuration = Duration.ofMinutes(rs.getInt("estimatedDuration"));
            boolean toDo = rs.getBoolean("toDo");
            boolean optionalDuty = rs.getBoolean("optionalDuty");
            boolean isRecipe = rs.getBoolean("isRecipe");
            int order_number = rs.getInt("order_number");

            KitchenDuty duty = isRecipe ?
                    Recipe.loadRecipeById(kitchenDuty_id) :
                    Preparation.getPreparationById(kitchenDuty_id);

            Task task = new Task(id, duty, estimatedDuration, toDo, optionalDuty, order_number);
            tasks.add(task);
        });

        return tasks;
    }

    public void editDetails(Integer newAmount, Duration newDuration, Boolean newToDo) {
        if (newAmount != null) {
            this.amount = newAmount;
        }
        if (newDuration != null) {
            this.estimatedDuration = newDuration;
            Task.changeEstimatedDuration(this.task_id,newDuration);
        }
        if (newToDo != null) {
            this.toDo = newToDo;
            Task.changeToDo(this.task_id, newToDo);
        }
    }

    public KitchenJob addKitchenJob(KitchenTurn turn, int amount, Duration estimatedDuration) {
        KitchenJob job = new KitchenJob(turn, amount, estimatedDuration);
        jobs.add(job);
        KitchenJob.createKitchenJob(job,task_id);
        return job;
    }

    public void deleteKitchenJob(KitchenJob job) {
        job.getTurn().freeTime(job.getCook(), job.getDuration());
        jobs.remove(job);
        KitchenJob.deleteKitchenJob(task_id, job.getCook().getId(),job.getTurn().getTurn_id());
    }

    public int getAmount() {
        return amount;
    }

    public int getTask_id() { return task_id; }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Duration getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setDuration(Duration estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public boolean isToDo() {
        return toDo;
    }

    public void setToDo(boolean toDo) {
        this.toDo = toDo;
    }

    public boolean isOptionalDuty() {
        return optionalDuty;
    }

    public ArrayList<KitchenJob> getJobs() {
        return jobs;
    }

    public KitchenDuty getDuty() {
        return duty;
    }



    public void deleteAllKitchenJobs() {
        //TODO
    }

    @Override
    public String toString() {
        String taskString = this.toDo ?
                String.format("Compito per %s, da %d porzioni con una durata di %d minuti.",
                              duty.getName(),
                              amount,
                              estimatedDuration.toMinutes()) :
                String.format("Compito per %s, indicato come non necessario da preparare.",
                              duty.getName());

        return taskString.concat(optionalDuty ? " (Fuori Menù)" : "");
    }

    public static void deleteTask(int task_id){
        String rem = "DELETE FROM Task WHERE id = " + task_id;
        PersistenceManager.executeUpdate(rem);
    }

    public static void changeOrderTask(int task_id, int new_order){
        String upd = "UPDATE Task SET order_number = '" + new_order +
                "' WHERE id = " + task_id;
        PersistenceManager.executeUpdate(upd);
    }

    public static void changeToDo(int task_id, boolean new_toDO){
        String upd = "UPDATE Task SET toDo = '" + new_toDO +
                "' WHERE id = " + task_id;
        PersistenceManager.executeUpdate(upd);
    }


    public static void changeEstimatedDuration(int task_id, Duration estimatedDuration){
        int new_estimatedDuration = (int)(estimatedDuration.getSeconds())/60;
        String upd = "UPDATE Task SET estimatedDuration = '" + new_estimatedDuration +
                "' WHERE id = " + task_id;
        PersistenceManager.executeUpdate(upd);
    }

    public static void createTaskNoOrder(Task task, int service_id){
        String obtainPosition = "SELECT MAX(VAL(order_number)) AS order_number FROM Task WHERE service_id = " + service_id;
        PersistenceManager.executeQuery(obtainPosition, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                task.order_numer = rs.getInt("order_number") + 1;
            }
        });
        createTask(task,service_id);
    }


    @Override
    public int compareTo(Task o) {
        return Integer.compare(this.order_numer, o.order_numer);
    }
}

package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;
import businesslogic.recipe.Preparation;
import businesslogic.recipe.Recipe;
import businesslogic.turn.KitchenTurn;
import persistence.PersistenceManager;

import java.time.Duration;
import java.util.ArrayList;

public class Task {
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
        }
        if (newToDo != null) {
            this.toDo = newToDo;
        }
    }

    public KitchenJob addKitchenJob(KitchenTurn turn, int amount, Duration estimatedDuration) {
        KitchenJob job = new KitchenJob(turn, amount, estimatedDuration);
        jobs.add(job);
        return job;
    }

    public int getAmount() {
        return amount;
    }

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

    public void deleteKitchenJob(KitchenJob job) {
        job.getTurn().freeTime(job.getCook(), job.getDuration());
        jobs.remove(job);
    }

    public void deleteAllKitchenJobs() {
        //TODO
    }


}

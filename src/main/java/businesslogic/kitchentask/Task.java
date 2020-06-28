package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;
import businesslogic.recipe.Preparation;
import businesslogic.recipe.Recipe;
import businesslogic.turn.KitchenTurn;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public Task(int task_id, KitchenDuty kitchenDuty,Duration estimatedDuration,boolean toDo,boolean optionalDuty,int order_number) {
        this.task_id = task_id;
        this.duty = kitchenDuty;
        this.estimatedDuration = estimatedDuration;
        this.toDo = toDo;
        this.optionalDuty = optionalDuty;
        this.order_numer = order_number;
        this.jobs = new ArrayList<>();
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


    //Metodi per il db
    public static void createTask(Task task, int service_id){
        String itemInsert = "INSERT INTO catering.Task (id, kitchenDuty_id, service_id, estimatedDuration, toDo, optionalDuty, order_number) VALUES ("
                + task.task_id + "," + task.getDuty().getKitchenDutyId() + "," + service_id + "," + task.estimatedDuration + "," + task.toDo + ","
                + task.optionalDuty + "," + task.order_numer +")";

        PersistenceManager.executeUpdate(itemInsert);
    }

    public static ArrayList<Task> getAllTasks(int service_id){
        String query = "SELECT * FROM Menus WHERE serivice_id = " + service_id;
        ArrayList <Task> tasks = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                int kitchenDuty_id = rs.getInt("kitchenDuty_id");
                int duration  = rs.getInt("estimatedDuration");
                Duration estimatedDuration = Duration.ofMinutes(duration);
                boolean toDo = rs.getBoolean("toDo");
                boolean optionalDuty = rs.getBoolean("optionalDuty");
                boolean isRecipe = rs.getBoolean("isRecipe");
                int order_number = rs.getInt("order_number");
                KitchenDuty kitchenDuty;

                if(isRecipe){
                    kitchenDuty = Recipe.loadRecipeById(id);
                }
                else {
                    kitchenDuty = Preparation.getPreparationById(id);
                }

                Task task = new Task(id,kitchenDuty,estimatedDuration,toDo,optionalDuty,order_number);
                tasks.add(task);


            }
        });
        return tasks;
    }




}

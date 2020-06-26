package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;
import businesslogic.turn.KitchenTurn;
import businesslogic.user.User;

import java.time.Duration;
import java.util.ArrayList;

public class Task {
    private int amount;
    private Duration estimatedDuration;
    private boolean toDo;
    private boolean optionalDuty;
    private ArrayList<KitchenJob> jobs;
    private KitchenDuty duty;

    public Task(boolean optionalDuty, KitchenDuty duty) {
        this.optionalDuty = optionalDuty;
        this.duty = duty;
        jobs = new ArrayList<>();
        toDo = true;
        //TODO check inizializzazione altri parametri
    }

    public void editDetails(Integer newAmount, Duration newDuration, Boolean newToDo){
        if(newAmount!=null) {
            this.amount = newAmount;
        }
        if(newDuration!=null) {
            this.estimatedDuration = newDuration;
        }
        if(newToDo!=null) {
            this.toDo = newToDo;
        }
    }

    public void addKitchenJob(User cook, KitchenTurn turn, int amount, Duration estimatedDuration){
        //TODO
    }

    public void deleteKitchenJob(KitchenJob job){
        //TODO
    }

    public void deleteAllKitchenJobs(){
        //TODO
    }

}

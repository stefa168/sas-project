package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;
import businesslogic.turn.KitchenTurn;

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
        amount = 0;
        estimatedDuration = Duration.ZERO;
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

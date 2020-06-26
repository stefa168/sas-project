package businesslogic.kitchentask;

import businesslogic.CatERing;
import businesslogic.EventException;
import businesslogic.TaskException;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.recipe.KitchenDuty;
import businesslogic.turn.KitchenTurn;
import businesslogic.turn.Turn;
import businesslogic.user.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

public class KitchenTaskManager {
    private SummarySheet currentSheet;
    private ArrayList<KitchenTaskEventReceiver> eventReceivers;

    public KitchenTaskManager(){
        eventReceivers = new ArrayList<>();
    }

    public KitchenTaskManager(SummarySheet sheet){
        currentSheet = sheet;
        eventReceivers = new ArrayList<>();
    }

    public void setCurrentSheet(SummarySheet sheet){ this.currentSheet = sheet;}

    private void notifySheetCreate(SummarySheet sheet){
        for(KitchenTaskEventReceiver eventReceiver: eventReceivers){
            eventReceiver.updateSheetCreate(sheet);
        }
    }
    private void notifyAddExtraDuty(KitchenDuty kitchenDuty, Task task){
        for(KitchenTaskEventReceiver eventReceiver: eventReceivers){
            eventReceiver.updateAddExtraDuty(kitchenDuty,task);
        }
    }
    private void notifyDeleteExtraDuty(KitchenDuty kitchenDuty, Task task){
        for(KitchenTaskEventReceiver eventReceiver: eventReceivers){
            eventReceiver.updateDeleteExtraDuty(kitchenDuty, task);
        }
    }
    private void notifyEditTask(Task task){
        for(KitchenTaskEventReceiver eventReceiver: eventReceivers){
            eventReceiver.updateEditTask(task);
        }
    }
    private void notifyCreatedKitchenJob(KitchenJob kitchenJob){
        for(KitchenTaskEventReceiver eventReceiver: eventReceivers){
            eventReceiver.updateCreateKitchenJob(kitchenJob);
        }
    }
    private void notifyDeletedKitchenJob(KitchenJob kitchenJob){
        for(KitchenTaskEventReceiver eventReceiver: eventReceivers){
            eventReceiver.updateDeletedKitchenJob(kitchenJob);
        }
    }
    private void notifyEditKitchenJob(KitchenJob kitchenJob){
        for(KitchenTaskEventReceiver eventReceiver: eventReceivers){
            eventReceiver.updateEditKitchenJob(kitchenJob);
        }
    }

    public SummarySheet createSummarySheet(Event event, Service service)throws UseCaseLogicException, EventException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef() || event.getAssignedChef() != user || event.containsService(service)){
            throw new UseCaseLogicException();
        }
        if(!event.isActive()){
            throw new EventException();
        }

        this.currentSheet = service.createSummarySheet();
        notifySheetCreate(currentSheet);

        return currentSheet;
    }

    public Task addExtraDuty(KitchenDuty kitchenDuty)throws UseCaseLogicException{
        if(currentSheet == null){
            throw new UseCaseLogicException();
        }
        Task task = currentSheet.addExtraDuty(kitchenDuty);
        notifyAddExtraDuty(kitchenDuty,task);
        return task;
    }

    public SummarySheet deleteExtraDuty(KitchenDuty kitchenDuty) throws UseCaseLogicException{
        if(currentSheet == null){
            throw new UseCaseLogicException();
        }
        Task task = currentSheet.deleteExtraDuty(kitchenDuty);
        notifyDeleteExtraDuty(kitchenDuty,task);
        return currentSheet;
    }

    public KitchenJob createKitchenJob(Task task, KitchenTurn kitchenTurn, int amount, Duration estimatedDuration) throws TaskException, UseCaseLogicException{
        if(currentSheet == null || LocalDate.now().compareTo(kitchenTurn.getEnd()) >0 || kitchenTurn.isComplete()){
            throw new UseCaseLogicException();
        }
        if(task.getAmount()<=0 || task.getEstimatedDuration() == Duration.ZERO || !task.isToDo() ){
            throw  new TaskException();
        }

        KitchenJob job = task.addKitchenJob(kitchenTurn,amount, estimatedDuration);
        notifyCreatedKitchenJob(job);
        return job;
    }

    public Task deleteKitchenJob(Task task, KitchenJob job) throws TaskException{
        if(!task.getJobs().contains(job)){
            throw new TaskException();
        }
        task.deleteKitchenJob(job);
        notifyDeletedKitchenJob(job);
        return task;
    }

    public KitchenJob assignCook(KitchenJob job, User user) throws UseCaseLogicException, TaskException{
        if(user == null || !user.isCook() || LocalDate.now().compareTo(job.getTurn().getEnd()) >0 ){
            throw new UseCaseLogicException();
        }
        if(job.getCook() != user){
            if(job.getTurn().hasUserEnoughTime(user, job.getEstimatedDuration())){
                if(job.getCook()!=null){
                    job.getTurn().freeTime(job.getCook(), job.getEstimatedDuration() );
                }
                job.getTurn().takeTime(user, job.getEstimatedDuration());
                job.assignCook(user);
                notifyEditKitchenJob(job);
            }
            else {
                throw new TaskException();
            }
        }
        return job;
    }




}

package businesslogic.kitchentask;

import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.recipe.KitchenDuty;
import businesslogic.turn.KitchenTurn;
import businesslogic.user.User;

import java.time.Duration;
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

    public SummarySheet createSummarySheet(Event event, Service service){
        //TODO
        return null;
    }

    public Task addExtraDuty(KitchenDuty kitchenDuty){
        //TODO
        return null;
    }

    public SummarySheet deleteExtraDuty(KitchenDuty kitchenDuty){
        //TODO
        return null;
    }
    public KitchenJob createKitchenJob(Task task, KitchenTurn kitchenTurn, int amount, Duration estimatedDuration){
        //TODO
        return null;
    }
    public Task deleteKitchenJob(Task task, KitchenJob job){
        //TODO
        return null;
    }
    public KitchenJob assignCook(KitchenJob job, User cook){
        //TODO
        return null;
    }




}

package persistence;

import businesslogic.kitchentask.KitchenJob;
import businesslogic.kitchentask.KitchenTaskEventReceiver;
import businesslogic.kitchentask.Task;
import businesslogic.turn.KitchenTurn;
import businesslogic.user.User;

import java.util.List;

public class KitchenTaskPersistence implements KitchenTaskEventReceiver {
    @Override
    public void updateTaskOrderChanged(List<Task> involvedTasks) {
        involvedTasks.forEach(task -> Task.changeOrderTask(task.getTask_id(), task.getOrderIndex()));
    }

    @Override
    public void updateCookAssigned(User user, KitchenJob kitchenJob) {
        KitchenJob.changeCook(kitchenJob.getKitchenJob_id(),user.getId());
    }

    @Override
    public void updateKitchenTurnComplete(KitchenTurn kitchenTurn) {
        KitchenTurn.changeComplete(kitchenTurn.getTurn_id(),1);
    }
}

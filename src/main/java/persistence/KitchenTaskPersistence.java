package persistence;

import businesslogic.kitchentask.KitchenTaskEventReceiver;
import businesslogic.kitchentask.Task;

import java.util.List;

public class KitchenTaskPersistence implements KitchenTaskEventReceiver {
    @Override
    public void updateTaskOrderChanged(List<Task> involvedTasks) {
        involvedTasks.forEach(task -> Task.changeOrderTask(task.getTask_id(), task.getOrderIndex()));
    }
}

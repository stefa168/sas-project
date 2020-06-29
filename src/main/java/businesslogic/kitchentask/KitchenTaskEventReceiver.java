package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;
import businesslogic.turn.KitchenTurn;
import businesslogic.user.User;

import java.util.List;

public interface KitchenTaskEventReceiver {
    public default void updateDeleteExtraDuty(KitchenDuty kitchenDuty, Task task) {

    }

    public default void updateSheetCreate(SummarySheet summarySheet) {

    }

    public default void updateAddExtraDuty(Task task) {

    }

    public default void updateEditTask(Task task) {

    }

    public default void updateCreateKitchenJob(KitchenJob kitchenJob) {

    }

    public default void updateDeletedKitchenJob(KitchenJob kitchenJob) {

    }

    public default void updateEditKitchenJob(KitchenJob kitchenJob) {

    }

    public default void updateTaskOrderChanged(List<Task> involvedTasks) {

    }

    public default void updateEditTurn(KitchenTurn turn) {

    }

    public default void updateKitchenTurnComplete(KitchenTurn kitchenTurn){

    }

    public default void updateCookAssigned(User user, KitchenJob kitchenJob){

    }
}

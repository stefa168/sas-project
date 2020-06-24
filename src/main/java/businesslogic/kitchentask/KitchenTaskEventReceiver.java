package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;

public interface KitchenTaskEventReceiver {
    public void updateDeleteExtraDuty(KitchenDuty kitchenDuty, Task task);
    public void updateSheetCreate(SummarySheet summarySheet);
    public void updateAddExtraDuty(KitchenDuty kitchenDuty, Task task);
    public void updateEditTask(Task task);
    public void updateCreateKitchenJob(KitchenJob kitchenJob);
    public void updateDeletedKitchenJob(KitchenJob kitchenJob);
    public void updateEditKitchenJob(KitchenJob kitchenJob);
}

package businesslogic.kitchentask;

import businesslogic.event.AdditionPatch;
import businesslogic.event.RemovalPatch;
import businesslogic.event.Service;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuItem;
import businesslogic.menu.Section;
import businesslogic.recipe.KitchenDuty;
import persistence.PersistenceManager;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SummarySheet {
    private ArrayList<Task> tasks;
    private ArrayList<KitchenDuty> extraDuties;

    public SummarySheet() {

    }

    public SummarySheet(Menu menu, ArrayList<AdditionPatch> additionPatches, ArrayList<RemovalPatch> removalPatches,
                        int service_id) {
        ArrayList<KitchenDuty> kitchenDuties = new ArrayList<>();
        for (Section section : menu.getSections()) {
            for (MenuItem menuItem : section.getItems()) {
                if (!containsRemoval(removalPatches, menuItem)) {
                    kitchenDuties.add(menuItem.getItemRecipe());
                    kitchenDuties.addAll(menuItem.getItemRecipe().getSubDuties());
                }
            }
        }
        for (MenuItem freeItem : menu.getFreeItems()) {
            kitchenDuties.add(freeItem.getItemRecipe());
            kitchenDuties.addAll(freeItem.getItemRecipe().getSubDuties());
        }
        for (AdditionPatch additionPatch : additionPatches) {
            kitchenDuties.add(additionPatch.getDuty());
            kitchenDuties.addAll(additionPatch.getDuty().getSubDuties());
        }
        ArrayList<Task> tasks = new ArrayList<>();
        int i = 0;
        for (KitchenDuty kitchenDuty : kitchenDuties) {
            Task task = new Task(false, kitchenDuty, service_id, i);
            tasks.add(task);
            i++;
        }
        this.tasks = tasks;
    }

    // metodi db
    public static SummarySheet loadSummarySheetForService(Service s) {
        if (SummarySheet.hasSummarySheet(s)) {
            SummarySheet result = new SummarySheet();

            result.tasks = Task.getAllTasks(s.getService_id());
            result.extraDuties = result.tasks.stream()
                                             .filter(Task::isOptionalDuty)
                                             .map(Task::getDuty)
                                             .collect(Collectors.toCollection(ArrayList::new));

            return result;
        } else {
            return null;
        }
    }

    public static boolean hasSummarySheet(Service s) {
        // uso un array perchè non posso passare una variabile singola all'interno di una classe anonima/lambda
        boolean[] foundOne = new boolean[1];

        //language=MySQL
        String query = "SELECT exists(SELECT * FROM task WHERE service_id = " + +s.getService_id() + ")";
        PersistenceManager.executeQuery(query, rs -> foundOne[0] = rs.getBoolean(1));

        return foundOne[0];
    }

    public boolean containsRemoval(ArrayList<RemovalPatch> removalPatches, MenuItem menuItem) {
        for (RemovalPatch removalPatch : removalPatches) {
            if (removalPatch.getMenuItem().equals(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<KitchenDuty> getExtraDuties() {
        return extraDuties;
    }

    public Task addExtraDuty(KitchenDuty kitchenDuty) {
        extraDuties.add(kitchenDuty);
        Task task = new Task(true, kitchenDuty);
        tasks.add(task);
        return task;
    }

    public Task deleteExtraDuty(KitchenDuty kitchenDuty) {
        Task deletedTask = null;
        for (Task task : tasks) {
            if (task.isOptionalDuty() && kitchenDuty == task.getDuty()) {
                for (KitchenJob job : task.getJobs()) {
                    job.getTurn().freeTime(job.getCook(), job.getDuration());
                    task.getJobs().remove(job);
                }
                deletedTask = task;
                tasks.remove(task);
            }
        }
        extraDuties.remove(kitchenDuty);
        return deletedTask;
    }

    public void deleteAllTasks() {
        // TODO
    }

    public void changeTaskOrder(Task a, Task b) {
        int oldAIndex = tasks.indexOf(a);
        int oldBIndex = tasks.indexOf(b);

        if (oldAIndex == -1 || oldBIndex == -1) {
            throw new RuntimeException();
        }

        tasks.set(oldAIndex, b);
        tasks.set(oldBIndex, a);
    }

    public boolean containsTask(Task task) {
        return tasks.contains(task);
    }
}

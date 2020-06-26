package businesslogic.kitchentask;

import businesslogic.menu.Menu;
import businesslogic.menu.MenuItem;
import businesslogic.menu.Section;
import businesslogic.recipe.KitchenDuty;
import businesslogic.recipe.Recipe;

import java.util.ArrayList;

public class SummarySheet {
    private ArrayList<Task> tasks;
    private ArrayList<KitchenDuty> extraDuties;

    public SummarySheet(Menu menu){
        ArrayList<KitchenDuty> kitchenDuties = new ArrayList<>();
        for(Section section: menu.getSections()){
            for(MenuItem menuItem: section.getItems()){
                kitchenDuties.add(menuItem.getItemRecipe());
                kitchenDuties.addAll(menuItem.getItemRecipe().getSubDuties());
            }
        }
        for(MenuItem freeItem: menu.getFreeItems()){
            kitchenDuties.add(freeItem.getItemRecipe());
            kitchenDuties.addAll(freeItem.getItemRecipe().getSubDuties());
        }
        ArrayList<Task> tasks = new ArrayList<>();
        for(KitchenDuty kitchenDuty: kitchenDuties){
            Task task = new Task(false, kitchenDuty);
            tasks.add(task);
        }
        this.tasks = tasks;
    }


    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<KitchenDuty> getExtraDuties() {
        return extraDuties;
    }

    public Task addExtraDuty(KitchenDuty kitchenDuty){
        extraDuties.add(kitchenDuty);
        Task task = new Task(true, kitchenDuty);
        tasks.add(task);
        return task;
    }

    public Task deleteExtraDuty(KitchenDuty kitchenDuty){
        Task deletedTask = null;
        for (Task task: tasks){
            if(task.isOptionalDuty() && kitchenDuty == task.getDuty()){
                for (KitchenJob job: task.getJobs()){
                    job.getTurn().freeTime(job.getCook(),job.getEstimatedDuration());
                    task.getJobs().remove(job);
                }
                deletedTask = task;
                tasks.remove(task);
            }
        }
        extraDuties.remove(kitchenDuty);
        return deletedTask;
    }

    public void deleteAllTasks(){
        //TODO
    }

}

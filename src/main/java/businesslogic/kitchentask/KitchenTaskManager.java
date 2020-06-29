package businesslogic.kitchentask;

import businesslogic.CatERing;
import businesslogic.EventException;
import businesslogic.TaskException;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.recipe.KitchenDuty;
import businesslogic.turn.KitchenTurn;
import businesslogic.user.User;
import persistence.KitchenTaskPersistence;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KitchenTaskManager {
    private SummarySheet currentSheet;
    private HashMap<SummarySheet, Service> serviceOfSheet;
    private ArrayList<KitchenTaskEventReceiver> eventReceivers;

    public KitchenTaskManager() {
        eventReceivers = new ArrayList<>();
        serviceOfSheet = new HashMap<>();
    }

    public SummarySheet getCurrentSheet() {
        return currentSheet;
    }

    public void setCurrentSheet(SummarySheet sheet) { this.currentSheet = sheet;}

    private void notifySheetCreate(SummarySheet sheet) {
        for (KitchenTaskEventReceiver eventReceiver : eventReceivers) {
            eventReceiver.updateSheetCreate(sheet);
        }
    }

    private void notifyTaskOrderChanged(List<Task> involvedTasks) {
        eventReceivers.forEach(er -> er.updateTaskOrderChanged(involvedTasks));
    }

    private void notifyAddExtraDuty(Task task) {
        for (KitchenTaskEventReceiver eventReceiver : eventReceivers) {
            eventReceiver.updateAddExtraDuty(task);
        }
    }

    private void notifyAssignedCook(User user, KitchenJob kitchenJob) {
        for (KitchenTaskEventReceiver eventReceiver : eventReceivers) {
            eventReceiver.updateCookAssigned(user,kitchenJob);
        }
    }

    private void notifyDeleteExtraDuty(KitchenDuty kitchenDuty, Task task) {
        for (KitchenTaskEventReceiver eventReceiver : eventReceivers) {
            eventReceiver.updateDeleteExtraDuty(kitchenDuty, task);
        }
    }

    private void notifyEditTask(Task task) {
        for (KitchenTaskEventReceiver eventReceiver : eventReceivers) {
            eventReceiver.updateEditTask(task);
        }
    }

    private void notifyCreatedKitchenJob(KitchenJob kitchenJob) {
        for (KitchenTaskEventReceiver eventReceiver : eventReceivers) {
            eventReceiver.updateCreateKitchenJob(kitchenJob);
        }
    }

    private void notifyDeletedKitchenJob(KitchenJob kitchenJob) {
        for (KitchenTaskEventReceiver eventReceiver : eventReceivers) {
            eventReceiver.updateDeletedKitchenJob(kitchenJob);
        }
    }

    public void notifyKitchenTurnComplete(KitchenTurn kitchenTurn){
        for (KitchenTaskEventReceiver eventReceiver : eventReceivers) {
            eventReceiver.updateKitchenTurnComplete(kitchenTurn);
        }
    }

    private void notifyEditKitchenJob(KitchenJob kitchenJob) {
        for (KitchenTaskEventReceiver eventReceiver : eventReceivers) {
            eventReceiver.updateEditKitchenJob(kitchenJob);
        }
    }

    private void notifyEditTurn(KitchenTurn turn) {
        eventReceivers.forEach(er -> er.updateEditTurn(turn));
    }

    public SummarySheet createSummarySheet(Event event, Service service) throws UseCaseLogicException, EventException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef() || !event.getAssignedChef().equals(user) || !event.containsService(service)) {
            throw new UseCaseLogicException();
        }
        if (!service.isConfirmed()) {
            throw new EventException();
        }

        this.currentSheet = service.createSummarySheet();
        serviceOfSheet.put(currentSheet, service);
        notifySheetCreate(currentSheet);

        return currentSheet;
    }

    public SummarySheet openSummarySheetForEditing(Event event, Service associatedService) throws UseCaseLogicException, EventException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef() || !event.containsService(associatedService) || associatedService.getSheet() == null) {
            throw new UseCaseLogicException();
        }

        if (!event.isAssignedTo(user) || !associatedService.isConfirmed()) {
            throw new EventException();
        }

        this.currentSheet = associatedService.getSheet();
        this.serviceOfSheet.put(currentSheet, associatedService);
        return this.currentSheet;
    }

    public SummarySheet openSummarySheetForViewing(Service service) throws UseCaseLogicException {
        if (service.getSheet() == null) {
            throw new UseCaseLogicException();
        }

        this.currentSheet = service.getSheet();
        return currentSheet;
    }

    public ArrayList<Task> addExtraDuty(KitchenDuty kitchenDuty) throws UseCaseLogicException {
        if (currentSheet == null) {
            throw new UseCaseLogicException();
        }

        ArrayList<Task> newTasks = currentSheet.addExtraDuty(kitchenDuty,
                                                             serviceOfSheet.get(currentSheet)
                                                                           .getService_id());

        newTasks.forEach(this::notifyAddExtraDuty);

        return newTasks;
    }

    public SummarySheet deleteExtraDuty(KitchenDuty kitchenDuty) throws UseCaseLogicException {
        if (currentSheet == null) {
            throw new UseCaseLogicException();
        }
        Task task = currentSheet.deleteExtraDuty(kitchenDuty);
        notifyDeleteExtraDuty(kitchenDuty, task);
        return currentSheet;
    }

    public void changeTaskOrder(Task a, Task b) throws UseCaseLogicException {
        if (currentSheet == null) {
            throw new UseCaseLogicException();
        }

        currentSheet.changeTaskOrder(a, b);

        notifyTaskOrderChanged(List.of(a, b));
    }

    public void editTask(Task taskToEdit, Integer newAmount, Duration newDuration, Boolean toDo) throws UseCaseLogicException, TaskException {
        if (currentSheet == null || !currentSheet.containsTask(taskToEdit)) {
            throw new UseCaseLogicException();
        }

        if (newAmount != null) {
            taskToEdit.setAmount(newAmount);
        }

        if (newDuration != null && !newDuration.isNegative()) {
            taskToEdit.setDuration(newDuration);
        }

        if (toDo != null) {
            if (toDo) {
                taskToEdit.setToDo(true);
            } else {
                if (taskToEdit.getJobs().size() > 0) {
                    throw new TaskException();
                }

                taskToEdit.setToDo(false);
            }
        }

        notifyEditTask(taskToEdit);
    }

    public KitchenJob createKitchenJob(Task task, KitchenTurn kitchenTurn, int amount, Duration estimatedDuration) throws TaskException, UseCaseLogicException {
        if (currentSheet == null || Instant.now().compareTo(kitchenTurn.getEnd()) > 0 || kitchenTurn.isComplete()) {
            throw new UseCaseLogicException();
        }
        if (task.getAmount() <= 0 ||
            task.getEstimatedDuration()
                .isNegative() ||
            task.getEstimatedDuration() == Duration.ZERO ||
            !task.isToDo()) {
            throw new TaskException();
        }

        KitchenJob job = task.addKitchenJob(kitchenTurn, amount, estimatedDuration);
        notifyCreatedKitchenJob(job);
        return job;
    }

    public Task deleteKitchenJob(Task task, KitchenJob job) throws TaskException {
        if (!task.getJobs().contains(job)) {
            throw new TaskException();
        }
        task.deleteKitchenJob(job);
        notifyDeletedKitchenJob(job);
        return task;
    }

    public KitchenJob editKitchenJob(KitchenJob job, Integer newAmount, Duration newDuration) throws TaskException {
        KitchenTurn turn = job.getTurn();

        if (!turn.hasConcluded()) {
            if (job.getCook() != null) {
                if (turn.hasUserEnoughTime(job.getCook(), job.getDuration(), newDuration)) {
                    turn.freeTime(job.getCook(), job.getDuration());
                    turn.takeTime(job.getCook(), newDuration);
                    job.setDuration(newDuration);
                }
            } else {
                job.setDuration(newDuration);
            }

            if (newAmount != null) {
                job.setAmount(newAmount);
            }
        } else {
            throw new TaskException();
        }

        notifyEditKitchenJob(job);

        return job;
    }

    public KitchenJob assignCook(KitchenJob job, User user) throws UseCaseLogicException, TaskException {
        if ((user != null && !user.isCook()) || Instant.now().compareTo(job.getTurn().getEnd()) > 0) {
            throw new UseCaseLogicException();
        }

        if (job.getCook() != user) {
            if (user == null || job.getTurn().hasUserEnoughTime(user, job.getDuration())) {
                if (job.getCook() != null) {
                    job.getTurn().freeTime(job.getCook(), job.getDuration());
                }

                if (user != null) {
                    job.getTurn().takeTime(user, job.getDuration());
                }

                job.assignCook(user);
            } else {
                throw new TaskException();
            }
        }
        return job;
    }

    public KitchenTurn changeKitchenTurnState(KitchenTurn turn, boolean complete) throws TaskException {
        if (turn.hasConcluded()) {
            throw new TaskException();
        }

        turn.setComplete(complete);

        notifyKitchenTurnComplete(turn);

        return turn;
    }

    public void addReceiver(KitchenTaskPersistence kitchenTaskPersistence) {
        eventReceivers.add(kitchenTaskPersistence);
    }
}

package businesslogic.event;

import businesslogic.user.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class Event {
    private LocalDate startDate;
    private State state;
    private static enum State {PROGRAMMATO, ATTIVO, TERMINATO, ANNULLATO};
    private MacroEvent macroEvent;
    private ArrayList<Service> services;
    private User assignedChef;

    public Event(LocalDate startDate, MacroEvent macroEvent){
        this.startDate = startDate;
        this.macroEvent = macroEvent;
        this.state = State.PROGRAMMATO;
        services = new ArrayList<>();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public State getState() {
        return state;
    }

    public MacroEvent getMacroEvent() {
        return macroEvent;
    }

    public User getAssignedChef() {
        return assignedChef;
    }

    public Event cancel(){
        //TODO
        return null;
    }

    public Event delete(){
        //TODO
        return null;
    }
    public boolean isActive(){
        return this.state==State.ATTIVO;
    }

    public boolean containsService(Service service){
        return services.contains(service);
    }

}

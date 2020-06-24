package businesslogic.event;

import businesslogic.user.User;

import java.time.LocalDate;

public class Event {
    private LocalDate startDate;
    private State state;
    private static enum State {PROGRAMMATO, ATTIVO, TERMINATO, ANNULLATO};
    private MacroEvent macroEvent;
    private User assignedChef;

    public Event(LocalDate startDate, MacroEvent macroEvent){
        this.startDate = startDate;
        this.macroEvent = macroEvent;
        this.state = State.PROGRAMMATO;
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
        //TODO
        return false;
    }

}

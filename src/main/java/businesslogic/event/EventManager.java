package businesslogic.event;

import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;

public class EventManager {

    private ArrayList<EventEventReceivers> receivers;
    private Event currentEvent;
    public static enum Strategy {All, AllNext, OnlyThis};

    public EventManager(Event event){
        receivers = new ArrayList<>();
        currentEvent = event;
    }

    public EventManager(){
        receivers = new ArrayList<>();
    }

    public void setCurrentEvent(Event event){
        if(event!=null){
            this.currentEvent = event;
        }
    }

    public Event createEvent(LocalDate startDate, String client){
        //TODO
        return  null;
    }

    public ArrayList<Event> cancelEvent(Strategy strategy){
        //TODO
        return  null;
    }

    public ArrayList<Event> deleteEvent(Strategy strategy){
        //TODO
        return null;
    }

    public Pair<ArrayList<Event>, ArrayList<Event>> editEventFrequency(Event baseEvent, EventFrequency frequency, LocalDate editFromDate){
        //TODO
        return null;
    }

    public ObservableList<EventInfo> getEventInfo() {
        return EventInfo.loadAllEventInfo();
    }





    public void addReceiver(EventEventReceivers er){this.receivers.add(er);}

    public void removeReceiver(EventEventReceivers er) {this.receivers.remove(er);}

    private void notifyCreateEvent (Event event){
        for(EventEventReceivers er: this.receivers){
            er.updateCreateEvent(event);
        }
    }
    private void notifyCancelEvent(Event event){
        for(EventEventReceivers er: this.receivers){
            er.updateCancelEvent(event);
        }
    }
    private void notifyEditEvent(Event event){
        for(EventEventReceivers er: this.receivers){
            er.updateEditEvent(event);
        }
    }
    private void notifyDeleteEvent(Event event){
        for(EventEventReceivers er: this.receivers){
            er.updateDeleteEvent(event);
        }
    }
}

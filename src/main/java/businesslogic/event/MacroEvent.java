package businesslogic.event;

import businesslogic.user.User;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;

public class MacroEvent {
    private String name;
    private String notes;
    private String client;
    private EventFrequency frequency;
    private ArrayList<Event> events;
    private User organizer;
    public static enum Strategy {All, AllNext, OnlyThis};

    public MacroEvent (String client, User organizer, String name){
        this.client = client;
        this.organizer = organizer;
        this.name = name;
        events = new ArrayList<>();
    }

    public Event addEvent(LocalDate startDate){
        //TODO
        return null;
    }

    public ArrayList<Event> cancel(Event event, Strategy strategy){
        //TODO
        return null;
    }

    public ArrayList<Event> delete(Event event, Strategy strategy){
        //TODO
        return null;
    }

    public Pair<ArrayList<Event>, ArrayList> changeFrequency(Event event, EventFrequency frequency, LocalDate editFrom){
        //TODO
        return null;
    }

}

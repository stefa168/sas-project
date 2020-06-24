package businesslogic.event;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public class EventManager {

    private ArrayList<EventEventReceivers> receivers;
    private Event currentEvent;

    public EventManager(){
        receivers = new ArrayList<>();
    }

    public ObservableList<EventInfo> getEventInfo() {
        return EventInfo.loadAllEventInfo();
    }
}

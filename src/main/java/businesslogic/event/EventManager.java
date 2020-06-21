package businesslogic.event;

import javafx.collections.ObservableList;

public class EventManager {
    public ObservableList<EventInfo> getEventInfo() {
        return EventInfo.loadAllEventInfo();
    }
}

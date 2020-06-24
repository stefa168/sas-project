package businesslogic.event;

public interface EventEventReceivers {
    public void updateCreateEvent(Event event);
    public void updateCancelEvent(Event event);
    public void updateEditEvent(Event event);
    public void updateDeleteEvent(Event event);
}

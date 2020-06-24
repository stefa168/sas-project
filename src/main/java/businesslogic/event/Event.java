package businesslogic.event;

import java.time.LocalDate;

public class Event {
    private LocalDate startDate;
    public static enum State {PROGRAMMATO, ATTIVO, TERMINATO, ANNULLATO};
}

package businesslogic.turn;

import java.time.Instant;
import java.time.LocalDate;

public class ServiceTurn extends Turn {
    public ServiceTurn(Instant startHour, Instant endHour) {
        super(startHour, endHour);
    }
}

package businesslogic.turn;

import java.time.Instant;
import java.time.LocalDate;

public class ServiceTurn extends Turn {
    public ServiceTurn(LocalDate day, Instant startHour, Instant endHour) {
        super(startHour, endHour);
    }
}

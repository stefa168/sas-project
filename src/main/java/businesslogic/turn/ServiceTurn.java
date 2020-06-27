package businesslogic.turn;

import java.time.Instant;

public class ServiceTurn extends Turn {
    public ServiceTurn(Instant startHour, Instant endHour) {
        super(startHour, endHour);
    }

    @Override
    public boolean hasConcluded() {
        throw new RuntimeException("Not Implemented");
    }
}

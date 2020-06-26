package businesslogic.turn;

import java.time.Instant;
import java.time.LocalDate;

public abstract class Turn {
    protected LocalDate start;
    protected LocalDate end;

    public Turn(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
}

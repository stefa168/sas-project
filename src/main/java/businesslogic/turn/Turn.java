package businesslogic.turn;

import java.time.Instant;

public abstract class Turn {
    protected Instant start;
    protected Instant end;

    public Turn(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public abstract boolean hasConcluded();
}

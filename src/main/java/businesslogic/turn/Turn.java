package businesslogic.turn;

import java.time.Instant;

public abstract class Turn {
    protected Instant start;
    protected Instant end;

    public Turn(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }
    public Turn(){}

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public abstract boolean hasConcluded();

    @Override
    public String toString() {
        return String.format("Inizio: %s; Fine: %s", start.toString(), end.toString());
    }
}

package businesslogic.turn;

import java.time.Instant;

public abstract class Turn implements TurnItemInfo{
    protected Instant start;
    protected Instant end;
    protected int turn_id;

    public Turn(Instant start, Instant end, int turn_id) {
        this.start = start;
        this.end = end;
        this.turn_id = turn_id;
    }

    public Turn(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }

    public Turn() {}

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

    public int getTurn_id() { return turn_id; }


}

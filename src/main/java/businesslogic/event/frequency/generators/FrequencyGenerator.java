package businesslogic.event.frequency.generators;

import java.time.LocalDate;

public interface FrequencyGenerator {
    public LocalDate nextDate(LocalDate fromDate);
}

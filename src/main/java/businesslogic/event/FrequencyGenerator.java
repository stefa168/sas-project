package businesslogic.event;

import java.time.LocalDate;

public interface FrequencyGenerator {
    public LocalDate nextDate(LocalDate fromDate);
}

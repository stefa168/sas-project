package businesslogic.event.frequency.generators;

import java.time.LocalDate;
import java.util.Set;

public class WeeklyFrequencyGenerator extends AbstractFrequencyGenerator{

    private Set<String> weekdays;

    WeeklyFrequencyGenerator(int baseFrequency, Set<String> weekdays){
        super(baseFrequency);
        this.weekdays = weekdays;
    }
    @Override
    public LocalDate nextDate(LocalDate fromDate) {
        //TODO
        return null;
    }
}

package businesslogic.event;

import java.time.LocalDate;

public class DailyFrequencyGenerator extends AbstractFrequencyGenerator {

    DailyFrequencyGenerator(int baseFrequency){
        super(baseFrequency);
    }

    @Override
    public LocalDate nextDate(LocalDate fromDate) {
        //TODO
        return null;
    }
}

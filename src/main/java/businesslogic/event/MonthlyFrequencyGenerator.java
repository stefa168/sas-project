package businesslogic.event;

import java.time.LocalDate;

public class MonthlyFrequencyGenerator extends AbstractFrequencyGenerator{

    MonthlyFrequencyGenerator(int baseFrequency){
        super(baseFrequency);
    }
    @Override
    public LocalDate nextDate(LocalDate fromDate) {
        //TODO
        return null;
    }
}

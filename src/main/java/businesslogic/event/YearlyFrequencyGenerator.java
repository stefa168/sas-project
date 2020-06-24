package businesslogic.event;

import java.time.LocalDate;

public class YearlyFrequencyGenerator extends AbstractFrequencyGenerator{

    YearlyFrequencyGenerator(int baseFrequency){
        super(baseFrequency);
    }
    @Override
    public LocalDate nextDate(LocalDate fromDate) {
        //TODO
        return null;
    }
}

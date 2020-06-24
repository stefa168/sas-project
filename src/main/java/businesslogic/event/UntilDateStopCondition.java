package businesslogic.event;

import java.time.LocalDate;
import java.util.ArrayList;

public class UntilDateStopCondition implements FrequencyStopCondition{

    private LocalDate endDate;

    UntilDateStopCondition(LocalDate endDate){
        this.endDate = endDate;
    }

    @Override
    public boolean shouldContinue(MacroEvent macroEvent, LocalDate baseDate, ArrayList<LocalDate> newDates) {
        //TODO
        return false;
    }
}

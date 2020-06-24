package businesslogic.event;

import java.time.LocalDate;
import java.util.ArrayList;

public class SpecificAmountStopCondition implements FrequencyStopCondition {

    private int wantedOccurencesCount;

    SpecificAmountStopCondition(int wantedOccurencesCount){
        this.wantedOccurencesCount = wantedOccurencesCount;
    }

    @Override
    public boolean shouldContinue(MacroEvent macroEvent, LocalDate baseDate, ArrayList<LocalDate> newDates) {
        //TODO
        return false;
    }
}

package businesslogic.event.frequency.conditions;

import businesslogic.event.MacroEvent;

import java.time.LocalDate;
import java.util.ArrayList;

public interface FrequencyStopCondition {
    public boolean shouldContinue(MacroEvent macroEvent, LocalDate baseDate, ArrayList<LocalDate> newDates);
}

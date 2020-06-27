package businesslogic.event.frequency;

import businesslogic.event.MacroEvent;
import businesslogic.event.frequency.conditions.FrequencyStopCondition;
import businesslogic.event.frequency.generators.FrequencyGenerator;

import java.time.LocalDate;
import java.util.ArrayList;

public class EventFrequency {
    private FrequencyGenerator generator;
    private FrequencyStopCondition stopCondition;

    public ArrayList<LocalDate> getDates(MacroEvent event, LocalDate baseDate){
        //TODO
        return null;
    }
}

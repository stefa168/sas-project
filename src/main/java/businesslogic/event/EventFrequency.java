package businesslogic.event;

import jdk.jfr.Frequency;

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

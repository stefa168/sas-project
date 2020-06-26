package businesslogic.recipe;

import java.time.Duration;
import java.util.ArrayList;

public class Preparation extends KitchenDuty{

    private ArrayList<Preparation> subDuties;

    public Preparation(String name, String instruction, int resultingAmount, Duration constantConcreteActivityTime, Duration variableConcreteActivityTime){
        super(name,instruction,resultingAmount,constantConcreteActivityTime,variableConcreteActivityTime);
        subDuties = new ArrayList<>();
    }
    @Override
    public ArrayList<KitchenDuty> getSubDuties() {
        //TODO
        return null;
    }
}

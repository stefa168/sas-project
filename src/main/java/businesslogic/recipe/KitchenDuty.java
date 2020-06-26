package businesslogic.recipe;

import java.time.Duration;
import java.util.ArrayList;

public abstract class KitchenDuty {
    protected String name;
    protected String instruction;
    protected int resultingAmount;
    protected Duration constantConcreteActivityTime;
    protected Duration variableConcreteActivityTime;

    public KitchenDuty(String name){
        this.name = name;
    }
    public KitchenDuty(){}

    public KitchenDuty(String name, String instruction, int resultingAmount, Duration constantConcreteActivityTime, Duration variableConcreteActivityTime) {
        this.name = name;
        this.instruction = instruction;
        this.resultingAmount = resultingAmount;
        this.constantConcreteActivityTime = constantConcreteActivityTime;
        this.variableConcreteActivityTime = variableConcreteActivityTime;
    }

    public abstract ArrayList<KitchenDuty> getSubDuties();
}

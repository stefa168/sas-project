package businesslogic.recipe;

import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

public abstract class KitchenDuty {
    protected String name;
    protected String instruction;
    protected int resultingAmount;
    protected Duration constantConcreteActivityTime;
    protected Duration variableConcreteActivityTime;

    public KitchenDuty(String name) {
        this.name = name;
    }

    public KitchenDuty() {}

    public KitchenDuty(String name, String instruction, int resultingAmount, Duration constantConcreteActivityTime,
                       Duration variableConcreteActivityTime) {
        this.name = name;
        this.instruction = instruction;
        this.resultingAmount = resultingAmount;
        this.constantConcreteActivityTime = constantConcreteActivityTime;
        this.variableConcreteActivityTime = variableConcreteActivityTime;
    }

    public static ArrayList<KitchenDuty> loadAllSubKitchenDuty(int kitchenDuty_id) {
        String query = "SELECT * FROM SubDuties WHERE kitchenDuty_id =" + kitchenDuty_id;
        ArrayList<KitchenDuty> subDuties = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int subKitchenDuty_id = rs.getInt("subKitchenDuty_id");

                boolean subIsRecipe = rs.getBoolean("subIsRecipe");
                KitchenDuty kitchenDuty;
                if (subIsRecipe) {
                    kitchenDuty = Recipe.loadRecipeById(subKitchenDuty_id);
                    subDuties.add(kitchenDuty);
                } else {
                    if (subKitchenDuty_id != kitchenDuty_id) {
                        kitchenDuty = Preparation.getPreparationById(subKitchenDuty_id);
                        subDuties.add(kitchenDuty);
                    }
                }
            }
        });
        return subDuties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KitchenDuty duty = (KitchenDuty) o;
        return resultingAmount == duty.resultingAmount &&
               name.equals(duty.name) &&
               constantConcreteActivityTime.equals(duty.constantConcreteActivityTime) &&
               variableConcreteActivityTime.equals(duty.variableConcreteActivityTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, instruction, resultingAmount, constantConcreteActivityTime,
                            variableConcreteActivityTime);
    }

    public abstract ArrayList<KitchenDuty> getSubDuties();

    public abstract int getKitchenDutyId();

    public String getName() {
        return name;
    }
}

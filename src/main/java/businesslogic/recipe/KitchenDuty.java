package businesslogic.recipe;

import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    public abstract int getKitchenDutyId();

    public static ArrayList<KitchenDuty> loadAllSubKitchenDuty(int kitchenDuty_id){
        String query = "SELECT * FROM SubDuties WHERE kitchenDuty_id =" + kitchenDuty_id;
        ArrayList<KitchenDuty> subDuties = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int subKitchenDuty_id = rs.getInt("subKitchenDuty_id");
                int subIsRecipe = rs.getInt("subIsRecipe");
                KitchenDuty kitchenDuty;
                if(subIsRecipe == 1){
                    kitchenDuty = Recipe.loadRecipeById(subKitchenDuty_id);
                }
                else{
                    kitchenDuty = Preparation.getPreparationById(subKitchenDuty_id);
                }
                subDuties.add(kitchenDuty);
            }
        });
        return subDuties;
    }

}

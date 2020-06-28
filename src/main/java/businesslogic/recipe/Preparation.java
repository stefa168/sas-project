package businesslogic.recipe;

import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;

public class Preparation extends KitchenDuty{

    private ArrayList<KitchenDuty> subDuties;
    private int preparation_id;

    public Preparation(){

    }


    public Preparation(String name, String instruction, int resultingAmount, Duration constantConcreteActivityTime, Duration variableConcreteActivityTime){
        super(name,instruction,resultingAmount,constantConcreteActivityTime,variableConcreteActivityTime);
    }
    @Override
    public ArrayList<KitchenDuty> getSubDuties() {
        return subDuties;
    }

    @Override
    public int getKitchenDutyId() {
        return preparation_id;
    }

    public void setId(int id){this.preparation_id = id;}

    //metodoDB

    public static Preparation getPreparationById(int id) {
        String query = "SELECT * FROM Preparation WHERE id =" + id;
        Preparation preparation = new Preparation();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                preparation.preparation_id = rs.getInt("id");
                preparation.name = rs.getString("name");
                preparation.subDuties = KitchenDuty.loadAllSubKitchenDuty(preparation.preparation_id);
            }
        });
        return preparation;
    }

    public static ArrayList<Preparation> getAllPreparations(){
        String query = "SELECT * FROM Preparation";
        ArrayList<Preparation> preparations = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Preparation preparation = new Preparation();
                preparation.preparation_id = rs.getInt("id");
                preparation.name = rs.getString("name");
                preparation.subDuties = KitchenDuty.loadAllSubKitchenDuty(preparation.preparation_id);
                preparations.add(preparation);
            }
        });

        return preparations;
    }

}

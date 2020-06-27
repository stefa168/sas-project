package businesslogic.event;

import businesslogic.kitchentask.SummarySheet;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuItem;
import businesslogic.recipe.Recipe;
import businesslogic.turn.ServiceJob;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import javax.sound.midi.Patch;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

public class Service implements EventItemInfo{
    private String name;
    private int offsetDay;
    private Time startHour;
    private Time endHour;
    private String place;
    private int diners;
    private String typology;
    private Menu menu;
    private ArrayList<AdditionPatch> additionPatches;
    private ArrayList<RemovalPatch> removalPatches;
    private State state;
    private ArrayList<ServiceJob> serviceJobs;
    private SummarySheet sheet;
    private int service_id;

    public void setState(State state) {
        this.state = state;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public Service(String name, int startOffset, Time startHour, Time endHour, int diners, String place, String details) {
        this.name = name;
        this.offsetDay = startOffset;
        this.startHour = startHour;
        this.endHour = endHour;
        this.diners = diners;
        this.place = place;
        this.typology = details;
        this.state = State.INPREPARAZIONE;
    }

    public ArrayList<AdditionPatch> getAdditionPatches() {
        return additionPatches;
    }

    public ArrayList<RemovalPatch> getRemovalPatches() {
        return removalPatches;
    }

    public String getName() {
        return name;
    }

    public int getOffsetDay() {
        return offsetDay;
    }

    public void service_id(int id){ this.service_id = id;}

    public Time getStartHour() {
        return startHour;
    }

    public Time getEndHour() {
        return endHour;
    }

    public String getPlace() {
        return place;
    }

    public int getDiners() {
        return diners;
    }

    public String getTypology() {
        return typology;
    }

    public State getState() {
        return state;
    }

    public ArrayList<ServiceJob> getServiceJobs() {
        return serviceJobs;
    }

    public SummarySheet getSheet() {
        return sheet;
    }

    public Menu getMenu() {return this.menu;}

    public SummarySheet createSummarySheet() {
        SummarySheet sheet = new SummarySheet(this.getMenu());
        this.sheet = sheet;
        return sheet;
    }

    public SummarySheet loadSummarySheet(Service linkedService) {
        //todo durante sql
        return null;
    }

    public boolean isConfirmed() {
        return state == State.CONFERMATO;
    }

    //metodi per il db

    public static ObservableList<Service> loadServiceForEvent(int event_id) {
        ObservableList<Service> result = FXCollections.observableArrayList();
        String query = "SELECT * FROM Service WHERE event_id = " + event_id;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String name = rs.getString("name");
                int serviceId = rs.getInt("service_id");
                int menuId = rs.getInt("menu_id");
                int offsetDay = rs.getInt("offsetDay");
                Time startHour = rs.getTime  ("startHour");
                Time endHour = rs.getTime("endHour");
                int diners = rs.getInt("diners");
                String typology = rs.getString("typology");
                String place = rs.getString("place");
                int s = rs.getInt("state");
                State state;
                if(s == 0){
                    state = State.INPREPARAZIONE;
                }
                else if(s==1){
                    state = State.CONFERMATO;
                }
                else if(s==2){
                    state = State.ANNULLATO;
                }
                else {
                    state = State.TERMINATO;
                }
                Service service = new Service(name,offsetDay,startHour,endHour,diners,place,typology);
                service.setService_id(serviceId);
                service.setState(state);
                /* messi come commentati per risolvere problema in eventsinfo

                Menu menu = Menu.loadMenuById(menuId);
                service.setMenu(menu);
                service.getAllAdditionPatches();
                service.getAllRemovalPatches();  */
                result.add(service);
            }
        });

        return result;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void getAllAdditionPatches(){
        String query = "SELECT * FROM Patch WHERE menuItem_id IS NULL AND service_id =" + this.service_id;
        ArrayList<AdditionPatch> additionPatches = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int recipeId = rs.getInt("recipe_id");
                int idPatch = rs.getInt("patch_id");
                Recipe recipe = Recipe.loadRecipeById(recipeId);
                AdditionPatch additionPatch = new AdditionPatch(recipe,idPatch);
                additionPatches.add(additionPatch);

            }
        });
        this.additionPatches = additionPatches;
    }

    public void getAllRemovalPatches(){
        String query = "SELECT * FROM `Patch` WHERE `menuItem_id` IS NOT NULL AND `service_id` = `" + this.service_id + "`";
        ArrayList<RemovalPatch> removalPatches = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int recipeId = rs.getInt("recipe_id");
                int idPatch = rs.getInt("patch_id");
                int menuItemId = rs.getInt("menuItem_id");
                Recipe recipe = Recipe.loadRecipeById(recipeId);
                MenuItem menuItem = MenuItem.getMenuItemById(menuItemId);
                menuItem.setItemRecipe(recipe);
                RemovalPatch additionPatch = new RemovalPatch(menuItem, idPatch);
                removalPatches.add(additionPatch);

            }
        });
        this.removalPatches = removalPatches;
    }

    private static enum State {INPREPARAZIONE, CONFERMATO, ANNULLATO, TERMINATO}

    public String toString() {
        return name + ": " + offsetDay + " (" + startHour + "-" + endHour + "), " + diners + " pp.";
    }

}

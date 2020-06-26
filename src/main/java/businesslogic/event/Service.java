package businesslogic.event;

import businesslogic.kitchentask.SummarySheet;
import businesslogic.menu.Menu;
import businesslogic.turn.ServiceJob;

import javax.sound.midi.Patch;
import java.util.ArrayList;

public class Service {
    private String name;

    public String getName() {
        return name;
    }

    public int getOffsetDay() {
        return offsetDay;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
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

    public ArrayList<Patch> getPatches() {
        return patches;
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

    private int offsetDay;
    private int startHour;
    private int endHour;
    private String place;
    private int diners;
    private String typology;
    private Menu menu;
    private ArrayList<Patch> patches;
    private State state;
    private static enum State {INPREPARAZIONE, CONFERMATO, ANNULLATO, TERMINATO};
    private ArrayList<ServiceJob> serviceJobs;
    private SummarySheet sheet;

    public Service(String name, int startOffset, int startHour, int endHour, int diners, String place, String details){
        this.name = name;
        this.offsetDay = startOffset;
        this.startHour = startHour;
        this.endHour = endHour;
        this.diners = diners;
        this.place = place;
        this.typology = details;
        this.state = State.INPREPARAZIONE;
    }

    public Menu getMenu(){return this.menu;}

    public SummarySheet createSummarySheet(){
        SummarySheet sheet = new SummarySheet(this.getMenu());
        this.sheet = sheet;
        return sheet;
    }

}

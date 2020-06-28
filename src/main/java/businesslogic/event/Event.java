package businesslogic.event;

import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Event implements EventItemInfo {
    private LocalDate startDate;
    private LocalDate endDate;
    private State state;
    private MacroEvent macroEvent;
    private ObservableList<Service> services;
    private User assignedChef;
    private int event_id;
    private int participants;
    private User organizer;
    private String name;

    public Event(LocalDate startDate, MacroEvent macroEvent) {
        this.startDate = startDate;
        this.macroEvent = macroEvent;
        this.state = State.PROGRAMMATO;
    }

    public Event(int event_id, String name, LocalDate startDate, LocalDate endDate, int participants, User organizer,
                 State state, User assignedChef) {
        this.event_id = event_id;
        this.startDate = startDate;
        this.name = name;
        this.endDate = endDate;
        this.participants = participants;
        this.organizer = organizer;
        this.state = state;
        this.assignedChef = assignedChef;
    }

    //database
    public static ObservableList<Event> loadAllEventInfo() {
        ObservableList<Event> all = FXCollections.observableArrayList();
        String query = "SELECT * FROM Event WHERE true";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String n = rs.getString("name");
                int event_id = rs.getInt("event_id");
                Date sqlDateStart = rs.getDate("date_start");
                Date sqlDateEnd = rs.getDate("date_end");
                LocalDate dateStart = sqlDateStart.toLocalDate();
                LocalDate dateEnd = sqlDateEnd.toLocalDate();
                int participants = rs.getInt("expected_participants");
                int org = rs.getInt("organizer_id");
                int chef = rs.getInt("chef_id");
                int s = rs.getInt("state");
                State state;
                if (s == 0) {
                    state = State.PROGRAMMATO;
                } else if (s == 1) {
                    state = State.ATTIVO;
                } else if (s == 2) {
                    state = State.TERMINATO;
                } else {
                    state = State.ANNULLATO;
                }

                Event event = new Event(event_id, n, dateStart, dateEnd, participants, User.loadUserById(org), state,
                                        User.loadUserById(chef));
                all.add(event);
            }
        });

        for (Event event : all) {
            event.services = Service.loadServiceForEvent(event.event_id, event);
        }
        return all;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public State getState() {
        return state;
    }

    public MacroEvent getMacroEvent() {
        return macroEvent;
    }

    public User getAssignedChef() {
        return assignedChef;
    }

    public Event cancel() {
        //TODO
        return null;
    }

    public Event delete() {
        //TODO
        return null;
    }

    public String toString() {

        return name + ": " + state + "-" + startDate + "-" + endDate + ", " + participants + " pp. ( Organizzatore: " + organizer
                .getUserName() + ", Chef: " + assignedChef.getUserName() + ")";
    }

    public boolean isActive() {
        return this.state == State.ATTIVO;
    }

    public boolean containsService(Service service) {
        return services.contains(service);
    }

    public boolean isAssignedTo(User user) {
        return assignedChef.equals(user);
    }

    public ObservableList<Service> getServices() {
        return services;
    }

    private static enum State {PROGRAMMATO, ATTIVO, TERMINATO, ANNULLATO}

}

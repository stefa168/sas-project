package businesslogic.turn;

import businesslogic.event.Event;
import javafx.collections.ObservableList;

public class TurnManager {

    public ObservableList<KitchenTurn> getTurnInfo() {
        return KitchenTurn.loadAllTurnInfo();
    }

}

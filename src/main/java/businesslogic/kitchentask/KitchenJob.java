package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;
import businesslogic.turn.KitchenTurn;
import businesslogic.user.User;

import java.time.Duration;

public class KitchenJob {
    private int amount;
    private Duration estimatedDuration;
    private KitchenTurn turn;
    private User cook;

    public KitchenJob(KitchenTurn turn, int amount, Duration estimatedDuration){
        this.amount = amount;
        this.turn = turn;
        this.estimatedDuration = estimatedDuration;
    }

    public void edit (int amount, Duration estimatedDuration){
        //TODO
    }

    public void assignCook(User user){
        if(user.isCook()){
            this.cook = user;
        }
    }
}

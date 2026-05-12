package entity.accessory;

import core.GameManager;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import javafx.scene.image.Image;

import java.util.HashMap;

public class SukunaCloak extends Accessory {

    //growth
    private final GameManager gameManager;
    private int amountOfTimeGrowthGotMultiply;

    public SukunaCloak(GameManager gameManager){
        super("Sukuna's Cloak",5);
        this.gameManager=gameManager;
        this.setIcon(new Image("accessory/icon/sukunaCloak.png"));
        amountOfTimeGrowthGotMultiply=0;
    }

    @Override
    public GameObject copy() {
        return null;
    }

    @Override
    public void procEffect() {
        if(amountOfTimeGrowthGotMultiply<level) {
            gameManager.increaseGrowth(1.08);
            amountOfTimeGrowthGotMultiply++;
        }
    }
}


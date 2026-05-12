package entity.accessory;

import core.GameManager;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import javafx.scene.image.Image;

import java.util.HashMap;

public class Blindfold extends Accessory{

    //magnet radius*1.3
    private final GameManager gameManager;
    private int amountOfTimeMagnetRadiusGotMultiply;

    public Blindfold(GameManager gameManager){
        super("Gojo's Blindfold",5);
        this.gameManager = gameManager;
        this.setIcon(new Image("accessory/icon/gojoblindfold.png"));
        amountOfTimeMagnetRadiusGotMultiply=0;
    }

    @Override
    public GameObject copy() {
        return null;
    }

    @Override
    public void procEffect() {
        if(amountOfTimeMagnetRadiusGotMultiply<level){
            gameManager.getCharacter().setMagnetRadius(gameManager.getCharacter().getMagnetRadius()*1.3);
            amountOfTimeMagnetRadiusGotMultiply++;
        }

    }
}

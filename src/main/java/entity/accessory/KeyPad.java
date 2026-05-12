package entity.accessory;

import core.GameManager;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.DurationIncreasable;
import javafx.scene.image.Image;

import java.util.HashMap;

public class KeyPad extends Accessory{

    //duration
    private final GameManager gameManager;
    protected HashMap<Weapon,Integer> weaponTrackingHashMap;

    public KeyPad(GameManager gameManager){
        super("Wooting",5);
        this.gameManager = gameManager;
        this.setIcon(utils.SpriteManager.loadImage("accessory/icon/keypad.png"));
    }

    @Override
    public GameObject copy() {
        return null;
    }

    @Override
    public void procEffect() {
        for (Weapon weapon : gameManager.getWeaponList()){
            if(weapon==null){
                break;
            }
            if(weapon instanceof DurationIncreasable durationIncreasable){
                if( weaponTrackingHashMap.containsKey(weapon)){
                    if(weaponTrackingHashMap.get(weapon)<level){
                        durationIncreasable.increaseDuration(Math.pow(1.1, level - weaponTrackingHashMap.get(weapon)));
                        weaponTrackingHashMap.put(weapon, this.level);
                    }
                }
                else if(!weaponTrackingHashMap.containsKey(weapon)){
                    durationIncreasable.increaseDuration(Math.pow(1.1,level));
                    weaponTrackingHashMap.put(weapon,this.level);
                }
            }
        }
    }
}

package entity.accessory;

import core.GameManager;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.SizeIncreasable;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;

public class SixEye extends Accessory{

    //increase size by 10% up to 5 time;
    private final GameManager gameManager;
    protected HashMap<Weapon,Integer> weaponTrackingHashMap;

    public SixEye(GameManager gameManager){
        super("Rikugan",5);
        this.setIcon(utils.SpriteManager.loadImage("accessory/icon/sixeye.png"));
        this.gameManager = gameManager;
        weaponTrackingHashMap = new HashMap<>();
    }

    @Override
    public void procEffect() {
        for (Weapon weapon : gameManager.getWeaponList()){
            if(weapon==null){
                continue;
            }
            if(weapon instanceof SizeIncreasable sizeIncreasable){
                if( weaponTrackingHashMap.containsKey(weapon)){
                    if(weaponTrackingHashMap.get(weapon)<level){
                        sizeIncreasable.increaseSize(Math.pow(1.1, level - weaponTrackingHashMap.get(weapon)));
                        weaponTrackingHashMap.put(weapon, this.level);
                    }
                }
                else if(!weaponTrackingHashMap.containsKey(weapon)){
                    sizeIncreasable.increaseSize(Math.pow(1.1,level));
                    weaponTrackingHashMap.put(weapon,this.level);
                }
            }
        }
    }

    @Override
    public GameObject copy() {
        return null;
    }
}

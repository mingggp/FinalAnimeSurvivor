package entity.accessory;

import core.GameManager;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.DamageIncreasable;
import entityInterface.weaponInterface.SizeIncreasable;
import javafx.scene.image.Image;

import java.util.HashMap;

public class SukunaArm extends Accessory{

    //damage

    private final GameManager gameManager;
    protected HashMap<Weapon,Integer> weaponTrackingHashMap;

    public SukunaArm(GameManager gameManager){
        super("Sukuna's Arm",5);
        this.gameManager=gameManager;
        this.setIcon(utils.SpriteManager.loadImage("accessory/icon/sukunaArm.png"));
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
            if(weapon instanceof DamageIncreasable damageIncreasable){
                if( weaponTrackingHashMap.containsKey(weapon)){
                    if(weaponTrackingHashMap.get(weapon)<level){
                        damageIncreasable.increaseDamage(Math.pow(1.1, level - weaponTrackingHashMap.get(weapon)));
                        weaponTrackingHashMap.put(weapon, this.level);
                    }
                }
                else if(!weaponTrackingHashMap.containsKey(weapon)){
                    damageIncreasable.increaseDamage(Math.pow(1.1,level));
                    weaponTrackingHashMap.put(weapon,this.level);
                }
            }
        }
    }
}

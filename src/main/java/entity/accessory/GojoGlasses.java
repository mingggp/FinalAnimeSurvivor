package entity.accessory;

import core.GameManager;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.CooldownDecreasable;
import javafx.scene.image.Image;

import java.util.HashMap;

public class GojoGlasses extends Accessory{
    //reduce cooldown
    private final GameManager gameManager;
    protected HashMap<Weapon,Integer> weaponTrackingHashMap;

    public GojoGlasses(GameManager gameManager){
        super("Gojo's Glasses",5);
        this.gameManager=gameManager;
        this.setIcon(new Image("accessory/icon/gojoglass.png"));
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
            if(weapon instanceof CooldownDecreasable cooldownDecreasable){
                if( weaponTrackingHashMap.containsKey(weapon)){
                    if(weaponTrackingHashMap.get(weapon)<level){
                        cooldownDecreasable.decreaseCooldown(Math.pow(0.92, level - weaponTrackingHashMap.get(weapon)));
                        weaponTrackingHashMap.put(weapon, this.level);
                    }
                }
                else if(!weaponTrackingHashMap.containsKey(weapon)){
                    cooldownDecreasable.decreaseCooldown(Math.pow(0.92,level));
                    weaponTrackingHashMap.put(weapon,this.level);
                }
            }
        }
    }
}

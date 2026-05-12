package entity.accessory;

import core.GameManager;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.DamageIncreasable;
import entityInterface.weaponInterface.SizeIncreasable;
import javafx.scene.image.Image;

import java.util.HashMap;

/**
 * Accessory — Sukuna's Arm.
 *
 * <p>Increases the damage of all {@link DamageIncreasable} weapons by +10 %
 * per upgrade level (up to +61 % at level 5).  Uses the same incremental
 * tracking pattern as {@link SixEye} so upgrades stack correctly as the
 * accessory is levelled up during a run.
 */
public class SukunaArm extends Accessory {

    /** Reference to the game manager used to read the equipped weapon slots. */
    private final GameManager gameManager;

    /**
     * Tracks the accessory level at which each weapon last received a damage buff.
     */
    protected HashMap<Weapon, Integer> weaponTrackingHashMap;

    /**
     * Constructs Sukuna's Arm accessory.
     *
     * @param gameManager the game manager (source of the weapon list)
     */
    public SukunaArm(GameManager gameManager) {
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

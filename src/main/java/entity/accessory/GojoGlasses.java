package entity.accessory;

import core.GameManager;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.CooldownDecreasable;
import javafx.scene.image.Image;

import java.util.HashMap;

/**
 * Accessory — Gojo's Glasses.
 *
 * <p>Reduces the cooldown of all {@link CooldownDecreasable} weapons by 8 %
 * per upgrade level (multiplicative; ×0.92 per level, up to ×0.66 at level 5).
 * Uses the same incremental tracking pattern as {@link SixEye}.
 */
public class GojoGlasses extends Accessory {

    /** Reference to the game manager used to read the equipped weapon slots. */
    private final GameManager gameManager;

    /**
     * Tracks the accessory level at which each weapon last received a cooldown reduction.
     */
    protected HashMap<Weapon, Integer> weaponTrackingHashMap;

    /**
     * Constructs Gojo's Glasses accessory.
     *
     * @param gameManager the game manager (source of the weapon list)
     */
    public GojoGlasses(GameManager gameManager) {
        super("Gojo's Glasses",5);
        this.gameManager=gameManager;
        this.setIcon(utils.SpriteManager.loadImage("accessory/icon/gojoglass.png"));
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

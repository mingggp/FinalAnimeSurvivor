package entity.accessory;

import core.GameManager;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.SizeIncreasable;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Accessory — Rikugan (Six Eyes).
 *
 * <p>Grants all {@link SizeIncreasable} weapons a cumulative +10 % hit-area
 * bonus per upgrade level (up to +61 % at level 5).
 *
 * <p>A {@link HashMap} keyed by weapon instance tracks how many levels of the
 * bonus have already been applied to each weapon, so upgrades are incremental
 * and newly equipped weapons receive the full current-level bonus on first proc.
 */
public class SixEye extends Accessory {

    /** Reference to the game manager used to read the equipped weapon slots. */
    private final GameManager gameManager;

    /**
     * Tracks the accessory level at which each weapon last received a size buff.
     * Value &lt; {@link #level} means an incremental boost is still owed.
     */
    protected HashMap<Weapon, Integer> weaponTrackingHashMap;

    /**
     * Constructs the Six Eyes accessory.
     *
     * @param gameManager the game manager (source of the weapon list)
     */
    public SixEye(GameManager gameManager) {
        super("Rikugan",5);
        this.setIcon(utils.SpriteManager.loadImage("accessory/icon/sixeye.png"));
        this.gameManager = gameManager;
        weaponTrackingHashMap = new HashMap<>();
    }

    /**
     * Applies the size buff to all equipped {@link SizeIncreasable} weapons.
     *
     * <p>If the weapon is new (not in the tracking map), the full
     * {@code 1.1^level} multiplier is applied.  If the weapon was buffed at a
     * lower level, only the incremental delta ({@code 1.1^(level − prevLevel)})
     * is applied to avoid double-counting.
     */
    @Override
    public void procEffect() {
        for (Weapon weapon : gameManager.getWeaponList()) {
            if (weapon == null) {
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

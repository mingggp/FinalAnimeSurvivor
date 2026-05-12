package entity.accessory;

import core.GameManager;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import javafx.scene.image.Image;

import java.util.HashMap;

/**
 * Accessory — Sukuna's Cloak.
 *
 * <p>Increases the XP gain multiplier by ×1.08 per upgrade level, making the
 * player level up faster.  The multiplier is applied once per level-up (not
 * every frame) via {@link GameManager#increaseGrowth(double)}.
 *
 * <p>Also acts as a prerequisite for evolving {@link entity.weapon.Cleave}
 * into {@link entity.weapon.evolvedWeapon.MaximumCleave}.
 */
public class SukunaCloak extends Accessory {

    /** Game manager — target of the XP growth boost. */
    private final GameManager gameManager;

    /** Number of ×1.08 growth boosts already applied this run. */
    private int amountOfTimeGrowthGotMultiply;

    public SukunaCloak(GameManager gameManager){
        super("Sukuna's Cloak",5);
        this.gameManager=gameManager;
        this.setIcon(utils.SpriteManager.loadImage("accessory/icon/sukunaCloak.png"));
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


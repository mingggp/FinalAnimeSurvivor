package entity.accessory;

import core.GameManager;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import javafx.scene.image.Image;

import java.util.HashMap;

/**
 * Accessory — Gojo's Blindfold.
 *
 * <p>Multiplies the player's item-magnet radius by 1.3 per upgrade level.
 * The multiplier is applied once per level-up (not every frame), tracked by
 * {@link #amountOfTimeMagnetRadiusGotMultiply} so the boost is only applied
 * the exact number of times the accessory has been upgraded.
 */
public class Blindfold extends Accessory {

    /** Game manager — provides access to the character's magnet radius. */
    private final GameManager gameManager;

    /** Number of ×1.3 multiplications already applied to the magnet radius. */
    private int amountOfTimeMagnetRadiusGotMultiply;

    public Blindfold(GameManager gameManager){
        super("Gojo's Blindfold",5);
        this.gameManager = gameManager;
        this.setIcon(utils.SpriteManager.loadImage("accessory/icon/gojoblindfold.png"));
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

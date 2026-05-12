package gui.gameLayout;

import core.GameManager;
import entity.accessory.Accessory;
import entity.weapon.Weapon;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

/**
 * HUD panel showing two rows of six {@link ItemSquare} slots:
 * the top row for weapons (indices 0–5) and the bottom row for accessories (6–11).
 *
 * <p>The panel is positioned in the top-left corner of the game viewport.
 * {@link utils.SceneManager#updateWeaponAndAccessoryPanel(Weapon[], Accessory[])}
 * refreshes icon and level labels when the player equips or upgrades.
 * {@link #tickCooldowns(Weapon[])} is called every frame to animate the
 * cooldown overlays on weapon slots.
 */
public class WeaponAndAccessoryPanel extends GridPane {

    /**
     * Constructs a 6 × 2 grid of empty {@link ItemSquare} slots.
     *
     * @param gameManager the game manager passed to each {@link ItemSquare}
     */
    public WeaponAndAccessoryPanel(GameManager gameManager) {
        this.setAlignment(Pos.TOP_LEFT);
        this.setTranslateX(10);
        this.setTranslateY(60);
        this.setHgap(8);
        this.setVgap(8);
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 6; i++) {
                ItemSquare square = new ItemSquare(gameManager);
                this.add(square,i,j);
            }
        }
    }
    /**
     * Refreshes all 12 slots from the current weapon and accessory arrays.
     *
     * <p>Slots with a non-null entry call {@code updateWeaponSquare} or
     * {@code updateAccessorySquare}; empty slots call {@code initializeCellColor}
     * to reset the slot to its empty grey state.
     *
     * @param weaponList    the 6-slot equipped weapon array
     * @param accessoryList the 6-slot equipped accessory array
     */
    public void updateWeaponAndAccessoryPanel(Weapon[] weaponList, Accessory[] accessoryList) {
        for(int i = 0 ;i < 6 ; i++ ){
            if(weaponList[i] != null){
                ((ItemSquare)this.getChildren().get(i)).updateWeaponSquare(weaponList[i]);
            }
            else{
                ((ItemSquare)this.getChildren().get(i)).initializeCellColor();
            }
            if(accessoryList[i] != null){
                ((ItemSquare)this.getChildren().get(6+i)).updateAccessorySquare(accessoryList[i]);
            }
            else{
                ((ItemSquare)this.getChildren().get(6+i)).initializeCellColor();
            }
        }
    }

    /**
     * Lightweight per-frame refresh that only updates the cooldown overlay on
     * occupied weapon slots.
     *
     * <p>Designed to be cheap — it skips null slots and delegates to
     * {@link ItemSquare#tickCooldown(Weapon)} which only modifies the overlay
     * height (no layout pass triggered).
     *
     * @param weaponList the 6-slot equipped weapon array
     */
    public void tickCooldowns(Weapon[] weaponList) {
        for(int i = 0; i < 6; i++){
            if(weaponList[i] != null){
                ((ItemSquare)this.getChildren().get(i)).tickCooldown(weaponList[i]);
            }
        }
    }
}

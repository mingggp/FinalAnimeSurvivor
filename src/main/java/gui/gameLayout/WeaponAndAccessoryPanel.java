package gui.gameLayout;

import core.GameManager;
import entity.accessory.Accessory;
import entity.weapon.Weapon;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class WeaponAndAccessoryPanel extends GridPane {
    public WeaponAndAccessoryPanel(GameManager gameManager){
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
    public void updateWeaponAndAccessoryPanel(Weapon[] weaponList,Accessory[] accessoryList){
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
     * Lightweight per-frame refresh that only updates the cooldown overlay
     * on weapon slots. Cheap to call from the game loop.
     */
    public void tickCooldowns(Weapon[] weaponList){
        for(int i = 0; i < 6; i++){
            if(weaponList[i] != null){
                ((ItemSquare)this.getChildren().get(i)).tickCooldown(weaponList[i]);
            }
        }
    }
}

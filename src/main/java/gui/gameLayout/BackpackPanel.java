package gui.gameLayout;

import core.GameManager;
import entity.item.Item;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

public class BackpackPanel extends GridPane {

    public BackpackPanel(GameManager gameManager) {
        this.setAlignment(Pos.TOP_RIGHT);
        this.setTranslateX(-10);
        this.setTranslateY(60);
        this.setHgap(8);
        this.setVgap(8);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                ItemSquare square = new ItemSquare(6*i+j,gameManager);
                this.add(square,j,i);
            }
        }
        this.setVisible(false);
    }
    public void updateBackPack(Item[] backpack ){
        for(int i = 0 ;i < backpack.length ; i++ ){
            if(backpack[i] != null){
                ((ItemSquare)this.getChildren().get(i)).update(backpack[i]);
            }
            else{
                ((ItemSquare)this.getChildren().get(i)).initializeCellColor();
            }
        }
    }
}

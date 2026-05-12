package gui;

import core.GameManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class CharacterMenuCanvas extends GridPane {

    private GameManager gameManager;


    public CharacterMenuCanvas(GameManager gameManager){
        this.gameManager = gameManager;
        this.setHgap(8);
        this.setVgap(8);
        this.setPadding(new Insets(8));
        this.setPrefWidth(600);
        this.setAlignment(Pos.CENTER);
        for(int i = 0 ; i < 4 ; i++){
            for(int j = 0 ; j < 4 ; j++){
                CharacterMenuSquare square = new CharacterMenuSquare(gameManager,4*i+j);
                Pane BG = new Pane();
                BG.setBorder(new Border(new BorderStroke( Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)));
                this.add(BG,j,i);
                this.add(square,j,i);

            }
        }

    }

}

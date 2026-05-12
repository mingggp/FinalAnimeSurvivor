package gui;

import core.GameManager;
import core.GameState;
import entity.character.Character;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import utils.SceneManager;

public class CharacterMenuSquare extends Pane {
    private final GameManager gameManager;
    private final int slot;

    public CharacterMenuSquare(GameManager gameManager,int slot) {

        this.gameManager = gameManager;
        this.slot = slot;
        if(gameManager.getAllCharacterList()[slot] != null) {
            Image icon = gameManager.getAllCharacterList()[slot].getIcon();
            draw(icon);
        }
        else{
            this.setVisible(false);
        }
        this.setPrefHeight(100);
        this.setPrefWidth(100);
        this.setMinHeight(100);
        this.setMinWidth(100);
        this.setOnMouseClicked(event -> onClickHandler());
        this.setBorder(new Border(new BorderStroke( Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)));
    }

    private void onClickHandler(){
        gameManager.setCharacter(new Character(gameManager.getAllCharacterList()[slot]));
        SceneManager.switchToGame();
        gameManager.startGame();
        gameManager.setCurrentState(GameState.PLAYING);

    }
    private void draw(Image image) {
        BackgroundSize bgSize = new BackgroundSize(100,100,false,false,false,false);
        BackgroundImage bgImg = new BackgroundImage(image, null, null, null, bgSize);
        BackgroundImage[] bgImgA = {bgImg};
        this.setBackground(new Background(bgImgA));
    }
}

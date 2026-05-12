package gui.gameLayout;

import core.GameManager;
import javafx.scene.control.Button;

public class QuitGamePanel extends Button {

    private final GameManager gameManager;
    public QuitGamePanel(GameManager gameManager){
        this.gameManager = gameManager;
        this.setText("Quit");
        this.setPrefWidth(340);
        this.setPrefHeight(75);
        this.setOnMouseClicked(event -> QuitGameButtonHandler());
        this.setTranslateX(780);
        this.setTranslateY(482.5);
        this.setVisible(false);
    }
    private void QuitGameButtonHandler(){
        gameManager.resetGame();
    }
}

package gui.gameLayout;

import core.GameManager;
import core.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import utils.SceneManager;
import utils.SoundManager;

/** Semi-transparent pause overlay centred on screen. */
public class QuitGamePanel extends VBox {

    private static final String BTN_NORMAL =
        "-fx-background-color: #2c2c54; -fx-text-fill: #f5f5f5; -fx-font-size: 22px;" +
        "-fx-font-weight: bold; -fx-background-radius: 10; -fx-border-color: #e94560;" +
        "-fx-border-width: 2; -fx-border-radius: 10; -fx-cursor: hand;";
    private static final String BTN_HOVER =
        "-fx-background-color: #e94560; -fx-text-fill: #ffffff; -fx-font-size: 22px;" +
        "-fx-font-weight: bold; -fx-background-radius: 10; -fx-border-color: #e94560;" +
        "-fx-border-width: 2; -fx-border-radius: 10; -fx-cursor: hand;";

    private final GameManager gameManager;

    public QuitGamePanel(GameManager gameManager) {
        this.gameManager = gameManager;

        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(50, 60, 50, 60));
        this.setMaxSize(440, 380);
        this.setStyle(
            "-fx-background-color: rgba(13,13,26,0.93);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: #e94560; -fx-border-width: 3; -fx-border-radius: 18;");

        Text title = new Text("⏸  PAUSED");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 52));
        title.setFill(Color.web("#ffd700"));
        title.setEffect(new DropShadow(14, Color.web("#ffd700")));

        Text hint = new Text("Press ESC to resume");
        hint.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        hint.setFill(Color.web("#aaaacc"));

        Button resumeBtn = makeButton("▶  Resume");
        resumeBtn.setOnMouseClicked(e -> {
            gameManager.setCurrentState(GameState.PLAYING);
            SceneManager.unpause();
        });

        Button menuBtn = makeButton("⏹  Main Menu");
        menuBtn.setOnMouseClicked(e -> gameManager.resetGame());

        this.getChildren().addAll(title, hint, resumeBtn, menuBtn);
        this.setVisible(false);
    }

    private Button makeButton(String label) {
        Button b = new Button(label);
        b.setPrefWidth(300);
        b.setPrefHeight(60);
        b.setStyle(BTN_NORMAL);
        b.setOnMouseEntered(e -> b.setStyle(BTN_HOVER));
        b.setOnMouseExited(e -> b.setStyle(BTN_NORMAL));
        b.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
            e -> SoundManager.getInstance().playSFX(SoundManager.SFX_UI_CLICK));
        return b;
    }
}
